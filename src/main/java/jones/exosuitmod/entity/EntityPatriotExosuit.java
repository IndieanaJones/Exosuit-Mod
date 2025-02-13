package jones.exosuitmod.entity;

import jones.exosuitmod.ExosuitMod;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPatriotExosuit extends AbstractExosuit
{
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
        this.setMaxRightClickCooldown(800);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
    }

    public float getEyeHeight()
    {
        return 4F;
    }

    public void travel(float strafe, float vertical, float forward)
    {
        super.travel(strafe, vertical, forward);
        this.limbSwing -= this.limbSwingAmount;
        this.limbSwingAmount = this.limbSwingAmount * 0.75F;
        this.limbSwing += this.limbSwingAmount;
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
}
