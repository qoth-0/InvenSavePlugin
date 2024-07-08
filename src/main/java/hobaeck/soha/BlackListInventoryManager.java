package hobaeck.soha;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class BlackListInventoryManager {

    private static final Map<UUID, List<ItemStack>> playerByBlackListItems = new HashMap<>();

    public static void openExcludeInventory(Player player) {
        Inventory inventory = getServer().createInventory(null, 18, player.getUniqueId().toString());
        inventory.setContents(playerByBlackListItems.get(player.getUniqueId()).toArray(new ItemStack[0]));
        player.openInventory(inventory);
    }

    public static void saveInventory(Player player, List<ItemStack> items) {
        playerByBlackListItems.put(player.getUniqueId(), items);
    }

    public static List<ItemStack> getPlayerBlackListItems(Player player) {
        return playerByBlackListItems.get(player.getUniqueId());
    }

}
