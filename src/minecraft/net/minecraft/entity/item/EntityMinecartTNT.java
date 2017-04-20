package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart
{
    private int minecartTNTFuse = -1;

    public EntityMinecartTNT(World par1World)
    {
        super(par1World);
    }

    public EntityMinecartTNT(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public int getMinecartType()
    {
        return 3;
    }

    public Block getDefaultDisplayTile()
    {
        return Block.tnt;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.minecartTNTFuse > 0)
        {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        else if (this.minecartTNTFuse == 0)
        {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }

        if (this.isCollidedHorizontally)
        {
            double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d0 >= 0.009999999776482582D)
            {
                this.explodeCart(d0);
            }
        }
    }

    public void killMinecart(DamageSource par1DamageSource)
    {
        super.killMinecart(par1DamageSource);
        double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

        if (!par1DamageSource.isExplosion())
        {
            this.entityDropItem(new ItemStack(Block.tnt, 1), 0.0F);
        }

        if (par1DamageSource.isFireDamage() || par1DamageSource.isExplosion() || d0 >= 0.009999999776482582D)
        {
            this.explodeCart(d0);
        }
    }

    /**
     * Makes the minecart explode.
     */
    protected void explodeCart(double par1)
    {
        if (!this.worldObj.isRemote)
        {
            double d1 = Math.sqrt(par1);

            if (d1 > 5.0D)
            {
                d1 = 5.0D;
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * d1), true);
            this.setDead();
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        if (par1 >= 3.0F)
        {
            float f1 = par1 / 10.0F;
            this.explodeCart((double)(f1 * f1));
        }

        super.fall(par1);
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int par1, int par2, int par3, boolean par4)
    {
        if (par4 && this.minecartTNTFuse < 0)
        {
            this.ignite();
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 10)
        {
            this.ignite();
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    /**
     * Ignites this TNT cart.
     */
    public void ignite()
    {
        this.minecartTNTFuse = 80;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.setEntityState(this, (byte)10);
            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public int func_94104_d()
    {
        return this.minecartTNTFuse;
    }

    /**
     * Returns true if the TNT minecart is ignited.
     */
    public boolean isIgnited()
    {
        return this.minecartTNTFuse > -1;
    }

    /**
     * Gets a block's resistance to this entity's explosion. Used to make rails immune to TNT minecarts' explosions and
     * Wither skulls more destructive.
     */
    public float getBlockExplosionResistance(Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block)
    {
        return this.isIgnited() && (BlockRailBase.isRailBlock(par6Block.blockID) || BlockRailBase.isRailBlockAt(par2World, par3, par4 + 1, par5)) ? 0.0F : super.getBlockExplosionResistance(par1Explosion, par2World, par3, par4, par5, par6Block);
    }

    public boolean shouldExplodeBlock(Explosion par1Explosion, World par2World, int par3, int par4, int par5, int par6, float par7)
    {
        return this.isIgnited() && (BlockRailBase.isRailBlock(par6) || BlockRailBase.isRailBlockAt(par2World, par3, par4 + 1, par5)) ? false : super.shouldExplodeBlock(par1Explosion, par2World, par3, par4, par5, par6, par7);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("TNTFuse"))
        {
            this.minecartTNTFuse = par1NBTTagCompound.getInteger("TNTFuse");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
