/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;

/** Resource-pack extension factory registered before resource loading. */
final class EnderIoResourceExtensionType
        implements ResourcePack.Extension<EnderIoResourceExtension> {

    private static final Key KEY = Key.parse("bluemap_enderio:paint_extension");
    private final EnderIoRuntime runtime;

    EnderIoResourceExtensionType(EnderIoRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public EnderIoResourceExtension create(ResourcePack pack) {
        return new EnderIoResourceExtension(pack, runtime);
    }
}
