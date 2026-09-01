/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.RegistryGuard;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;
import io.github.janguenter.bluemap.enderio.profile.EnderIo8211Profile;

/** Exact BlueMap 5.23 feature-backport registration boundary. */
public final class BlueMap523Adapter {

    private static final EnderIoRuntime RUNTIME = EnderIoRuntime.INSTANCE;
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            Key.parse("bluemap_enderio:painted_redstone"),
            (pack, gallery, settings) -> new EnderIoPaintedBlockRenderer(
                    pack, gallery, settings, RUNTIME
            )
    );
    private static final ResourcePack.Extension<EnderIoResourceExtension> EXTENSION =
            new ResourceExtensionType<>(
                    Key.parse("bluemap_enderio:paint_extension"),
                    pack -> new EnderIoResourceExtension(pack, RUNTIME)
            );
    private static final BlockEntityType BLOCK_ENTITY_TYPE = new BlockEntityType.Impl(
            Key.parse(EnderIo8211Profile.BLOCK_ENTITY_ID), EnderIoBlockEntityData.class
    );

    private BlueMap523Adapter() {
    }

    public static synchronized boolean install() {
        if (!RegistryGuard.canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !RegistryGuard.canRegister(BlockEntityType.REGISTRY, BLOCK_ENTITY_TYPE)) {
            RUNTIME.inactive("registry-collision");
            return false;
        }
        if (!RegistryGuard.register(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.register(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !RegistryGuard.register(BlockEntityType.REGISTRY, BLOCK_ENTITY_TYPE)) {
            RUNTIME.inactive("registry-registration-failed");
            return false;
        }
        if (!BlueNbtHotAddSupport.refreshSharedDeserializerCache()) {
            RUNTIME.inactive("bluenbt-cache-refresh-failed");
            return false;
        }
        return true;
    }

    static BlockRendererType renderer() {
        return RENDERER;
    }

    static EnderIoResourceExtension extension(ResourcePack resourcePack) {
        return resourcePack.getExtension(EXTENSION);
    }
}
