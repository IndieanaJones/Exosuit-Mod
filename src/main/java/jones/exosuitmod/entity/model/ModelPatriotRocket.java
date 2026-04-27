package jones.exosuitmod.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPatriotRocket extends ModelBase {
	private final ModelRenderer Base;

	public ModelPatriotRocket() {
		textureWidth = 16;
		textureHeight = 16;

		Base = new ModelRenderer(this);
		Base.setRotationPoint(0.0F, 24.0F, 0.0F);
		Base.cubeList.add(new ModelBox(Base, 0, 8, -1.0F, -2.5F, -3.5F, 2, 2, 1, 0.0F, false));
		Base.cubeList.add(new ModelBox(Base, 0, 0, -1.5F, -3.0F, -2.5F, 3, 3, 5, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Base.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}