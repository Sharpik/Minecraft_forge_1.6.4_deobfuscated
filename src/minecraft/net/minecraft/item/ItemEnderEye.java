package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class ItemEnderEye extends Item
{
    public ItemEnderEye(int par1)
    {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int i1 = par3World.getBlockId(par4, par5, par6);
        int j1 = par3World.getBlockMetadata(par4, par5, par6);

        if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && i1 == Block.endPortalFrame.blockID && !BlockEndPortalFrame.isEnderEyeInserted(j1))
        {
            if (par3World.isRemote)
            {
                return true;
            }
            else
            {
                par3World.setBlockMetadataWithNotify(par4, par5, par6, j1 + 4, 2);
                par3World.func_96440_m(par4, par5, par6, Block.endPortalFrame.blockID);
                --par1ItemStack.stackSize;
                int k1;

                for (k1 = 0; k1 < 16; ++k1)
                {
                    double d0 = (double)((float)par4 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d1 = (double)((float)par5 + 0.8125F);
                    double d2 = (double)((float)par6 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double d3 = 0.0D;
                    double d4 = 0.0D;
                    double d5 = 0.0D;
                    par3World.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
                }

                k1 = j1 & 3;
                int l1 = 0;
                int i2 = 0;
                boolean flag = false;
                boolean flag1 = true;
                int j2 = Direction.rotateRight[k1];
                int k2;
                int l2;
                int i3;
                int j3;
                int k3;

                for (k2 = -2; k2 <= 2; ++k2)
                {
                    j3 = par4 + Direction.offsetX[j2] * k2;
                    l2 = par6 + Direction.offsetZ[j2] * k2;
                    k3 = par3World.getBlockId(j3, par5, l2);

                    if (k3 == Block.endPortalFrame.blockID)
                    {
                        i3 = par3World.getBlockMetadata(j3, par5, l2);

                        if (!BlockEndPortalFrame.isEnderEyeInserted(i3))
                        {
                            flag1 = false;
                            break;
                        }

                        i2 = k2;

                        if (!flag)
                        {
                            l1 = k2;
                            flag = true;
                        }
                    }
                }

                if (flag1 && i2 == l1 + 2)
                {
                    for (k2 = l1; k2 <= i2; ++k2)
                    {
                        j3 = par4 + Direction.offsetX[j2] * k2;
                        l2 = par6 + Direction.offsetZ[j2] * k2;
                        j3 += Direction.offsetX[k1] * 4;
                        l2 += Direction.offsetZ[k1] * 4;
                        k3 = par3World.getBlockId(j3, par5, l2);
                        i3 = par3World.getBlockMetadata(j3, par5, l2);

                        if (k3 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(i3))
                        {
                            flag1 = false;
                            break;
                        }
                    }

                    for (k2 = l1 - 1; k2 <= i2 + 1; k2 += 4)
                    {
                        for (j3 = 1; j3 <= 3; ++j3)
                        {
                            l2 = par4 + Direction.offsetX[j2] * k2;
                            k3 = par6 + Direction.offsetZ[j2] * k2;
                            l2 += Direction.offsetX[k1] * j3;
                            k3 += Direction.offsetZ[k1] * j3;
                            i3 = par3World.getBlockId(l2, par5, k3);
                            int l3 = par3World.getBlockMetadata(l2, par5, k3);

                            if (i3 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(l3))
                            {
                                flag1 = false;
                                break;
                            }
                        }
                    }

                    if (flag1)
                    {
                        for (k2 = l1; k2 <= i2; ++k2)
                        {
                            for (j3 = 1; j3 <= 3; ++j3)
                            {
                                l2 = par4 + Direction.offsetX[j2] * k2;
                                k3 = par6 + Direction.offsetZ[j2] * k2;
                                l2 += Direction.offsetX[k1] * j3;
                                k3 += Direction.offsetZ[k1] * j3;
                                par3World.setBlock(l2, par5, k3, Block.endPortal.blockID, 0, 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = par2World.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);

            if (i == Block.endPortalFrame.blockID)
            {
                return par1ItemStack;
            }
        }

        if (!par2World.isRemote)
        {
            ChunkPosition chunkposition = par2World.findClosestStructure("Stronghold", (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ);

            if (chunkposition != null)
            {
                EntityEnderEye entityendereye = new EntityEnderEye(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY + 1.62D - (double)par3EntityPlayer.yOffset, par3EntityPlayer.posZ);
                entityendereye.moveTowards((double)chunkposition.x, chunkposition.y, (double)chunkposition.z);
                par2World.spawnEntityInWorld(entityendereye);
                par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                par2World.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ, 0);

                if (!par3EntityPlayer.capabilities.isCreativeMode)
                {
                    --par1ItemStack.stackSize;
                }
            }
        }

        return par1ItemStack;
    }
}
