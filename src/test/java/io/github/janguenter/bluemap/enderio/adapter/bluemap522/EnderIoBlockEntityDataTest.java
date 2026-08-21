/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import de.bluecolored.bluenbt.NBTName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnderIoBlockEntityDataTest {

    @Test
    void mapsOnlyTheExactTopLevelPaintField() throws NoSuchFieldException {
        Field field = EnderIoBlockEntityData.class.getDeclaredField("paint");
        assertArrayEquals(
                new String[] {"Paint"}, field.getAnnotation(NBTName.class).value()
        );
        assertEquals("minecraft:stone", new EnderIoBlockEntityData("minecraft:stone").paint());
    }
}
