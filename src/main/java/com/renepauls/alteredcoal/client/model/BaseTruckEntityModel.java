package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.BaseTruckEntity;
import com.renepauls.alteredcoal.entities.SnowMobileEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BaseTruckEntityModel<T extends BaseTruckEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer wheels;
	private final ModelRenderer wheel_backLeft1;
	private final ModelRenderer wheel_backLeft2;
	private final ModelRenderer wheel_backRight1;
	private final ModelRenderer wheel_backRight2;
	private final ModelRenderer wheel_frontLeft1;
	private final ModelRenderer wheel_forntRight1;
	private final ModelRenderer achse;
	private final ModelRenderer achse_back1;
	private final ModelRenderer achse_back2;
	private final ModelRenderer achse_front;
	private final ModelRenderer frame;
	private final ModelRenderer side;
	private final ModelRenderer wheeltop;
	private final ModelRenderer frontrightconnector;
	private final ModelRenderer frontrightdown;
	private final ModelRenderer frontrightup;
	private final ModelRenderer frontleftup;
	private final ModelRenderer frontleftdown;
	private final ModelRenderer frontleftconnector;
	private final ModelRenderer backleftconnector1;
	private final ModelRenderer backleft1down;
	private final ModelRenderer backright1down;
	private final ModelRenderer backleft1up;
	private final ModelRenderer backleft2down;
	private final ModelRenderer backright2down;
	private final ModelRenderer backleft2up;
	private final ModelRenderer backright2up;
	private final ModelRenderer backright1up;
	private final ModelRenderer backleftconnector2;
	private final ModelRenderer backrightconnector2;
	private final ModelRenderer backrightconnector1;
	private final ModelRenderer kabine;
	private final ModelRenderer motor;
	private final ModelRenderer kabineFrame;
	private final ModelRenderer rightFrontBeam;
	private final ModelRenderer leftFrontBeam;
	private final ModelRenderer roof;
	private final ModelRenderer roofDetail;
	private final ModelRenderer inclinedFront;
	private final ModelRenderer inlinedRightRamp;
	private final ModelRenderer inlinedLeftRamp;
	private final ModelRenderer seats;
	private final ModelRenderer leftSeat;
	private final ModelRenderer leftSeatBack;
	private final ModelRenderer steerConnector;
	private final ModelRenderer steer;
	private final ModelRenderer leftToRightDown;
	private final ModelRenderer leftToRightUp;

	public BaseTruckEntityModel() {
		textureWidth = 304;
		textureHeight = 304;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, -16.0F);
		setRotationAngle(body, 0.0F, 3.1416F, 0.0F);

		wheels = new ModelRenderer(this);
		wheels.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(wheels);

		wheel_backLeft1 = new ModelRenderer(this);
		wheel_backLeft1.setRotationPoint(-15.0F, 0.0F, -33.0F);
		wheels.addChild(wheel_backLeft1);
		wheel_backLeft1.setTextureOffset(48, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backLeft1.setTextureOffset(120, 249).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backLeft1.setTextureOffset(0, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backLeft1.setTextureOffset(26, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backLeft2 = new ModelRenderer(this);
		wheel_backLeft2.setRotationPoint(-15.0F, 0.0F, -45.0F);
		wheels.addChild(wheel_backLeft2);
		wheel_backLeft2.setTextureOffset(60, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backLeft2.setTextureOffset(136, 249).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backLeft2.setTextureOffset(20, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backLeft2.setTextureOffset(50, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backRight1 = new ModelRenderer(this);
		wheel_backRight1.setRotationPoint(17.0F, 0.0F, -33.0F);
		wheels.addChild(wheel_backRight1);
		wheel_backRight1.setTextureOffset(72, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backRight1.setTextureOffset(152, 249).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backRight1.setTextureOffset(40, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backRight1.setTextureOffset(74, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backRight2 = new ModelRenderer(this);
		wheel_backRight2.setRotationPoint(17.0F, 0.0F, -45.0F);
		wheels.addChild(wheel_backRight2);
		wheel_backRight2.setTextureOffset(84, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backRight2.setTextureOffset(168, 249).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backRight2.setTextureOffset(60, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backRight2.setTextureOffset(98, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_frontLeft1 = new ModelRenderer(this);
		wheel_frontLeft1.setRotationPoint(-15.0F, 0.0F, 0.0F);
		wheels.addChild(wheel_frontLeft1);
		wheel_frontLeft1.setTextureOffset(108, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(0, 261).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(100, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(146, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_forntRight1 = new ModelRenderer(this);
		wheel_forntRight1.setRotationPoint(17.0F, 0.0F, 0.0F);
		wheels.addChild(wheel_forntRight1);
		wheel_forntRight1.setTextureOffset(96, 261).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_forntRight1.setTextureOffset(184, 249).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_forntRight1.setTextureOffset(80, 249).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_forntRight1.setTextureOffset(122, 228).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		achse = new ModelRenderer(this);
		achse.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(achse);

		achse_back1 = new ModelRenderer(this);
		achse_back1.setRotationPoint(0.0F, -3.0F, -45.0F);
		achse.addChild(achse_back1);
		achse_back1.setTextureOffset(184, 155).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_back1.setTextureOffset(146, 142).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		achse_back2 = new ModelRenderer(this);
		achse_back2.setRotationPoint(0.0F, -3.0F, -33.0F);
		achse.addChild(achse_back2);
		achse_back2.setTextureOffset(0, 184).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_back2.setTextureOffset(146, 146).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		achse_front = new ModelRenderer(this);
		achse_front.setRotationPoint(0.0F, -3.0F, 0.0F);
		achse.addChild(achse_front);
		achse_front.setTextureOffset(0, 188).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_front.setTextureOffset(0, 155).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		frame = new ModelRenderer(this);
		frame.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(frame);
		frame.setTextureOffset(0, 0).addBox( -14.0F, -10.0F, -53.0F, 27, 4, 62, 0.0F, false);

		frame.setTextureOffset(0, 206).addBox( -17.0F, -12.0F, -24.0F, 9, 7, 15, 0.0F, false);

		frame.setTextureOffset(134, 184).addBox( 7.0F, -12.0F, -24.0F, 9, 7, 15, 0.0F, false);

		frame.setTextureOffset(154, 88).addBox( -14.0F, -13.0F, -52.0F, 12, 3, 23, 0.0F, false);

		frame.setTextureOffset(84, 88).addBox( 1.0F, -13.0F, -52.0F, 12, 3, 23, 0.0F, false);

		frame.setTextureOffset(126, 261).addBox( -2.0F, -13.0F, -33.0F, 3, 3, 4, 0.0F, false);

		frame.setTextureOffset(160, 280).addBox( -2.0F, -13.0F, -52.0F, 3, 3, 2, 0.0F, false);

		frame.setTextureOffset(144, 114).addBox( -6.0F, -13.0F, -29.0F, 11, 3, 21, 0.0F, false);


		side = new ModelRenderer(this);
		side.setRotationPoint(0.0F, 0.0F, 0.0F);
		frame.addChild(side);

		wheeltop = new ModelRenderer(this);
		wheeltop.setRotationPoint(0.0F, 0.0F, 0.0F);
		side.addChild(wheeltop);

		frontrightconnector = new ModelRenderer(this);
		frontrightconnector.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightconnector);
		frontrightconnector.setTextureOffset(112, 217).addBox( -19.0F, -12.0981F, -4.366F, 5, 2, 9, 0.0F, false);


		frontrightdown = new ModelRenderer(this);
		frontrightdown.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(frontrightdown, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightdown);
		frontrightdown.setTextureOffset(28, 280).addBox( 0.0F, 19.0F, 38.8372F, 5, 5, 2, 0.0F, false);


		frontrightup = new ModelRenderer(this);
		frontrightup.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(frontrightup, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightup);
		frontrightup.setTextureOffset(14, 280).addBox( 0.0F, -25.5F, 37.9711F, 5, 5, 2, 0.0F, false);


		frontleftup = new ModelRenderer(this);
		frontleftup.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(frontleftup, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftup);
		frontleftup.setTextureOffset(0, 280).addBox( -32.0F, -25.5F, 37.9711F, 5, 5, 2, 0.0F, false);


		frontleftdown = new ModelRenderer(this);
		frontleftdown.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(frontleftdown, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftdown);
		frontleftdown.setTextureOffset(126, 268).addBox( -32.0F, 19.0F, 38.8372F, 5, 5, 2, 0.0F, false);


		frontleftconnector = new ModelRenderer(this);
		frontleftconnector.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftconnector);
		frontleftconnector.setTextureOffset(112, 206).addBox( -51.0F, -12.0981F, -4.366F, 5, 2, 9, 0.0F, false);


		backleftconnector1 = new ModelRenderer(this);
		backleftconnector1.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(backleftconnector1);
		backleftconnector1.setTextureOffset(168, 206).addBox( -19.0F, -12.0981F, -48.366F, 5, 2, 8, 0.0F, false);


		backleft1down = new ModelRenderer(this);
		backleft1down.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(backleft1down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft1down);
		backleft1down.setTextureOffset(70, 280).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backright1down = new ModelRenderer(this);
		backright1down.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(backright1down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright1down);
		backright1down.setTextureOffset(84, 280).addBox( -32.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft1up = new ModelRenderer(this);
		backleft1up.setRotationPoint(-18.0F, -9.0F, -50.0F);
		setRotationAngle(backleft1up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft1up);
		backleft1up.setTextureOffset(126, 280).addBox( -1.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft2down = new ModelRenderer(this);
		backleft2down.setRotationPoint(-19.0F, -8.0F, -28.0F);
		setRotationAngle(backleft2down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft2down);
		backleft2down.setTextureOffset(42, 280).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backright2down = new ModelRenderer(this);
		backright2down.setRotationPoint(13.0F, -8.0F, -28.0F);
		setRotationAngle(backright2down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright2down);
		backright2down.setTextureOffset(56, 280).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft2up = new ModelRenderer(this);
		backleft2up.setRotationPoint(-19.0F, -9.0F, -50.0F);
		setRotationAngle(backleft2up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft2up);
		backleft2up.setTextureOffset(98, 280).addBox( 0.0F, -9.0F, 9.3923F, 5, 5, 2, 0.0F, false);


		backright2up = new ModelRenderer(this);
		backright2up.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(backright2up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright2up);
		backright2up.setTextureOffset(112, 280).addBox( 0.0F, -9.0F, 9.3923F, 5, 5, 2, 0.0F, false);


		backright1up = new ModelRenderer(this);
		backright1up.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(backright1up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright1up);
		backright1up.setTextureOffset(140, 280).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleftconnector2 = new ModelRenderer(this);
		backleftconnector2.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(backleftconnector2);
		backleftconnector2.setTextureOffset(140, 206).addBox( -19.0F, -12.0981F, -37.366F, 5, 2, 9, 0.0F, false);


		backrightconnector2 = new ModelRenderer(this);
		backrightconnector2.setRotationPoint(0.0F, 0.0F, 0.0F);
		wheeltop.addChild(backrightconnector2);
		backrightconnector2.setTextureOffset(140, 217).addBox( -19.0F, -12.0981F, -37.366F, 5, 2, 9, 0.0F, false);


		backrightconnector1 = new ModelRenderer(this);
		backrightconnector1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wheeltop.addChild(backrightconnector1);
		backrightconnector1.setTextureOffset(0, 228).addBox( -19.0F, -12.0981F, -48.366F, 5, 2, 8, 0.0F, false);


		kabine = new ModelRenderer(this);
		kabine.setRotationPoint(0.0F, 0.0F, 0.0F);
		frame.addChild(kabine);

		motor = new ModelRenderer(this);
		motor.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabine.addChild(motor);
		motor.setTextureOffset(178, 0).addBox( -17.0F, -14.0F, -7.0F, 33, 4, 25, 0.0F, false);

		motor.setTextureOffset(102, 66).addBox( -17.0F, -10.0F, 6.0F, 33, 8, 12, 0.0F, false);


		kabineFrame = new ModelRenderer(this);
		kabineFrame.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabine.addChild(kabineFrame);
		kabineFrame.setTextureOffset(132, 155).addBox( -17.0F, -18.0F, -7.0F, 1, 4, 25, 0.0F, false);

		kabineFrame.setTextureOffset(0, 192).addBox( -16.0F, -18.0F, 17.0F, 31, 4, 1, 0.0F, false);

		kabineFrame.setTextureOffset(80, 155).addBox( 15.0F, -18.0F, -7.0F, 1, 4, 25, 0.0F, false);

		kabineFrame.setTextureOffset(0, 114).addBox( -16.0F, -34.0F, -8.0F, 31, 25, 3, 0.0F, false);

		kabineFrame.setTextureOffset(204, 228).addBox( 15.0F, -34.0F, -7.0F, 1, 16, 5, 0.0F, false);

		kabineFrame.setTextureOffset(192, 228).addBox( -17.0F, -34.0F, -7.0F, 1, 16, 5, 0.0F, false);


		rightFrontBeam = new ModelRenderer(this);
		rightFrontBeam.setRotationPoint(-28.0F, -24.0F, 15.0F);
		setRotationAngle(rightFrontBeam, 0.1745F, 0.0F, 0.0F);
		kabineFrame.addChild(rightFrontBeam);
		rightFrontBeam.setTextureOffset(16, 261).addBox( 11.0F, -9.5702F, -1.0875F, 1, 16, 3, 0.0F, false);

		rightFrontBeam.setTextureOffset(32, 261).addBox( 11.0F, -9.5702F, 0.9125F, 3, 16, 1, 0.0F, false);


		leftFrontBeam = new ModelRenderer(this);
		leftFrontBeam.setRotationPoint(0.0F, -24.0F, 15.0F);
		setRotationAngle(leftFrontBeam, 0.1745F, 0.0F, 0.0F);
		kabineFrame.addChild(leftFrontBeam);
		leftFrontBeam.setTextureOffset(24, 261).addBox( 15.0F, -9.5702F, -1.0875F, 1, 16, 3, 0.0F, false);

		leftFrontBeam.setTextureOffset(40, 261).addBox( 13.0F, -9.5702F, 0.9125F, 3, 16, 1, 0.0F, false);


		roof = new ModelRenderer(this);
		roof.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabineFrame.addChild(roof);
		roof.setTextureOffset(0, 66).addBox( -16.0F, -34.0F, -5.0F, 31, 2, 20, 0.0F, false);


		roofDetail = new ModelRenderer(this);
		roofDetail.setRotationPoint(0.0F, 0.0F, 0.0F);
		roof.addChild(roofDetail);
		roofDetail.setTextureOffset(192, 66).addBox( -13.0F, -35.0F, -8.0F, 25, 1, 19, 0.0F, false);

		roofDetail.setTextureOffset(0, 88).addBox( -13.0F, -36.0F, -8.0F, 25, 1, 17, 0.0F, false);

		roofDetail.setTextureOffset(224, 88).addBox( -13.0F, -37.0F, -8.0F, 25, 1, 15, 0.0F, false);

		roofDetail.setTextureOffset(68, 114).addBox( -13.0F, -38.0F, -8.0F, 25, 1, 13, 0.0F, false);

		roofDetail.setTextureOffset(74, 142).addBox( -13.0F, -39.9019F, -7.9622F, 25, 2, 11, 0.0F, false);


		inclinedFront = new ModelRenderer(this);
		inclinedFront.setRotationPoint(0.0F, -39.0F, 11.0F);
		setRotationAngle(inclinedFront, -0.5236F, 0.0F, 0.0F);
		roofDetail.addChild(inclinedFront);
		inclinedFront.setTextureOffset(208, 114).addBox( -13.0F, 3.0F, -7.0F, 25, 2, 12, 0.0F, false);


		inlinedRightRamp = new ModelRenderer(this);
		inlinedRightRamp.setRotationPoint(5.0F, -12.0F, -25.0F);
		setRotationAngle(inlinedRightRamp, 0.0F, -0.8727F, 0.0F);
		frame.addChild(inlinedRightRamp);
		inlinedRightRamp.setTextureOffset(48, 206).addBox( -4.0F, -1.0F, -8.0F, 5, 3, 11, 0.0F, false);


		inlinedLeftRamp = new ModelRenderer(this);
		inlinedLeftRamp.setRotationPoint(-5.0F, -12.0F, -25.0F);
		setRotationAngle(inlinedLeftRamp, 0.0F, 0.8727F, 0.0F);
		frame.addChild(inlinedLeftRamp);
		inlinedLeftRamp.setTextureOffset(80, 206).addBox( -1.0F, -1.0F, -8.0F, 5, 3, 11, 0.0F, false);


		seats = new ModelRenderer(this);
		seats.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(seats);
		seats.setTextureOffset(160, 285).addBox( -9.0F, -22.0F, 10.0F, 2, 2, 2, 0.0F, false);


		leftSeat = new ModelRenderer(this);
		leftSeat.setRotationPoint(0.0F, 0.0F, 0.0F);
		seats.addChild(leftSeat);
		leftSeat.setTextureOffset(0, 142).addBox( -14.0F, -17.0F, 1.0F, 27, 2, 10, 0.0F, false);


		leftSeatBack = new ModelRenderer(this);
		leftSeatBack.setRotationPoint(0.0F, -21.0F, 0.0F);
		setRotationAngle(leftSeatBack, 0.2618F, 0.0F, 0.0F);
		leftSeat.addChild(leftSeatBack);
		leftSeatBack.setTextureOffset(76, 184).addBox( -14.0F, -5.0F, -1.0F, 27, 11, 2, 0.0F, false);


		steerConnector = new ModelRenderer(this);
		steerConnector.setRotationPoint(-8.0F, -20.0F, 12.0F);
		setRotationAngle(steerConnector, -0.8727F, 0.0F, 0.0F);
		seats.addChild(steerConnector);
		steerConnector.setTextureOffset(170, 228).addBox( -1.0F, -1.0F, -1.0F, 2, 2, 9, 0.0F, false);


		steer = new ModelRenderer(this);
		steer.setRotationPoint(44.0F, 3.0F, -27.0F);
		steer.setTextureOffset(108, 273).addBox( -40.0F, -1.0F, 0.0F, 8, 2, 1, 0.0F, false);

		steer.setTextureOffset(154, 280).addBox( -37.0F, -4.0F, 0.0F, 2, 8, 1, 0.0F, false);


		leftToRightDown = new ModelRenderer(this);
		leftToRightDown.setRotationPoint(-32.0F, -2.0F, 0.0F);
		setRotationAngle(leftToRightDown, 0.0F, 0.0F, 0.7854F);
		steer.addChild(leftToRightDown);
		leftToRightDown.setTextureOffset(174, 280).addBox( 1.1213F, 2.2213F, 0.0F, 1, 4, 1, 0.0F, false);

		leftToRightDown.setTextureOffset(170, 280).addBox( -4.9497F, 2.2213F, 0.0F, 1, 4, 1, 0.0F, false);


		leftToRightUp = new ModelRenderer(this);
		leftToRightUp.setRotationPoint(-32.0F, -2.0F, 0.0F);
		setRotationAngle(leftToRightUp, 0.0F, 0.0F, -0.7854F);
		steer.addChild(leftToRightUp);
		leftToRightUp.setTextureOffset(182, 280).addBox( -1.7071F, -3.4355F, 0.0F, 1, 4, 1, 0.0F, false);

		leftToRightUp.setTextureOffset(178, 280).addBox( -7.7782F, -3.4355F, 0.0F, 1, 4, 1, 0.0F, false);
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
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void renderBody(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void renderSteering(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		//TODO add steering
	}
}