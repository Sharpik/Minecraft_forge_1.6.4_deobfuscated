package net.minecraft.tileentity;

import java.util.concurrent.Callable;

class CallableTileEntityData implements Callable
{
    final TileEntity theTileEntity;

    CallableTileEntityData(TileEntity par1TileEntity)
    {
        this.theTileEntity = par1TileEntity;
    }

    public String callTileEntityDataInfo()
    {
        int i = this.theTileEntity.worldObj.getBlockMetadata(this.theTileEntity.xCoord, this.theTileEntity.yCoord, this.theTileEntity.zCoord);

        if (i < 0)
        {
            return "Unknown? (Got " + i + ")";
        }
        else
        {
            String s = String.format("%4s", new Object[] {Integer.toBinaryString(i)}).replace(" ", "0");
            return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] {Integer.valueOf(i), s});
        }
    }

    public Object call()
    {
        return this.callTileEntityDataInfo();
    }
}
