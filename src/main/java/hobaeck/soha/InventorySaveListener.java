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

    // 타이틀 비교 방법,


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (event.getView().getOriginalTitle().equals(p.getUniqueId().toString())) {
            BlackListInventoryManager.saveInventory(p, Arrays.asList(inventory.getContents()));
            p.sendMessage("아이템을 인벤세이브에서 제외하였습니다.");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean hasInventorySave = false;

        List<ItemStack> excludeItems = BlackListInventoryManager.getPlayerBlackListItems(player);

        // 인벤세이브 아이템이 있는지 확인
        for (ItemStack item : player.getInventory().getContents()) {

            NamespacedKey key = NamespacedKey.fromString("inventory_save_ticket");

            if (item == null) {
                continue;
            }

            if (key == null) {
                continue;
            }

            if (!item.getItemMeta().getPersistentDataContainer().has(key)) {
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

        if (hasInventorySave) {
            // 인벤세이브 제외 아이템 제거
            for (ItemStack excludeItem : excludeItems) {
                player.getInventory().removeItem(excludeItem);
            }
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
        player.sendMessage("인벤세이브권으로 인벤토리가 유지됩니다.");
    }

}
