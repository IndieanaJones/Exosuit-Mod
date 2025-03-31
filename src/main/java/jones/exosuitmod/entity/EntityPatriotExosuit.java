package jones.exosuitmod.entity;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.item.ItemInit;
import jones.exosuitmod.sound.SoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;

public class EntityPatriotExosuit extends AbstractExosuit
{
    public float minigunRotation = 0;
    private static final DataParameter<Boolean> MINIGUN_SPINNING = EntityDataManager.<Boolean>createKey(EntityPatriotExosuit.class, DataSerializers.BOOLEAN);
    public int ticksUntilNextMinigunBullet = 0;

    public EntityPatriotExosuit(World worldIn) 
    {
        super(worldIn);
        this.setSize(2F, 4.5F);
        this.stepHeight = 1;
        this.limbSwing = 1;
    }

    public void entityInit()
    {
        super.entityInit();
        this.setMaxLeftClickCooldown(200);
        this.setMaxRightClickCooldown(0);
        this.dataManager.register(MINIGUN_SPINNING, false);
    }

    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    public void onLeftClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(pressed && leftClickCooldown <= 0)
        {
            this.shootRocketLauncherProjectile();
            updateCooldown("left", this.getMaxLeftClickCooldown(), true);
        }
        leftClickPressed = pressed;
    }

    public float getEyeHeight()
    {
        return 4F;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if(!this.world.isRemote)
        {
            this.setMinigunSpinning(this.rightClickPressed);
            this.ticksUntilNextMinigunBullet = Math.max(this.ticksUntilNextMinigunBullet - 1, 0);
        }
        this.minigunRotation = Math.max(0, Math.min(this.minigunRotation + (this.getMinigunSpinning() ? 5F : -5F), 100));
        if(this.getMinigunSpinning())
        {
            if(!this.world.isRemote)
            {
                if(this.minigunRotation >= 100 && this.ticksUntilNextMinigunBullet <= 0)
                {
                    shootMinigunProjectile();
                    ticksUntilNextMinigunBullet = 4;
                }
            }
        }
        else
        {
            if(!this.world.isRemote)
            {
                ticksUntilNextMinigunBullet = 4;
            }
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (!player.world.isRemote && hand == EnumHand.MAIN_HAND && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemInit.EXOSUIT_REPAIR_KIT && !player.getCooldownTracker().hasCooldown(player.getHeldItem(hand).getItem()) && this.getHealth() < this.getMaxHealth())
        {
            this.heal(20);
            player.getCooldownTracker().setCooldown(player.getHeldItem(EnumHand.MAIN_HAND).getItem(), 300);
            player.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
            this.playSound(SoundHandler.EXOSUIT_REPAIR, 1.0F, 1.0F);
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void setFire(int seconds) 
    {
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
    }

    public void addPotionEffect(PotionEffect potioneffectIn)
    {
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundHandler.EXOSUIT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundHandler.EXOSUIT_STEP, 0.15F, 1.0F);
    }

    public Boolean getMinigunSpinning() 
    {
        return this.dataManager.get(MINIGUN_SPINNING);
    }

    public void setMinigunSpinning(Boolean value) 
    {
        this.dataManager.set(MINIGUN_SPINNING, value);
    }

    public void jump()
    {
        this.jumpPower = 0.4F;
    }

    public void travel(float strafe, float vertical, float forward)
    {
        super.travel(strafe, vertical, forward);
        this.limbSwing -= this.limbSwingAmount;
        this.limbSwingAmount = this.limbSwingAmount * 0.75F;
        this.limbSwing += this.limbSwingAmount;
    }

    public void shootMinigunProjectile()
    {
        EntityTippedArrow arrow = new EntityTippedArrow(this.world, this);
        Vec3d vec3d = this.getLook(1.0F);

        double d3 = vec3d.y;

        double xOffset = -2;
        Vec3d entityPosition = this.getPositionEyes(1.0F);
        float yaw = this.rotationYaw;

        float yawRadians = (float) Math.toRadians(yaw);

        double cosYaw = Math.cos(yawRadians);
        double sinYaw = Math.sin(yawRadians);

        double offsetX = xOffset * cosYaw;
        double offsetZ = xOffset * sinYaw;
        double offsetY = -1.5;

        Vec3d firePosition = entityPosition.add(new Vec3d(offsetX, offsetY, offsetZ));

        Vec3d crosshairPosition = this.getCrosshairTargetPosition(world);
        Vec3d direction = crosshairPosition.subtract(firePosition).normalize();

        arrow.setPosition(firePosition.x, firePosition.y + (d3), firePosition.z);
        arrow.shoot(direction.x, direction.y, direction.z, (float) 5.0F, 3F);
        this.world.spawnEntity(arrow);
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SKELETON_SHOOT, this.getSoundCategory(), 1.0F, 1.0F + (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F);
    }

    public void shootRocketLauncherProjectile()
    {
        Vec3d vec3d = this.getLook(1.0F);

        double xOffset = 2;
        Vec3d entityPosition = this.getPositionEyes(1.0F);
        float yaw = this.rotationYaw;

        float yawRadians = (float) Math.toRadians(yaw);

        double cosYaw = Math.cos(yawRadians);
        double sinYaw = Math.sin(yawRadians);

        double offsetX = xOffset * cosYaw;
        double offsetZ = xOffset * sinYaw;
        double offsetY = -1.5;

        Vec3d firePosition = entityPosition.add(new Vec3d(offsetX, offsetY, offsetZ));

        final double d1 = 16.0;
        final double d2 = vec3d.x * d1;
        final double d3 = vec3d.y * d1;
        final double d4 = vec3d.z * d1;
        Vec3d crosshairPosition = this.getCrosshairTargetPosition(world);
        Vec3d direction = crosshairPosition.subtract(firePosition).normalize();
        final EntityLargeFireball fireball = new EntityLargeFireball(this.world, this, d2, d3, d4);
        fireball.motionX = direction.x * 3;
        fireball.motionY = direction.y * 3;
        fireball.motionZ = direction.z * 3;
        fireball.explosionPower = 4;
        fireball.posX = firePosition.x;
        fireball.posY = firePosition.y;
        fireball.posZ = firePosition.z;
        this.world.spawnEntity((Entity)fireball);

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, this.getSoundCategory(), 1.0F, 1.0F + (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F);
    }

    private Vec3d getCrosshairTargetPosition(World world) 
    {
        float maxDistance = 100;
        Vec3d lookDirection = this.getLook(1.0F);
        Vec3d cameraPos = this.getPositionEyes(1.0F);
        Vec3d targetPos = cameraPos.add(new Vec3d(lookDirection.x * maxDistance, lookDirection.y * maxDistance, lookDirection.z * maxDistance));

        RayTraceResult rayTraceResult = this.world.rayTraceBlocks(cameraPos, targetPos);
        if (rayTraceResult != null) 
        {
            targetPos = new Vec3d(rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z);
        }
        return targetPos;
    }

    public int getTextureLength()
    {
        return 256;
    }

    public int getTextureHeight()
    {
        return 256;
    }

    public String getExosuitIdentifier()
    {
        return "patriot_exosuit";
    }

    public int getTotalTextureLayers()
    {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTextureResource(int id)
    {
        switch(id)
        {
            case 0:
            {
                return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/patriot_exosuit/patriot_exosuit.png");
            }
        }
        return null;
    }

    public int getExtraThirdPersonZoom()
    {
        return 4;
    }
}
