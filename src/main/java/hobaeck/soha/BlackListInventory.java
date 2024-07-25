package hobaeck.soha;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackListInventory extends CustomInventory {

    public BlackListInventory() {
        super(54, Component.text("블랙리스트 아이템 설정"));
    }

    final BlackListInventoryManager blackListInventoryManager = new BlackListInventoryManager();

    @Override
    void onInventoryOpenEvent(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        List<ItemStack> excludeItems = blackListInventoryManager.getPlayerBlackListItems(player);
        if (excludeItems != null) {
            inventory.setContents(excludeItems.toArray(new ItemStack[0]));
        }
        ItemStack nonClick = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        inventory.setItem(45, nonClick);
        inventory.setItem(46, nonClick);
        inventory.setItem(47, nonClick);
        inventory.setItem(48, nonClick);
        inventory.setItem(50, nonClick);
        inventory.setItem(51, nonClick);
        inventory.setItem(52, nonClick);
        inventory.setItem(53, nonClick);


        ItemStack saveButton = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        inventory.setItem(49, saveButton);

    }

    @Override
    void onInventoryCloseEvent(InventoryCloseEvent event) {

    }

    @Override
    void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        int slot = event.getSlot();
        if (event.getView().getOriginalTitle().equals("블랙리스트 아이템 설정")) {

            if (slot == 49) {
                blackListInventoryManager.saveInventory(player.getUniqueId(), Arrays.asList(inventory.getContents()));
                blackListInventoryManager.saveBlackListItems();
                player.sendMessage("아이템을 인벤세이브에서 제외하였습니다.");
                event.setCancelled(true);
            }
            if(slot >= 45 && slot <= 53) {
                event.setCancelled(true);
            }
        }
    }
}
