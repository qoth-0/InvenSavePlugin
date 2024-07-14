package hobaeck.soha;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class BlackListInventoryManager {

    private static final Map<UUID, List<ItemStack>> playerByBlackListItems = new HashMap<>();

    private final InventorySave inventorySave;

    private final File file;
    private final YamlConfiguration dataConfig;

    public BlackListInventoryManager(InventorySave inventorySave) {
        this.inventorySave = inventorySave;
        this.file = new File(inventorySave.getDataFolder(), "blacklist_items.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(file);
        loadBlackListItems();
    }

    public void openExcludeInventory(Player player) {
        Inventory inventory = getServer().createInventory(player, 18, player.getUniqueId().toString());
        List<ItemStack> excludeItems = playerByBlackListItems.get(player.getUniqueId());
        if (excludeItems != null) {
            inventory.setContents(excludeItems.toArray(new ItemStack[0]));;
        }
//        inventory.setContents(playerByBlackListItems.get(player.getUniqueId()).toArray(new ItemStack[0]));
        player.openInventory(inventory);
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
