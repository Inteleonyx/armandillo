package dev.inteleonyx.armandillo.core.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import dev.inteleonyx.armandillo.utils.ArmandilloResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Inteleonyx. Created on 04/12/2025
 * @project armandillo
 */

@Mixin(TagLoader.class)
public abstract class TagLoaderMixin {
    @Shadow(remap = false)
    private final String directory;

    private TagLoaderMixin(String directory) {
        this.directory = directory;
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void armandillo$injectLoad(ResourceManager manager, CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
        String currentFolder = this.directory;

        // Extração mais segura do targetFolder, evitando confusão com múltiplas barras
        String targetFolder = null;
        if (currentFolder.startsWith("tags/")) {
            String remainder = currentFolder.substring("tags/".length());
            int slashIndex = remainder.indexOf("/");
            if (slashIndex == -1) {
                targetFolder = remainder;
            } else {
                targetFolder = remainder.substring(0, slashIndex);
            }
        } else {
            targetFolder = currentFolder;
        }

        // Só continue se o diretório atual realmente corresponde ao folder esperado
        if (currentFolder.startsWith("tags/" + targetFolder)) {
            System.out.println("[Armandillo DEBUG] OK: Applying modifications for folder: " + targetFolder);

            Map<ResourceLocation, List<TagLoader.EntryWithSource>> tags = cir.getReturnValue();
            Map<ResourceLocation, JsonObject> runtimeData = RuntimeDataRegistry.getAllTagData();

            // Adição de entradas conforme runtimeData
            for (Map.Entry<ResourceLocation, JsonObject> entry : runtimeData.entrySet()) {
                ResourceLocation registryKey = entry.getKey();

                // Se o path não tem "/" é ignorado (espera-se pasta/subpasta no path)
                if (!registryKey.getPath().contains("/")) continue;

                // pasta agendada para essa tag (ex: "item", "block")
                String scheduledFolder = registryKey.getPath().split("/")[0];

                if (scheduledFolder.equals(targetFolder)) {
                    // Retira o prefixo da pasta do path para gerar a tag real
                    String path = registryKey.getPath().substring(targetFolder.length() + 1);
                    ResourceLocation tagId = ResourceLocation.fromNamespaceAndPath(registryKey.getNamespace(), path);

                    JsonObject json = entry.getValue();
                    if (!json.has("values")) continue;
                    JsonArray values = json.getAsJsonArray("values");

                    List<TagLoader.EntryWithSource> list = tags.getOrDefault(tagId, new ArrayList<>());

                    for (JsonElement e : values) {
                        String v = e.getAsString();

                        System.out.println("  -> Adding Element entry to " + tagId + ": " + v);

                        if (v.startsWith("#")) {
                            ResourceLocation t = ResourceLocation.tryParse(v.substring(1));
                            if (t != null) list.add(new TagLoader.EntryWithSource(TagEntry.tag(t), "armandillo"));
                        } else {
                            ResourceLocation i = ResourceLocation.tryParse(v);
                            if (i != null) {
                                list.add(new TagLoader.EntryWithSource(TagEntry.element(i), "armandillo"));
                            }
                        }
                    }
                    tags.put(tagId, list);
                }
            }

            // Processa critérios de remoção adicionados dinamicamente
            for (String criteria : RuntimeDataRegistry.getTagRemovalCriteria()) {
                try {
                    String[] parts = criteria.split(":", 3);
                    if (parts.length < 2) {
                        System.err.println("[Armandillo ERROR] Critério inválido: " + criteria);
                        continue;
                    }
                    String protocol = parts[0];
                    String target = parts.length > 1 ? parts[1] : null;
                    String removeEntry = parts.length > 2 ? parts[2] : null;

                    // Valida se o critério se aplica à pasta atual ou é global
                    boolean matchesTagProtocol = (protocol.equals("TAG_CLEAR_ENTRIES") || protocol.equals("TAG_REMOVE_ENTRY"))
                            && target != null && (target.equals(targetFolder) || target.startsWith(targetFolder + "/") || target.startsWith(targetFolder + ":") || target.contains(":"));
                    boolean matchesAllProtocol = protocol.equals("ITEM_REMOVE_FROM_ALL");

                    if (!matchesTagProtocol && !matchesAllProtocol) continue;

                    if (protocol.equals("TAG_CLEAR_ENTRIES")) {
                        // "target" já deve ser um ResourceLocation em formato string (ex: "c:sandstone/slabs")
                        ResourceLocation t = ArmandilloResourceLocation.parse(target);

                        if (t != null && tags.containsKey(t)) {
                            tags.get(t).clear();
                            System.out.println("[Armandillo DEBUG] Removed all entries from Tag: " + t);
                        } else {
                            System.out.println("[Armandillo DEBUG] TAG_CLEAR_ENTRIES: Tag não encontrada: " + target);
                        }
                    }

                    if (protocol.equals("TAG_REMOVE_ENTRY") && removeEntry != null && target != null) {
                        ResourceLocation tag = ArmandilloResourceLocation.parse(target);

                        System.out.println("--- [Armandillo DIAGNOSE REMOVE] ---");
                        System.out.println("Pasta Atual: " + targetFolder);
                        System.out.println("Tag Alvo Calculada (Esperada): " + tag);
                        System.out.println("Tags disponíveis nesta pasta (keySet): " + tags.keySet());
                        System.out.println("------------------------------------");

                        if (tag != null && tags.containsKey(tag)) {
                            List<TagLoader.EntryWithSource> list = tags.get(tag);
                            int sizeBefore = list.size();

                            list.removeIf(x -> extract(x.entry()).equals(removeEntry));

                            if (list.size() < sizeBefore) {
                                System.out.println("[Armandillo DEBUG] Removed entry " + removeEntry + " from Tag: " + tag);
                            }
                        } else {
                            System.out.println("[Armandillo DIAGNOSE] FALHA: A Tag '" + tag + "' não foi encontrada no mapa carregado nesta pasta.");
                        }
                    }

                    if (protocol.equals("ITEM_REMOVE_FROM_ALL") && target != null) {
                        String remove = target;
                        for (var l : tags.values()) {
                            l.removeIf(x -> extract(x.entry()).equals(remove));
                        }
                        System.out.println("[Armandillo DEBUG] Removed entry " + remove + " from ALL Tags in folder: " + targetFolder);
                    }
                } catch (Exception e) {
                    System.err.println("[Armandillo ERROR] Error processing tag removal criteria: " + criteria + ": " + e.getMessage());
                }
            }

        } else {
            System.out.println("[Armandillo DEBUG] SKIPPING INJECTION: Tag type is not targeted (Directory: " + currentFolder + ")");
        }
    }

    @Unique
    private static String extract(TagEntry e) {
        if (e == null) return "";
        String s = e.toString();
        if (s == null) return "";

        s = s.replace("{", "").replace("}", "").replace("[", "").replace("]", "").trim();

        if (s.contains("tag=")) {
            try {
                String id = s.split("tag=")[1].split(",")[0].trim();
                return "#" + id.replace("#", "");
            } catch (Exception ignored) {
                return s;
            }
        }

        if (s.contains("element=")) {
            try {
                String id = s.split("element=")[1].split(",")[0].trim();
                return id;
            } catch (Exception ignored) {
                return s;
            }
        }

        return s;
    }
}