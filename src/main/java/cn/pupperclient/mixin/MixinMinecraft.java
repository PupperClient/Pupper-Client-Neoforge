package cn.pupperclient.mixin;

import cn.pupperclient.PupperClient;
import cn.pupperclient.event.EventBus;
import cn.pupperclient.event.client.ClientTickEvent;
import cn.pupperclient.event.client.GameLoopEvent;
import cn.pupperclient.management.config.ConfigType;
import cn.pupperclient.shader.impl.Kawaseblur;
import cn.pupperclient.skia.context.SkiaContext;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.minecraft.client.Minecraft.checkModStatus;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow @Final private Window window;

    @Shadow
    public String createTitle() {
        StringBuilder stringbuilder = new StringBuilder("Minecraft");
        if (checkModStatus().shouldReportAsModified()) {
            stringbuilder.append(' ').append(net.neoforged.neoforge.forge.snapshots.ForgeSnapshotsMod.BRANDING_NAME).append('*');
        }

        stringbuilder.append(" ");
        stringbuilder.append(SharedConstants.getCurrentVersion().getName());
        ClientPacketListener clientpacketlistener = Minecraft.getInstance().getConnection();
        if (clientpacketlistener != null && clientpacketlistener.getConnection().isConnected()) {
            stringbuilder.append(" - ");
            ServerData serverdata = Minecraft.getInstance().getCurrentServer();
            if (Minecraft.getInstance().getSingleplayerServer() != null && !Minecraft.getInstance().getSingleplayerServer().isPublished()) {
                stringbuilder.append(I18n.get("title.singleplayer"));
            } else if (serverdata != null && serverdata.isRealm()) {
                stringbuilder.append(I18n.get("title.multiplayer.realms"));
            } else if (Minecraft.getInstance().getSingleplayerServer() == null && (serverdata == null || !serverdata.isLan())) {
                stringbuilder.append(I18n.get("title.multiplayer.other"));
            } else {
                stringbuilder.append(I18n.get("title.multiplayer.lan"));
            }
        }

        return stringbuilder.toString();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onClientTick(CallbackInfo ci) {
        EventBus.getInstance().post(new ClientTickEvent());
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;handleDelayedCrash()V"))
    public void onGameLoop(CallbackInfo ci) {
        EventBus.getInstance().post(new GameLoopEvent());
    }

    /**
     * @author EldoDebug
     * @reason updateTitle
     */
    @Overwrite
    public void updateTitle() {
        this.window.setTitle(PupperClient.getInstance().getName() + " Client v" + PupperClient.getInstance().getVersion() + " for "
        + createTitle());
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) throws IOException {
        SkiaContext.createSurface(window.getWidth(), window.getHeight());
    }

    @Inject(method = "resizeDisplay", at = @At("TAIL"))
    public void onResolutionChanged(CallbackInfo info) {
        Kawaseblur.GUI_BLUR.resize();
        Kawaseblur.INGAME_BLUR.resize();
    }

//    @Inject(method = "stop", at = @At("HEAD"))
//    public void onStop(CallbackInfo ci) {
//        PupperClient.getInstance().getConfigManager().save(ConfigType.MOD);
//    }
}
