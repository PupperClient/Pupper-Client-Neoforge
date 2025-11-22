package cn.pupperclient.management.Keybind;

import cn.pupperclient.PupperClient;
import cn.pupperclient.event.EventBus;
import cn.pupperclient.event.EventListener;
import cn.pupperclient.event.client.KeyEvent;
import cn.pupperclient.management.mod.Mod;
import cn.pupperclient.management.mod.ModManager;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static cn.pupperclient.PupperClient.LOGGER;

public class KeybindManager {
    private static KeybindManager instance;
    public final Map<Integer, List<Mod>> keybindMap = new HashMap<>();
    private boolean initialized = false;
    private final Minecraft mc = Minecraft.getInstance();

    private KeybindManager() {}

    public static KeybindManager getInstance() {
        if (instance == null) {
            instance = new KeybindManager();
        }
        return instance;
    }

    public void initialize() {
        if (initialized) return;

        EventBus.getInstance().register(this);
        LOGGER.info("KeybindManager initialized");
        initialized = true;

        refreshKeybinds();
    }

    public void refreshKeybinds() {
        keybindMap.clear();
        ModManager modManager = PupperClient.getInstance().getModManager();

        if (modManager != null) {
            for (Mod mod : modManager.getMods()) {
                if (mod.getKeybind() != 0) {
                    registerKeybind(mod.getKeybind(), mod);
                    LOGGER.debug("Registered keybind: {} -> {}", mod.getKeybind(), mod.getName());
                }
            }
        }
        LOGGER.info("Refreshed keybinds: {} key mappings with total {} mods",
                keybindMap.size(), getTotalBoundMods());
    }

    public void registerKeybind(int keyCode, Mod mod) {
        if (keyCode != 0) {
            keybindMap.computeIfAbsent(keyCode, k -> new CopyOnWriteArrayList<>()).add(mod);
            LOGGER.debug("Registered keybind: {} -> {}", keyCode, mod.getName());
        }
    }

    public void unregisterKeybind(int keyCode, Mod mod) {
        List<Mod> mods = keybindMap.get(keyCode);
        if (mods != null) {
            boolean removed = mods.remove(mod);
            if (removed) {
                LOGGER.debug("Unregistered keybind: {} -> {}", keyCode, mod.getName());
            }
            if (mods.isEmpty()) {
                keybindMap.remove(keyCode);
            }
        }
    }

    public void updateKeybind(int oldKeyCode, int newKeyCode, Mod mod) {
        if (oldKeyCode != 0) {
            unregisterKeybind(oldKeyCode, mod);
        }
        if (newKeyCode != 0) {
            registerKeybind(newKeyCode, mod);
        }
        LOGGER.debug("Updated keybind: {} -> {} for mod {}", oldKeyCode, newKeyCode, mod.getName());
    }

    public List<Mod> getModsByKey(int keyCode) {
        return keybindMap.getOrDefault(keyCode, Collections.emptyList());
    }

    public int getTotalBoundMods() {
        return keybindMap.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    @EventListener
    public void onKeyInput(KeyEvent event) {
        handleGlobalKeyEvent(event);
    }

    private void handleGlobalKeyEvent(KeyEvent event) {
        if (!event.isState()) return;

        if (mc.screen != null) return;

        int keyCode = event.getKeybind();
        List<Mod> mods = getModsByKey(keyCode);

        if (!mods.isEmpty()) {
            LOGGER.debug("Keybind triggered: {} for {} mod(s)", keyCode, mods.size());

            for (Mod mod : mods) {
                LOGGER.debug("Toggling mod: {}", mod.getName());
                mod.toggle();
            }
        }
    }

    public void cleanup() {
        keybindMap.clear();
        LOGGER.info("KeybindManager cleaned up");
    }
}
