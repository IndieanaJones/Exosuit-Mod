package jones.exosuitmod.entity;

import net.minecraft.world.World;
import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.inventory.ExosuitInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
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
    public int timeUntilNextEgg;

    public EntityMessagerChicken(World worldIn) 
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.maxLeftCooldownTime = 40;
        this.maxRightCooldownTime = 160;
        this.inventory = new ExosuitInventory(5);
    }

    public void onLeftClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(!leftClickPressed && pressed && leftClickCooldown <= 0)
        {
            this.playLivingSound();

            leftClickCooldown = this.maxLeftCooldownTime;
            handleSendingCooldown(this.leftClickCooldown, 0);
        }
        leftClickPressed = pressed;
    }

    public void onRightClickPressed(boolean pressed)
    {
        if(this.world.isRemote)
            return;
        if(pressed && rightClickCooldown <= 0)
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            EntityEgg egg = new EntityEgg(this.world, this);
            egg.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, 1.5F, 1.0F);
            this.world.spawnEntity(egg);

            rightClickCooldown = this.maxRightCooldownTime;
            handleSendingCooldown(this.rightClickCooldown, 1);
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
        for(int i = 0; i < this.inventory.getSizeInventory(); i++)
        {
            if(!compound.hasKey("InventoryItem" + i))
                continue;
            ItemStack itemstack = new ItemStack(compound.getCompoundTag("InventoryItem" + i));
            this.inventory.setInventorySlotContents(i, itemstack);
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
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
}
