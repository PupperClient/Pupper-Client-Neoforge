package cn.pupperclient.management.mod.impl.hud;

import cn.pupperclient.event.EventListener;
import cn.pupperclient.event.client.RenderSkiaEvent;
import cn.pupperclient.management.mod.api.hud.SimpleHUDMod;
import cn.pupperclient.skia.font.Icon;

public class Test extends SimpleHUDMod {
    public Test() {
        super("mod.test.name", "mod.test.description", Icon.TEXT_FORMAT);
    }

    @EventListener
    public void onRenderSkia(RenderSkiaEvent event) {
        this.draw();
    }

    @Override
    public String getText() {
        return "Test";
    }

    @Override
    public String getIcon() {
        return "";
    }
}
