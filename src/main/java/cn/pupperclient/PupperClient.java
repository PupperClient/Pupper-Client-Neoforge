package cn.pupperclient;

import cn.pupperclient.management.Keybind.KeybindManager;
import cn.pupperclient.management.color.ColorManager;
import cn.pupperclient.management.config.ConfigManager;
import cn.pupperclient.management.config.ConfigType;
import cn.pupperclient.management.mod.ModManager;
import cn.pupperclient.management.music.MusicManager;
import cn.pupperclient.management.profile.ProfileManager;
import cn.pupperclient.skia.font.Fonts;
import cn.pupperclient.utils.file.FileLocation;
import cn.pupperclient.utils.language.I18n;
import cn.pupperclient.utils.language.Language;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import org.apache.logging.log4j.Logger;

@Mod(value = PupperClient.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PupperClient.MODID, value = Dist.CLIENT)
public class PupperClient {
    public static final String MODID = "pupperclient";
    public static final Logger LOGGER = PupperLogger.getLogger();

    private final static PupperClient instance = new PupperClient();
    private final String name = "Pupper";
    private final String version = "8.5.0";

    private static ModManager modManager;
    private static KeybindManager keybindManager;
    private static ConfigManager configManager;
    private static ColorManager colorManager;
    private static MusicManager musicManager;
    private static ProfileManager profileManager;

    public PupperClient() {}

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        Fonts.loadAll();
        FileLocation.init();
        I18n.setLanguage(Language.ENGLISH);

        modManager = new ModManager();
        modManager.init();

        keybindManager = KeybindManager.getInstance();
        keybindManager.initialize();

        colorManager = new ColorManager();
        configManager = new ConfigManager();
        musicManager = new MusicManager();
        profileManager = new ProfileManager();
    }

    @SubscribeEvent
    static void onClientShutdown(GameShuttingDownEvent event) {
        keybindManager.cleanup();
        configManager.save(ConfigType.MOD);
    }

    public static PupperClient getInstance() {
        return instance;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public String getVersion() {
        return version;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public String getName() {
        return name;
    }
}
