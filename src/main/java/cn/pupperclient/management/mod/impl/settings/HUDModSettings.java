package cn.pupperclient.management.mod.impl.settings;

import cn.pupperclient.PupperClient;
import cn.pupperclient.event.EventBus;
import cn.pupperclient.event.client.ClientTickEvent;
import cn.pupperclient.management.mod.Mod;
import cn.pupperclient.management.mod.ModCategory;
import cn.pupperclient.management.mod.settings.impl.BooleanSetting;
import cn.pupperclient.management.mod.settings.impl.ComboSetting;
import cn.pupperclient.management.mod.settings.impl.NumberSetting;
import cn.pupperclient.skia.font.Icon;

import java.util.Arrays;

public class HUDModSettings extends Mod {

	private static HUDModSettings instance;

	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);
	private ComboSetting designSetting = new ComboSetting("setting.design", "setting.design.description", Icon.PALETTE,
			this, Arrays.asList("design.simple", "design.classic", "design.clear", "design.materialyou"),
			"design.simple");
	private final NumberSetting blurIntensitySetting = new NumberSetting("setting.blurintensity",
			"setting.blurintensity.description", Icon.BLUR_LINEAR, this, 5, 1, 20, 1){
		@Override
		public boolean isVisible() {
			return blurSetting.isEnabled();
		}
	};

	public HUDModSettings() {
		super("mod.hudsettings.name", "mod.hudsettings.description", Icon.BROWSE_ACTIVITY, ModCategory.MISC);
		this.setHidden(true);
		this.setEnabled(true);

		instance = this;
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if (PupperClient.getInstance().getModManager().getCurrentDesign() != null) {
			if (!designSetting.getOption().equals(PupperClient.getInstance().getModManager().getCurrentDesign().getName())) {
				PupperClient.getInstance().getModManager().setCurrentDesign(designSetting.getOption());
			}
		} else {
			PupperClient.getInstance().getModManager().setCurrentDesign("design.simple");
		}
	};

	@Override
	public void onEnable() {
		super.onEnable();
		// 确保在启用时设置默认设计
		if (PupperClient.getInstance().getModManager().getCurrentDesign() == null) {
			PupperClient.getInstance().getModManager().setCurrentDesign("design.simple");
		}
	}

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}

	public static HUDModSettings getInstance() {
		return instance;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}

	public NumberSetting getBlurIntensitySetting() {
		return blurIntensitySetting;
	}
}