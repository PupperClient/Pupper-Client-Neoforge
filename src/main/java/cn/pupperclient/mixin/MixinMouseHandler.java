package cn.pupperclient.mixin;

import cn.pupperclient.PupperClient;
import cn.pupperclient.management.mod.settings.impl.KeybindSetting;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V", shift = At.Shift.AFTER))
    public void onPressed(long window, int button, int action, int mods, CallbackInfo ci) {

        for (KeybindSetting s : PupperClient.getInstance().getModManager().getKeybindSettings()) {

            if (s.getKey().equals(InputConstants.Type.MOUSE.getOrCreate(button))) {

                if (action == GLFW.GLFW_PRESS) {
                    s.setPressed();
                }

                s.setKeyDown(true);
            }
        }
    }

    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", shift = At.Shift.AFTER, ordinal = 0))
    public void onReleased(long window, int button, int action, int mods, CallbackInfo ci) {
        for (KeybindSetting s : PupperClient.getInstance().getModManager().getKeybindSettings()) {
            if (s.getKey().equals(InputConstants.Type.MOUSE.getOrCreate(button))) {
                s.setKeyDown(false);
            }
        }
    }
}
