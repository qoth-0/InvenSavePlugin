package hobaeck.soha;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InventorySave extends JavaPlugin implements Listener, CommandExecutor {

    private Inventory excludeInventory;
    private Map<String, Inventory> playerInventories = new HashMap<>();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("인벤세이브").setExecutor(this);
        getCommand("인벤세이브").setTabCompleter(this);
    }

    public void openExcludeInventory(Player p) {
        excludeInventory = getServer().createInventory(null, 18, "인벤세이브 제외 아이템");
        Inventory savedInventory = playerInventories.get(p.getName());
        if (savedInventory != null) {
            excludeInventory.setContents(savedInventory.getContents());
        }
        p.openInventory(excludeInventory);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (inventory == excludeInventory) {
            playerInventories.put(p.getName(), inventory);
            p.sendMessage("아이템을 인벤세이브에서 제외하였습니다.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 1 && args[0].equals("생성")) {
            // 인벤세이브 아이템 생성
            ItemStack item = new ItemStack(Material.DIAMOND); // 원하는 아이템으로 변경 가능
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                meta.setDisplayName("인벤세이브권");
                item.setItemMeta(meta);
            }

            // 플레이어에게 아이템 지급
            p.getInventory().addItem(item);
            p.sendMessage("인벤세이브 아이템이 지급되었습니다.");
        }

        if (args.length == 1 && args[0].equals("제외")) {
            // 인벤세이브 제외 아이템 설정
            openExcludeInventory(p);
        }

        if (args.length == 2 && args[0].equals("아이템") && args[1].equals("설정")) {
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if (itemInHand != null) {
                itemInHand.setType(Material.DIAMOND);
                ItemMeta meta = itemInHand.getItemMeta();
                meta.setDisplayName("인벤세이브권");
                itemInHand.setItemMeta(meta);
                p.sendMessage("인벤세이브권을 설정했습니다.");
                return true;
            } else {
                p.sendMessage("들고 있는 아이템이 없습니다.");
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        boolean hasInventorySave = false;

        excludeInventory = playerInventories.get(p.getName());
        List<ItemStack> excludeItems = new ArrayList<>();
        if (excludeInventory != null) {
            for (ItemStack item : excludeInventory.getContents()) {
                if (item != null) {
                    excludeItems.add(item);
                }
            }
        }

        // 인벤세이브 아이템이 있는지 확인
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND && item.getItemMeta().getDisplayName().equals("인벤세이브권")) {
                hasInventorySave = true;
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }

        if(hasInventorySave) {
            // 인벤세이브 제외 아이템 제거
            for (ItemStack excludeItem : excludeItems) {
                p.getInventory().removeItem(excludeItem);
            }
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
        p.sendMessage("인벤세이브권으로 인벤토리가 유지됩니다.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("생성".startsWith(args[0])) {
                completions.add("생성");
            }
            if ("제외".startsWith(args[0])) {
                completions.add("제외");
            }
            if ("아이템".startsWith(args[0])) {
                completions.add("아이템");
            }
        }
        if (args.length == 2 && args[0].equals("아이템")) {
            if ("설정".startsWith(args[1])) {
                completions.add("설정");
            }
        }
        return completions;
    }

    @Override
    public void onDisable() {
        getLogger().info("안녕히가세용~~~");
    }

    @EventHandler
    public void a1(PlayerMoveEvent a) {
        Player p = a.getPlayer();
    }

    @EventHandler
    public void a2(PlayerJoinEvent a) {
        Player p = a.getPlayer();
        p.sendMessage("플레이어 접속");
    }
    @EventHandler
    public void a3(PlayerChatEvent a) {
        Player p = a.getPlayer();
        p.sendMessage("플레이어 이름" + p.getName());
    }
}
