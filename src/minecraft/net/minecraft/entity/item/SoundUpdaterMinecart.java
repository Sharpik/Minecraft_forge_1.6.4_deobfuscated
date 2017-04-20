package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class SoundUpdaterMinecart implements IUpdatePlayerListBox
{
    private final SoundManager theSoundManager;

    /** Minecart which sound is being updated. */
    private final EntityMinecart theMinecart;

    /** The player that is getting the minecart sound updates. */
    private final EntityPlayerSP thePlayer;
    private boolean playerSPRidingMinecart;
    private boolean minecartIsDead;
    private boolean minecartIsMoving;
    private boolean silent;
    private float minecartSoundPitch;
    private float minecartMoveSoundVolume;
    private float minecartRideSoundVolume;
    private double minecartSpeed;

    public SoundUpdaterMinecart(SoundManager par1SoundManager, EntityMinecart par2EntityMinecart, EntityPlayerSP par3EntityPlayerSP)
    {
        this.theSoundManager = par1SoundManager;
        this.theMinecart = par2EntityMinecart;
        this.thePlayer = par3EntityPlayerSP;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        boolean flag = false;
        boolean flag1 = this.playerSPRidingMinecart;
        boolean flag2 = this.minecartIsDead;
        boolean flag3 = this.minecartIsMoving;
        float f = this.minecartMoveSoundVolume;
        float f1 = this.minecartSoundPitch;
        float f2 = this.minecartRideSoundVolume;
        double d0 = this.minecartSpeed;
        this.playerSPRidingMinecart = this.thePlayer != null && this.theMinecart.riddenByEntity == this.thePlayer;
        this.minecartIsDead = this.theMinecart.isDead;
        this.minecartSpeed = (double)MathHelper.sqrt_double(this.theMinecart.motionX * this.theMinecart.motionX + this.theMinecart.motionZ * this.theMinecart.motionZ);
        this.minecartIsMoving = this.minecartSpeed >= 0.01D;

        if (flag1 && !this.playerSPRidingMinecart)
        {
            this.theSoundManager.stopEntitySound(this.thePlayer);
        }

        if (this.minecartIsDead || !this.silent && this.minecartMoveSoundVolume == 0.0F && this.minecartRideSoundVolume == 0.0F)
        {
            if (!flag2)
            {
                this.theSoundManager.stopEntitySound(this.theMinecart);

                if (flag1 || this.playerSPRidingMinecart)
                {
                    this.theSoundManager.stopEntitySound(this.thePlayer);
                }
            }

            this.silent = true;

            if (this.minecartIsDead)
            {
                return;
            }
        }

        if (!this.theSoundManager.isEntitySoundPlaying(this.theMinecart) && this.minecartMoveSoundVolume > 0.0F)
        {
            this.theSoundManager.playEntitySound("minecart.base", this.theMinecart, this.minecartMoveSoundVolume, this.minecartSoundPitch, false);
            this.silent = false;
            flag = true;
        }

        if (this.playerSPRidingMinecart && !this.theSoundManager.isEntitySoundPlaying(this.thePlayer) && this.minecartRideSoundVolume > 0.0F)
        {
            this.theSoundManager.playEntitySound("minecart.inside", this.thePlayer, this.minecartRideSoundVolume, 1.0F, true);
            this.silent = false;
            flag = true;
        }

        if (this.minecartIsMoving)
        {
            if (this.minecartSoundPitch < 1.0F)
            {
                this.minecartSoundPitch += 0.0025F;
            }

            if (this.minecartSoundPitch > 1.0F)
            {
                this.minecartSoundPitch = 1.0F;
            }

            float f3 = MathHelper.clamp_float((float)this.minecartSpeed, 0.0F, 4.0F) / 4.0F;
            this.minecartRideSoundVolume = 0.0F + f3 * 0.75F;
            f3 = MathHelper.clamp_float(f3 * 2.0F, 0.0F, 1.0F);
            this.minecartMoveSoundVolume = 0.0F + f3 * 0.7F;
        }
        else if (flag3)
        {
            this.minecartMoveSoundVolume = 0.0F;
            this.minecartSoundPitch = 0.0F;
            this.minecartRideSoundVolume = 0.0F;
        }

        if (!this.silent)
        {
            if (this.minecartSoundPitch != f1)
            {
                this.theSoundManager.setEntitySoundPitch(this.theMinecart, this.minecartSoundPitch);
            }

            if (this.minecartMoveSoundVolume != f)
            {
                this.theSoundManager.setEntitySoundVolume(this.theMinecart, this.minecartMoveSoundVolume);
            }

            if (this.minecartRideSoundVolume != f2)
            {
                this.theSoundManager.setEntitySoundVolume(this.thePlayer, this.minecartRideSoundVolume);
            }
        }

        if (!flag && (this.minecartMoveSoundVolume > 0.0F || this.minecartRideSoundVolume > 0.0F))
        {
            this.theSoundManager.updateSoundLocation(this.theMinecart);

            if (this.playerSPRidingMinecart)
            {
                this.theSoundManager.updateSoundLocation(this.thePlayer, this.theMinecart);
            }
        }
        else
        {
            if (this.theSoundManager.isEntitySoundPlaying(this.theMinecart))
            {
                this.theSoundManager.stopEntitySound(this.theMinecart);
            }

            if (this.playerSPRidingMinecart && this.theSoundManager.isEntitySoundPlaying(this.thePlayer))
            {
                this.theSoundManager.stopEntitySound(this.thePlayer);
            }
        }
    }
}
