package hobaeck.soha;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * 1. 6,1 ~ 6,4 / 6,6 ~ 6,9 에 검은색 스테인글래스 팬 아이템 넣고, 6,5 에는 저장하는 버튼? 초록색이나 빨간색 스테인 글래스 넣고
 *  6,5 클릭하면 저장, 1,1 ~ 5,9 의 아이템들만 저장됨.
 *
 * 2. 코틀린으로 전환 << *****
 */
public final class InventorySave extends JavaPlugin {

    // singleton
    private static InventorySave instance;

    public static InventorySave getInstance() {
        return instance;
    }

    private BlackListInventoryManager blackListInventoryManager = new BlackListInventoryManager();

    @Override
    public void onEnable() {
//        saveDefaultConfig();
        instance = this;
        getServer().getPluginManager().registerEvents(new InventorySaveListener(blackListInventoryManager, this), this);
        InventorySaveCommand inventorySaveCommand = new InventorySaveCommand(blackListInventoryManager);
        Objects.requireNonNull(getCommand("인벤세이브")).setExecutor(inventorySaveCommand);
        Objects.requireNonNull(getCommand("인벤세이브")).setTabCompleter(inventorySaveCommand);
    }

    @Override
    public void onDisable() {
        blackListInventoryManager.saveBlackListItems();
    }
}
