package jones.exosuitmod.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityMob;

public class EntityMorphDragon extends EntityMob
{
	private static final DataParameter<Boolean> IS_BIG = EntityDataManager.<Boolean>createKey(EntityMorphDragon.class, DataSerializers.BOOLEAN);
	
    public boolean isFlying;
    public float oldCos;
    public boolean needsToFlap;
    public EntityEnderCrystal healingEnderCrystal;
    
    public EntityMorphDragon(final World worldIn) 
    {
        super(worldIn);
        this.setSize(4.0f, 1.9f);
        this.ignoreFrustumCheck = true;
        this.isFlying = false;
    }
    
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0);
    }
    
    public void onLivingUpdate()
    {
    	if(this.getIfBig())
    	{
    		this.setSize(10.0f, 4.75f);
    	}
    	else
    	{
    		this.setSize(4.0f, 1.9f);
    	}
    	super.onLivingUpdate();
    	if(this.isFlying)
    	{
	    	if(oldCos < 0 && MathHelper.cos(this.ticksExisted * 0.3f) > 0)
	    		this.needsToFlap = true;
	    	if(this.needsToFlap && oldCos > MathHelper.cos(this.ticksExisted))
	    	{
	    		this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
	    		this.needsToFlap = false;
	    	}
	    	this.oldCos = MathHelper.cos(this.ticksExisted * 0.3f);
    	}
    	
    	if(this.getHealth() > 0.0F)
    		this.updateDragonEnderCrystal();
    }
    
    public void updateDragonEnderCrystal()
    {
        if (this.healingEnderCrystal != null)
        {
            if (this.healingEnderCrystal.isDead)
            {
                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth())
            {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0)
        {
            List<EntityEnderCrystal> list = this.world.<EntityEnderCrystal>getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand(128.0D, 128.0D, 128.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;

            for (EntityEnderCrystal entityendercrystal1 : list)
            {
                double d1 = entityendercrystal1.getDistanceSq(this);

                if (d1 < d0)
                {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.healingEnderCrystal = entityendercrystal;
        }
    }
    
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERDRAGON_GROWL;
    }
    
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERDRAGON_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERDRAGON_DEATH;
    }
    
    public boolean isNonBoss() {
        return false;
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(IS_BIG, Boolean.valueOf(false));
    }
    
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (((Boolean)this.dataManager.get(IS_BIG)).booleanValue())
        {
            compound.setBoolean("is_big", true);
        }
    }
    
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.dataManager.set(IS_BIG, Boolean.valueOf(compound.getBoolean("is_big")));
    }
    
    public boolean getIfBig()
    {
        return ((Boolean)this.dataManager.get(IS_BIG)).booleanValue();
    }
}
