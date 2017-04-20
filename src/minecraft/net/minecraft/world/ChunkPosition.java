package net.minecraft.world;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ChunkPosition
{
    /** The x coordinate of this ChunkPosition */
    public final int x;

    /** The y coordinate of this ChunkPosition */
    public final int y;

    /** The z coordinate of this ChunkPosition */
    public final int z;

    public ChunkPosition(int par1, int par2, int par3)
    {
        this.x = par1;
        this.y = par2;
        this.z = par3;
    }

    public ChunkPosition(Vec3 par1Vec3)
    {
        this(MathHelper.floor_double(par1Vec3.xCoord), MathHelper.floor_double(par1Vec3.yCoord), MathHelper.floor_double(par1Vec3.zCoord));
    }

    public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof ChunkPosition))
        {
            return false;
        }
        else
        {
            ChunkPosition chunkposition = (ChunkPosition)par1Obj;
            return chunkposition.x == this.x && chunkposition.y == this.y && chunkposition.z == this.z;
        }
    }

    public int hashCode()
    {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}
