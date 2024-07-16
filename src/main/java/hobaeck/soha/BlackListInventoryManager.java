package hobaeck.soha;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlackListInventoryManager {

    private static final Map<UUID, List<ItemStack>> playerByBlackListItems = new HashMap<>();

    private final File file;
    private final YamlConfiguration dataConfig;

    public BlackListInventoryManager() {
        this.file = new File(InventorySave.getInstance().getDataFolder(), "blacklist_items.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(file);
        loadBlackListItems();
    }

    public void openExcludeInventory(Player player) {
        new BlackListInventory().open(player);
    }

    public void saveInventory(UUID playerUUID, List<ItemStack> items) {
        playerByBlackListItems.put(playerUUID, items);
    }

    public List<ItemStack> getPlayerBlackListItems(Player player) {
        return playerByBlackListItems.get(player.getUniqueId());
    }

    public void saveBlackListItems() {
        for (Map.Entry<UUID, List<ItemStack>> entry : playerByBlackListItems.entrySet()) {
            List<ItemStack> items = entry.getValue();
            dataConfig.set(entry.getKey().toString(), items);
        }
        try {
            dataConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlackListItems() {
        for (String key : dataConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            List<ItemStack> items = (List<ItemStack>) dataConfig.getList(key);
            saveInventory(playerUUID, items);
        }
    }

}
