package net.minecraft.entity.boss;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob
{
    private float[] field_82220_d = new float[2];
    private float[] field_82221_e = new float[2];
    private float[] field_82217_f = new float[2];
    private float[] field_82218_g = new float[2];
    private int[] field_82223_h = new int[2];
    private int[] field_82224_i = new int[2];
    private int field_82222_j;

    /** Selector used to determine the entities a wither boss should attack. */
    private static final IEntitySelector attackEntitySelector = new EntityWitherAttackFilter();

    public EntityWither(World par1World)
    {
        super(par1World);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9F, 4.0F);
        this.isImmuneToFire = true;
        this.getNavigator().setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
        this.experienceValue = 50;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Invul", this.func_82212_n());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.func_82215_s(par1NBTTagCompound.getInteger("Invul"));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return this.height / 8.0F;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.wither.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.wither.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.wither.death";
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.motionY *= 0.6000000238418579D;
        double d0;
        double d1;
        double d2;

        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0)
        {
            Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));

            if (entity != null)
            {
                if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D)
                {
                    if (this.motionY < 0.0D)
                    {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                double d3 = entity.posX - this.posX;
                d0 = entity.posZ - this.posZ;
                d1 = d3 * d3 + d0 * d0;

                if (d1 > 9.0D)
                {
                    d2 = (double)MathHelper.sqrt_double(d1);
                    this.motionX += (d3 / d2 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d0 / d2 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
        {
            this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
        }

        super.onLivingUpdate();
        int i;

        for (i = 0; i < 2; ++i)
        {
            this.field_82218_g[i] = this.field_82221_e[i];
            this.field_82217_f[i] = this.field_82220_d[i];
        }

        int j;

        for (i = 0; i < 2; ++i)
        {
            j = this.getWatchedTargetId(i + 1);
            Entity entity1 = null;

            if (j > 0)
            {
                entity1 = this.worldObj.getEntityByID(j);
            }

            if (entity1 != null)
            {
                d0 = this.func_82214_u(i + 1);
                d1 = this.func_82208_v(i + 1);
                d2 = this.func_82213_w(i + 1);
                double d4 = entity1.posX - d0;
                double d5 = entity1.posY + (double)entity1.getEyeHeight() - d1;
                double d6 = entity1.posZ - d2;
                double d7 = (double)MathHelper.sqrt_double(d4 * d4 + d6 * d6);
                float f = (float)(Math.atan2(d6, d4) * 180.0D / Math.PI) - 90.0F;
                float f1 = (float)(-(Math.atan2(d5, d7) * 180.0D / Math.PI));
                this.field_82220_d[i] = this.func_82204_b(this.field_82220_d[i], f1, 40.0F);
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], f, 10.0F);
            }
            else
            {
                this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], this.renderYawOffset, 10.0F);
            }
        }

        boolean flag = this.isArmored();

        for (j = 0; j < 3; ++j)
        {
            double d8 = this.func_82214_u(j);
            double d9 = this.func_82208_v(j);
            double d10 = this.func_82213_w(j);
            this.worldObj.spawnParticle("smoke", d8 + this.rand.nextGaussian() * 0.30000001192092896D, d9 + this.rand.nextGaussian() * 0.30000001192092896D, d10 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

            if (flag && this.worldObj.rand.nextInt(4) == 0)
            {
                this.worldObj.spawnParticle("mobSpell", d8 + this.rand.nextGaussian() * 0.30000001192092896D, d9 + this.rand.nextGaussian() * 0.30000001192092896D, d10 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (this.func_82212_n() > 0)
        {
            for (j = 0; j < 3; ++j)
            {
                this.worldObj.spawnParticle("mobSpell", this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (double)(this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    protected void updateAITasks()
    {
        int i;

        if (this.func_82212_n() > 0)
        {
            i = this.func_82212_n() - 1;

            if (i <= 0)
            {
                this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
                this.worldObj.func_82739_e(1013, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }

            this.func_82215_s(i);

            if (this.ticksExisted % 10 == 0)
            {
                this.heal(10.0F);
            }
        }
        else
        {
            super.updateAITasks();
            int j;

            for (i = 1; i < 3; ++i)
            {
                if (this.ticksExisted >= this.field_82223_h[i - 1])
                {
                    this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);

                    if (this.worldObj.difficultySetting >= 2)
                    {
                        int k = i - 1;
                        int l = this.field_82224_i[i - 1];
                        this.field_82224_i[k] = this.field_82224_i[i - 1] + 1;

                        if (l > 15)
                        {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)f, this.posX + (double)f);
                            double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)f1, this.posY + (double)f1);
                            double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)f, this.posZ + (double)f);
                            this.func_82209_a(i + 1, d0, d1, d2, true);
                            this.field_82224_i[i - 1] = 0;
                        }
                    }

                    j = this.getWatchedTargetId(i);

                    if (j > 0)
                    {
                        Entity entity = this.worldObj.getEntityByID(j);

                        if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0D && this.canEntityBeSeen(entity))
                        {
                            this.func_82216_a(i + 1, (EntityLivingBase)entity);
                            this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                            this.field_82224_i[i - 1] = 0;
                        }
                        else
                        {
                            this.func_82211_c(i, 0);
                        }
                    }
                    else
                    {
                        List list = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(20.0D, 8.0D, 20.0D), attackEntitySelector);

                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1)
                        {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));

                            if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase))
                            {
                                if (entitylivingbase instanceof EntityPlayer)
                                {
                                    if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                                    {
                                        this.func_82211_c(i, entitylivingbase.entityId);
                                    }
                                }
                                else
                                {
                                    this.func_82211_c(i, entitylivingbase.entityId);
                                }

                                break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (this.getAttackTarget() != null)
            {
                this.func_82211_c(0, this.getAttackTarget().entityId);
            }
            else
            {
                this.func_82211_c(0, 0);
            }

            if (this.field_82222_j > 0)
            {
                --this.field_82222_j;

                if (this.field_82222_j == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    i = MathHelper.floor_double(this.posY);
                    j = MathHelper.floor_double(this.posX);
                    int j1 = MathHelper.floor_double(this.posZ);
                    boolean flag = false;

                    for (int k1 = -1; k1 <= 1; ++k1)
                    {
                        for (int l1 = -1; l1 <= 1; ++l1)
                        {
                            for (int i2 = 0; i2 <= 3; ++i2)
                            {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                int i3 = this.worldObj.getBlockId(j2, k2, l2);

                                Block block = Block.blocksList[i3];
                                if (block != null && block.canEntityDestroy(worldObj, j2, k2, l2, this))
                                {
                                    flag = this.worldObj.destroyBlock(j2, k2, l2, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag)
                    {
                        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
                    }
                }
            }

            if (this.ticksExisted % 20 == 0)
            {
                this.heal(1.0F);
            }
        }
    }

    public void func_82206_m()
    {
        this.func_82215_s(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {}

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return 4;
    }

    private double func_82214_u(int par1)
    {
        if (par1 <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (par1 - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double)f1 * 1.3D;
        }
    }

    private double func_82208_v(int par1)
    {
        return par1 <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double func_82213_w(int par1)
    {
        if (par1 <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (par1 - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double)f1 * 1.3D;
        }
    }

    private float func_82204_b(float par1, float par2, float par3)
    {
        float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);

        if (f3 > par3)
        {
            f3 = par3;
        }

        if (f3 < -par3)
        {
            f3 = -par3;
        }

        return par1 + f3;
    }

    private void func_82216_a(int par1, EntityLivingBase par2EntityLivingBase)
    {
        this.func_82209_a(par1, par2EntityLivingBase.posX, par2EntityLivingBase.posY + (double)par2EntityLivingBase.getEyeHeight() * 0.5D, par2EntityLivingBase.posZ, par1 == 0 && this.rand.nextFloat() < 0.001F);
    }

    private void func_82209_a(int par1, double par2, double par4, double par6, boolean par8)
    {
        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
        double d3 = this.func_82214_u(par1);
        double d4 = this.func_82208_v(par1);
        double d5 = this.func_82213_w(par1);
        double d6 = par2 - d3;
        double d7 = par4 - d4;
        double d8 = par6 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d6, d7, d8);

        if (par8)
        {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.worldObj.spawnEntityInWorld(entitywitherskull);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.func_82216_a(0, par1EntityLivingBase);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (par1DamageSource == DamageSource.drown)
        {
            return false;
        }
        else if (this.func_82212_n() > 0)
        {
            return false;
        }
        else
        {
            Entity entity;

            if (this.isArmored())
            {
                entity = par1DamageSource.getSourceOfDamage();

                if (entity instanceof EntityArrow)
                {
                    return false;
                }
            }

            entity = par1DamageSource.getEntity();

            if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == this.getCreatureAttribute())
            {
                return false;
            }
            else
            {
                if (this.field_82222_j <= 0)
                {
                    this.field_82222_j = 20;
                }

                for (int i = 0; i < this.field_82224_i.length; ++i)
                {
                    this.field_82224_i[i] += 3;
                }

                return super.attackEntityFrom(par1DamageSource, par2);
            }
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        this.dropItem(Item.netherStar.itemID, 1);
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity()
    {
        this.entityAge = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {}

    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect par1PotionEffect) {}

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(40.0D);
    }

    @SideOnly(Side.CLIENT)
    public float func_82207_a(int par1)
    {
        return this.field_82221_e[par1];
    }

    @SideOnly(Side.CLIENT)
    public float func_82210_r(int par1)
    {
        return this.field_82220_d[par1];
    }

    public int func_82212_n()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public void func_82215_s(int par1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    /**
     * Returns the target entity ID if present, or -1 if not @param par1 The target offset, should be from 0-2
     */
    public int getWatchedTargetId(int par1)
    {
        return this.dataWatcher.getWatchableObjectInt(17 + par1);
    }

    public void func_82211_c(int par1, int par2)
    {
        this.dataWatcher.updateObject(17 + par1, Integer.valueOf(par2));
    }

    /**
     * Returns whether the wither is armored with its boss armor or not by checking whether its health is below half of
     * its maximum.
     */
    public boolean isArmored()
    {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity)
    {
        this.ridingEntity = null;
    }
}
