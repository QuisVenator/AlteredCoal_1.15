package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SnowMobile2EntityModel<T extends SnowMobileEntity> extends EntityModel<T> {
	private final ModelRenderer vehicle;
	private final ModelRenderer front;
	private final ModelRenderer body;
	private final ModelRenderer frontinclined;
	private final ModelRenderer sidecovers;
	private final ModelRenderer frontextensionright;
	private final ModelRenderer frontextensionleft;
	private final ModelRenderer smootherright;
	private final ModelRenderer smootherleft;
	private final ModelRenderer connectors;
	private final ModelRenderer skier;
	private final ModelRenderer connectors2;
	private final ModelRenderer rightconnector;
	private final ModelRenderer leftconnector;
	private final ModelRenderer straightplanks;
	private final ModelRenderer inclinedfrontplank;
	private final ModelRenderer steeringwheel;
	private final ModelRenderer connector;
	private final ModelRenderer steering;
	private final ModelRenderer left;
	private final ModelRenderer right;
	private final ModelRenderer seat;
	private final ModelRenderer backwing;
	private final ModelRenderer extension;
	private final ModelRenderer guardabarro;
	private final ModelRenderer backseatinclined;
	private final ModelRenderer wheel;
	private final ModelRenderer wheelBorderElement1;
	private final ModelRenderer wheelBorderElement2;
	private final ModelRenderer wheelBorderElement3;
	private final ModelRenderer wheelBorderElement5;
	private final ModelRenderer wheelBorderElement4;
	private final ModelRenderer wheelBorderElement6;
	private final ModelRenderer wheelBorderElement7;
	private final ModelRenderer wheelBorderElement8;

	public SnowMobile2EntityModel() {
		textureWidth = 512;
		textureHeight = 512;

		vehicle = new ModelRenderer(this);
		vehicle.setRotationPoint(0.0F, 24.0F, 0.0F);

		front = new ModelRenderer(this);
		front.setRotationPoint(0.0F, 0.0F, 0.0F);
		vehicle.addChild(front);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -15.0F, 0.0F);
		front.addChild(body);
		body.setTextureOffset(0, 190).addBox( -8.0F, 6.0F, -24.0F, 16, 2, 12, 0.0F, false);

		body.setTextureOffset(0, 205).addBox( -8.0F, 0.0F, -17.0F, 16, 6, 5, 0.0F, false);

		body.setTextureOffset(0, 316).addBox( -9.0F, 7.5279F, -32.3779F, 18, 4, 12, 0.0F, false);


		frontinclined = new ModelRenderer(this);
		frontinclined.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(frontinclined, 0.3491F, 0.0F, 0.0F);
		body.addChild(frontinclined);
		frontinclined.setTextureOffset(0, 288).addBox( -9.0F, -4.0F, -33.0F, 18, 4, 20, 0.0F, false);


		sidecovers = new ModelRenderer(this);
		sidecovers.setRotationPoint(10.0F, 8.0F, -20.0F);
		setRotationAngle(sidecovers, 0.4363F, 0.0F, 0.0F);
		body.addChild(sidecovers);
		sidecovers.setTextureOffset(0, 244).addBox( -1.0F, -3.0F, -1.0F, 1, 5, 9, 0.0F, false);

		sidecovers.setTextureOffset(0, 244).addBox( -20.0F, -3.0F, -1.0F, 1, 5, 9, 0.0F, true);


		frontextensionright = new ModelRenderer(this);
		frontextensionright.setRotationPoint(-20.0F, 0.0F, 0.0F);
		setRotationAngle(frontextensionright, -0.3491F, 0.0F, 0.0F);
		sidecovers.addChild(frontextensionright);
		frontextensionright.setTextureOffset(0, 267).addBox( 0.0F, -1.7786F, -9.2557F, 1, 4, 9, 0.0F, true);


		frontextensionleft = new ModelRenderer(this);
		frontextensionleft.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(frontextensionleft, -0.3491F, 0.0F, 0.0F);
		sidecovers.addChild(frontextensionleft);
		frontextensionleft.setTextureOffset(0, 267).addBox( -1.0F, -1.7786F, -9.2557F, 1, 4, 9, 0.0F, false);


		smootherright = new ModelRenderer(this);
		smootherright.setRotationPoint(-20.0F, 0.9063F, -0.4226F);
		setRotationAngle(smootherright, -0.1745F, 0.0F, 0.0F);
		sidecovers.addChild(smootherright);
		smootherright.setTextureOffset(0, 227).addBox( 0.0F, -4.0F, -7.0F, 1, 2, 10, 0.0F, true);


		smootherleft = new ModelRenderer(this);
		smootherleft.setRotationPoint(0.0F, 0.9063F, -0.4226F);
		setRotationAngle(smootherleft, -0.1745F, 0.0F, 0.0F);
		sidecovers.addChild(smootherleft);
		smootherleft.setTextureOffset(0, 227).addBox( -1.0F, -4.0F, -7.0F, 1, 2, 10, 0.0F, false);


		connectors = new ModelRenderer(this);
		connectors.setRotationPoint(0.0F, 0.0F, 0.0F);
		front.addChild(connectors);
		connectors.setTextureOffset(0, 341).addBox( 2.0F, -7.0F, -27.0F, 2, 2, 28, 0.0F, true);

		connectors.setTextureOffset(0, 341).addBox( -4.0F, -7.0F, -27.0F, 2, 2, 28, 0.0F, false);


		skier = new ModelRenderer(this);
		skier.setRotationPoint(0.0F, 0.0F, 0.0F);
		front.addChild(skier);

		connectors2 = new ModelRenderer(this);
		connectors2.setRotationPoint(0.0F, 0.0F, 0.0F);
		skier.addChild(connectors2);

		rightconnector = new ModelRenderer(this);
		rightconnector.setRotationPoint(-4.0F, -19.0F, -21.0F);
		setRotationAngle(rightconnector, 1.309F, 0.0F, 0.1745F);
		connectors2.addChild(rightconnector);
		rightconnector.setTextureOffset(0, 171).addBox( -2.0F, -1.0F, -20.0F, 2, 2, 8, 0.0F, false);


		leftconnector = new ModelRenderer(this);
		leftconnector.setRotationPoint(4.0F, -19.0F, -21.0F);
		setRotationAngle(leftconnector, 1.309F, 0.0F, -0.1745F);
		connectors2.addChild(leftconnector);
		leftconnector.setTextureOffset(0, 171).addBox( 0.0F, -1.0F, -20.0F, 2, 2, 8, 0.0F, true);


		straightplanks = new ModelRenderer(this);
		straightplanks.setRotationPoint(0.0F, 0.0F, 0.0F);
		skier.addChild(straightplanks);
		straightplanks.setTextureOffset(0, 486).addBox( -10.0F, -1.0F, -36.0F, 4, 1, 21, 0.0F, false);

		straightplanks.setTextureOffset(0, 486).addBox( 6.0F, -1.0F, -36.0F, 4, 1, 21, 0.0F, true);


		inclinedfrontplank = new ModelRenderer(this);
		inclinedfrontplank.setRotationPoint(0.0F, 12.0F, -2.0F);
		setRotationAngle(inclinedfrontplank, -0.3491F, 0.0F, 0.0F);
		skier.addChild(inclinedfrontplank);
		inclinedfrontplank.setTextureOffset(0, 486).addBox( -10.0F, -0.6476F, -41.0538F, 4, 1, 5, 0.0F, false);

		inclinedfrontplank.setTextureOffset(0, 486).addBox( 6.0F, -0.6476F, -41.0538F, 4, 1, 5, 0.0F, true);


		steeringwheel = new ModelRenderer(this);
		steeringwheel.setRotationPoint(0.0F, 0.0F, 0.0F);
		front.addChild(steeringwheel);

		connector = new ModelRenderer(this);
		connector.setRotationPoint(0.0F, -14.0F, -14.0F);
		setRotationAngle(connector, -0.8727F, 0.0F, 0.0F);
		steeringwheel.addChild(connector);
		connector.setTextureOffset(75, 0).addBox( -3.5F, -5.0F, -1.0F, 7, 7, 3, 0.0F, false);


		steering = new ModelRenderer(this);
		steering.setRotationPoint(0.0F, 0.0F, 0.0F);
		steeringwheel.addChild(steering);
		steering.setTextureOffset(0, 165).addBox( -5.0F, -17.0F, -10.0F, 10, 1, 1, 0.0F, false);


		left = new ModelRenderer(this);
		left.setRotationPoint(-5.0F, -14.0F, -10.0F);
		setRotationAngle(left, 0.0F, 0.6109F, 0.0F);
		steering.addChild(left);
		left.setTextureOffset(0, 165).addBox( -5.0F, -3.0F, 0.0F, 5, 1, 1, 0.0F, false);


		right = new ModelRenderer(this);
		right.setRotationPoint(5.0F, -14.0F, -10.0F);
		setRotationAngle(right, 0.0F, -0.6109F, 0.0F);
		steering.addChild(right);
		right.setTextureOffset(0, 165).addBox( 0.0F, -3.0F, 0.0F, 5, 1, 1, 0.0F, true);


		seat = new ModelRenderer(this);
		seat.setRotationPoint(0.0F, 0.0F, 0.0F);
		vehicle.addChild(seat);
		seat.setTextureOffset(0, 377).addBox( -6.0F, -6.0F, -10.0F, 12, 2, 20, 0.0F, false);

		seat.setTextureOffset(0, 436).addBox( 5.0F, -8.0F, 0.0F, 3, 5, 11, 0.0F, false);

		seat.setTextureOffset(0, 436).addBox( -8.0F, -8.0F, 0.0F, 3, 5, 11, 0.0F, true);

		seat.setTextureOffset(0, 456).addBox( -6.0F, -9.0F, -9.0F, 11, 3, 19, 0.0F, false);


		backwing = new ModelRenderer(this);
		backwing.setRotationPoint(0.0F, 4.0F, 3.0F);
		setRotationAngle(backwing, 0.0873F, 0.0F, 0.0F);
		seat.addChild(backwing);

		extension = new ModelRenderer(this);
		extension.setRotationPoint(0.0F, 0.0F, 0.0F);
		backwing.addChild(extension);
		extension.setTextureOffset(0, 404).addBox( -4.0F, -11.0F, 7.9F, 8, 3, 10, 0.0F, false);


		guardabarro = new ModelRenderer(this);
		guardabarro.setRotationPoint(0.0F, 0.0F, 0.0F);
		backwing.addChild(guardabarro);
		guardabarro.setTextureOffset(0, 421).addBox( -3.0F, -11.6584F, 15.7273F, 6, 7, 1, 0.0F, false);


		backseatinclined = new ModelRenderer(this);
		backseatinclined.setRotationPoint(0.0F, -8.0F, 10.0F);
		setRotationAngle(backseatinclined, 0.4363F, 0.0F, 0.0F);
		seat.addChild(backseatinclined);
		backseatinclined.setTextureOffset(0, 456).addBox( -4.0F, -1.0F, 0.0F, 8, 2, 5, 0.0F, false);


		wheel = new ModelRenderer(this);
		wheel.setRotationPoint(0.0F, 5.0F, 0.0F);
		vehicle.addChild(wheel);

		wheelBorderElement1 = new ModelRenderer(this);
		wheelBorderElement1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement1);
		wheelBorderElement1.setTextureOffset(0, 117).addBox( -5.0F, -8.0F, 11.4F, 10, 2, 2, 0.0F, true);


		wheelBorderElement2 = new ModelRenderer(this);
		wheelBorderElement2.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(wheelBorderElement2, 0.7854F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement2);
		wheelBorderElement2.setTextureOffset(0, 128).addBox( -5.0F, 0.1213F, 11.035F, 10, 2, 2, 0.0F, true);


		wheelBorderElement3 = new ModelRenderer(this);
		wheelBorderElement3.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(wheelBorderElement3, 1.5708F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement3);
		wheelBorderElement3.setTextureOffset(0, 136).addBox( -5.0F, -6.4142F, 4.6142F, 10, 16, 4, 0.0F, false);


		wheelBorderElement5 = new ModelRenderer(this);
		wheelBorderElement5.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(wheelBorderElement5, 0.7854F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement5);
		wheelBorderElement5.setTextureOffset(69, 114).addBox( -5.0F, -11.2066F, -0.2929F, 10, 2, 2, 0.0F, true);


		wheelBorderElement4 = new ModelRenderer(this);
		wheelBorderElement4.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(wheelBorderElement4, 0.7854F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement4);
		wheelBorderElement4.setTextureOffset(69, 128).addBox( -5.0F, -10.1924F, -1.6787F, 10, 2, 2, 0.0F, true);


		wheelBorderElement6 = new ModelRenderer(this);
		wheelBorderElement6.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(wheelBorderElement6, 0.7854F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement6);

		wheelBorderElement7 = new ModelRenderer(this);
		wheelBorderElement7.setRotationPoint(0.0F, 0.0F, -3.0F);
		wheel.addChild(wheelBorderElement7);
		wheelBorderElement7.setTextureOffset(70, 141).addBox( -5.0F, -8.0F, -2.4142F, 10, 2, 2, 0.0F, true);


		wheelBorderElement8 = new ModelRenderer(this);
		wheelBorderElement8.setRotationPoint(0.0F, 0.0F, 6.0F);
		setRotationAngle(wheelBorderElement8, 0.7854F, 0.0F, 0.0F);
		wheel.addChild(wheelBorderElement8);
		wheelBorderElement8.setTextureOffset(70, 153).addBox( -5.0F, -0.5858F, 7.8995F, 10, 2, 2, 0.0F, true);

	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		vehicle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void renderBody(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		seat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		wheel.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void renderSteering(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		front.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void rotateSteering(float angle) {
		this.front.rotateAngleY += angle;
	}
}