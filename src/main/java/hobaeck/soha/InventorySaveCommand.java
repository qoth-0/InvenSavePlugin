package hobaeck.soha;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class InventorySaveCommand implements CommandExecutor, TabCompleter {

    private final BlackListInventoryManager blackListInventoryManager;

    public InventorySaveCommand(BlackListInventoryManager blackListInventoryManager) {
        this.blackListInventoryManager = blackListInventoryManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String sub1 = args[0];
        getLogger().info(sub1);
        Player player = (Player) sender;
        switch (sub1) {

            case "생성":
                ItemStack item = new ItemStack(Material.DIAMOND);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text("인벤세이브권"));

                // 인벤세이브권 태그 설정
                meta.getPersistentDataContainer().set(new NamespacedKey("soha", "inventory_save_ticket"),
                        PersistentDataType.STRING, "inventory_save_ticket");

                item.setItemMeta(meta);

                // 플레이어에게 아이템 지급
                player.getInventory().addItem(item);
                player.sendMessage("인벤세이브 아이템이 지급되었습니다.");
                break;

            case "제외":
                blackListInventoryManager.openExcludeInventory(player);
                break;

            case "아이템":
                if (args.length == 2 && args[1].equals("설정")) {
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();

                    if (itemInHand.getType().isAir()) {
                        player.sendMessage("아이템을 들고 있지 않습니다.");
                        return true;
                    }
                    ItemMeta meta2 = itemInHand.getItemMeta();
                    meta2.displayName(Component.text("인벤세이브권"));

                    // 인벤세이브권 태그 설정
                    meta2.getPersistentDataContainer().set(new NamespacedKey("soha", "inventory_save_ticket"),
                            PersistentDataType.STRING, "inventory_save_ticket");

                    itemInHand.setItemMeta(meta2);

                    player.sendMessage("인벤세이브권을 설정했습니다.");
                    return true;
                }
                break;
        }
        return true;
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

}
