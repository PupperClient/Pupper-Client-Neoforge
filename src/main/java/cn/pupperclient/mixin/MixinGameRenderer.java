package cn.pupperclient.mixin;

import cn.pupperclient.event.EventBus;
import cn.pupperclient.event.client.RenderSkiaEvent;
import cn.pupperclient.management.mod.impl.settings.HUDModSettings;
import cn.pupperclient.management.mod.impl.settings.ModMenuSettings;
import cn.pupperclient.shader.impl.Kawaseblur;
import cn.pupperclient.skia.Skia;
import cn.pupperclient.skia.context.SkiaContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.BEFORE))
    public void render(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {

        if (HUDModSettings.getInstance().getBlurSetting().isEnabled()) {
            Kawaseblur.INGAME_BLUR.draw((int) HUDModSettings.getInstance().getBlurIntensitySetting().getValue());
        }

        SkiaContext.draw((context) -> {
            Skia.save();
            Skia.scale((float) Minecraft.getInstance().getWindow().getGuiScale());
            EventBus.getInstance().post(new RenderSkiaEvent(context));
            Skia.restore();
        });
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.AFTER))
    public void renderGuiBlur(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {

        if (HUDModSettings.getInstance().getBlurSetting().isEnabled()) {
            Kawaseblur.GUI_BLUR.draw((int) ModMenuSettings.getInstance().getBlurIntensitySetting().getValue());
        }
    }
}
