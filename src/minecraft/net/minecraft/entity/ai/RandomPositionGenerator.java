package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator
{
    /**
     * used to store a driection when the user passes a point to move towards or away from. WARNING: NEVER THREAD SAFE.
     * MULTIPLE findTowards and findAway calls, will share this var
     */
    private static Vec3 staticVector = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks
     */
    public static Vec3 findRandomTarget(EntityCreature par0EntityCreature, int par1, int par2)
    {
        return findRandomTargetBlock(par0EntityCreature, par1, par2, (Vec3)null);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the direction of the point par3
     */
    public static Vec3 findRandomTargetBlockTowards(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        staticVector.xCoord = par3Vec3.xCoord - par0EntityCreature.posX;
        staticVector.yCoord = par3Vec3.yCoord - par0EntityCreature.posY;
        staticVector.zCoord = par3Vec3.zCoord - par0EntityCreature.posZ;
        return findRandomTargetBlock(par0EntityCreature, par1, par2, staticVector);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the reverse direction of the point par3
     */
    public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        staticVector.xCoord = par0EntityCreature.posX - par3Vec3.xCoord;
        staticVector.yCoord = par0EntityCreature.posY - par3Vec3.yCoord;
        staticVector.zCoord = par0EntityCreature.posZ - par3Vec3.zCoord;
        return findRandomTargetBlock(par0EntityCreature, par1, par2, staticVector);
    }

    /**
     * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction of
     * par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
     */
    private static Vec3 findRandomTargetBlock(EntityCreature par0EntityCreature, int par1, int par2, Vec3 par3Vec3)
    {
        Random random = par0EntityCreature.getRNG();
        boolean flag = false;
        int k = 0;
        int l = 0;
        int i1 = 0;
        float f = -99999.0F;
        boolean flag1;

        if (par0EntityCreature.hasHome())
        {
            double d0 = (double)(par0EntityCreature.getHomePosition().getDistanceSquared(MathHelper.floor_double(par0EntityCreature.posX), MathHelper.floor_double(par0EntityCreature.posY), MathHelper.floor_double(par0EntityCreature.posZ)) + 4.0F);
            double d1 = (double)(par0EntityCreature.func_110174_bM() + (float)par1);
            flag1 = d0 < d1 * d1;
        }
        else
        {
            flag1 = false;
        }

        for (int j1 = 0; j1 < 10; ++j1)
        {
            int k1 = random.nextInt(2 * par1) - par1;
            int l1 = random.nextInt(2 * par2) - par2;
            int i2 = random.nextInt(2 * par1) - par1;

            if (par3Vec3 == null || (double)k1 * par3Vec3.xCoord + (double)i2 * par3Vec3.zCoord >= 0.0D)
            {
                k1 += MathHelper.floor_double(par0EntityCreature.posX);
                l1 += MathHelper.floor_double(par0EntityCreature.posY);
                i2 += MathHelper.floor_double(par0EntityCreature.posZ);

                if (!flag1 || par0EntityCreature.func_110176_b(k1, l1, i2))
                {
                    float f1 = par0EntityCreature.getBlockPathWeight(k1, l1, i2);

                    if (f1 > f)
                    {
                        f = f1;
                        k = k1;
                        l = l1;
                        i1 = i2;
                        flag = true;
                    }
                }
            }
        }

        if (flag)
        {
            return par0EntityCreature.worldObj.getWorldVec3Pool().getVecFromPool((double)k, (double)l, (double)i1);
        }
        else
        {
            return null;
        }
    }
}
