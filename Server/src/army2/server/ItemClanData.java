package army2.server;

import java.util.ArrayList;

public class ItemClanData {

    public static final class ItemClanEntry {

        int id;
        int level;
        String name;
        short time;
        byte onsole;
        int xu = 0;
        int luong = 0;

    }

    public static ArrayList<ItemClanEntry> entrys;

    public static final ItemClanEntry getItemClanId(int id) {
        ItemClanEntry idEntry = null;
        for (int i = 0; i < entrys.size(); i++) {
            ItemClanEntry idEntry2 = entrys.get(i);
            if (idEntry2.id == id) {
                idEntry = idEntry2;
                break;
            }
        }
        return idEntry;
    }

}
