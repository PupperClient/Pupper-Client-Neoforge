package cn.pupperclient.ui.component.handler.impl;

import cn.pupperclient.ui.component.handler.ComponentHandler;
import com.mojang.blaze3d.platform.InputConstants;

public abstract class KeybindHandler extends ComponentHandler {
	public abstract void onBinded(InputConstants.Key key);
}
