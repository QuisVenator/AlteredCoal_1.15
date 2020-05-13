package com.renepauls.alteredcoal.objects.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renepauls.alteredcoal.AlteredCoal;
import com.renepauls.alteredcoal.objects.gui.containers.BaseTruckContainer;
import com.renepauls.alteredcoal.objects.gui.containers.SnowMobileContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnowMobileScreen extends ContainerScreen<SnowMobileContainer> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(AlteredCoal.MOD_ID, "textures/gui/snow_mobile.png");

	public SnowMobileScreen(SnowMobileContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		guiLeft = 0;
		guiTop = 0;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.textureManager.bindTexture(TEXTURE);
		int x = (this.width-this.xSize)/2;
		int y = (this.height-this.ySize)/2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);
	}
}
