/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.profile;

/** Exact All the Mons 1.2.0 profile for Ender IO 8.2.11-beta. */
public final class EnderIo8211Profile {

    public static final String PROFILE_ID = "enderio-8.2.11-beta";
    public static final String MOD_ID = "enderio";
    public static final String VERSION = "8.2.11-beta";
    public static final String ARTIFACT = "enderio-8.2.11-beta.jar";
    public static final long JAR_SIZE = 6_592_813L;
    public static final String JAR_SHA1 =
            "87b7218eed2e2f325ddcb8d3ce94a67aabaa17ca";
    public static final String JAR_SHA256 =
            "e01af48907781f2d5ccdfa8d71975b611c33f295be11b7021cb91be06ce8070c";
    public static final String HOST_ID = "enderio:painted_redstone_block";
    public static final String BLOCK_ENTITY_ID = "enderio:single_painted";

    private EnderIo8211Profile() {
    }

    public static boolean matches(String blockId, String blockEntityId) {
        return HOST_ID.equals(blockId) && BLOCK_ENTITY_ID.equals(blockEntityId);
    }
}
