package dev.inteleonyx.armandillo.utils;

import net.minecraft.resources.ResourceLocation;

/**
 * @author Inteleonyx. Created on 05/12/2025
 * @project armandillo
 */

public class ArmandilloResourceLocation {
    private ArmandilloResourceLocation() {}

    public static ResourceLocation parse(String tagPath) {
        if (tagPath == null || tagPath.isEmpty()) {
            return null;
        }

        String[] rlParts = tagPath.split(":", 2);

        if (rlParts.length == 2) {
            return ResourceLocation.fromNamespaceAndPath(rlParts[0], rlParts[1]);
        } else if (rlParts.length == 1) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", rlParts[0]);
        }
        return null;
    }
}
