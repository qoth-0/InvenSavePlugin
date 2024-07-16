package hobaeck.soha;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

abstract public class CustomInventory implements Listener {

    protected final Inventory inventory;

    public CustomInventory(int size, Component title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public void open(Player player) {
        Bukkit.getPluginManager().registerEvents(this, InventorySave.getInstance());
        player.openInventory(inventory);
    }

    abstract void onInventoryOpenEvent(InventoryOpenEvent event);

    abstract void onInventoryCloseEvent(InventoryCloseEvent event);

    abstract void onInventoryClickEvent(InventoryClickEvent event);

    @EventHandler
    public void onInventoryOpenEventRegister(InventoryOpenEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        onInventoryOpenEvent(event);
    }

    @EventHandler
    public void onInventoryCloseEventRegister(InventoryCloseEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        onInventoryCloseEvent(event);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClickEventRegister(InventoryClickEvent event) {
        if (event.getInventory() != inventory) {
            return;
        }
        onInventoryClickEvent(event);
    }

}
