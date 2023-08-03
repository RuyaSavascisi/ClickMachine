package dev.shadowsoffire.clickmachine.block.gui;

import java.util.ArrayList;
import java.util.List;

import dev.shadowsoffire.clickmachine.ClickMachine;
import dev.shadowsoffire.clickmachine.ClickMachineConfig;
import dev.shadowsoffire.placebo.menu.IDataUpdateListener;
import dev.shadowsoffire.placebo.screen.PlaceboContainerScreen;
import dev.shadowsoffire.placebo.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AutoClickScreen extends PlaceboContainerScreen<AutoClickContainer> implements IDataUpdateListener {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ClickMachine.MODID, "textures/gui/auto_click.png");
    protected Player player = Minecraft.getInstance().player;
    protected SpeedSlider slider;

    public AutoClickScreen(AutoClickContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.imageWidth = 176;
        this.imageHeight = 196;
        this.menu.addDataListener(this);
    }

    @Override
    public void init() {
        super.init();
        int x = this.width / 2 - this.imageWidth / 2 + 30;
        int y = this.height / 2 - this.imageHeight / 2 + 26;
        this.slider = this.addRenderableWidget(new SpeedSlider(this, x, y, 100, 20));
        this.addRenderableWidget(new ClickerCheckboxButton(this, x, y + 22, 20, 20, Component.translatable("gui.clickmachine.sneaking"), 3, this.menu::isSneaking));
        this.addRenderableWidget(new ClickerCheckboxButton(this, x, y + 44, 20, 20, Component.translatable("gui.clickmachine.right_click"), 4, this.menu::isRightClicking));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.getFocused() != null && this.isDragging() && button == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY) : super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, partialTicks);
        this.renderTooltip(gfx, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        gfx.drawString(font, this.getNarrationMessage(), 8, 6, 4210752, false);
        gfx.drawString(font, this.player.getInventory().getDisplayName().getString(), 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        gfx.blit(GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int x = i + 150;
        int y = j + 26;
        gfx.blit(GUI_TEXTURE, x, y, this.imageWidth + 21, 0, 21, 64);
        if (ClickMachineConfig.usesRF) {
            int maxP = ClickMachineConfig.maxPowerStorage;
            double p = this.menu.getEnergy();
            double ratio = p / maxP;
            gfx.blit(GUI_TEXTURE, x, y, this.imageWidth, 0, 21, 64 - (int) (ratio * 64));
        }
        else {
            ClientUtil.colorBlit(gfx.pose(), x + 1, y + 1, this.imageWidth + 43, 1, 19, 62, colors[(int) ((partialTicks + Minecraft.getInstance().player.tickCount / 0.5F) % colors.length)]);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics gfx, int x, int y) {
        super.renderTooltip(gfx, x, y);
        if (this.isHovering(150, 26, 21, 64, x, y)) {
            if (ClickMachineConfig.usesRF) {
                List<Component> comps = new ArrayList<>(2);
                comps.add(Component.translatable("gui.clickmachine.power", this.menu.getEnergy(), ClickMachineConfig.maxPowerStorage));
                comps.add(Component.translatable("gui.clickmachine.power.usage", ClickMachineConfig.powerPerSpeed[this.menu.getSpeedIdx()]));
                gfx.renderComponentTooltip(font, comps, x, y);
            }
            else {
                List<Component> comps = new ArrayList<>(1);
                comps.add(Component.translatable("gui.clickmachine.rainbow_magic"));
                gfx.renderComponentTooltip(font, comps, x, y);
            }
        }
    }

    static int[] colors = { 0xffff00, 0xffff06, 0xffff0c, 0xffff12, 0xffff18, 0xffff1e, 0xffff24, 0xffff2a, 0xffff30, 0xffff36, 0xffff3c, 0xffff42, 0xffff48, 0xffff4e, 0xffff54, 0xffff5a, 0xffff60, 0xffff66, 0xffff6c, 0xffff72,
        0xffff78, 0xffff7e, 0xffff84, 0xffff8a, 0xffff90, 0xffff96, 0xffff9c, 0xffffa2, 0xffffa8, 0xffffae, 0xffffb4, 0xffffba, 0xffffc0, 0xffffc6, 0xffffcc, 0xffffd2, 0xffffd8, 0xffffde, 0xffffe4, 0xffffea, 0xfffff0, 0xfffff6,
        0xfffffc, 0xfffcff, 0xfff6ff, 0xfff0ff, 0xffeaff, 0xffe4ff, 0xffdeff, 0xffd8ff, 0xffd2ff, 0xffccff, 0xffc6ff, 0xffc0ff, 0xffbaff, 0xffb4ff, 0xffaeff, 0xffa8ff, 0xffa2ff, 0xff9cff, 0xff96ff, 0xff90ff, 0xff8aff, 0xff84ff,
        0xff7eff, 0xff78ff, 0xff72ff, 0xff6cff, 0xff66ff, 0xff60ff, 0xff5aff, 0xff54ff, 0xff4eff, 0xff48ff, 0xff42ff, 0xff3cff, 0xff36ff, 0xff30ff, 0xff2aff, 0xff24ff, 0xff1eff, 0xff18ff, 0xff12ff, 0xff0cff, 0xff06ff, 0xff00ff,
        0x6ff00ff, 0xbff00ff, 0x12ff00ff, 0x17ff00ff, 0x1eff00ff, 0x23ff00ff, 0x2aff00ff, 0x2fff00ff, 0x36ff00ff, 0x3bff00ff, 0x42ff00ff, 0x47ff00ff, 0x4eff00ff, 0x53ff00ff, 0x5aff00ff, 0x5fff00ff, 0x66ff00ff, 0x6bff00ff, 0x72ff00ff,
        0x77ff00ff, 0x7eff00ff, 0x83ff00ff, 0x8aff00ff, 0x8fff00ff, 0x96ff00ff, 0x9bff00ff, 0xa2ff00ff, 0xa7ff00ff, 0xaeff00ff, 0xb3ff00ff, 0xbaff00ff, 0xbfff00ff, 0xc6ff00ff, 0xcbff00ff, 0xd2ff00ff, 0xd7ff00ff, 0xdeff00ff, 0xe3ff00ff,
        0xeaff00ff, 0xefff00ff, 0xf6ff00ff, 0xfbff00ff, 0xffff00fc, 0xffff00f6, 0xffff00f0, 0xffff00ea, 0xffff00e4, 0xffff00de, 0xffff00d8, 0xffff00d2, 0xffff00cc, 0xffff00c6, 0xffff00c0, 0xffff00ba, 0xffff00b4, 0xffff00ae, 0xffff00a8,
        0xffff00a2, 0xffff009c, 0xffff0096, 0xffff0090, 0xffff008a, 0xffff0084, 0xffff007e, 0xffff0078, 0xffff0072, 0xffff006c, 0xffff0066, 0xffff0060, 0xffff005a, 0xffff0054, 0xffff004e, 0xffff0048, 0xffff0042, 0xffff003c, 0xffff0036,
        0xffff0030, 0xffff002a, 0xffff0024, 0xffff001e, 0xffff0018, 0xffff0012, 0xffff000c, 0xffff0006, 0xffff0000, 0xffff0600, 0xffff0c00, 0xffff1100, 0xffff1700, 0xffff1e00, 0xffff2400, 0xffff2a00, 0xffff2f00, 0xffff3600, 0xffff3c00,
        0xffff4100, 0xffff4700, 0xffff4e00, 0xffff5400, 0xffff5a00, 0xffff5f00, 0xffff6600, 0xffff6c00, 0xffff7100, 0xffff7700, 0xffff7e00, 0xffff8400, 0xffff8a00, 0xffff8f00, 0xffff9600, 0xffff9c00, 0xffffa100, 0xffffa700, 0xffffae00,
        0xffffb400, 0xffffba00, 0xffffbf00, 0xffffc600, 0xffffcc00, 0xffffd100, 0xffffd700, 0xffffde00, 0xffffe400, 0xffffea00, 0xffffef00, 0xfffff600, 0xfffffc00, 0xfdffff00, 0xf7ffff00, 0xf0ffff00, 0xeaffff00, 0xe4ffff00, 0xdfffff00,
        0xd8ffff00, 0xd2ffff00, 0xcdffff00, 0xc7ffff00, 0xc0ffff00, 0xbaffff00, 0xb4ffff00, 0xafffff00, 0xa8ffff00, 0xa2ffff00, 0x9dffff00, 0x97ffff00, 0x90ffff00, 0x8affff00, 0x84ffff00, 0x7fffff00, 0x78ffff00, 0x72ffff00, 0x6dffff00,
        0x67ffff00, 0x60ffff00, 0x5affff00, 0x54ffff00, 0x4fffff00, 0x48ffff00, 0x42ffff00, 0x3dffff00, 0x37ffff00, 0x30ffff00, 0x2affff00, 0x24ffff00, 0x1fffff00, 0x18ffff00, 0x12ffff00, 0xdffff00, 0x7ffff00 };

    @Override
    public void dataUpdated(int id, int value) {
        if (id == 2) this.slider.setValue(value);
    }

}
