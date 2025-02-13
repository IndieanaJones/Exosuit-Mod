package jones.exosuitmod.entity.model;

import jones.exosuitmod.entity.EntityPatriotExosuit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPatriotExosuit extends ModelBase 
{
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer RightLeg;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer RocketLauncher;
	private final ModelRenderer RocketSpot;
	private final ModelRenderer Minigun;
	private final ModelRenderer Barrel;

	public ModelPatriotExosuit() 
	{
		textureWidth = 256;
		textureHeight = 256;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -12.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 60, -12.0F, -8.0F, -12.0F, 24, 16, 24, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -8.0F, 0.0F);
		body.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 0, -17.0F, -28.0F, -15.0F, 34, 28, 32, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 100, -13.0F, -26.0F, -20.0F, 26, 35, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 68, 142, 17.0F, -28.0F, -5.0F, 4, 20, 20, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 116, 142, -21.0F, -28.0F, -5.0F, 4, 20, 20, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 56, 143, 13.0F, -50.0F, 10.0F, 2, 22, 2, 0.0F, false));

		RocketLauncher = new ModelRenderer(this);
		RocketLauncher.setRotationPoint(21.0F, -18.0F, 5.0F);
		head.addChild(RocketLauncher);
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 96, 60, 0.0F, -11.0F, -16.0F, 13, 24, 28, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 68, 112, 1.0F, -13.0F, -16.0F, 11, 2, 28, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 82, 100, 5.0F, 5.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 76, 108, 5.0F, 1.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 84, 108, 5.0F, -3.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 56, 167, 5.0F, -7.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 68, 107, 1.0F, 7.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 156, 182, 1.0F, 3.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 0, 194, 1.0F, -1.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 8, 194, 1.0F, -5.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 16, 194, 1.0F, -9.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 82, 104, 9.0F, 7.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 56, 171, 9.0F, 3.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 56, 175, 9.0F, -1.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 140, 182, 9.0F, -5.0F, -17.0F, 3, 3, 1, 0.0F, false));
		RocketLauncher.cubeList.add(new ModelBox(RocketLauncher, 148, 182, 9.0F, -9.0F, -17.0F, 3, 3, 1, 0.0F, false));

		RocketSpot = new ModelRenderer(this);
		RocketSpot.setRotationPoint(6.0F, 0.0F, -16.0F);
		RocketLauncher.addChild(RocketSpot);
		

		Minigun = new ModelRenderer(this);
		Minigun.setRotationPoint(-21.0F, -18.0F, 5.0F);
		head.addChild(Minigun);
		Minigun.cubeList.add(new ModelBox(Minigun, 132, 0, -10.0F, -1.0F, -11.0F, 10, 10, 22, 0.0F, false));
		Minigun.cubeList.add(new ModelBox(Minigun, 164, 140, -10.0F, -8.0F, -9.0F, 10, 7, 18, 0.0F, false));
		Minigun.cubeList.add(new ModelBox(Minigun, 178, 95, -8.0F, 9.0F, -7.0F, 6, 3, 12, 0.0F, false));
		Minigun.cubeList.add(new ModelBox(Minigun, 96, 182, -9.0F, 12.0F, -8.0F, 8, 8, 14, 0.0F, false));
		Minigun.cubeList.add(new ModelBox(Minigun, 174, 188, -12.0F, 18.0F, -5.0F, 6, 6, 12, 0.0F, false));

		Barrel = new ModelRenderer(this);
		Barrel.setRotationPoint(-5.0F, 16.0F, -8.0F);
		Minigun.addChild(Barrel);
		Barrel.cubeList.add(new ModelBox(Barrel, 68, 100, -3.0F, -3.0F, -1.0F, 6, 6, 1, 0.0F, false));
		Barrel.cubeList.add(new ModelBox(Barrel, 132, 32, -1.0F, -2.0F, -27.0F, 2, 2, 26, 0.0F, false));
		Barrel.cubeList.add(new ModelBox(Barrel, 0, 143, -2.0F, 0.0F, -27.0F, 2, 2, 26, 0.0F, false));
		Barrel.cubeList.add(new ModelBox(Barrel, 146, 112, 0.0F, 0.0F, -27.0F, 2, 2, 26, 0.0F, false));

		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-12.0F, 3.0F, 5.0F);
		body.addChild(RightLeg);
		RightLeg.cubeList.add(new ModelBox(RightLeg, 164, 165, -10.0F, 28.0F, -15.0F, 10, 5, 18, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-5.0F, 20.5801F, -6.067F);
		RightLeg.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.5236F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 188, 32, -4.0F, -8.0F, -4.5F, 8, 16, 9, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-5.0F, 5.0607F, -5.6464F);
		RightLeg.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 178, 60, -5.0F, -12.5F, -5.0F, 10, 25, 10, 0.0F, false));

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(12.0F, 3.0F, 5.0F);
		body.addChild(LeftLeg);
		LeftLeg.cubeList.add(new ModelBox(LeftLeg, 0, 171, 0.0F, 28.0F, -15.0F, 10, 5, 18, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		LeftLeg.addChild(cube_r3);
		setRotationAngle(cube_r3, -0.5236F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 140, 188, 1.0F, 12.5801F, 0.433F, 8, 16, 9, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
		LeftLeg.addChild(cube_r4);
		setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 56, 182, 0.0F, -4.4393F, -5.6464F, 10, 25, 10, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
	{
		body.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) 
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) 
	{
        final EntityPatriotExosuit exosuit = (EntityPatriotExosuit)entityIn;
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * -limbSwingAmount;
		this.Minigun.rotateAngleX = headPitch * 0.017453292F;
		this.RocketLauncher.rotateAngleX = headPitch * 0.017453292F;
	}
}