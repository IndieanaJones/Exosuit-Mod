package jones.exosuitmod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPatriotBullet extends EntityFireball
{
    public EntityPatriotBullet(World worldIn)
    {
        super(worldIn);
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

        // Get the motion vector (velocity)
        double motionX = this.motionX;
        double motionY = this.motionY;
        double motionZ = this.motionZ;

        // Calculate the yaw (rotation around the Y-axis)
        this.rotationYaw = (float) (Math.atan2(motionZ, motionX) * (180.0D / Math.PI));

        // Calculate the pitch (rotation around the X-axis)
        double horizontalSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);
        this.rotationPitch = (float) (Math.atan2(motionY, horizontalSpeed) * (180.0D / Math.PI));

        // Update the rotation
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.entityHit != null)
            {
                result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 4.0F);
                this.applyEnchantments(this.shootingEntity, result.entityHit);
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
