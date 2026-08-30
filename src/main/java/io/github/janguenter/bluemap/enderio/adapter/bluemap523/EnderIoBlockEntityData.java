/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.world.mca.blockentity.MCABlockEntity;
import de.bluecolored.bluenbt.NBTName;

/** Narrow BlueNBT projection of Ender IO's persisted primary paint. */
public final class EnderIoBlockEntityData extends MCABlockEntity {

    @NBTName("Paint")
    private String paint;

    public EnderIoBlockEntityData() {
    }

    EnderIoBlockEntityData(String paint) {
        this.paint = paint;
    }

    String paint() {
        return paint;
    }
}
