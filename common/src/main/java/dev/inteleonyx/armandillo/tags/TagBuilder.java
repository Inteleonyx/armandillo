package dev.inteleonyx.armandillo.tags;

import com.google.gson.*;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Inteleonyx. Created on 03/12/2025
 * @project armandillo
 */

public class TagBuilder {
    private static ResourceLocation getRegistryKey(String folder, ResourceLocation tag) {
        return ResourceLocation.fromNamespaceAndPath(tag.getNamespace(), folder + "/" + tag.getPath());
    }

    private static JsonObject getOrCreateTagJson(ResourceLocation registryKey) {
        JsonObject j = RuntimeDataRegistry.getAllTagData().getOrDefault(registryKey, null);

        if (j == null) {
            j = new JsonObject();
            j.add("values", new JsonArray());
            RuntimeDataRegistry.addTagData(registryKey, j);
        }

        if (!j.has("values")) {
            j.add("values", new JsonArray());
        }

        return j;
    }

    public static void addEntryToTag(String tagStr, String folder, String entryStr) {
        try {
            ResourceLocation tag = ResourceLocation.parse(tagStr);
            if (entryStr == null || entryStr.isEmpty()) return;

            ResourceLocation registryKey = getRegistryKey(folder, tag);

            JsonObject runtimeJson = getOrCreateTagJson(registryKey);
            JsonArray runtimeValues = runtimeJson.getAsJsonArray("values");


            boolean exists = runtimeValues.asList().stream()
                    .map(JsonElement::getAsString)
                    .anyMatch(s -> s.equals(entryStr));

            if (!exists) {
                runtimeValues.add(entryStr);
            }

            RuntimeDataRegistry.addTagData(registryKey, runtimeJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
