package jones.exosuitmod.entity;

import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityMessagerChicken extends EntityCreature
{
    public boolean leftClickPressed = false;
    public boolean rightClickPressed = false;

    public float jumpPower = 0.0f;
    public boolean isMountJumping = false;
    public long lastTimeHitCountdown = 0;

    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg;

    public EntityMessagerChicken(World worldIn) 
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    }

    public void onLeftClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(!leftClickPressed && pressed)
        {
            this.playLivingSound();
        }
        leftClickPressed = pressed;
    }

    public void onRightClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        rightClickPressed = pressed;
    }

    public float getEyeHeight()
    {
        return this.height - 0.05F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        livingSoundTime = -1;
        if(lastTimeHitCountdown > 0)
            lastTimeHitCountdown -= 1;

        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

        if (!this.onGround && this.wingRotDelta < 1.0F)
        {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.wingRotation += this.wingRotDelta * 2.0F;

        if (!this.world.isRemote && !this.isChild() && --this.timeUntilNextEgg <= 0)
        {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.EGG, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }

        if (!this.world.isRemote && this.rand.nextInt(900) == 0 && this.deathTime == 0)
        {
            this.heal(1.0F);
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("EggLayTime"))
        {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void updatePassenger(Entity passenger)
    {
        passenger.setPosition(this.posX, this.posY, this.posZ);
        passenger.setSprinting(false);
    }

    public void removePassenger(Entity passenger)
    {
        super.removePassenger(passenger);
        EntityPlayer playerPassenger = (EntityPlayer)passenger;
        playerPassenger.eyeHeight = playerPassenger.getDefaultEyeHeight();
    }

    public boolean canBeSteered()
    {
        return true;
    }

    public boolean shouldDismountInWater(Entity rider)
    {
        return false;
    }

    @Nullable
    public Entity getControllingPassenger()
    {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (!this.isBeingRidden() && !this.world.isRemote)
        {
            player.startRiding(this);
            return true;
        }
        return super.processInteract(player, hand);
    }

    public void jump()
    {
        this.jumpPower = 0.5F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entity = source.getTrueSource();
        return this.isBeingRidden() && entity != null && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
    }

    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
    {
        super.knockBack(entityIn, strength, xRatio, zRatio);
        if(!this.world.isRemote && this.getControllingPassenger() instanceof EntityPlayerMP)
        {
            lastTimeHitCountdown = 10;
            ((EntityPlayerMP)this.getControllingPassenger()).connection.sendPacket(new SPacketEntityVelocity(this));
        }
    }

    public void travel(float strafe, float vertical, float forward)
    {
        if (this.isBeingRidden() && this.canBeSteered() && this.getHealth() > 0)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();

            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.rotationYaw;
            strafe = entitylivingbase.moveStrafing;
            forward = entitylivingbase.moveForward;

            if (this.jumpPower > 0.0F && !this.isMountJumping && this.onGround)
            {
                this.motionY = (double)this.jumpPower;

                if (this.isPotionActive(MobEffects.JUMP_BOOST))
                {
                    this.motionY = this.motionY * (1.5f + (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() * 0.5F));
                }

                this.isMountJumping = true;
                this.isAirBorne = true;

                if (forward > 0.0F)
                {
                    float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
                    float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
                    this.motionX += (double)(-0.4F * f * this.jumpPower);
                    this.motionZ += (double)(0.4F * f1 * this.jumpPower);
                }

                this.jumpPower = 0.0F;
            }

            if (this.canPassengerSteer())
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.2F;
                super.travel(strafe, vertical, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer && lastTimeHitCountdown <= 0)
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;

                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d5 = this.posX - this.prevPosX;
                double d7 = this.posZ - this.prevPosZ;
                double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? this.posY - this.prevPosY : 0.0D;
                float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;
        
                if (f10 > 1.0F)
                {
                    f10 = 1.0F;
                }
        
                this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            }

            if (this.onGround)
            {
                this.jumpPower = 0.0F;
                this.isMountJumping = false;
            }
        }
        else
        {
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }
}
