package hobaeck.soha;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 *  1. 인벤토리 아이템 티켓을 생성할때 태그 추가하기, 그리고 그 태그를 이벤트에서 인식하기 ( 단, 추가 및 가져올때 유틸 클래스 하나 고민? )
 *  2. 현재 인벤토리 타이틀로 구분을 하고 있는데 다른 방법이 있으면 그걸로 바꿔보기 ( 리스너 클래스 생성할대 인벤토리를 인자로 넘겨보는것이 힌트 )
 *  3. 인벤토리 아이템 티켓을 만들고 가져오는 방법 1가지로 만들기
 *      i. 현재 손에 들고 있는 아이템을 티켓으로 만들기 (+태그)
 *  4. 블랙리스트 한거 (아이템 전체) 저장하고 불러오기, ( 서버 리붓이나 리로드가 되어도 유지되어야 함 )
 *      i. 디비를 써도 되고, 파일을 써도 되고 ( YamlConfiguration )
 *  5. 심화, 이건 해도 되고 안해도됨, ( Inventory Click Event )
 *      i. 블랙리스트 저장하는 인벤토리에서 6,5 ( 가장 아래 가운데 ) 슬롯에 있는 아이템을 클릭하면 저장
 *      ii. 6,1 ~ 6,4 / 6,6 ~ 6,9 슬롯에는 검은색 스테인글래스 팬 아이템을 넣고 클릭하면 클릭이 취소되어야함.
 */
public final class InventorySave extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventorySaveListener(), this);
        InventorySaveCommand inventorySaveCommand = new InventorySaveCommand();
        Objects.requireNonNull(getCommand("인벤세이브")).setExecutor(inventorySaveCommand);
        Objects.requireNonNull(getCommand("인벤세이브")).setTabCompleter(inventorySaveCommand);
    }

    @Override
    public void onDisable() {
        getLogger().info("안녕히가세용~~~");
    }
}
