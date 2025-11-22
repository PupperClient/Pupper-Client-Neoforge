package cn.pupperclient.ui.component.handler.impl;

import cn.pupperclient.ui.component.handler.ComponentHandler;

import java.io.File;

public abstract class FileSelectorHandler extends ComponentHandler {
	public abstract void onSelect(File file);
}
