package hobaeck.soha;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class InventorySaveListener implements Listener {

    private final BlackListInventoryManager blackListInventoryManager;
    private final InventorySave inventorySave;

    public InventorySaveListener(BlackListInventoryManager blackListInventoryManager, InventorySave inventorySave) {
        this.blackListInventoryManager = blackListInventoryManager;
        this.inventorySave = inventorySave;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (event.getView().getOriginalTitle().equals(player.getUniqueId().toString())) {
            blackListInventoryManager.saveInventory(player.getUniqueId(), Arrays.asList(inventory.getContents()));
            blackListInventoryManager.saveBlackListItems();
            player.sendMessage("아이템을 인벤세이브에서 제외하였습니다.");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean hasInventorySave = false;

        List<ItemStack> excludeItems = blackListInventoryManager.getPlayerBlackListItems(player);
        Inventory inventory = player.getInventory();

        NamespacedKey key = new NamespacedKey("soha", "inventory_save_ticket");

        // 인벤세이브 아이템이 있는지 확인
        for (ItemStack item : inventory) {

            if (item == null  || item.getItemMeta() == null) {
                continue;
            }

            if (!item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                continue;
            }

            String ticket = item.getItemMeta().getPersistentDataContainer()
                    .get(key, PersistentDataType.STRING);

            if (ticket != null) {
                hasInventorySave = true;
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }

        inventorySave.getLogger().info(hasInventorySave + "");

        if (hasInventorySave) {
            if (excludeItems != null) {
                // null 값을 제외한 새로운 리스트 생성
                List<ItemStack> filteredExcludeItems = new ArrayList<>();
                for (ItemStack excludeItem : excludeItems) {
                    if (excludeItem != null) {
                        filteredExcludeItems.add(excludeItem);
                    }
                }

                // 인벤세이브 제외 아이템 제거
                for (ItemStack excludeItem : filteredExcludeItems) {
                    removeAllItemsOfType(inventory, excludeItem);
                }
            }
            event.setKeepInventory(true);
            event.getDrops().clear();
            player.sendMessage("인벤세이브권으로 인벤토리가 유지됩니다.");
            hasInventorySave = false;
        }
    }
    private void removeAllItemsOfType(Inventory inventory, ItemStack item) {
        ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].isSimilar(item)) {
                inventory.setItem(i, null);
            }
        }
    }

}
