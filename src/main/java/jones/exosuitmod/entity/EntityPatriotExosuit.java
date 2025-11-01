package jones.exosuitmod.entity;

import java.util.List;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.model.ModelPatriotExosuit;
import jones.exosuitmod.entity.render.AdvancedEntityTextureHandler;
import jones.exosuitmod.inventory.ExosuitInventory;
import jones.exosuitmod.item.ItemInit;
import jones.exosuitmod.sound.SoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class EntityPatriotExosuit extends AbstractExosuit
{
    public float currentMinigunModelRotation = 0;
    public float minigunRotation = 0;
    private static final DataParameter<Boolean> MINIGUN_SPINNING = EntityDataManager.<Boolean>createKey(EntityPatriotExosuit.class, DataSerializers.BOOLEAN);
    public int ticksUntilNextMinigunBullet = 0;

    public float oldNightVisionUpgradeStatus = 0;

    private static final DataParameter<Integer> NIGHTVISION_UPGRADE_STATUS = EntityDataManager.<Integer>createKey(EntityPatriotExosuit.class, DataSerializers.VARINT);

    public EntityPatriotExosuit(World worldIn) 
    {
        super(worldIn);
        this.setSize(2F, 4.5F);
        this.stepHeight = 1;
        this.limbSwing = 1;
        this.strafeMultiplier = 0.9F;
        this.inventory = new ExosuitInventory(1);
        this.inventory.addInventoryChangeListener(this);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setMaxLeftClickCooldown(200);
        this.setMaxRightClickCooldown(0);
        this.dataManager.register(MINIGUN_SPINNING, false);
        this.dataManager.register(NIGHTVISION_UPGRADE_STATUS, Integer.valueOf(0));
    }

    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(6.0D);
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

    public void renderMobHands(float partialTicks)
    {
        if(!this.world.isRemote)
            return;
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Render<?> render = rm.getEntityRenderObject(this);
        RenderLivingBase<?> mobRenderer = (RenderLivingBase<?>) render;
        
        ModelBase model = mobRenderer.getMainModel();
        if (!(model instanceof ModelPatriotExosuit)) return;
        ModelPatriotExosuit myModel = (ModelPatriotExosuit) model;

        GlStateManager.pushMatrix();

        // Enable vanilla-like lighting
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(AdvancedEntityTextureHandler.INSTANCE.getTextureLocation(this));

        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        myModel.Minigun.rotateAngleX = 0;
		myModel.RocketLauncher.rotateAngleX = 0;

        myModel.Barrel.rotateAngleZ = this.currentMinigunModelRotation * 0.017453292F;

        // --- Transform and render ROCKET LAUNCHER ---
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.6F, -1.8F, -1.6F); // adjust position
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        myModel.RocketLauncher.render(0.0625F);
        GlStateManager.popMatrix();

        // --- Transform and render MINIGUN ---
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.6F, -1.5F, -1.6F);
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        myModel.Minigun.render(0.0625F);
        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();


        // Disable lighting after
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();

        GlStateManager.popMatrix();
    }

    public void updateExosuitCapabilities()
    {
        ItemStack nightVisionUpgradeSlot = inventory.getStackInSlot(0);

        //Night vision
        if(nightVisionUpgradeSlot.getItem() == Items.AIR)
        {
            this.setNightVisionUpgradeStatus(0);
        }
        else if(nightVisionUpgradeSlot.getItem() == ItemInit.EXOSUIT_NIGHTIVISON_UPGRADE)
        {
            this.setNightVisionUpgradeStatus(1);
        }
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
                    ticksUntilNextMinigunBullet = 2;
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

        if(!this.world.isRemote && this.ticksExisted % 5 == 0 && this.getNightVisionUpgradeStatus() == 1 && this.getControllingPassenger() != null)
        {
            EntityLivingBase rider = (EntityLivingBase)getControllingPassenger();
            rider.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, false, false));
        }

        if(this.world.isRemote && (this.oldNightVisionUpgradeStatus != this.getNightVisionUpgradeStatus()))
        {
            this.oldNightVisionUpgradeStatus = this.getNightVisionUpgradeStatus();
            AdvancedEntityTextureHandler.INSTANCE.updateExosuitTexture(this);
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

        // Every few ticks to reduce lag
        if (this.ticksExisted % 5 == 0) 
        {
            EntityLivingBase rider = (EntityLivingBase)getControllingPassenger();
            if (rider != null && !this.world.isRemote && this.onGround && Math.abs(rider.moveForward) + Math.abs(rider.moveStrafing) > 0) 
            {
                stompNearbyEntities();
                if(this.ticksExisted % 20 == 0)
                    breakNearbyLeaves();
            }
        }
    }

    public void stompNearbyEntities()
    {
        double radius = 0.35D;

        if(!this.isBeingRidden())
            return;

        // Get nearby entities (excluding itself)
        List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                this.getEntityBoundingBox().grow(radius, 0, radius),
                e -> e != this && e.isEntityAlive() && !this.isOnSameTeam(e) && !e.isRidingOrBeingRiddenBy(this));

        for (EntityLivingBase entity : entities) 
        {
            // Apply damage
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0F);
        }
    }

    private void breakNearbyLeaves() 
    {
        BlockPos mobPos = new BlockPos(this.posX, this.posY, this.posZ);

        for (int x = (int)-this.width; x <= this.width; x++) 
        {
            for (int y = -1; y <= this.height; y++) 
            {
                for (int z = (int)-this.width; z <= this.width; z++) 
                {
                    BlockPos checkPos = mobPos.add(x, y, z);
                    IBlockState state = world.getBlockState(checkPos);
                    Block block = state.getBlock();

                    if (block.isLeaves(state, world, checkPos)) 
                    {
                        world.destroyBlock(checkPos, false);
                    }
                }
            }
        }
    }

    public void shootMinigunProjectile()
    {
        Vec3d vec3d = this.getLook(1.0F);

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

        final double d1 = 32.0;
        final double d2 = vec3d.x * d1;
        final double d3 = vec3d.y * d1;
        final double d4 = vec3d.z * d1;
        Vec3d crosshairPosition = this.getCrosshairTargetPosition(world);
        Vec3d direction = crosshairPosition.subtract(firePosition).normalize();
        final EntityPatriotBullet fireball = new EntityPatriotBullet(this.world, this, d2, d3, d4);
        fireball.motionX = direction.x * 3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)15;
        fireball.motionY = direction.y * 3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)15;
        fireball.motionZ = direction.z * 3 + this.rand.nextGaussian() * 0.007499999832361937D * (double)15;
        fireball.posX = firePosition.x;
        fireball.posY = firePosition.y;
        fireball.posZ = firePosition.z;
        this.world.spawnEntity((Entity)fireball);

        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundHandler.EXOSUIT_MINIGUN_FIRE, this.getSoundCategory(), 0.25F, 1.0F + (this.getRNG().nextFloat() - this.getRNG().nextFloat()) * 0.2F);
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
        final EntityPatriotRocket fireball = new EntityPatriotRocket(this.world, this, d2, d3, d4);
        fireball.motionX = direction.x * 3;
        fireball.motionY = direction.y * 3;
        fireball.motionZ = direction.z * 3;
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

        // Expand the search AABB to include entities along the ray path
        AxisAlignedBB searchBox = this.getEntityBoundingBox().expand(lookDirection.x * maxDistance, lookDirection.y * maxDistance, lookDirection.z * maxDistance)
            .grow(1.0D, 1.0D, 1.0D); // small buffer

        List<Entity> candidates = world.getEntitiesWithinAABBExcludingEntity(this, searchBox);

        Entity closestEntity = null;
        Vec3d hitVec = null;

        double blockDist = targetPos != null ? cameraPos.distanceTo(targetPos) : maxDistance;
        double closestDist = blockDist; // don't consider entities beyond the block hit

        for (Entity entity : candidates) 
        {
            if (entity == null || entity == this || entity.isRidingOrBeingRiddenBy(this) || !(entity instanceof EntityLivingBase) || entity instanceof IProjectile) continue;
            if (!entity.canBeCollidedWith()) continue;

            // grow by collision border size
            AxisAlignedBB entityBB = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());

            RayTraceResult intercept = entityBB.calculateIntercept(cameraPos, targetPos);
            if (intercept != null) {
                double distToHit = cameraPos.distanceTo(intercept.hitVec);
                if (distToHit < closestDist) 
                {
                    closestEntity = entity;
                    hitVec = intercept.hitVec;
                    closestDist = distToHit;
                }
            }
        }

        if (closestEntity != null) 
        {
            // Return a RayTraceResult representing the entity hit.
            RayTraceResult result = new RayTraceResult(closestEntity, hitVec);
            return new Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z);
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
        return 2;
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
            case 1:
            {
                switch(this.getNightVisionUpgradeStatus())
                {
                    case 1:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/patriot_exosuit/patriot_exosuit_nightvision.png");
                    }
                    default:
                    {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public int getExtraThirdPersonZoom()
    {
        return 4;
    }

    public void setNightVisionUpgradeStatus(int value) 
    {
        this.dataManager.set(NIGHTVISION_UPGRADE_STATUS, Integer.valueOf(value));
    }
    
    public int getNightVisionUpgradeStatus() 
    {
        return this.dataManager.get(NIGHTVISION_UPGRADE_STATUS).intValue();
    }
}
