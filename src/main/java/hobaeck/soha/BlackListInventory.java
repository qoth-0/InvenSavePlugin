package hobaeck.soha;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BlackListInventory extends CustomInventory {

    public BlackListInventory() {
        super(54, Component.text("블랙리스트 아이템 설정"));
    }

    final BlackListInventoryManager blackListInventoryManager = new BlackListInventoryManager();

    @Override
    void onInventoryOpenEvent(InventoryOpenEvent event) {
        List<ItemStack> excludeItems = blackListInventoryManager.getPlayerBlackListItems((Player) event.getPlayer());
        if (excludeItems != null) {
            inventory.setContents(excludeItems.toArray(new ItemStack[0]));
        }
    }

    @Override
    void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (event.getView().getOriginalTitle().equals(player.getUniqueId().toString())) {
            blackListInventoryManager.saveInventory(player.getUniqueId(), Arrays.asList(inventory.getContents()));
            blackListInventoryManager.saveBlackListItems();
            player.sendMessage("아이템을 인벤세이브에서 제외하였습니다.");
        }
    }

    @Override
    void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getSlot() == 45) {
            // save item with close
            return;
        }
    }
}
