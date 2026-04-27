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

public class EntityPatriotBullet extends EntityFireball
{
    public EntityPatriotBullet(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
    }

    @SideOnly(Side.CLIENT)
    public EntityPatriotBullet(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public EntityPatriotBullet(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public void onUpdate() 
    {
        super.onUpdate();
        if (!this.world.isRemote && this.ticksExisted > 100) 
        {
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
                target.hurtResistantTime = 0;
                target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 2.5F);
                target.motionX *= 0.5;  // reduce horizontal push
                target.motionZ *= 0.5;
                this.applyEnchantments(this.shootingEntity, target);
            }
            this.setDead();
        }
    }

    @Override
    protected boolean isFireballFiery()
    {
        return false;
    }
}
