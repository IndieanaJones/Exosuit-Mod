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

    public EntityExosuitExplosiveEgg(World worldIn) 
    {
        super(worldIn);
    }

    public EntityExosuitExplosiveEgg(World worldIn, EntityLivingBase throwerIn) 
    {
        super(worldIn, throwerIn);
    }

    public EntityExosuitExplosiveEgg(World worldIn, double x, double y, double z) 
    {
        super(worldIn, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) 
    {
        if (id == 3) 
        {
            for(int ticker = 0; ticker < 8; ++ticker) 
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

        if (!this.world.isRemote) 
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, false);
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
