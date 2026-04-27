package jones.exosuitmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPatriotRocket extends EntityFireball
{
    public EntityPatriotRocket(World worldIn)
    {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    public EntityPatriotRocket(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public EntityPatriotRocket(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public void onUpdate() 
    {
        super.onUpdate();
        if (!this.world.isRemote && this.ticksExisted > 100) 
        {
            this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 3.5F, false, true);
            this.setDead();
        }
    }

    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.entityHit != null)
            {
                Entity target = result.entityHit;
                if (target instanceof MultiPartEntityPart) 
                {
                    MultiPartEntityPart part = (MultiPartEntityPart) target;
                    if (part.parent != null && part.parent instanceof Entity)
                        target = (Entity) part.parent;
                }
                target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 25.0F);
                this.applyEnchantments(this.shootingEntity, target);
            }

            this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 3.5F, false, true);
            this.setDead();
        }
    }

    @Override
    protected boolean isFireballFiery()
    {
        return false;
    }
}
