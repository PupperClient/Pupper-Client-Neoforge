package cn.pupperclient.gui.api;

import cn.pupperclient.skia.Skia;
import cn.pupperclient.skia.context.SkiaContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SimpleSoarGui {
	public Minecraft client = Minecraft.getInstance();
	private final boolean mcScale;

	public SimpleSoarGui(boolean mcScale) {
		this.mcScale = mcScale;
	}

	public void init() {
	}

	public void draw(double mouseX, double mouseY) {
	}

	public void mousePressed(double mouseX, double mouseY, int button) {
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
	}

	public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
	}

	public void charTyped(char chr, int modifiers) {
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
	}

	public Screen build() {
		return new Screen(Component.empty()) {

			@Override
			public void init() {
				SimpleSoarGui.this.init();
			}

			@Override
			public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {

				SkiaContext.draw((skiaContext) -> {

					Skia.save();

					if (mcScale) {
                        if (client != null) {
                            Skia.scale((float) client.getWindow().getGuiScale());
                        }
                    }

                    if (client != null) {
                        SimpleSoarGui.this.draw(mcScale ? mouseX : client.mouseHandler.xpos(),
                                mcScale ? mouseY : client.mouseHandler.ypos());
                    }
                    Skia.restore();
				});
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (client != null) {
                    SimpleSoarGui.this.mousePressed(mcScale ? mouseX : client.mouseHandler.xpos(),
                            mcScale ? mouseY : client.mouseHandler.ypos(), button);
                }
                return true;
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button) {
                if (client != null) {
                    SimpleSoarGui.this.mouseReleased(mcScale ? mouseX : client.mouseHandler.xpos(),
                            mcScale ? mouseY : (int) client.mouseHandler.ypos(), button);
                }
                return true;
			}

			@Override
			public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
                if (client != null) {
                    SimpleSoarGui.this.mouseScrolled(mcScale ? mouseX : client.mouseHandler.xpos(),
                            mcScale ? mouseY : (int) client.mouseHandler.ypos(), horizontalAmount, verticalAmount);
                }
                return true;
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				SimpleSoarGui.this.keyPressed(keyCode, scanCode, modifiers);
				return true;
			}

			@Override
			public boolean charTyped(char chr, int modifiers) {
				SimpleSoarGui.this.charTyped(chr, modifiers);
				return true;
			}

			@Override
			public boolean isPauseScreen() {
				return false;
			}
		};
	}
}
