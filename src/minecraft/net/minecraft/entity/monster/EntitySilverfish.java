package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob
{
    /**
     * A cooldown before this entity will search for another Silverfish to join them in battle.
     */
    private int allySummonCooldown;

    public EntitySilverfish(World par1World)
    {
        super(par1World);
        this.setSize(0.3F, 0.7F);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(1.0D);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        double d0 = 8.0D;
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.silverfish.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.silverfish.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.silverfish.kill";
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
        else
        {
            if (this.allySummonCooldown <= 0 && (par1DamageSource instanceof EntityDamageSource || par1DamageSource == DamageSource.magic))
            {
                this.allySummonCooldown = 20;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (this.attackTime <= 0 && par2 < 1.2F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(par1Entity);
        }
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (!this.worldObj.isRemote)
        {
            int i;
            int j;
            int k;
            int l;

            if (this.allySummonCooldown > 0)
            {
                --this.allySummonCooldown;

                if (this.allySummonCooldown == 0)
                {
                    i = MathHelper.floor_double(this.posX);
                    j = MathHelper.floor_double(this.posY);
                    k = MathHelper.floor_double(this.posZ);
                    boolean flag = false;

                    for (l = 0; !flag && l <= 5 && l >= -5; l = l <= 0 ? 1 - l : 0 - l)
                    {
                        for (int i1 = 0; !flag && i1 <= 10 && i1 >= -10; i1 = i1 <= 0 ? 1 - i1 : 0 - i1)
                        {
                            for (int j1 = 0; !flag && j1 <= 10 && j1 >= -10; j1 = j1 <= 0 ? 1 - j1 : 0 - j1)
                            {
                                int k1 = this.worldObj.getBlockId(i + i1, j + l, k + j1);

                                if (k1 == Block.silverfish.blockID)
                                {
                                    if (!this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                                    {
                                        int l1 = this.worldObj.getBlockMetadata(i + i1, j + l, k + j1);
                                        Block block = Block.stone;

                                        if (l1 == 1)
                                        {
                                            block = Block.cobblestone;
                                        }

                                        if (l1 == 2)
                                        {
                                            block = Block.stoneBrick;
                                        }

                                        this.worldObj.setBlock(i + i1, j + l, k + j1, block.blockID, 0, 3);
                                    }
                                    else
                                    {
                                        this.worldObj.destroyBlock(i + i1, j + l, k + j1, false);
                                    }

                                    Block.silverfish.onBlockDestroyedByPlayer(this.worldObj, i + i1, j + l, k + j1, 0);

                                    if (this.rand.nextBoolean())
                                    {
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.entityToAttack == null && !this.hasPath())
            {
                i = MathHelper.floor_double(this.posX);
                j = MathHelper.floor_double(this.posY + 0.5D);
                k = MathHelper.floor_double(this.posZ);
                int i2 = this.rand.nextInt(6);
                l = this.worldObj.getBlockId(i + Facing.offsetsXForSide[i2], j + Facing.offsetsYForSide[i2], k + Facing.offsetsZForSide[i2]);

                if (BlockSilverfish.getPosingIdByMetadata(l))
                {
                    this.worldObj.setBlock(i + Facing.offsetsXForSide[i2], j + Facing.offsetsYForSide[i2], k + Facing.offsetsZForSide[i2], Block.silverfish.blockID, BlockSilverfish.getMetadataForBlockType(l), 3);
                    this.spawnExplosionParticle();
                    this.setDead();
                }
                else
                {
                    this.updateWanderPath();
                }
            }
            else if (this.entityToAttack != null && !this.hasPath())
            {
                this.entityToAttack = null;
            }
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return this.worldObj.getBlockId(par1, par2 - 1, par3) == Block.stone.blockID ? 10.0F : super.getBlockPathWeight(par1, par2, par3);
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return entityplayer == null;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
