package jones.exosuitmod.entity;

import jones.exosuitmod.item.ItemInit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityExosuitExplosiveEgg extends EntityThrowable 
{
    public float explosionRadius = 1.5f;

    public EntityExosuitExplosiveEgg(World p_i1779_1_) 
    {
        super(p_i1779_1_);
    }

    public EntityExosuitExplosiveEgg(World p_i1780_1_, EntityLivingBase p_i1780_2_) 
    {
        super(p_i1780_1_, p_i1780_2_);
    }

    public EntityExosuitExplosiveEgg(World p_i1781_1_, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) 
    {
        super(p_i1781_1_, p_i1781_2_, p_i1781_4_, p_i1781_6_);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte p_70103_1_) 
    {
        if (p_70103_1_ == 3) 
        {
            for(int lvt_4_1_ = 0; lvt_4_1_ < 8; ++lvt_4_1_) 
            {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5) * 0.08, ((double)this.rand.nextFloat() - 0.5) * 0.08, ((double)this.rand.nextFloat() - 0.5) * 0.08, new int[]{Item.getIdFromItem(ItemInit.EXOSUIT_EXPLOSIVE_EGG)});
            }
        }
    }

    protected void onImpact(RayTraceResult rayResult) 
    {
        if (rayResult.entityHit != null) 
        {
            if(rayResult.entityHit == this.thrower)
                return;
            rayResult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, false);

        this.world.setEntityState(this, (byte)3);
        this.setDead();
    }
}
