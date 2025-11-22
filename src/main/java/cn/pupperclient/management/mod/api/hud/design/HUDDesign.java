package cn.pupperclient.management.mod.api.hud.design;

import io.github.humbleui.skija.Font;

import java.awt.*;

public abstract class HUDDesign {

	private String name;

	public HUDDesign(String name) {
		this.name = name;
	}

	public abstract void drawBackground(float x, float y, float width, float height, float radius);

	public abstract void drawText(String text, float x, float y, Font font);

	public abstract Color getTextColor();

	public abstract Color getOnTextColor();

	public String getName() {
		return name;
	}
}
