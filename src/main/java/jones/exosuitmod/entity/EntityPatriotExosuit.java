package jones.exosuitmod.entity;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.item.ItemInit;
import jones.exosuitmod.sound.SoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
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
        this.setMaxLeftClickCooldown(40);
        this.setMaxRightClickCooldown(0);
        this.dataManager.register(MINIGUN_SPINNING, false);
    }

    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
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
        this.minigunRotation = Math.max(0, Math.min(this.minigunRotation + (this.getMinigunSpinning() ? 2.5F : -2.5F), 100));
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
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemInit.EXOSUIT_REPAIR_KIT)
        {
            this.heal(20);
            player.getCooldownTracker().setCooldown(player.getHeldItem(EnumHand.MAIN_HAND).getItem(), 300);
            this.playSound(SoundHandler.EXOSUIT_REPAIR, 1.0F, 1.0F);
        }
        return super.processInteract(player, hand);
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
        Vec3d entityPosition = this.getPositionEyes(1.0F);  // Entity's eye position (or another reference point)
        float yaw = this.rotationYaw;  // The entity's yaw (horizontal rotation)
        float pitch = this.rotationPitch;

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
        arrow.shoot(direction.x, direction.y, direction.z, (float) 4.0F, 5F);
        this.world.spawnEntity(arrow);
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SKELETON_SHOOT, this.getSoundCategory(), 1.0F, 1.0F + (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F);
    }

    private Vec3d getCrosshairTargetPosition(World world) 
    {
        Vec3d playerLook = this.getLookVec();
        double distance = 20.0;
        return this.getPositionVector().add(new Vec3d(playerLook.x * distance, (playerLook.y * distance) + 4.5F, playerLook.z * distance));
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
