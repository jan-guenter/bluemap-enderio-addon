/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnderIo8211ProfileTest {

    @Test
    void pinsExactRuntimeAndOwnedHost() {
        assertEquals("8.2.11-beta", EnderIo8211Profile.VERSION);
        assertEquals(6_592_813L, EnderIo8211Profile.JAR_SIZE);
        assertEquals(
                "e01af48907781f2d5ccdfa8d71975b611c33f295be11b7021cb91be06ce8070c",
                EnderIo8211Profile.JAR_SHA256
        );
        assertTrue(EnderIo8211Profile.matches(
                "enderio:painted_redstone_block", "enderio:single_painted"
        ));
        assertFalse(EnderIo8211Profile.matches(
                "enderio:painted_stone", "enderio:single_painted"
        ));
    }
}
