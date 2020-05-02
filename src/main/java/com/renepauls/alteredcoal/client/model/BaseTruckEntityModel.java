package com.renepauls.alteredcoal.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.renepauls.alteredcoal.entities.vehicle.BaseTruckEntity;
import com.renepauls.alteredcoal.entities.vehicle.SnowMobileEntity;

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
		textureWidth = 256;
		textureHeight = 256;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, -16.0F);
		setRotationAngle(body, 0.0F, 3.1416F, 0.0F);

		wheels = new ModelRenderer(this);
		wheels.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(wheels);

		wheel_backLeft1 = new ModelRenderer(this);
		wheel_backLeft1.setRotationPoint(-15.0F, 0.0F, -33.0F);
		wheels.addChild(wheel_backLeft1);
		wheel_backLeft1.setTextureOffset(49, 26).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backLeft1.setTextureOffset(0, 115).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backLeft1.setTextureOffset(91, 66).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backLeft1.setTextureOffset(0, 0).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backLeft2 = new ModelRenderer(this);
		wheel_backLeft2.setRotationPoint(-15.0F, 0.0F, -45.0F);
		wheels.addChild(wheel_backLeft2);
		wheel_backLeft2.setTextureOffset(0, 95).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backLeft2.setTextureOffset(67, 115).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backLeft2.setTextureOffset(116, 11).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backLeft2.setTextureOffset(0, 12).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backRight1 = new ModelRenderer(this);
		wheel_backRight1.setRotationPoint(17.0F, 0.0F, -33.0F);
		wheels.addChild(wheel_backRight1);
		wheel_backRight1.setTextureOffset(108, 115).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backRight1.setTextureOffset(173, 62).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backRight1.setTextureOffset(163, 5).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backRight1.setTextureOffset(0, 29).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_backRight2 = new ModelRenderer(this);
		wheel_backRight2.setRotationPoint(17.0F, 0.0F, -45.0F);
		wheels.addChild(wheel_backRight2);
		wheel_backRight2.setTextureOffset(0, 163).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_backRight2.setTextureOffset(173, 74).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_backRight2.setTextureOffset(129, 170).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_backRight2.setTextureOffset(0, 41).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_frontLeft1 = new ModelRenderer(this);
		wheel_frontLeft1.setRotationPoint(-15.0F, 0.0F, 0.0F);
		wheels.addChild(wheel_frontLeft1);
		wheel_frontLeft1.setTextureOffset(92, 181).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(48, 176).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(169, 170).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_frontLeft1.setTextureOffset(0, 78).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		wheel_forntRight1 = new ModelRenderer(this);
		wheel_forntRight1.setRotationPoint(17.0F, 0.0F, 0.0F);
		wheels.addChild(wheel_forntRight1);
		wheel_forntRight1.setTextureOffset(80, 181).addBox( -3.0F, -9.0F, -1.0F, 3, 9, 3, 0.0F, false);

		wheel_forntRight1.setTextureOffset(173, 113).addBox( -3.0F, -8.0F, -2.0F, 3, 7, 5, 0.0F, false);

		wheel_forntRight1.setTextureOffset(149, 170).addBox( -3.0F, -7.0F, -3.0F, 3, 5, 7, 0.0F, false);

		wheel_forntRight1.setTextureOffset(0, 66).addBox( -3.0F, -6.0F, -4.0F, 3, 3, 9, 0.0F, false);


		achse = new ModelRenderer(this);
		achse.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(achse);

		achse_back1 = new ModelRenderer(this);
		achse_back1.setRotationPoint(0.0F, -3.0F, -45.0F);
		achse.addChild(achse_back1);
		achse_back1.setTextureOffset(0, 159).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_back1.setTextureOffset(116, 54).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		achse_back2 = new ModelRenderer(this);
		achse_back2.setRotationPoint(0.0F, -3.0F, -33.0F);
		achse.addChild(achse_back2);
		achse_back2.setTextureOffset(159, 92).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_back2.setTextureOffset(116, 58).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		achse_front = new ModelRenderer(this);
		achse_front.setRotationPoint(0.0F, -3.0F, 0.0F);
		achse.addChild(achse_front);
		achse_front.setTextureOffset(159, 96).addBox( -19.0F, -3.0F, 0.0F, 37, 3, 1, 0.0F, false);

		achse_front.setTextureOffset(116, 88).addBox( -19.0F, -2.0F, -1.0F, 37, 1, 3, 0.0F, false);


		frame = new ModelRenderer(this);
		frame.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(frame);
		frame.setTextureOffset(0, 0).addBox( -14.0F, -10.0F, -53.0F, 27, 4, 62, 0.0F, false);

		frame.setTextureOffset(0, 163).addBox( -17.0F, -12.0F, -24.0F, 9, 7, 15, 0.0F, false);

		frame.setTextureOffset(61, 159).addBox( 7.0F, -12.0F, -24.0F, 9, 7, 15, 0.0F, false);

		frame.setTextureOffset(116, 0).addBox( -14.0F, -13.0F, -52.0F, 12, 3, 23, 0.0F, false);

		frame.setTextureOffset(61, 115).addBox( 1.0F, -13.0F, -52.0F, 12, 3, 23, 0.0F, false);

		frame.setTextureOffset(46, 14).addBox( -2.0F, -13.0F, -33.0F, 3, 3, 4, 0.0F, false);

		frame.setTextureOffset(15, 15).addBox( -2.0F, -13.0F, -52.0F, 3, 3, 2, 0.0F, false);

		frame.setTextureOffset(0, 133).addBox( -6.0F, -13.0F, -29.0F, 11, 3, 21, 0.0F, false);


		side = new ModelRenderer(this);
		side.setRotationPoint(0.0F, 0.0F, 0.0F);
		frame.addChild(side);

		wheeltop = new ModelRenderer(this);
		wheeltop.setRotationPoint(0.0F, 0.0F, 0.0F);
		side.addChild(wheeltop);

		frontrightconnector = new ModelRenderer(this);
		frontrightconnector.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightconnector);
		frontrightconnector.setTextureOffset(27, 43).addBox( -19.0F, -12.0981F, -4.366F, 5, 2, 9, 0.0F, false);


		frontrightdown = new ModelRenderer(this);
		frontrightdown.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(frontrightdown, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightdown);
		frontrightdown.setTextureOffset(90, 106).addBox( 0.0F, 19.0F, 38.8372F, 5, 5, 2, 0.0F, false);


		frontrightup = new ModelRenderer(this);
		frontrightup.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(frontrightup, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontrightup);
		frontrightup.setTextureOffset(91, 78).addBox( 0.0F, -25.5F, 37.9711F, 5, 5, 2, 0.0F, false);


		frontleftup = new ModelRenderer(this);
		frontleftup.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(frontleftup, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftup);
		frontleftup.setTextureOffset(48, 0).addBox( -32.0F, -25.5F, 37.9711F, 5, 5, 2, 0.0F, false);


		frontleftdown = new ModelRenderer(this);
		frontleftdown.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(frontleftdown, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftdown);
		frontleftdown.setTextureOffset(46, 43).addBox( -32.0F, 19.0F, 38.8372F, 5, 5, 2, 0.0F, false);


		frontleftconnector = new ModelRenderer(this);
		frontleftconnector.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(frontleftconnector);
		frontleftconnector.setTextureOffset(27, 14).addBox( -51.0F, -12.0981F, -4.366F, 5, 2, 9, 0.0F, false);


		backleftconnector1 = new ModelRenderer(this);
		backleftconnector1.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(backleftconnector1);
		backleftconnector1.setTextureOffset(43, 133).addBox( -19.0F, -12.0981F, -48.366F, 5, 2, 8, 0.0F, false);


		backleft1down = new ModelRenderer(this);
		backleft1down.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(backleft1down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft1down);
		backleft1down.setTextureOffset(51, 163).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backright1down = new ModelRenderer(this);
		backright1down.setRotationPoint(13.0F, -8.0F, -40.0F);
		setRotationAngle(backright1down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright1down);
		backright1down.setTextureOffset(142, 170).addBox( -32.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft1up = new ModelRenderer(this);
		backleft1up.setRotationPoint(-18.0F, -9.0F, -50.0F);
		setRotationAngle(backleft1up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft1up);
		backleft1up.setTextureOffset(176, 157).addBox( -1.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft2down = new ModelRenderer(this);
		backleft2down.setRotationPoint(-19.0F, -8.0F, -28.0F);
		setRotationAngle(backleft2down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft2down);
		backleft2down.setTextureOffset(108, 131).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backright2down = new ModelRenderer(this);
		backright2down.setRotationPoint(13.0F, -8.0F, -28.0F);
		setRotationAngle(backright2down, 0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright2down);
		backright2down.setTextureOffset(43, 143).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleft2up = new ModelRenderer(this);
		backleft2up.setRotationPoint(-19.0F, -9.0F, -50.0F);
		setRotationAngle(backleft2up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backleft2up);
		backleft2up.setTextureOffset(162, 170).addBox( 0.0F, -9.0F, 9.3923F, 5, 5, 2, 0.0F, false);


		backright2up = new ModelRenderer(this);
		backright2up.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(backright2up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright2up);
		backright2up.setTextureOffset(176, 5).addBox( 0.0F, -9.0F, 9.3923F, 5, 5, 2, 0.0F, false);


		backright1up = new ModelRenderer(this);
		backright1up.setRotationPoint(13.0F, -9.0F, -50.0F);
		setRotationAngle(backright1up, -0.5236F, 0.0F, 0.0F);
		wheeltop.addChild(backright1up);
		backright1up.setTextureOffset(178, 145).addBox( 0.0F, -3.5F, -0.134F, 5, 5, 2, 0.0F, false);


		backleftconnector2 = new ModelRenderer(this);
		backleftconnector2.setRotationPoint(32.0F, 0.0F, 0.0F);
		wheeltop.addChild(backleftconnector2);
		backleftconnector2.setTextureOffset(78, 95).addBox( -19.0F, -12.0981F, -37.366F, 5, 2, 9, 0.0F, false);


		backrightconnector2 = new ModelRenderer(this);
		backrightconnector2.setRotationPoint(0.0F, 0.0F, 0.0F);
		wheeltop.addChild(backrightconnector2);
		backrightconnector2.setTextureOffset(94, 161).addBox( -19.0F, -12.0981F, -37.366F, 5, 2, 9, 0.0F, false);


		backrightconnector1 = new ModelRenderer(this);
		backrightconnector1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wheeltop.addChild(backrightconnector1);
		backrightconnector1.setTextureOffset(33, 163).addBox( -19.0F, -12.0981F, -48.366F, 5, 2, 8, 0.0F, false);


		kabine = new ModelRenderer(this);
		kabine.setRotationPoint(0.0F, 0.0F, 0.0F);
		frame.addChild(kabine);

		motor = new ModelRenderer(this);
		motor.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabine.addChild(motor);
		motor.setTextureOffset(0, 66).addBox( -17.0F, -14.0F, -7.0F, 33, 4, 25, 0.0F, false);

		motor.setTextureOffset(0, 95).addBox( -17.0F, -10.0F, 6.0F, 33, 8, 12, 0.0F, false);


		kabineFrame = new ModelRenderer(this);
		kabineFrame.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabine.addChild(kabineFrame);
		kabineFrame.setTextureOffset(0, 29).addBox( -17.0F, -18.0F, -7.0F, 1, 4, 25, 0.0F, false);

		kabineFrame.setTextureOffset(163, 0).addBox( -16.0F, -18.0F, 17.0F, 31, 4, 1, 0.0F, false);

		kabineFrame.setTextureOffset(0, 0).addBox( 15.0F, -18.0F, -7.0F, 1, 4, 25, 0.0F, false);

		kabineFrame.setTextureOffset(116, 26).addBox( -16.0F, -34.0F, -8.0F, 31, 25, 3, 0.0F, false);

		kabineFrame.setTextureOffset(117, 170).addBox( 15.0F, -34.0F, -7.0F, 1, 16, 5, 0.0F, false);

		kabineFrame.setTextureOffset(0, 133).addBox( -17.0F, -34.0F, -7.0F, 1, 16, 5, 0.0F, false);


		rightFrontBeam = new ModelRenderer(this);
		rightFrontBeam.setRotationPoint(-28.0F, -24.0F, 15.0F);
		setRotationAngle(rightFrontBeam, 0.1745F, 0.0F, 0.0F);
		kabineFrame.addChild(rightFrontBeam);
		rightFrontBeam.setTextureOffset(12, 133).addBox( 11.0F, -9.5702F, -1.0875F, 1, 16, 3, 0.0F, false);

		rightFrontBeam.setTextureOffset(64, 181).addBox( 11.0F, -9.5702F, 0.9125F, 3, 16, 1, 0.0F, false);


		leftFrontBeam = new ModelRenderer(this);
		leftFrontBeam.setRotationPoint(0.0F, -24.0F, 15.0F);
		setRotationAngle(leftFrontBeam, 0.1745F, 0.0F, 0.0F);
		kabineFrame.addChild(leftFrontBeam);
		leftFrontBeam.setTextureOffset(109, 172).addBox( 15.0F, -9.5702F, -1.0875F, 1, 16, 3, 0.0F, false);

		leftFrontBeam.setTextureOffset(72, 181).addBox( 13.0F, -9.5702F, 0.9125F, 3, 16, 1, 0.0F, false);


		roof = new ModelRenderer(this);
		roof.setRotationPoint(0.0F, 0.0F, 0.0F);
		kabineFrame.addChild(roof);
		roof.setTextureOffset(91, 66).addBox( -16.0F, -34.0F, -5.0F, 31, 2, 20, 0.0F, false);


		roofDetail = new ModelRenderer(this);
		roofDetail.setRotationPoint(0.0F, 0.0F, 0.0F);
		roof.addChild(roofDetail);
		roofDetail.setTextureOffset(90, 95).addBox( -13.0F, -35.0F, -8.0F, 25, 1, 19, 0.0F, false);

		roofDetail.setTextureOffset(0, 115).addBox( -13.0F, -36.0F, -8.0F, 25, 1, 17, 0.0F, false);

		roofDetail.setTextureOffset(108, 115).addBox( -13.0F, -37.0F, -8.0F, 25, 1, 15, 0.0F, false);

		roofDetail.setTextureOffset(118, 131).addBox( -13.0F, -38.0F, -8.0F, 25, 1, 13, 0.0F, false);

		roofDetail.setTextureOffset(115, 157).addBox( -13.0F, -39.9019F, -7.9622F, 25, 2, 11, 0.0F, false);


		inclinedFront = new ModelRenderer(this);
		inclinedFront.setRotationPoint(0.0F, -39.0F, 11.0F);
		setRotationAngle(inclinedFront, -0.5236F, 0.0F, 0.0F);
		roofDetail.addChild(inclinedFront);
		inclinedFront.setTextureOffset(52, 145).addBox( -13.0F, 3.0F, -7.0F, 25, 2, 12, 0.0F, false);


		inlinedRightRamp = new ModelRenderer(this);
		inlinedRightRamp.setRotationPoint(5.0F, -12.0F, -25.0F);
		setRotationAngle(inlinedRightRamp, 0.0F, -0.8727F, 0.0F);
		frame.addChild(inlinedRightRamp);
		inlinedRightRamp.setTextureOffset(27, 0).addBox( -4.0F, -1.0F, -8.0F, 5, 3, 11, 0.0F, false);


		inlinedLeftRamp = new ModelRenderer(this);
		inlinedLeftRamp.setRotationPoint(-5.0F, -12.0F, -25.0F);
		setRotationAngle(inlinedLeftRamp, 0.0F, 0.8727F, 0.0F);
		frame.addChild(inlinedLeftRamp);
		inlinedLeftRamp.setTextureOffset(27, 29).addBox( -1.0F, -1.0F, -8.0F, 5, 3, 11, 0.0F, false);


		seats = new ModelRenderer(this);
		seats.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(seats);
		seats.setTextureOffset(0, 12).addBox( -9.0F, -22.0F, 10.0F, 2, 2, 2, 0.0F, false);


		leftSeat = new ModelRenderer(this);
		leftSeat.setRotationPoint(0.0F, 0.0F, 0.0F);
		seats.addChild(leftSeat);
		leftSeat.setTextureOffset(114, 145).addBox( -14.0F, -17.0F, 1.0F, 27, 2, 10, 0.0F, false);


		leftSeatBack = new ModelRenderer(this);
		leftSeatBack.setRotationPoint(0.0F, -21.0F, 0.0F);
		setRotationAngle(leftSeatBack, 0.2618F, 0.0F, 0.0F);
		leftSeat.addChild(leftSeatBack);
		leftSeatBack.setTextureOffset(159, 100).addBox( -14.0F, -5.0F, -1.0F, 27, 11, 2, 0.0F, false);


		steerConnector = new ModelRenderer(this);
		steerConnector.setRotationPoint(-8.0F, -20.0F, 12.0F);
		setRotationAngle(steerConnector, -0.8727F, 0.0F, 0.0F);
		seats.addChild(steerConnector);
		steerConnector.setTextureOffset(116, 0).addBox( -1.0F, -1.0F, -1.0F, 2, 2, 9, 0.0F, false);


		steer = new ModelRenderer(this);
		steer.setRotationPoint(8.0F, 3.0F, -27.0F);
		steer.setTextureOffset(0, 58).addBox( -4.0F, -1.0F, 0.0F, 8, 2, 1, 0.0F, false);

		steer.setTextureOffset(0, 0).addBox( -1.0F, -4.0F, 0.0F, 2, 8, 1, 0.0F, false);


		leftToRightDown = new ModelRenderer(this);
		leftToRightDown.setRotationPoint(4.0F, -2.0F, 0.0F);
		setRotationAngle(leftToRightDown, 0.0F, 0.0F, 0.7854F);
		steer.addChild(leftToRightDown);
		leftToRightDown.setTextureOffset(0, 16).addBox( 1.1213F, 2.2213F, 0.0F, 1, 4, 1, 0.0F, false);

		leftToRightDown.setTextureOffset(15, 0).addBox( -4.9497F, 2.2213F, 0.0F, 1, 4, 1, 0.0F, false);


		leftToRightUp = new ModelRenderer(this);
		leftToRightUp.setRotationPoint(4.0F, -2.0F, 0.0F);
		setRotationAngle(leftToRightUp, 0.0F, 0.0F, -0.7854F);
		steer.addChild(leftToRightUp);
		leftToRightUp.setTextureOffset(18, 4).addBox( -1.7071F, -3.4355F, 0.0F, 1, 4, 1, 0.0F, false);

		leftToRightUp.setTextureOffset(4, 16).addBox( -7.7782F, -3.4355F, 0.0F, 1, 4, 1, 0.0F, false);

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
		steer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	public void rotateSteer(float angle) {
		steer.rotateAngleZ = (float) (angle / -90f * Math.PI);
	}
}