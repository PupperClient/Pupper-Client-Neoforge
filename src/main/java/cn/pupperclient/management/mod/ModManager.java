package cn.pupperclient.management.mod;

import cn.pupperclient.management.mod.api.hud.HUDMod;
import cn.pupperclient.management.mod.api.hud.design.HUDDesign;
import cn.pupperclient.management.mod.api.hud.design.impl.ClassicDesign;
import cn.pupperclient.management.mod.api.hud.design.impl.ClearDesign;
import cn.pupperclient.management.mod.api.hud.design.impl.MaterialYouDesign;
import cn.pupperclient.management.mod.api.hud.design.impl.SimpleDesign;
import cn.pupperclient.management.mod.impl.hud.Test;
import cn.pupperclient.management.mod.impl.player.Sprint;
import cn.pupperclient.management.mod.impl.settings.HUDModSettings;
import cn.pupperclient.management.mod.impl.settings.ModMenuSettings;
import cn.pupperclient.management.mod.impl.settings.SystemSettings;
import cn.pupperclient.management.mod.settings.Setting;
import cn.pupperclient.management.mod.settings.impl.KeybindSetting;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ModManager {
    private final List<Mod> mods = new CopyOnWriteArrayList<>();
    private final List<Setting> settings = new CopyOnWriteArrayList<>();
    private final List<HUDDesign> designs = new CopyOnWriteArrayList<>();

    private HUDDesign currentDesign;

    public void init() {
        initDesigns(); // 先初始化设计
        initMods();    // 再初始化模组
    }

    public void initMods() {
        mods.add(new Sprint());
        mods.add(new Test());
        mods.add(new HUDModSettings());
        mods.add(new ModMenuSettings());
        mods.add(new SystemSettings());

        sortMods();
        Sprint sprint = new Sprint();
        Test test = new Test();

        sprint.setEnabled(true);
        test.setEnabled(true);
    }

    private void initDesigns() {
        designs.add(new ClassicDesign());
        designs.add(new ClearDesign());
        designs.add(new MaterialYouDesign());
        designs.add(new SimpleDesign());

        if (currentDesign == null) {
            setCurrentDesign("design.simple");
        }
    }

    public List<Mod> getMods() {
        return mods;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public List<HUDMod> getHUDMods() {
        return mods.stream().filter(m -> m instanceof HUDMod).map(m -> (HUDMod) m).collect(Collectors.toList());
    }

    public List<KeybindSetting> getKeybindSettings() {
        return settings.stream().filter(s -> s instanceof KeybindSetting).map(s -> (KeybindSetting) s)
                .collect(Collectors.toList());
    }

    public List<Setting> getSettingsByMod(Mod m) {
        return settings.stream().filter(s -> s.getParent().equals(m)).collect(Collectors.toList());
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public HUDDesign getCurrentDesign() {
        // 确保永远不会返回null
        if (currentDesign == null && !designs.isEmpty()) {
            currentDesign = designs.get(0);
        }
        return currentDesign;
    }

    public void setCurrentDesign(String name) {
        HUDDesign design = getDesignByName(name);
        if (design != null) {
            this.currentDesign = design;
        }
    }

    // 修复死循环问题
    public HUDDesign getDesignByName(String name) {
        for (HUDDesign design : designs) {
            if (design.getName().equals(name)) {
                return design;
            }
        }

        for (HUDDesign design : designs) {
            if ("design.simple".equals(design.getName())) {
                return design;
            }
        }

        if (!designs.isEmpty()) {
            return designs.get(0);
        }

        return null;
    }

    private void sortMods() {
        mods.sort(Comparator.comparing(Mod::getName));
    }

    public void setModEnabledByName(boolean enabled, String modName) {
        Mod targetMod = getModByName(modName);
        if (targetMod != null) {
            targetMod.setEnabled(enabled);
        }
    }

    public Mod getModByName(String modName) {
        return mods.stream()
                .filter(mod -> mod.getName().equalsIgnoreCase(modName))
                .findFirst()
                .orElse(null);
    }

    public Mod getModByRawName(String modName) {
        return mods.stream()
                .filter(mod -> mod.getRawName().equalsIgnoreCase(modName))
                .findFirst()
                .orElse(null);
    }
}
