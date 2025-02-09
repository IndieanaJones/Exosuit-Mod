package jones.exosuitmod.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AbstractExosuit extends EntityCreature 
{
    public boolean leftClickPressed = false;
    public boolean rightClickPressed = false;

    public float jumpPower = 0.0f;
    public boolean isMountJumping = false;
    public long lastTimeHitCountdown = 0;

    public AbstractExosuit(World worldIn) 
    {
        super(worldIn);
    }

    public void onLeftClickPressed(boolean pressed)
    {

    }

    public void onRightClickPressed(boolean pressed)
    {

    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if(lastTimeHitCountdown > 0)
            lastTimeHitCountdown -= 1;
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

    public void applyEntityCollision(Entity entityIn)
    {
        if (!this.isRidingSameEntity(entityIn))
        {
            if (!entityIn.noClip && !this.noClip)
            {
                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D)
                {
                    d2 = (double)MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D)
                    {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                    d1 = d1 * (double)(1.0F - this.entityCollisionReduction);


                    this.addVelocity(-d0, 0.0D, -d1);
                    if(!this.world.isRemote && this.getControllingPassenger() instanceof EntityPlayerMP)
                    {
                        lastTimeHitCountdown = 10;
                        ((EntityPlayerMP)this.getControllingPassenger()).connection.sendPacket(new SPacketEntityVelocity(this));
                    }

                    if (!entityIn.isBeingRidden())
                    {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
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