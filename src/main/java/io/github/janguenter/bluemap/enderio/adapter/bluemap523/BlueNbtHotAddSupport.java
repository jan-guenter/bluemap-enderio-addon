/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.world.BlockEntity;
import de.bluecolored.bluemap.core.world.mca.MCAUtil;
import de.bluecolored.bluenbt.BlueNBT;
import de.bluecolored.bluenbt.NBTWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/** Keeps BlueMap's shared BlueNBT resolver usable after this late-loaded add-on. */
final class BlueNbtHotAddSupport {

    private static final List<String> RESOLVER_CACHES = List.of(
            "typeDeserializerMap",
            "typeResolverMap"
    );

    private BlueNbtHotAddSupport() {
    }

    /** Clears the exact BlueNBT 3.5.1 resolver snapshots after DTO registration. */
    static boolean refreshSharedDeserializerCache() {
        BlueNBT blueNbt = MCAUtil.BLUENBT;
        synchronized (blueNbt) {
            try {
                for (String name : RESOLVER_CACHES) {
                    Field field = BlueNBT.class.getDeclaredField(name);
                    if (!Map.class.isAssignableFrom(field.getType())) {
                        return false;
                    }
                    field.setAccessible(true);
                    Object value = field.get(blueNbt);
                    if (!(value instanceof Map<?, ?> cache)) {
                        return false;
                    }
                    cache.clear();
                }
                return true;
            } catch (ReflectiveOperationException | RuntimeException | LinkageError exception) {
                return false;
            }
        }
    }

    /** Verifies that the exact persisted Paint field survives shared parsing. */
    static boolean retainsPersistedPaint() {
        try {
            BlockEntity decoded = MCAUtil.BLUENBT.read(
                    new ByteArrayInputStream(probe()), BlockEntity.class
            );
            return decoded instanceof EnderIoBlockEntityData data
                    && "enderio:single_painted".equals(data.getId().getFormatted())
                    && data.getX() == 17
                    && data.getY() == -23
                    && data.getZ() == 41
                    && "minecraft:bricks".equals(data.paint());
        } catch (IOException | RuntimeException | LinkageError exception) {
            return false;
        }
    }

    private static byte[] probe() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (NBTWriter writer = new NBTWriter(bytes)) {
            writer.beginCompound();
            writer.name("id").value("enderio:single_painted");
            writer.name("x").value(17);
            writer.name("y").value(-23);
            writer.name("z").value(41);
            writer.name("Paint").value("minecraft:bricks");
            writer.endCompound();
        }
        return bytes.toByteArray();
    }
}
