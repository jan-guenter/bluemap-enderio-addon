/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.SyntheticDispatch;
import io.github.janguenter.bluemap.enderio.profile.EnderIo8211Profile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdapterBoundaryTest {

    @Test
    void reflectiveEntrypointTargetsTheBlueMap523Adapter() throws ReflectiveOperationException {
        Class<?> adapter = Class.forName(
                "io.github.janguenter.bluemap.enderio.adapter.bluemap523.BlueMap523Adapter"
        );
        assertNotNull(adapter.getMethod("install"));
    }

    @Test
    void sharedDispatchAcceptsTheExactLocalFixtureShape() {
        Variant variant = new Variant(ResourcePack.MISSING_BLOCK_MODEL);
        variant.setRenderer(BlueMap523Adapter.renderer());
        BlockState fixture = new BlockState(new Variants(
                new VariantSet[0], new VariantSet(variant)
        ));

        assertTrue(SyntheticDispatch.matches(fixture, BlueMap523Adapter.renderer()));
    }

    @Test
    void keepsExactlyOneEnderIoBlockEntityRegistration() throws IllegalAccessException {
        List<Field> registrations = Arrays.stream(BlueMap523Adapter.class.getDeclaredFields())
                .filter(field -> BlockEntityType.class.isAssignableFrom(field.getType()))
                .toList();
        assertEquals(1, registrations.size());

        Field registration = registrations.getFirst();
        registration.setAccessible(true);
        BlockEntityType type = (BlockEntityType) registration.get(null);
        assertEquals(EnderIo8211Profile.BLOCK_ENTITY_ID, type.getKey().getFormatted());
        assertSame(EnderIoBlockEntityData.class, type.getBlockEntityClass());
    }
}
