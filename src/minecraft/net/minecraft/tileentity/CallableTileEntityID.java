package net.minecraft.tileentity;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;

class CallableTileEntityID implements Callable
{
    final TileEntity theTileEntity;

    CallableTileEntityID(TileEntity par1TileEntity)
    {
        this.theTileEntity = par1TileEntity;
    }

    public String callTileEntityID()
    {
        int i = this.theTileEntity.worldObj.getBlockId(this.theTileEntity.xCoord, this.theTileEntity.yCoord, this.theTileEntity.zCoord);

        try
        {
            return String.format("ID #%d (%s // %s)", new Object[] {Integer.valueOf(i), Block.blocksList[i].getUnlocalizedName(), Block.blocksList[i].getClass().getCanonicalName()});
        }
        catch (Throwable throwable)
        {
            return "ID #" + i;
        }
    }

    public Object call()
    {
        return this.callTileEntityID();
    }
}
