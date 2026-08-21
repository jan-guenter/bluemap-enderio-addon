/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Key;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaintSnapshotDecoderTest {

    private final PaintSnapshotDecoder decoder = new PaintSnapshotDecoder();

    @Test
    void acceptsOrdinaryResourceIdentifiers() {
        assertEquals(
                Optional.of(Key.parse("minecraft:stone")),
                decoder.decode("minecraft:stone")
        );
        assertEquals(
                Optional.of(Key.parse("minecraft:bricks")),
                decoder.decode(new EnderIoBlockEntityData("minecraft:bricks"))
        );
    }

    @Test
    void rejectsMissingMalformedAndUnboundedText() {
        assertTrue(decoder.decode((String) null).isEmpty());
        assertTrue(decoder.decode("").isEmpty());
        assertTrue(decoder.decode("stone").isEmpty());
        assertTrue(decoder.decode("Minecraft:stone").isEmpty());
        assertTrue(decoder.decode("minecraft:stone[axis=y]").isEmpty());
        assertTrue(decoder.decode("minecraft:" + "a".repeat(119)).isEmpty());
    }

    @Test
    void rejectsRecursiveOrEmptyTargets() {
        assertTrue(decoder.decode("enderio:painted_redstone_block").isEmpty());
        assertTrue(decoder.decode("enderio:industrial_insulation_block").isEmpty());
        assertTrue(decoder.decode("minecraft:air").isEmpty());
    }
}
