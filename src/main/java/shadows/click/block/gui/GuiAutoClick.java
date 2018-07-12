package shadows.click.block.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import shadows.click.ClickMachine;
import shadows.click.block.TileAutoClick;
import shadows.click.net.MessageButtonClick;

public class GuiAutoClick extends GuiContainer {

	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ClickMachine.MODID, "textures/gui/auto_click.png");
	EntityPlayer player = Minecraft.getMinecraft().player;
	TileAutoClick tile;
	BetterButtonToggle[] buttons = new BetterButtonToggle[12];

	public GuiAutoClick(TileAutoClick tile) {
		super(new ContainerAutoClick(tile, Minecraft.getMinecraft().player));
		this.tile = tile;
		++ySize;
	}

	@Override
	public void initGui() {
		super.initGui();
		for (Buttons b : Buttons.values())
			buttons[b.ordinal()] = this.addButton(b.getAndInitButton(this));
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.format("gui.clickmachine.autoclick.name"), 8, 6, 4210752);
		this.fontRenderer.drawString(player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void renderHoveredToolTip(int x, int y) {
		super.renderHoveredToolTip(x, y);
		for (BetterButtonToggle b : buttons)
			if (b.isMouseOver()) this.drawHoveringText(Buttons.VALUES[b.id].getTooltip(), x, y, fontRenderer);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id < 12) {
			BetterButtonToggle toggle = (BetterButtonToggle) button;
			if (toggle.id < 9) for (BetterButtonToggle b : buttons) {
				if (b.id < 9 && b.id != toggle.id) b.setStateTriggered(false);
				toggle.setStateTriggered(true);
			}
			else if (toggle.id == 9) toggle.setStateTriggered(!toggle.isStateTriggered());
			else if (toggle.id > 9) for (BetterButtonToggle b : buttons) {
				if (b.id > 9 && b.id != toggle.id) b.setStateTriggered(false);
				toggle.setStateTriggered(true);
			}
		}
		ClickMachine.NETWORK.sendToServer(new MessageButtonClick(button.id));
	}

	enum Buttons {
		SPEED_0(18 * 2, 18, 176, 0, "gui.clickmachine.speed_0.tooltip"),
		SPEED_1(18 * 3, 18, 176, 18, "gui.clickmachine.speed_1.tooltip"),
		SPEED_2(18 * 4, 18, 176, 18 * 2, "gui.clickmachine.speed_2.tooltip"),
		SPEED_3(18 * 2, 36, 176, 18 * 3, "gui.clickmachine.speed_3.tooltip"),
		SPEED_4(18 * 3, 36, 176, 18 * 4, "gui.clickmachine.speed_4.tooltip"),
		SPEED_5(18 * 4, 36, 176, 18 * 5, "gui.clickmachine.speed_5.tooltip"),
		SPEED_6(18 * 2, 54, 176, 18 * 6, "gui.clickmachine.speed_6.tooltip"),
		SPEED_7(18 * 3, 54, 176, 18 * 7, "gui.clickmachine.speed_7.tooltip"),
		SPEED_8(18 * 4, 54, 176, 18 * 8, "gui.clickmachine.speed_8.tooltip"),
		SNEAK(18 * 6, 18, 176, 18 * 9, "gui.clickmachine.sneak.tooltip"),
		LEFT_CLICK(18 * 6 - 9, 54, 176, 18 * 10, "gui.clickmachine.left_click.tooltip"),
		RIGHT_CLICK(18 * 7 - 9, 54, 176, 18 * 11, "gui.clickmachine.right_click.tooltip");

		static final Buttons[] VALUES = Buttons.values();

		int id;
		int x;
		int y;
		int u;
		int v;
		String unlocalized;

		Buttons(int x, int y, int u, int v, String unlocalized) {
			this.id = ordinal();
			this.x = x - 1;
			this.y = y - 2;
			this.u = u;
			this.v = v;
			this.unlocalized = unlocalized;
		}

		List<String> getTooltip() {
			return Arrays.asList(I18n.format(unlocalized));
		}

		BetterButtonToggle getAndInitButton(GuiAutoClick gui) {
			BetterButtonToggle b = new BetterButtonToggle(id, gui.guiLeft + x, gui.guiTop + y, 18, 18, false);
			b.initTextureValues(u, v, 18, 0, GUI_TEXTURE);
			return b;
		}

	}

}
