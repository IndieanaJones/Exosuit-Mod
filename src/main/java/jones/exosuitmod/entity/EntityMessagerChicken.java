package jones.exosuitmod.entity;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.inventory.ExosuitInventory;
import jones.exosuitmod.item.ItemInit;
import jones.exosuitmod.network.PacketInit;
import jones.exosuitmod.network.packets.PacketChickenFlapClient;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityMessagerChicken extends AbstractExosuit
{
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;

    private static final DataParameter<Integer> EGG_UPGRADE_STATUS = EntityDataManager.<Integer>createKey(EntityMessagerChicken.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FEATHER_UPGRADE_STATUS = EntityDataManager.<Integer>createKey(EntityMessagerChicken.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LEG_UPGRADE_STATUS = EntityDataManager.<Integer>createKey(EntityMessagerChicken.class, DataSerializers.VARINT);

    public EntityMessagerChicken(World worldIn) 
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.inventory = new ExosuitInventory(3);
        this.inventory.addInventoryChangeListener(this);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setMaxLeftClickCooldown(40);
        this.setMaxRightClickCooldown(800); 
        this.dataManager.register(EGG_UPGRADE_STATUS, Integer.valueOf(0));
        this.dataManager.register(FEATHER_UPGRADE_STATUS, Integer.valueOf(0));
        this.dataManager.register(LEG_UPGRADE_STATUS, Integer.valueOf(0));
    }

    public void onLeftClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(!leftClickPressed && pressed && leftClickCooldown <= 0)
        {
            this.playLivingSound();

            this.updateCooldown("left", this.getMaxLeftClickCooldown(), true);
        }
        leftClickPressed = pressed;
    }

    public void onRightClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(pressed && rightClickCooldown <= 0)
        {
            switch(this.getEggUpgradeStatus())
            {
                case 0:
                {
                    this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    this.dropItem(Items.EGG, 1);

                    updateCooldown("right", this.getMaxRightClickCooldown(), true);
                    break;
                }
                case 1:
                {
                    this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                    EntityEgg egg = new EntityEgg(this.world, this);
                    egg.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, 1.5F, 1.0F);
                    this.world.spawnEntity(egg);
        
                    updateCooldown("right", this.getMaxRightClickCooldown(), true);
                    break;
                }
                case 2:
                {
                    this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                    EntityExosuitExplosiveEgg egg = new EntityExosuitExplosiveEgg(this.world, this);
                    egg.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, 1.5F, 1.0F);
                    this.world.spawnEntity(egg);
        
                    updateCooldown("right", this.getMaxRightClickCooldown(), true);
                    break;
                }
            }
        }
        rightClickPressed = pressed;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
    }

    public float getEyeHeight()
    {
        return this.height - 0.05F;
    }

    public void openGUI(EntityPlayer playerEntity)
    {
        playerEntity.openGui(ExosuitMod.instance, 1, this.world, this.getEntityId(), 0, 0);
    }

    public void updateExosuitCapabilities()
    {
        ItemStack eggUpgradeSlot = inventory.getStackInSlot(0);
        ItemStack featherUpgradeSlot = inventory.getStackInSlot(1);
        ItemStack legUpgradeSlot = inventory.getStackInSlot(2);

        //Egg ability
        if(eggUpgradeSlot.getItem() == Items.AIR)
        {
            this.setEggUpgradeStatus(0);
            this.setMaxRightClickCooldown(800);
        }
        else if(eggUpgradeSlot.getItem() == ItemInit.EXOSUIT_EGG_UPGRADE_MK1)
        {
            this.setEggUpgradeStatus(1);
            this.setMaxRightClickCooldown(160);
        }
        else if(eggUpgradeSlot.getItem() == ItemInit.EXOSUIT_EGG_UPGRADE_MK2)
        {
            this.setEggUpgradeStatus(2);
            this.setMaxRightClickCooldown(160);
        }

        //Double jump
        if(featherUpgradeSlot.getItem() == Items.AIR)
        {
            this.setFeatherUpgradeStatus(0);
            this.setMaxMidairJumps(0);
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
        }
        else if(featherUpgradeSlot.getItem() == ItemInit.EXOSUIT_CHICKEN_DOUBLE_JUMP_MK1)
        {
            this.setFeatherUpgradeStatus(1);
            this.setMaxMidairJumps(1);
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
        }
        else if(featherUpgradeSlot.getItem() == ItemInit.EXOSUIT_CHICKEN_ARMOR_MK1)
        {
            this.setFeatherUpgradeStatus(2);
            this.setMaxMidairJumps(0);
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);
        }

        if(legUpgradeSlot.getItem() == Items.AIR)
        {
            this.setLegUpgradeStatus(0);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        }
        else if(legUpgradeSlot.getItem() == ItemInit.EXOSUIT_CHICKEN_SPEED_MK1)
        {
            this.setLegUpgradeStatus(1);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1875D);
        }
        this.dataManager.setDirty(EGG_UPGRADE_STATUS);
        this.dataManager.setDirty(FEATHER_UPGRADE_STATUS);
        this.dataManager.setDirty(LEG_UPGRADE_STATUS);
    }

    public void onDoubleJump()
    {
        PacketInit.PACKET_HANDLER_INSTANCE.sendToServer(new PacketChickenFlapClient(this));
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        livingSoundTime = -1;

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

        if (!this.world.isRemote && this.rand.nextInt(900) == 0 && this.deathTime == 0)
        {
            this.heal(1.0F);
        }
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

    public void setEggUpgradeStatus(int value) 
    {
        this.dataManager.set(EGG_UPGRADE_STATUS, Integer.valueOf(value));
    }
    
    public int getEggUpgradeStatus() 
    {
        return this.dataManager.get(EGG_UPGRADE_STATUS).intValue();
    }

    public void setFeatherUpgradeStatus(int value) 
    {
        this.dataManager.set(FEATHER_UPGRADE_STATUS, Integer.valueOf(value));
    }
    
    public int getFeatherUpgradeStatus() 
    {
        return this.dataManager.get(FEATHER_UPGRADE_STATUS).intValue();
    }

    public void setLegUpgradeStatus(int value) 
    {
        this.dataManager.set(LEG_UPGRADE_STATUS, Integer.valueOf(value));
    }
    
    public int getLegUpgradeStatus() 
    {
        return this.dataManager.get(LEG_UPGRADE_STATUS).intValue();
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        for(int i = 0; i < this.inventory.getSizeInventory(); i++)
        {
            if(!compound.hasKey("InventoryItem" + i))
                continue;
            ItemStack itemstack = new ItemStack(compound.getCompoundTag("InventoryItem" + i));
            this.inventory.setInventorySlotContents(i, itemstack);
        }
        this.updateExosuitCapabilities();
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        for(int i = 0; i < this.inventory.getSizeInventory(); i++)
        {
            if(this.inventory.getStackInSlot(i).isEmpty())
                continue;
            compound.setTag("InventoryItem" + i, this.inventory.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    public void removePassenger(Entity passenger)
    {
        super.removePassenger(passenger);
        passenger.attackEntityFrom(DamageSource.FALL, 5);
    }

    public int getTextureLength()
    {
        return 64;
    }

    public int getTextureHeight()
    {
        return 32;
    }

    public String getExosuitIdentifier()
    {
        return "messager_chicken";
    }

    public int getTotalTextureLayers()
    {
        return 4;
    }
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTextureResource(int id)
    {
        switch(id)
        {
            case 0:
            {
                return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken.png");
            }
            case 1:
            {
                switch(this.getEggUpgradeStatus())
                {
                    case 1:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_egg_1.png");
                    }
                    case 2:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_egg_2.png");
                    }
                    default:
                    {
                        return null;
                    }
                }
            }
            case 2:
            {
                switch(this.getFeatherUpgradeStatus())
                {
                    case 1:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_jump_1.png");
                    }
                    case 2:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_armor_1.png");
                    }
                    default:
                    {
                        return null;
                    }
                }
            }
            case 3:
            {
                switch(this.getLegUpgradeStatus())
                {
                    case 1:
                    {
                        return new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_speed_1.png");
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
}