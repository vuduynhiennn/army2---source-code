package army2.server;

import army2.fight.FightWait;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import team.TeamImageOutput;
import network.Message;
import static army2.server.CaptionData.CaptionEntry;
import static army2.server.ItemData.ItemEntry;
import static army2.server.MapData.MapDataEntry;
import static army2.server.SpecialItemData.SpecialItemEntry;
import static army2.server.NVData.NVEntry;
import static army2.server.NVData.EquipmentData;
import static army2.server.NVData.EquipmentEntry;
import static army2.server.FomularData.FomularEntry;
import static army2.server.NapTienData.NapTienEntry;
import static army2.server.BangXHManager.bangXHString;
import static army2.server.BangXHManager.bangXHString1;
import static army2.server.BangXHManager.BangXHEntry;
import army2.server.ItemClanData.ItemClanEntry;
import static army2.server.MissionData.MissionEntry;
import static army2.server.User.nvEquipDefault;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Văm Tú
 */
public class ServerManager {

    private static boolean debug;
    private static byte n_area;
    protected static String host;
    protected static short post;
    protected static String mysql_host;
    protected static String mysql_user;
    protected static String mysql_pass;
    protected static String mysql_database;
    protected static byte equipVersion2;
    protected static byte iconversion2;
    protected static byte levelCVersion2;
    protected static byte valuesversion2;
    protected static byte playerVersion2;
    protected static byte nRoom[];
    protected static byte nRoomAll;

    public static byte maxElementFight;
    public static byte maxPlayers;
    public static byte numbPlayers;
    public static boolean mgtBullNew;
    protected static byte nPlayersInitRoom;
    protected static byte ltapMap;
    public static short Xltap[], Yltap[];
    protected static byte initMap;
    protected static byte initMapBoss;
    protected static String addInfo;
    protected static String addInfoURL;
    protected static String regTeamURL;
    protected static String taiGameName;
    protected static String taiGameInfo;
    protected static String taiGameURL;
    protected static int max_clients;
    protected static int max_ruong_tb;
    protected static int max_ruong_item;
    protected static int max_ruong_itemslot;
    protected static int max_item;
    protected static int max_friends;

    protected static int numClients;
    protected static ArrayList<ClientEntry> clients;
    protected static ServerSocket server;
    protected static boolean start;
    protected static int id;

    protected static ArrayList<User> listWait;
    protected static String[] roomTypes
            = {"PHÒNG SƠ CẤP", "PHÒNG TRUNG CẤP", "PHÒNG VIP", "PHÒNG ĐẤU TRƯỜNG", "PHÒNG TỰ DO", "PHÒNG ĐẤU TRÙM", "PHÒNG ĐẤU ĐỘI"};
    protected static String[] roomTypesEng
            = {"NEWBIE ROOM", "INTERMEDIATE ROOM", "VIP ROOM", "ARENA", "FREEDOM ROOM", "BOSS BATTLE ROOM", "CLAN BATTLE ROOM"};
    protected static int[] roomTypeStartNum;
    protected static String[] nameRooms =    {"Bom", "Nhện máy", "Người máy", "T-rex máy", "UFO", "Khí cầu", "Nhện độc", "Ma", "Trùm liên hoàn", "Trùm liên hoàn"};
    protected static int[] nameRoomNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    protected static int[] nameRoomTypes   = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    protected static Room[] rooms;
    public static int startRoomBoss;
    public static int startMapBoss;
    public static int numMapBoss;
    public static byte[] mapIdBoss = new byte[]{12, 12, 13, 14, 15, 16, 17, 22, 25, 26};

    private static void setCache(byte id) {
        try {
            switch (id) {
                case 0:
                    ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    DataOutputStream ds = new DataOutputStream(bas);
                    int numMap = MapData.entrys.size();
                    ds.writeByte(numMap);
                    System.out.println("Init map entry numMap=" + numMap);
                    for (int i = 0; i < numMap; i++) {
                        MapDataEntry mapEntry = MapData.entrys.get(i);
                        ds.writeByte(mapEntry.id);
                        ds.writeShort(mapEntry.data.length);
                        ds.write(mapEntry.data);
                        ds.writeShort(mapEntry.bg);
                        ds.writeShort(mapEntry.mapAddY);
                        ds.writeShort(mapEntry.bullEffShower);
                        ds.writeShort(mapEntry.inWaterAddY);
                        ds.writeShort(mapEntry.cl2AddY);
                        ds.writeUTF(mapEntry.name);
                        ds.writeUTF(mapEntry.file);
                        System.out.println("   - id= " + mapEntry.id + " name= " + mapEntry.name + " file= " + mapEntry.file);
                    }
                    byte[] ab = bas.toByteArray();
                    Until.saveFile("cache/valuesdata2", ab);
                    bas.close();
                    ds.close();
                    break;

                case 1:
                    ByteArrayOutputStream bas1 = new ByteArrayOutputStream();
                    DataOutputStream ds1 = new DataOutputStream(bas1);
                    int numChamp = NVData.entrys.size();
                    ds1.writeByte(numChamp);
                    System.out.println("Init nhan vat numNV= " + numChamp);
                    for (int i = 0; i < numChamp; i++) {
                        NVEntry nvEntry = NVData.entrys.get(i);
                        ds1.writeByte(nvEntry.id);
                        ds1.writeShort(nvEntry.sat_thuong);
                        int numEquipData = nvEntry.trangbis.size();
                        ds1.writeByte(numEquipData);
                        for (int j = 0; j < numEquipData; j++) {
                            EquipmentData equipDataEntry = nvEntry.trangbis.get(j);
                            ds1.writeByte(equipDataEntry.id);
                            int numEquip = equipDataEntry.entrys.size();
                            ds1.writeByte(numEquip);
                            for (int k = 0; k < numEquip; k++) {
                                EquipmentEntry equipEntry = equipDataEntry.entrys.get(k);
                                ds1.writeShort(equipEntry.id);
                                if (equipDataEntry.id == 0) {
                                    ds1.writeByte(equipEntry.bullId);
                                }
                                ds1.writeShort(equipEntry.frame);
                                ds1.writeByte(equipEntry.lvRequire);
                                for (int l = 0; l < 6; l++) {
                                    ds1.writeShort(equipEntry.bigImageCutX[l]);
                                    ds1.writeShort(equipEntry.bigImageCutY[l]);
                                    ds1.writeByte(equipEntry.bigImageSizeX[l]);
                                    ds1.writeByte(equipEntry.bigImageSizeY[l]);
                                    ds1.writeByte(equipEntry.bigImageAlignX[l]);
                                    ds1.writeByte(equipEntry.bigImageAlignY[l]);
                                }
                                for (int l = 0; l < 5; l++) {
                                    ds1.writeByte(equipEntry.invAdd[l]);
                                    ds1.writeByte(equipEntry.percenAdd[l]);
                                }
                            }
                        }
                    }

                    byte[] dat = Until.getFile("res/item_special.png");
                    if (dat == null) {
                        System.out.println("File item_special.png not found!");
                        System.exit(0);
                    }
                    System.out.println("[coreLG/a] " + "Lent Icon= " + dat.length);
                    ds1.writeShort(dat.length);
                    ds1.write(dat);
                    for (int i = 0; i < numChamp; i++) {
                        dat = Until.getFile("res/bullet" + i + ".png");
                        if (dat == null) {
                            System.out.println("File bullet" + i + ".png not found!");
                            System.exit(0);
                        }
                        ds1.writeShort(dat.length);
                        ds1.write(dat);
                    }

                    byte[] ab1 = bas1.toByteArray();
                    Until.saveFile("cache/equipdata2", ab1);
                    bas1.close();
                    ds1.close();
                    break;

                case 2:

                    ByteArrayOutputStream bas2 = new ByteArrayOutputStream();
                    DataOutputStream ds2 = new DataOutputStream(bas2);
                    int numCaption = CaptionData.entrys.size();
                    ds2.writeByte(numCaption);
                    System.out.println("Init caption entry numCaption= " + numCaption);
                    for (int i = numCaption - 1; i >= 0; i--) {
                        CaptionEntry capEntry = CaptionData.entrys.get(i);
                        ds2.writeUTF(capEntry.caption);
                        ds2.writeByte(capEntry.level);
                        System.out.println("  lvl= " + capEntry.level + " str= " + capEntry.caption);
                    }
                    byte[] ab2 = bas2.toByteArray();
                    Until.saveFile("cache/levelCData2", ab2);
                    bas2.close();
                    ds2.close();

                    ByteArrayOutputStream bas2_1 = new ByteArrayOutputStream();
                    DataOutputStream ds2_1 = new DataOutputStream(bas2_1);
                    int numCaption_1 = CaptionData.entrys_1.size();
                    ds2_1.writeByte(numCaption_1);
                    System.out.println("Init caption entry numCaption_1= " + numCaption_1);
                    for (int i = numCaption_1 - 1; i >= 0; i--) {
                        CaptionEntry capEntry_1 = CaptionData.entrys_1.get(i);
                        ds2_1.writeUTF(capEntry_1.caption);
                        ds2_1.writeByte(capEntry_1.level);
                        System.out.println("  lvl= " + capEntry_1.level + " str= " + capEntry_1.caption);
                    }
                    byte[] ab2_1 = bas2_1.toByteArray();
                    Until.saveFile("cache/levelCData2_1", ab2_1);
                    bas2_1.close();
                    ds2_1.close();
                    break;

                case 3:
                    System.out.println("Cache player image!");
                    TeamImageOutput tos = new TeamImageOutput();
                    File playerDir = new File("res/player");
                    if (!playerDir.exists()) {
                        throw new IOException("Folder player not found!");
                    }
                    File[] playerFiles = playerDir.listFiles();
                    for (File f : playerFiles) {
                        tos.addFile(f.getName(), f.getPath());
                    }
                    byte[] ab3 = tos.output();
                    Until.saveFile("cache/playerdata2", ab3);
                    break;

                case 4:
                    System.out.println("Cache map icon!");
                    TeamImageOutput tos2 = new TeamImageOutput();
                    File mapDir = new File("res/map/icon");
                    if (!mapDir.exists()) {
                        throw new IOException("Folder map icon not found!");
                    }
                    File[] mapFiles = mapDir.listFiles();
                    for (File f : mapFiles) {
                        tos2.addFile(f.getName(), f.getPath());
                    }
                    byte[] ab4 = tos2.output();
                    Until.saveFile("cache/icondata2", ab4);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static User getUser(int iddb) {
        synchronized (clients) {
            for (int i = 0; i < clients.size(); i++) {
                ClientEntry cl = clients.get(i);
                if (cl == null || !cl.connected || !cl.login) {
                    continue;
                }
                if (cl.user.getIDDB() == iddb) {
                    return cl.user;
                }
            }
        }
        return null;
    }

    protected static void getClanIconMessage(ClientEntry cl, Message ms) throws IOException {
        try {
            short ids = ms.reader().readShort();
            short icon = 1;
            ResultSet red = SQLManager.getStatement().executeQuery("SELECT `icon` FROM `clan` WHERE id = " + ids + " LIMIT 1;");
            if (red != null && red.first()) {
                icon = red.getShort("icon");
            }
            red.close();
            byte[] ab = Until.getFile("res/icon/clan/" + icon + ".png");
            ms = new Message(115);
            DataOutputStream ds = ms.writer();
            ds.writeShort(ids);
            ds.writeShort(ab.length);
            ds.write(ab, 0, ab.length);
            ds.flush();
            cl.sendMessage(ms);
        } catch (SQLException e) {

        }
    }

    protected static void getBigIconMessage(ClientEntry cl, Message ms) throws IOException {
        int idS = ms.reader().readByte();
        ms = new Message(120);
        DataOutputStream ds = ms.writer();
        ds.writeByte(idS);
        byte[] ab1 = Until.getFile("res/bigImage" + idS + ".png");
        if (ab1 != null) {
            ds.writeShort(ab1.length);
            ds.write(ab1);
        } else {
            ds.writeShort(0);
        }
        ds.flush();
        cl.sendMessage(ms);
    }

    protected static void getMaterialIconMessage(ClientEntry cl, Message ms) throws IOException {
        byte typeIcon = ms.reader().readByte();
        byte idIcon = ms.reader().readByte();
        byte indexIcon = 0;
        byte[] ab0 = null;
        switch (typeIcon) {
            case 0:
                ab0 = Until.getFile("res/icon/item/" + idIcon + ".png");
                break;
            case 1:
                ab0 = Until.getFile("res/icon/item/" + idIcon + ".png");
                break;
            case 2:
                ab0 = Until.getFile("res/icon/map/" + idIcon + ".png");
                break;
            case 3:
                indexIcon = ms.reader().readByte();
                ab0 = Until.getFile("res/icon/item/" + idIcon + ".png");
                break;
            case 4:
                indexIcon = ms.reader().readByte();
                ab0 = Until.getFile("res/icon/item/" + idIcon + ".png");
                break;
        }
        ms = new Message(126);
        DataOutputStream ds = ms.writer();
        ds.writeByte(typeIcon);
        ds.writeByte(idIcon);
        if (ab0 == null) {
            ab0 = Until.getFile("/icon.png");
        }
        ds.writeShort(ab0.length);
        ds.write(ab0);
        if ((typeIcon == 3) || (typeIcon == 4)) {
            ds.writeByte(indexIcon);
        }
        ds.flush();
        cl.sendMessage(ms);
    }

    private static void loadConfigFile() {
        byte[] ab = Until.getFile("army2.conf");
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }
        String data = new String(ab);
        HashMap<String, String> configMap = new HashMap<>();
        StringBuilder sbd = new StringBuilder();
        boolean bo = false;
        for (int i = 0; i <= data.length(); i++) {
            char es;
            if ((i == data.length()) || ((es = data.charAt(i)) == '\n')) {
                bo = false;
                String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    int j = sbf.indexOf(':');
                    if (j > 0) {
                        String key = sbf.substring(0, j).trim();
                        String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
                continue;
            }
            if (es == '#') {
                bo = true;
            }
            if (!bo) {
                sbd.append(es);
            }
        }
        if (configMap.containsKey("debug")) {
            debug = Boolean.parseBoolean(configMap.get("debug"));
        } else {
            debug = false;
        }
        if (configMap.containsKey("host")) {
            host = configMap.get("host");
        } else {
            host = "localhost";
        }
        if (configMap.containsKey("post")) {
            post = Short.parseShort(configMap.get("post"));
        } else {
            post = 8122;
        }
        if (configMap.containsKey("mysql-host")) {
            mysql_host = configMap.get("mysql-host");
        } else {
            mysql_host = "localhost";
        }
        if (configMap.containsKey("mysql-user")) {
            mysql_user = configMap.get("mysql-user");
        } else {
            mysql_user = "root";
        }
        if (configMap.containsKey("mysql-password")) {
            mysql_pass = configMap.get("mysql-password");
        } else {
            mysql_pass = "";
        }
        if (configMap.containsKey("mysql-database")) {
            mysql_database = configMap.get("mysql-database");
        } else {
            mysql_database = "dbarmy2";
        }
        if (configMap.containsKey("equipVersion2")) {
            equipVersion2 = Byte.parseByte(configMap.get("equipVersion2"));
        } else {
            equipVersion2 = 1;
        }
        if (configMap.containsKey("iconversion2")) {
            iconversion2 = Byte.parseByte(configMap.get("iconversion2"));
        } else {
            iconversion2 = 1;
        }
        if (configMap.containsKey("levelCVersion2")) {
            levelCVersion2 = Byte.parseByte(configMap.get("levelCVersion2"));
        } else {
            levelCVersion2 = 1;
        }
        if (configMap.containsKey("valuesversion2")) {
            valuesversion2 = Byte.parseByte(configMap.get("valuesversion2"));
        } else {
            valuesversion2 = 1;
        }
        if (configMap.containsKey("playerVersion2")) {
            playerVersion2 = Byte.parseByte(configMap.get("playerVersion2"));
        } else {
            playerVersion2 = 1;
        }
        nRoom = new byte[roomTypes.length];
        nRoomAll = 0;
        startRoomBoss = 0;
        for (int i = 0; i < roomTypes.length; i++) {
            if (configMap.containsKey("n-room-" + i)) {
                nRoom[i] = Byte.parseByte(configMap.get("n-room-" + i));
                nRoomAll += nRoom[i];
                if (i < 5) {
                    startMapBoss += nRoom[i];
                }
            } else {
                nRoom[i] = 0;
            }
        }
        if (configMap.containsKey("n-area")) {
            n_area = Byte.parseByte(configMap.get("n-area"));
        } else {
            n_area = 101;
        }
        if (configMap.containsKey("max-player")) {
            maxPlayers = Byte.parseByte(configMap.get("max-player"));
        } else {
            maxPlayers = 8;
        }
        if (configMap.containsKey("max-fight")) {
            maxElementFight = Byte.parseByte(configMap.get("max-fight"));
        } else {
            maxElementFight = 100;
        }
        if (configMap.containsKey("numb-player")) {
            numbPlayers = Byte.parseByte(configMap.get("numb-player"));
        } else {
            numbPlayers = 100;
        }
        if (configMap.containsKey("n-players-init-room")) {
            nPlayersInitRoom = Byte.parseByte(configMap.get("n-players-init-room"));
        } else {
            nPlayersInitRoom = 4;
        }
        Xltap = new short[2];
        Yltap = new short[2];
        if (configMap.containsKey("luyen-tap-map")) {
            ltapMap = Byte.parseByte(configMap.get("luyen-tap-map"));
            Xltap[0] = Short.parseShort(configMap.get("x-ltap"));
            Xltap[1] = Short.parseShort(configMap.get("x-ltap1"));
            Yltap[0] = Short.parseShort(configMap.get("y-ltap"));
            Yltap[1] = Short.parseShort(configMap.get("y-ltap1"));
        } else {
            ltapMap = 0;
        }
        if (configMap.containsKey("init-map")) {
            initMap = Byte.parseByte(configMap.get("init-map"));
        } else {
            initMap = 5;
        }
        if (configMap.containsKey("init-map-boss")) {
            initMapBoss = Byte.parseByte(configMap.get("init-map-boss"));
        } else {
            initMapBoss = (byte) startMapBoss;
        }
        if (configMap.containsKey("start-map-boss")) {
            startMapBoss = Byte.parseByte(configMap.get("start-map-boss"));
        } else {
            startMapBoss = 30;
        }
        if (configMap.containsKey("num-map-boss")) {
            numMapBoss = Byte.parseByte(configMap.get("num-map-boss"));
        } else {
            numMapBoss = 10;
        }
        if (configMap.containsKey("add-info")) {
            addInfo = configMap.get("add-info");
        } else {
            addInfo = "";
        }
        if (configMap.containsKey("add-info-url")) {
            addInfoURL = configMap.get("add-info-url");
        } else {
            addInfoURL = "";
        }
        if (configMap.containsKey("reg-team-url")) {
            regTeamURL = configMap.get("reg-team-url");
        } else {
            regTeamURL = "";
        }
        if (configMap.containsKey("tai-game-name")) {
            taiGameName = configMap.get("tai-game-name");
        } else {
            taiGameName = "";
        }
        if (configMap.containsKey("tai-game-info")) {
            taiGameInfo = configMap.get("tai-game-info");
        } else {
            taiGameInfo = "";
        }
        if (configMap.containsKey("tai-game-url")) {
            taiGameURL = configMap.get("tai-game-url");
        } else {
            taiGameURL = "";
        }
        if (configMap.containsKey("max-clients")) {
            max_clients = Integer.parseInt(configMap.get("max-clients"));
        } else {
            max_clients = 1000;
        }
        if (configMap.containsKey("max-ruong-trang-bi")) {
            max_ruong_tb = Integer.parseInt(configMap.get("max-ruong-trang-bi"));
        } else {
            max_ruong_tb = 100;
        }
        if (configMap.containsKey("max-ruong-item")) {
            max_ruong_item = Integer.parseInt(configMap.get("max-ruong-item"));
        } else {
            max_ruong_item = 100;
        }
        if (configMap.containsKey("max-ruong-itemslot")) {
            max_ruong_itemslot = Integer.parseInt(configMap.get("max-ruong-itemslot"));
        } else {
            max_ruong_itemslot = 30000;
        }
        if (configMap.containsKey("max-item")) {
            max_item = Integer.parseInt(configMap.get("max-item"));
        } else {
            max_item = 99;
        }
        if (configMap.containsKey("max-friends")) {
            max_friends = Integer.parseInt(configMap.get("max-friends"));
        } else {
            max_friends = 60;
        }
        if (configMap.containsKey("mgt-bull-new")) {
            mgtBullNew = Boolean.parseBoolean(configMap.get("mgt-bull-new"));
        } else {
            mgtBullNew = true;
        }
    }

    protected static void init() {
        start = false;
        loadConfigFile();
        SQLManager.create(mysql_host, mysql_database, mysql_user, mysql_pass);
        System.out.println("Load map data");
        ResultSet res;
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `map`;");
            MapData.entrys = new ArrayList<>();
            MapData.brickEntrys = new ArrayList<>();
            while (res.next()) {
                MapDataEntry mapEntry = new MapDataEntry();
                mapEntry.id = (byte) (res.getByte("id") - 1);
                mapEntry.name = res.getString("name");
                mapEntry.file = res.getString("file");
                if (mapEntry.id == 27) {
                    mapEntry.data = new byte[0];
                } else {
                    mapEntry.data = Until.getFile("res/map/" + mapEntry.file);
                }
                mapEntry.bg = res.getShort("bg");
                mapEntry.mapAddY = res.getShort("mapAddY");
                mapEntry.bullEffShower = res.getShort("bullEffShower");
                mapEntry.inWaterAddY = res.getShort("inWaterAddY");
                mapEntry.cl2AddY = res.getShort("cl2AddY");
                MapData.entrys.add(mapEntry);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        setCache((byte) 0);
        System.out.println("Load NV Data!");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `nhanvat`;");
            NVData.entrys = new ArrayList<>();
            NVData.equips = new ArrayList<>();
            NVData.nSaleEquip = 0;
            while (res.next()) {
                NVEntry nvEntry = new NVEntry();
                nvEntry.id = (byte) (res.getByte("nhanvat_id") - 1);
                nvEntry.name = res.getString("name");
                nvEntry.buyXu = res.getInt("xu");
                nvEntry.buyLuong = res.getInt("luong");
                nvEntry.ma_sat_gio = res.getByte("ma_sat_gio");
                nvEntry.goc_min = res.getByte("goc_min");
                nvEntry.so_dan = res.getByte("so_dan");
                nvEntry.sat_thuong = res.getShort("sat_thuong");
                nvEntry.sat_thuong_dan = res.getByte("sat_thuong_dan");
                nvEntry.trangbis = new ArrayList<>();
                NVData.entrys.add(nvEntry);
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `equip`;");
            while (res.next()) {
                EquipmentEntry equipEntry = new EquipmentEntry();
                equipEntry.idNV = res.getByte("nv");
                equipEntry.idEquipDat = res.getByte("equipType");
                equipEntry.id = res.getShort("equipId");
                equipEntry.name = res.getString("name");
                equipEntry.giaXu = res.getInt("giaXu");
                equipEntry.giaLuong = res.getInt("giaLuong");
                equipEntry.hanSD = res.getInt("hanSD");
                equipEntry.lvRequire = res.getByte("lvRequire");
                equipEntry.frame = res.getShort("frame");
                equipEntry.bullId = res.getByte("bullId");
                equipEntry.onSale = res.getBoolean("onSale");
                equipEntry.isSet = res.getBoolean("isSet");
                equipEntry.bigImageCutX = new short[6];
                equipEntry.bigImageCutY = new short[6];
                equipEntry.bigImageSizeX = new byte[6];
                equipEntry.bigImageSizeY = new byte[6];
                equipEntry.bigImageAlignX = new byte[6];
                equipEntry.bigImageAlignY = new byte[6];
                equipEntry.arraySet = new short[5];
                int l;
                JSONArray jArray3 = (JSONArray) JSONValue.parse(res.getString("bigCutX"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageCutX[l] = ((Long) jArray3.get(l)).shortValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("bigCutY"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageCutY[l] = ((Long) jArray3.get(l)).shortValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("bigSizeX"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageSizeX[l] = ((Long) jArray3.get(l)).byteValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("bigSizeY"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageSizeY[l] = ((Long) jArray3.get(l)).byteValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("bigAlignX"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageAlignX[l] = ((Long) jArray3.get(l)).byteValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("bigAlignY"));
                for (l = 0; l < 6; l++) {
                    equipEntry.bigImageAlignY[l] = ((Long) jArray3.get(l)).byteValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("arraySet"));
                for (l = 0; l < 5; l++) {
                    equipEntry.arraySet[l] = ((Long) jArray3.get(l)).shortValue();
                }
                equipEntry.invAdd = new byte[5];
                equipEntry.percenAdd = new byte[5];
                jArray3 = (JSONArray) JSONValue.parse(res.getString("addPN"));
                for (l = 0; l < 5; l++) {
                    equipEntry.invAdd[l] = ((Long) jArray3.get(l)).byteValue();
                }
                jArray3 = (JSONArray) JSONValue.parse(res.getString("addPN100"));
                for (l = 0; l < 5; l++) {
                    equipEntry.percenAdd[l] = ((Long) jArray3.get(l)).byteValue();
                }
                NVData.addEquipEntryById(equipEntry.idNV, equipEntry.idEquipDat, equipEntry.id, equipEntry);
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        // Default data nhan vat
        short[][] defaultNvData = new short[NVData.entrys.size()][5];
        User.nvEquipDefault = new EquipmentEntry[NVData.entrys.size()][5];
        for (int i = 0; i < NVData.entrys.size(); i++) {
            NVEntry nvdat = NVData.entrys.get(i);
            for (int j = 0; j < 3; j++) {
                defaultNvData[i][j] = nvdat.trangbis.get(j).entrys.get(0).id;
            }
            defaultNvData[i][3] = 0;
            defaultNvData[i][4] = 0;
        }
        for (int i = 0; i < NVData.entrys.size(); i++) {
            for (int j = 0; j < 3; j++) {
                User.nvEquipDefault[i][j] = NVData.getEquipEntryById(i, j, defaultNvData[i][j]);
            }
        }
        System.out.println("NV Loader Size= " + NVData.entrys.size());
        setCache((byte) 1);
        System.out.println("Load caption level!");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `captionlv`;");
            CaptionData.entrys = new ArrayList<>();
            while (res.next()) {
                CaptionEntry capEntry = new CaptionEntry();
                // level
                capEntry.level = res.getInt("lvl");
                // caption
                capEntry.caption = res.getString("caption");
                CaptionData.entrys.add(capEntry);
            }
            System.out.println("Load caption level 2 new!");
            res.close();
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `captionlv_1`;");
            CaptionData.entrys_1 = new ArrayList<>();
            while (res.next()) {
                CaptionEntry capEntry = new CaptionEntry();
                // level
                capEntry.level = res.getInt("lvl");
                // caption
                capEntry.caption = res.getString("caption");
                CaptionData.entrys_1.add(capEntry);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        setCache((byte) 2);
        System.out.println("Load Item Data!");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `item`;");
            ItemData.entrys = new ArrayList<>();
            while (res.next()) {
                ItemEntry iEntry = new ItemEntry();
                // Ten
                iEntry.name = res.getString("name");
                // Gia xu
                iEntry.buyXu = res.getInt("xu");
                // Gia luong
                iEntry.buyLuong = res.getInt("luong");
                ItemData.entrys.add(iEntry);
            }
            System.out.println("Item readed size=" + ItemData.entrys.size());
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        setCache((byte) 3);
        System.out.println("Load Item Clam Data");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `clanshop`;");
            System.out.println("load item clan");
            ItemClanData.entrys = new ArrayList<>();
            while (res.next()) {
                ItemClanEntry iEntry = new ItemClanEntry();
                iEntry.id = res.getInt("id");
                iEntry.level = res.getInt("level");
                iEntry.name = res.getString("name");
                iEntry.time = res.getShort("time");
                iEntry.onsole = res.getByte("onsale");
                iEntry.xu = res.getInt("xu");
                iEntry.luong = res.getInt("luong");
                ItemClanData.entrys.add(iEntry);
                System.out.println("id " + iEntry.id + " level " + iEntry.level + " name " + iEntry.name);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        setCache((byte) 4);
        System.out.println("Load Special Item Data!");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `specialItem`;");
            SpecialItemData.entrys = new ArrayList<>();
            SpecialItemData.nSaleItem = 0;
            while (res.next()) {
                SpecialItemEntry iEntry = new SpecialItemEntry();
                // Id
                iEntry.id = res.getInt("id");
                // Ten
                iEntry.name = res.getString("name");
                // Detail
                iEntry.detail = res.getString("detail");
                // Gia xu
                iEntry.buyXu = res.getInt("giaXu");
                // Gia luong
                iEntry.buyLuong = res.getInt("giaLuong");
                // Han SD
                iEntry.hanSD = res.getShort("hanSD");
                // Show Chon
                iEntry.showChon = res.getBoolean("showChon");
                // OnSale
                iEntry.onSale = res.getBoolean("onSale");
                if (iEntry.onSale) {
                    iEntry.indexSale = SpecialItemData.nSaleItem;
                    SpecialItemData.nSaleItem++;
                }
                JSONArray jarr = (JSONArray) JSONValue.parse(res.getString("ability"));
                iEntry.ability = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    iEntry.ability[i] = ((Long) jarr.get(i)).shortValue();
                }
                SpecialItemData.entrys.add(iEntry);
            }
            System.out.println("Special Item readed size=" + SpecialItemData.entrys.size());
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Load fomular data");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `fomular`;");
            FomularData.entrys = new ArrayList();
            while (res.next()) {
                int materialId = res.getInt("idMaterial");
                byte equipType = res.getByte("equipType");
                JSONArray jarr = (JSONArray) JSONValue.parse(res.getString("equipId"));
                short[] eqId = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    eqId[i] = ((Long) jarr.get(i)).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("equipNeed"));
                short[] eqNeedId = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    eqNeedId[i] = ((Long) jarr.get(i)).shortValue();
                }
                FomularEntry fE = new FomularEntry();
                fE.level = res.getByte("lv");
                fE.levelRequire = res.getInt("lvRequire");
                jarr = (JSONArray) JSONValue.parse(res.getString("addPNMin"));
                fE.invAddMin = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    fE.invAddMin[i] = ((Long) jarr.get(i)).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("addPNMax"));
                fE.invAddMax = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    fE.invAddMax[i] = ((Long) jarr.get(i)).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("addPP100Min"));
                fE.percenAddMin = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    fE.percenAddMin[i] = ((Long) jarr.get(i)).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("addPP100Max"));
                fE.percenAddMax = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    fE.percenAddMax[i] = ((Long) jarr.get(i)).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("itemRequire"));
                fE.itemNeed = new SpecialItemEntry[jarr.size()];
                fE.itemNeedNum = new short[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    JSONObject jobj = (JSONObject) jarr.get(i);
                    fE.itemNeed[i] = SpecialItemData.getSpecialItemById(((Long) jobj.get("id")).intValue());
                    fE.itemNeedNum[i] = ((Long) jobj.get("num")).shortValue();
                }
                jarr = (JSONArray) JSONValue.parse(res.getString("detail"));
                fE.detail = new String[jarr.size()];
                for (int i = 0; i < jarr.size(); i++) {
                    fE.detail[i] = (String) jarr.get(i);
                }
                FomularData.addFomularEntry(materialId, equipType, eqId, eqNeedId, fE);
            }
            System.out.println("Fomular readed size=" + FomularData.entrys.size());
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Load nap the data");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `napthe`;");
            NapTienData.entrys = new ArrayList<>();
            while (res.next()) {
                NapTienEntry nE = new NapTienEntry();
                nE.id = res.getString("id");
                nE.info = res.getString("info");
                nE.url = res.getString("url");
                nE.mssTo = res.getString("mssTo");
                nE.mssContent = res.getString("mssContent");
                NapTienData.entrys.add(nE);
            }
            System.out.println("Nap the readed size=" + NapTienData.entrys.size());
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Load mission data");
        try {
            res = SQLManager.getStatement().executeQuery("SELECT * FROM `mission`;");
            MissionData.entrys = new ArrayList<>();
            while (res.next()) {
                MissionEntry mE = new MissionEntry();
                int id = res.getInt("id");
                byte idNeed = res.getByte("idneed");
                mE.index = res.getInt("iddb");
                mE.level = res.getByte("level");
                mE.name = res.getString("name");
                mE.require = res.getInt("require");
                mE.reward = res.getString("reward");
                mE.rewardXu = res.getInt("rewardXu");
                mE.rewardLuong = res.getInt("rewardLuong");
                mE.rewardXP = res.getInt("rewardXP");
                mE.rewardCUP = res.getInt("rewardCUP");
                MissionData.addMissionEntry(id, idNeed, mE);
            }
            System.out.println("Mission readed size=" + MissionData.entrys.size());
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected static void start() {
        System.out.println("Start socket post=" + post);
        try {
            clients = new ArrayList<>();
            listWait = new ArrayList<>();
            rooms = new Room[nRoomAll];
            roomTypeStartNum = new int[roomTypes.length];
            int k = 0;
            for (int i = 0; i < roomTypes.length; i++) {
                for (int j = 0; j < nRoom[i]; j++) {
                    if (j == 0) {
                        roomTypeStartNum[i] = k;
                    }
                    rooms[k] = new Room(k, i, n_area, j);
                    k++;
                }
            }
            BangXHManager.init();
            server = new ServerSocket(post);
            id = 0;
            numClients = 0;
            start = true;
            log("Start server Success!");
            while (start) {
                try {
                    Socket client = server.accept();
                    ClientEntry cl = new ClientEntry(client, ++id);
                    cl.IPAddress = client.getInetAddress().getHostName();
                    clients.add(cl);
                    numClients++;
                    log("Accept socket " + cl + " done!");
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void stop() {
        if (start) {
            close();
            start = false;
            System.gc();
        }
    }

    protected static void close() {
        try {
            while (clients.size() > 0) {
                ClientEntry c = clients.get(0);
                c.close();
            }
            server.close();
            server = null;
            clients = null;
            SQLManager.close();
            System.gc();
            System.out.println("End socket");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static void log(Object obj) {
        if (debug) {
            System.out.println(obj);
        }
    }

    protected static void disconnect(ClientEntry cl) {
        if (cl.login && (cl.user.getState() == User.State.Waiting)) {
            removeWait(cl.user);
        }
        synchronized (clients) {
            clients.remove(cl);
            numClients--;
            System.out.println("Disconnect client: " + cl);
        }
    }

    public static void enterWait(User us) {
        if (!listWait.isEmpty()) {
            synchronized (listWait) {
                for (int i = 0; i < listWait.size(); i++) {
                    if (listWait.get(i).getIDDB() == us.getIDDB()) {
                        return;
                    }
                }
            }
        }
        listWait.add(us);
        us.setState(User.State.Waiting);
    }

    public static void removeWait(User us) {
        if (!listWait.isEmpty()) {
            synchronized (listWait) {
                for (int i = 0; i < listWait.size(); i++) {
                    if (listWait.get(i).getIDDB() == us.getIDDB()) {
                        listWait.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public static void removeWait(int iddb) {
        if (!listWait.isEmpty()) {
            synchronized (listWait) {
                for (int i = 0; i < listWait.size(); i++) {
                    if (listWait.get(i).getIDDB() == iddb) {
                        listWait.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public static User[] findWaitPlayers(User us) throws IOException {
        if (listWait.isEmpty()) {
            return new User[0];
        }
        synchronized (listWait) {
            User[] uss = new User[listWait.size() > 10 ? 10 : listWait.size()];
            for (int i = 0; i < uss.length; i++) {
                uss[i] = listWait.get(i);
            }
            return uss;
        }
    }

    protected static void sendNVData(User us) throws IOException {
        // Send mss 64
        Message ms = new Message(64);
        DataOutputStream ds = ms.writer();
        ArrayList<NVEntry> nvdatas = NVData.entrys;
        int len = nvdatas.size();
        ds.writeByte(len);
        // Ma sat gio cac nv
        for (int i = 0; i < len; i++) {
            ds.writeByte(nvdatas.get(i).ma_sat_gio);
        }
        // Goc cuu tieu
        ds.writeByte(len);
        for (int i = 0; i < len; i++) {
            ds.writeShort(nvdatas.get(i).goc_min);
        }
        // Sat thuong 1 vien dan
        ds.writeByte(len);
        for (int i = 0; i < len; i++) {
            ds.writeByte(nvdatas.get(i).sat_thuong_dan);
        }
        // So dan
        ds.writeByte(len);
        for (int i = 0; i < len; i++) {
            ds.writeByte(nvdatas.get(i).so_dan);
        }
        // Max player
        ds.writeByte(maxElementFight);
        // Map boss
        ds.writeByte(numMapBoss);
        for (int i = 0; i < numMapBoss; i++) {
            ds.writeByte(startMapBoss + i);
        }
        // Type map boss
        for (int i = 0; i < numMapBoss; i++) {
            ds.writeByte(mapIdBoss[i]);
        }
        // NUMB Player
        ds.writeByte(numbPlayers);
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void sendRoomInfo(User us) throws IOException {
        // Send mss 88
        Message ms = new Message(88);
        DataOutputStream ds = ms.writer();
        // Size
        ds.writeByte(roomTypes.length);
        for (int i = 0; i < 7; i++) {
            // Ten viet hoa
            ds.writeUTF(roomTypes[i]);
            // Ten tieng anh
            ds.writeUTF(roomTypesEng[i]);
        }
        ds.flush();
        us.sendMessage(ms);
        // Cap nhat ten khu vuc
        ms = new Message(-19);
        ds = ms.writer();
        // Size
        ds.writeByte(nameRooms.length);
        for (int i = 0; i < nameRooms.length; i++) {
            // He so cong
            int namen = nameRoomNumbers[i];
            int typen = nameRoomTypes[i];
            if (namen > (nRoom[typen] + roomTypeStartNum[typen])) {
                continue;
            }
            int notRoom = 0;
            for (int j = 0; j < typen; j++) {
                if (nRoom[j] > 0) {
                    notRoom++;
                }
            }
            ds.writeByte(roomTypeStartNum[typen] + notRoom);
            // Ten cho phong viet hoa
            ds.writeUTF("Phòng " + (roomTypeStartNum[typen] + namen) + ": " + nameRooms[i]);
            // So
            ds.writeByte(namen);
        }
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void sendMapCollisionInfo(User us) throws IOException {
        // Send mss 92
        Message ms = new Message(92);
        DataOutputStream ds = ms.writer();
        ds.writeShort(MapData.idNotColision.length);
        for (int i = 0; i < MapData.idNotColision.length; i++) {
            ds.writeShort(MapData.idNotColision[i]);
        }
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void sendTaiGameInfo(User us) throws IOException {
        // Send tai game Mss
        if (taiGameName != null && !taiGameName.equals("")) {
            Message ms = new Message(-100);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(taiGameName);
            ds.writeUTF(taiGameInfo);
            ds.writeUTF(taiGameURL);
            ds.flush();
            us.sendMessage(ms);
        }
    }

    protected static void getPackMessage(User us, Message ms) throws IOException {
        byte getc = ms.reader().readByte();
        byte vers = ms.reader().readByte();
        DataOutputStream ds;
        switch (getc) {
            case 1:
                ms = new Message(90);
                ds = ms.writer();
                ds.writeByte(getc);
                ds.writeByte(iconversion2);
                if (vers != iconversion2) {
                    byte[] ab = Until.getFile("cache/icondata2");
                    ds.writeShort(ab.length);
                    ds.write(ab);
                }
                ds.flush();
                us.sendMessage(ms);
                break;

            // Get map info
            case 2:
                ms = new Message(90);
                ds = ms.writer();
                ds.writeByte(getc);
                ds.writeByte(valuesversion2);
                if (vers != valuesversion2) {
                    byte[] ab = Until.getFile("cache/valuesdata2");
                    ds.writeShort(ab.length);
                    ds.write(ab);
                }
                ds.flush();
                us.sendMessage(ms);
                break;

            case 3:
                ms = new Message(90);
                ds = ms.writer();
                ds.writeByte(getc);
                ds.writeByte(playerVersion2);
                if (vers != playerVersion2) {
                    byte[] ab = Until.getFile("cache/playerdata2");
                    ds.writeShort(ab.length);
                    ds.write(ab);
                }
                ds.flush();
                us.sendMessage(ms);
                break;

            case 4:
                ms = new Message(90);
                ds = ms.writer();
                ds.writeByte(getc);
                ds.writeByte(equipVersion2);
                if (vers != equipVersion2) {
                    byte[] ab = Until.getFile("cache/equipdata2");
                    ds.writeInt(ab.length);
                    ds.write(ab);
                }
                ds.flush();
                us.sendMessage(ms);
                break;

            case 5:
                ms = new Message(90);
                ds = ms.writer();
                ds.writeByte(getc);
                ds.writeByte(levelCVersion2);
                if (vers != levelCVersion2) {
                    byte[] ab = "2.2.3".equals(us.client.versionARM) ? Until.getFile("cache/levelCData2") : Until.getFile("cache/levelCData2_1");
                    ds.writeShort(ab.length);
                    ds.write(ab);
                }
                ds.flush();
                us.sendMessage(ms);
                break;

            case 6:
                us.sendInfo();
                us.sendRuongDoInfo();
                break;
        }
    }

    protected static void equipShopMessage(User us) throws IOException {
        Message ms = new Message(103);
        DataOutputStream ds = ms.writer();
        // Size
        ds.writeShort(NVData.nSaleEquip);
        // Cac trang bi
        for (EquipmentEntry eqEntry : NVData.equips) {
            if (!eqEntry.onSale) {
                continue;
            }
            // idNV
            ds.writeByte(eqEntry.idNV);
            ds.writeByte(eqEntry.idEquipDat);
            ds.writeShort(eqEntry.id);
            ds.writeUTF(eqEntry.name);
            ds.writeInt(eqEntry.giaXu);
            ds.writeInt(eqEntry.giaLuong);
            ds.writeByte(eqEntry.hanSD);
            ds.writeByte(eqEntry.lvRequire);
        }
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void bangXHMessage(User us, Message ms) {
        try {
            byte type = ms.reader().readByte();
            byte page = ms.reader().readByte();
            ms = new Message(-14);
            DataOutputStream ds = ms.writer();
            ds.writeByte(type);
            switch (type) {
                case -1:
                    ds.writeByte(bangXHString.length);
                    for (String bangXHString2 : bangXHString) {
                        ds.writeUTF(bangXHString2);
                    }
                    break;

                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    if (page > BangXHManager.bangXH[type].size() / 10 || page >= 10) {
                        page = 0;
                    }
                    BangXHEntry[] bxhA = BangXHManager.getBangXH(type, page);
                    ds.writeByte(page);
                    ds.writeUTF(bangXHString1[type]);
                    for (BangXHEntry bxhA1 : bxhA) {
                        try {
                            int iddb = bxhA1.iddb;
                            ds.writeInt(iddb);
                            ResultSet red;
                            red = SQLManager.getStatement().executeQuery("SELECT `user` FROM `user` WHERE user_id=\"" + iddb + "\" LIMIT 1;");
                            red.first();
                            if (type == 0 && bxhA1.index <= 3) {
                                ds.writeUTF(red.getString("user") + " +" + (4 - bxhA1.index) + "0k xu mỗi ngày");
                            } else {
                                ds.writeUTF(red.getString("user"));
                            }
                            red.close();
                            red = SQLManager.getStatement().executeQuery("SELECT `NVused`, `nvXPMax`, `clan` FROM `armymem` WHERE id=\"" + iddb + "\" LIMIT 1;");
                            red.first();
                            byte nv = 0;
                            ds.writeByte((type == 1 ? (nv = red.getByte("nvXPMax")) : (nv = red.getByte("NVUsed"))) - 1);
                            ds.writeShort(red.getShort("clan"));
                            red.close();
                            red = SQLManager.getStatement().executeQuery("SELECT `NV" + nv + "` FROM `armymem` WHERE id=\"" + iddb + "\" LIMIT 1;");
                            red.first();
                            JSONObject jobj = (JSONObject) JSONValue.parse(red.getString("NV" + nv));
                            red.close();
                            /* lever */
                            int lever = ((Long) jobj.get("lever")).intValue();
                            ds.writeByte("2.2.3".equals(us.client.versionARM) ? (lever > 127 ? 127 : lever) : lever);
                            /* lever % */
                            int xp = ((Long) jobj.get("xp")).intValue();
                            // lever %
                            xp -= (lever) * (lever - 1) * 500;
                            ds.writeByte((byte) (xp / lever / 10));
                            // xh index
                            ds.writeByte(bxhA1.index);
                            /* data nhan vat */
                            short[] data = ServerManager.data(iddb, (byte) nv);
                            for (byte j = 0; j < 5; j++) {
                                ds.writeShort(data[j]);
                            }
                            ds.writeUTF(Until.getStringNumber(bxhA1.nXH));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default:
                    return;
            }
            us.sendMessage(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void getRoomsMessage(User us) throws IOException {
        Message ms = new Message(6);
        DataOutputStream ds = ms.writer();
        for (int i = 0; i < rooms.length; i++) {
            // So phong
            ds.writeByte(i);
            Room rs = rooms[i];
            // Tinh trang 0: do 1: vang 2: xanh
            ds.writeByte(rs.getFully());
            // Null byte
            ds.writeByte(0);
            // Loai phong 0->6
            ds.writeByte(rs.type);
        }
        ds.flush();
        us.sendMessage(ms);
        sendRoomInfo(us);
    }

    protected static void getRoomNumberMessage(User us, Message ms) throws IOException {
        int room_number = ms.reader().readByte();
        Room rs = rooms[room_number];
        if (rs.type == 6 && us.getClan() == 0) {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.notClan());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        ms = new Message(7);
        DataOutputStream ds = ms.writer();
        // so phong
        ds.writeByte(room_number);
        synchronized (rooms) {
            for (int i = 0; i < rs.entrys.length; i++) {
                FightWait fo = rs.entrys[i];
                synchronized (fo.players) {
                    if (fo.numPlayer == fo.maxSetPlayer || fo.started || (fo.isLH && fo.ntLH > 0)) {
                        continue;
                    }
                    // So khu vuc
                    ds.writeByte(i);
                    // So nguoi trong khu vuc
                    ds.writeByte(fo.numPlayer);
                    // So nguoi toi da
                    ds.writeByte(fo.maxSetPlayer);
                    // Co mat khau or khong
                    ds.writeBoolean(fo.passSet);
                    // So tien
                    ds.writeInt(fo.money);
                    // Null boolean
                    ds.writeBoolean(true);
                    // Ten khu vuc
                    ds.writeUTF(fo.name);
                    // Kieu 0: Tea 1: Free
                    ds.writeByte(0);
                }
            }
        }
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void enterShortPlayMessage(User us, Message ms) throws IOException {
        int type = ms.reader().readByte();
        FightWait fo1 = null;
        switch (type) {
            case 5:
                loop1:
                for (int i = startRoomBoss; i < startRoomBoss + nRoom[5]; i++) {
                    Room rm = rooms[i];
                    for (FightWait fl1 : rm.entrys) {
                        if (fl1.numPlayer < fl1.maxSetPlayer) {
                            fo1 = fl1;
                            break loop1;
                        }
                    }
                }
                break;
            case 0:
                loop2:
                for (Room rm : rooms) {
                    for (FightWait fl1 : rm.entrys) {
                        if (fl1.numPlayer == 0) {
                            fo1 = fl1;
                            break loop2;
                        }
                    }
                }
                break;
            case -1:
                while (fo1 == null || fo1.numPlayer >= fo1.maxSetPlayer) {
                    Room rm = rooms[Until.nextInt(rooms.length)];
                    fo1 = rm.entrys[Until.nextInt(rm.entrys.length)];
                }
                break;
            default:
                int nplayer = type * 2;
                loop3:
                for (Room rm : rooms) {
                    for (FightWait fl1 : rm.entrys) {
                        if ((fl1.numPlayer < fl1.maxSetPlayer) && (fl1.maxSetPlayer == nplayer)) {
                            fo1 = fl1;
                            break loop3;
                        }
                    }
                }
                break;
        }
        if (fo1 != null) {
            fo1.enterFireOval(us);
        } else {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.findKVError1());
            ds.flush();
            us.sendMessage(ms);
        }
    }

    protected static void joinRegionMessage(User us, Message ms) throws IOException {
        // So phong
        byte sophong2 = ms.reader().readByte();
        // So khu vuc
        byte sokhuvuc = ms.reader().readByte();
        // password
        String password = ms.reader().readUTF();
        FightWait fo0 = rooms[sophong2].entrys[sokhuvuc];
        if (fo0.passSet && !fo0.pass.equals(password)) {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.joinKVError1());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        fo0.enterFireOval(us);
    }

    protected static void napTheMessage(User us, Message ms) throws IOException {
        String type = ms.reader().readUTF().trim();
        String seri = ms.reader().readUTF().trim();
        String maSo = ms.reader().readUTF().trim();
        if (type != null && seri != null && "giftcode".equals(type)) {
            giftCode(us, seri);
            return;
        }
        DataOutputStream ds;
        ms = new Message(45);
        ds = ms.writer();
        ds.writeUTF("type: " + type + " seri: " + seri + " ma: " + maSo);
        ds.flush();
        us.sendMessage(ms);
    }

    protected static void napTienMessage(User us, Message ms) throws IOException {
        byte action = ms.reader().readByte();
        String id = "";
        if (action == 1) {
            id = ms.reader().readUTF();
        }
        DataOutputStream ds;
        if (action == 0) {
            ms = new Message(122);
            ds = ms.writer();
            ds.writeByte(0);
            for (int i = 0; i < NapTienData.entrys.size(); i++) {
                NapTienEntry nE = NapTienData.entrys.get(i);
                ds.writeUTF(nE.id);
                ds.writeUTF(nE.info);
                ds.writeUTF(nE.url);
            }
            ds.flush();
            us.sendMessage(ms);
        }
        if (action == 1) {
            for (int i = 0; i < NapTienData.entrys.size(); i++) {
                NapTienEntry nE = NapTienData.entrys.get(i);
                if (nE.id.equals(id)) {
                    ms = new Message(122);
                    ds = ms.writer();
                    ds.writeByte(2);
                    ds.writeUTF(nE.mssTo);
                    ds.writeUTF(nE.mssContent);
                    ds.flush();
                    us.sendMessage(ms);
                    return;
                }
            }
        }
    }

    protected static void userSendMessage(User us, Message ms) throws IOException {
        int iddb = ms.reader().readInt();
        String s = ms.reader().readUTF();
        // Neu la admin -> bo qua
        if (iddb == 1) {
            return;
        }
        if (s.length() > 100) {
            return;
        }
        // Neu la nguoi dua tin -> send Mss 46-> chat The gioi
        if (iddb == 2) {
            // 10000xu/lan
            if (us.getXu() < 10000) {
                return;
            }
            us.updateXu(-10000);
            ms = new Message(46);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(String.format(GameString.mssTGString(), us.getUserName(), s));
            ds.flush();
            sendToServer(ms);
            return;
        }
        User us2 = getUser(iddb);
        if (us2 == null) {
            return;
        }
        us2.sendMSSToUser(us, s);
    }

    protected static short[] data(int iddb, byte nv) {
        short[] data = new short[5];
        try {
            ResultSet red = SQLManager.getStatement().executeQuery("SELECT `ruongTrangBi`,NV" + nv + " FROM armymem WHERE id=\"" + iddb + "\" LIMIT 1;");
            red.first();
            JSONObject jobj = (JSONObject) JSONValue.parse(red.getString("NV" + nv));
            JSONArray trangBi = (JSONArray) JSONValue.parse(red.getString("ruongTrangBi"));
            red.close();
            JSONArray Jarr = (JSONArray) jobj.get("data");
            short indexS = ((Long) Jarr.get(5)).shortValue();
            if (indexS >= 0 && indexS < trangBi.size()) {
                JSONObject jobj1 = (JSONObject) trangBi.get(indexS);
                short nvId = Short.parseShort(jobj1.get("nvId").toString());
                short equipId = Short.parseShort(jobj1.get("id").toString());
                short equipType = Short.parseShort(jobj1.get("equipType").toString());
                EquipmentEntry eq = NVData.getEquipEntryById(nvId, equipType, equipId);
                data[0] = eq.arraySet[0];
                data[1] = eq.arraySet[1];
                data[2] = eq.arraySet[2];
                data[3] = eq.arraySet[3];
                data[4] = eq.arraySet[4];
            } else {
                for (byte a = 0; a < 5; a++) {
                    indexS = ((Long) Jarr.get(a)).shortValue();
                    if (indexS >= 0 && indexS < trangBi.size()) {
                        JSONObject jobj1 = (JSONObject) trangBi.get(indexS);
                        data[a] = Short.parseShort(jobj1.get("id").toString());
                    } else if (nvEquipDefault[nv - 1][a] != null && a != 5) {
                        data[a] = nvEquipDefault[nv - 1][a].id;
                    } else {
                        data[a] = -1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void sendToServer(Message ms) throws IOException {
        synchronized (clients) {
            for (int i = 0; i < clients.size(); i++) {
                ClientEntry cl = clients.get(i);
                if (cl.user != null) {
                    cl.user.sendMessage(ms);
                }
            }
        }

    }

    private static void giftCode(User us, String code) throws IOException {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m1 = p.matcher(code);
        Message ms;
        if (!m1.find()) {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF("Kí tự không hợp lệ ");
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        try {
            ResultSet red = SQLManager.getStatement().executeQuery("SELECT * FROM `giftcode` WHERE `code` = \"" + code + "\" LIMIT 1;");
            if (!red.first() || !red.getString("code").equals(code)) {
                ms = new Message(45);
                DataOutputStream ds = ms.writer();
                ds.writeUTF("Mã quà tặng không tồn tại hoặc đã sử dụng!");
                ds.flush();
                us.sendMessage(ms);
                red.close();
                return;
            }
            short limit = red.getShort("limit");
            boolean pl = red.getBoolean("public");
            Date time = red.getDate("expire");
            JSONArray jarr = (JSONArray) JSONValue.parse(red.getString("item"));
            JSONArray iddblist = (JSONArray) JSONValue.parse(red.getString("iddblist"));
            red.close();
            if (limit == 0) {
                ms = new Message(45);
                DataOutputStream ds = ms.writer();
                ds.writeUTF("Số lượng đã hết!");
                ds.flush();
                us.sendMessage(ms);
                return;
            }
            if (!time.after(new Date())) {
                ms = new Message(45);
                DataOutputStream ds = ms.writer();
                ds.writeUTF("Mã quà đã hết hạn lúc " + Until.toDateString(time));
                ds.flush();
                us.sendMessage(ms);
                return;
            }
            if (pl) {
                for (int i = 0; i < iddblist.size(); i++) {
                    if (us.getIDDB() == ((Long) iddblist.get(i)).intValue()) {
                        DataOutputStream ds;
                        ms = new Message(45);
                        ds = ms.writer();
                        ds.writeUTF("Tài khoản đã sử dụng mã quà này");
                        ds.flush();
                        us.sendMessage(ms);
                        return;
                    }
                }
            }
            if (limit > 0) {
                limit--;
            }
            iddblist.add(us.getIDDB());
            SQLManager.getStatement().executeUpdate("UPDATE `giftcode` SET `limit` = '" + limit + "', `iddblist` = '" + iddblist.toJSONString() + "' WHERE `code`='" + code + "' LIMIT 1;");
            DataOutputStream ds;
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF("Nạp thành công");
            ds.flush();
            us.sendMessage(ms);
            for (int i = 0; i < jarr.size(); i++) {
                JSONObject item = (JSONObject) JSONValue.parse(jarr.get(i).toString());
                byte idItem = (byte) ((Long) item.get("id")).intValue();
                int number = ((Long) item.get("numb")).intValue();
                String nameItem;
                switch (idItem) {
                    case -4:
                        us.updateDvong(number);
                        nameItem = "Cup";
                        break;
                    case -3:
                        us.updateXP(number, false);
                        nameItem = "XP";
                        break;
                    case -2:
                        us.updateLuong(number);
                        nameItem = "Lượng";
                        break;
                    case -1:
                        us.updateXu(number);
                        nameItem = "Xu";
                        break;
                    default:
                        us.updateSpecialItem(idItem, number);
                        nameItem = SpecialItemData.getSpecialItemById(idItem).name;
                        break;
                }
                us.sendMSSToUser(null, code + ": + " + Until.getStringNumber(number) + " " + nameItem);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            DataOutputStream ds;
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF("Lỗi hệ thống");
            ds.flush();
            us.sendMessage(ms);
        }

    }

}
