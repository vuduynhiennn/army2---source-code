package army2.server;

/*
@Văn Tú
 */
import static army2.server.User.nvEquipDefault;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ClanManager {

    public static class ClanEntry {

        int id;
        int master;
        String name;
        int icon;
        String thongBao;
        String item;
        int xu;
        int luong;
        int xp;
        int cup;
        int mem;
        int memMax;
        int level;
        String dateCreat;
        String masterName;

    }

    public static class ClanMemEntry {

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClan() {
            return clan;
        }

        public void setClan(int clan) {
            this.clan = clan;
        }

        public Date getTimeJoin() {
            return timeJoin;
        }

        public void setTimeJoin(Date timeJoin) {
            this.timeJoin = timeJoin;
        }

        public int getXu() {
            return xu;
        }

        public void setXu(int xu) {
            this.xu = xu;
        }

        public int getLuong() {
            return luong;
        }

        public void setLuong(int luong) {
            this.luong = luong;
        }

        public int getCup() {
            return cup;
        }

        public void setCup(int cup) {
            this.cup = cup;
        }

        public int getN_contribute() {
            return n_contribute;
        }

        public void setN_contribute(int n_contribute) {
            this.n_contribute = n_contribute;
        }

        public String getContribute_time() {
            return contribute_time;
        }

        public void setContribute_time(String contribute_time) {
            this.contribute_time = contribute_time;
        }

        public String getContribute_text() {
            return contribute_text;
        }

        public void setContribute_text(String contribute_text) {
            this.contribute_text = contribute_text;
        }

        public byte getRight() {
            return right;
        }

        public void setRight(byte rights) {
            this.right = rights;
        }

        public byte getNv() {
            return nv;
        }

        public void setNv(byte nv) {
            this.nv = nv;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public boolean getOnline() {
            return this.online;
        }

        public int getLever() {
            return lever;
        }

        public void setLever(int lever) {
            this.lever = lever;
        }

        public int getXp() {
            return xp;
        }

        public void setXp(int xp) {
            this.xp = xp;
        }

        public short[] getDataEquip() {
            return dataEquip;
        }

        public void setDataEquip(short[] dataEquip) {
            this.dataEquip = dataEquip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        int id;
        int clan;
        Date timeJoin;
        int xu;
        int luong;
        int cup;
        int n_contribute;
        String contribute_time;
        String contribute_text;
        byte right;
        byte nv;
        boolean online;
        int lever;
        int xp;
        short[] dataEquip;
        String name;

    }

    public static ArrayList<ClanMemEntry> member;

    protected static void getClanInfoMessage(User us, Message ms) {
        try {
            Short clan = ms.reader().readShort();
            ResultSet red;
            red = SQLManager.getStatement().executeQuery("SELECT * FROM `clan` WHERE `id` = '" + clan + "' LIMIT 1;");
            if (!red.first()) {
                ms = new Message(4);
                DataOutputStream ds = ms.writer();
                ds.writeUTF(GameString.clanNull());
                ds.flush();
                us.sendMessage(ms);
                red.close();
                return;
            }
            DataOutputStream ds;
            ms = new Message(117);
            ds = ms.writer();
            ds.writeShort(red.getShort("id")); // id
            ds.writeUTF(red.getString("name")); // tên clan
            ds.writeByte(red.getInt("mem")); // thành viên
            ds.writeByte(red.getInt("memmax")); // 
            ds.writeUTF(red.getString("masterName")); // chủ clan
            ds.writeInt(red.getInt("xu")); // xu
            ds.writeInt(red.getInt("luong")); // lượng
            ds.writeInt(red.getInt("cup")); // cúp
            int level = ((int) Math.sqrt(1 + red.getInt("xp") / 6250) + 1) >> 1;
            level = level > 127 ? 127 : level;
            int xp = red.getInt("xp");
            int maxXP = 25000 * 127 * 128;
            ds.writeInt(red.getInt("xp")); // exp
            int xpUpLv = 25000 * level * (level + 1);
            xpUpLv = xpUpLv > maxXP ? maxXP : xpUpLv;
            ds.writeInt(xpUpLv); // exp to up level
            ds.writeByte(level); // lever
            xp -= (level) * (level - 1) * 25000;
            ds.writeByte((byte) (xp / level / 500)); // phần trăm lv
            ds.writeUTF(red.getString("desc"));//giới thiệu
            ds.writeUTF(red.getString("dateCreat"));
            JSONArray itemClan = (JSONArray) JSONValue.parse(red.getString("Item"));
            int lentItem = itemClan.size();
            Date[] itemClanArray = new Date[lentItem];
            boolean[] isItem = new boolean[lentItem];
            int[] idItem = new int[lentItem];
            byte count = 0;
            for (int i = 0; i < lentItem; i++) {
                JSONObject jobj = (JSONObject) itemClan.get(i);
                itemClanArray[i] = Until.getDate(jobj.get("time").toString());
                idItem[i] = ((Long) jobj.get("id")).intValue();
                if (itemClanArray[i].after(new Date())) {
                    isItem[i] = true;
                    count++;
                }
            }
            red.close();
            ds.writeByte(count);
            for (byte i = 0; i < lentItem; i++) {
                if (isItem[i]) {
                    if (ItemClanData.getItemClanId(idItem[i]) != null) {
                        ds.writeUTF(ItemClanData.getItemClanId(idItem[i]).name);
                        ds.writeInt((int) (itemClanArray[i].getTime() / 1000) - (int) (new Date().getTime() / 1000));
                    } else {
                        ds.writeUTF("ITEM: " + idItem[i]);
                        ds.writeInt((int) (itemClanArray[i].getTime() / 1000) - (int) (new Date().getTime() / 1000));
                    }
                }
            }
            ds.flush();
            us.sendMessage(ms);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    protected static void getMemberClan(User us, Message ms) {
        try {
            byte page = ms.reader().readByte();
            short Ids = ms.reader().readShort();
            ResultSet res = SQLManager.getStatement().executeQuery(String.format("SELECT `Mem` FROM `clan` WHERE `id` = %d;", Ids));
            res.first();
            int mem = res.getInt("Mem");
            int numPage = (mem % 10 == 0) ? mem / 10 : mem / 10 + 1;
            res.close();
            if (page >= numPage) {
                page = 0;
            }
            DataOutputStream ds;
            ms = new Message(118);
            ds = ms.writer();
            ds.writeByte(page);
            ds.writeUTF("BIỆT ĐỘI");
            ResultSet red = SQLManager.getStatement().executeQuery(String.format("SELECT `clanmem`.*, `armymem`.*, `user`.`user` FROM `clanmem` INNER JOIN `armymem` ON `clanmem`.`user` = `armymem`.`id` INNER JOIN `user` ON `clanmem`.`user` = `user`.`user_id` WHERE `clanmem`.`clan` = %d ORDER BY `clanmem`.`rights` DESC, `clanmem`.`xp` DESC LIMIT %d, 10;", Ids, (page * 10)));
            member = new ArrayList<>();
            int CupClan = 0;
            while (red.next()) {
                ClanMemEntry memberEntry = new ClanMemEntry();
                memberEntry.setName(red.getString("user.user"));
                memberEntry.setId(red.getInt("armymem.id"));
                memberEntry.setClan(red.getInt("armymem.clan"));
                memberEntry.setTimeJoin(red.getDate("clanmem.itemJoin"));
                memberEntry.setXu(red.getInt("clanmem.xu"));
                memberEntry.setLuong(red.getInt("clanmem.luong"));
                memberEntry.setCup(red.getInt("armymem.dvong"));
                memberEntry.setN_contribute(red.getInt("clanmem.n_contribute"));
                memberEntry.setContribute_time(red.getString("clanmem.contribute_time"));
                memberEntry.setContribute_text(red.getString("clanmem.contribute_text"));
                memberEntry.setRight(red.getByte("clanmem.rights"));
                memberEntry.setNv((byte) (red.getByte("armymem.NVused") - 1));
                memberEntry.setOnline(red.getBoolean("armymem.online"));
                JSONObject jobj = (JSONObject) JSONValue.parse(red.getString("armymem.NV" + (memberEntry.nv + 1)));
                memberEntry.setLever(((Long) jobj.get("lever")).intValue());
                memberEntry.setXp(((Long) jobj.get("xp")).intValue());
                JSONArray trangBi = (JSONArray) JSONValue.parse(red.getString("armymem.ruongTrangBi"));
                JSONArray Jarr = (JSONArray) jobj.get("data");
                short indexS = ((Long) Jarr.get(5)).shortValue();
                short[] dataEquip = new short[5];
                if (indexS >= 0 && indexS < trangBi.size()) {
                    JSONObject jobj1 = (JSONObject) trangBi.get(indexS);
                    short nvId = Short.parseShort(jobj1.get("nvId").toString());
                    short equipId = Short.parseShort(jobj1.get("id").toString());
                    short equipType = Short.parseShort(jobj1.get("equipType").toString());
                    NVData.EquipmentEntry eq = NVData.getEquipEntryById(nvId, equipType, equipId);
                    dataEquip[0] = eq.arraySet[0];
                    dataEquip[1] = eq.arraySet[1];
                    dataEquip[2] = eq.arraySet[2];
                    dataEquip[3] = eq.arraySet[3];
                    dataEquip[4] = eq.arraySet[4];
                } else {
                    for (byte a = 0; a < 5; a++) {
                        indexS = ((Long) Jarr.get(a)).shortValue();
                        if (indexS >= 0 && indexS < trangBi.size()) {
                            JSONObject jobj1 = (JSONObject) trangBi.get(indexS);
                            dataEquip[a] = Short.parseShort(jobj1.get("id").toString());
                        } else if (nvEquipDefault[memberEntry.getNv()][a] != null && a != 5) {
                            dataEquip[a] = nvEquipDefault[memberEntry.getNv()][a].id;
                        } else {
                            dataEquip[a] = -1;
                        }
                    }
                }
                memberEntry.setDataEquip(dataEquip);
                member.add(memberEntry);
            }
            red.close();
            int length = member.size();
            for (int i = 0; i < length; i++) {
                ClanMemEntry memClan = member.get(i);
                ds.writeInt(memClan.getId()); // iddb
                ds.writeUTF(memClan.getName() + (memClan.getRight() == 2 ? " (Đội trưởng)" : (memClan.getRight() > 0 ? (" (Đội phó " + i + ")") : ""))); // tên nv
                ds.writeInt(1);
                ds.writeByte(memClan.getNv()); // stt nhân vật 0->9
                ds.writeByte(memClan.getOnline() ? 1 : 0); // online: 1, offline: 0
                ds.writeByte("2.2.3".equals(us.client.versionARM) ? (memClan.getLever() > 127 ? 127 : memClan.getLever()) : memClan.getLever()); // lever
                ds.writeByte((byte) (memClan.getXp() / memClan.getLever() / 10)); // % lever
                ds.writeByte((page * 10) + i); // số thứ tự thành viên
                CupClan += memClan.getCup();
                ds.writeInt(memClan.getCup());
                for (int j = 0; j < 5; j++) {
                    ds.writeShort(memClan.getDataEquip()[j]);
                }
                if (memClan.getN_contribute() > 0) {
                    ds.writeUTF("Góp " + memClan.getContribute_text() + " " + Until.getStrungTime((new Date().getTime() - Until.getDate(memClan.getContribute_time()).getTime())) + " trước");
                } else {
                    ds.writeUTF("Chưa đóng góp");
                }
                ds.writeUTF(memClan.getN_contribute() > 0 ? (memClan.getN_contribute() + " lần: " + Until.getStringNumber(memClan.getXu()) + " xu và " + Until.getStringNumber(memClan.getLuong()) + " lượng") : "");
            }
            member.clear();
            SQLManager.getStatement().executeUpdate("UPDATE `clan` SET `cup` =  " + CupClan + "  WHERE `id` = " + Ids + ";");
            ds.flush();
            us.sendMessage(ms);
        } catch (Exception e) {

        }
    }

    protected static void clanItemMessage(User us, Message ms) {
        try {
            int idClan = us.getClan();
            DataOutputStream ds;
            byte type = ms.reader().readByte();
            if (idClan > 0) {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT * FROM `clan` WHERE `id` = '" + idClan + "' LIMIT 1;");
                if (!red.first()) {
                    ms = new Message(45);
                    ds = ms.writer();
                    ds.writeUTF(GameString.clanNull());
                    ds.flush();
                    us.sendMessage(ms);
                    red.close();
                    return;
                }
                int level = red.getInt("level");
                red.close();
                if (type == 0) {
                    ms = new Message(-12);
                    ds = ms.writer();
                    ArrayList<ItemClanData.ItemClanEntry> Item = new ArrayList<>();
                    for (int i = 0; i < ItemClanData.entrys.size(); i++) {
                        ItemClanData.ItemClanEntry idtEntry = ItemClanData.entrys.get(i);
                        if (idtEntry == null || idtEntry.onsole == 0) {
                            continue;
                        }
                        Item.add(idtEntry);
                    }
                    ds.writeByte(Item.size());
                    for (ItemClanData.ItemClanEntry idtEntry : Item) {
                        ds.writeByte(idtEntry.id);
                        ds.writeUTF(idtEntry.name);
                        ds.writeInt(idtEntry.xu);
                        ds.writeInt(idtEntry.luong);
                        ds.writeByte(idtEntry.time);
                        ds.writeByte(idtEntry.level);
                    }
                    ds.flush();
                    us.sendMessage(ms);
                } else if (type == 1) {
                    byte buyType = ms.reader().readByte();
                    byte idS = ms.reader().readByte();
                    ItemClanData.ItemClanEntry spE = ItemClanData.getItemClanId(idS);
                    if (spE.level > level) {
                        ms = new Message(45);
                        ds = ms.writer();
                        ds.writeUTF(GameString.clanLevelNotEnought());
                        ds.flush();
                        us.sendMessage(ms);
                        return;
                    } else if (buyType == 0) {
                        int gia = spE.xu;
                        if (gia < 0 || spE.onsole < 1) {
                            return;
                        }
                        if (getXuClan(us) < gia) {
                            ms = new Message(45);
                            ds = ms.writer();
                            ds.writeUTF(GameString.clanXuNotEnought());
                            ds.flush();
                            us.sendMessage(ms);
                            return;
                        }
                        updateXu(us, -gia);
                    } else if (buyType == 1) {
                        int gia = spE.luong;
                        if (gia < 0 || spE.onsole < 1) {
                            return;
                        }
                        if (getLuongClan(us) < gia) {
                            ms = new Message(45);
                            ds = ms.writer();
                            ds.writeUTF(GameString.clanLuongNotEnought());
                            ds.flush();
                            us.sendMessage(ms);
                            return;
                        }
                        updateLuong(us, -gia);
                    } else {
                        return;
                    }
                    ItemClanData.ItemClanEntry newIt = ItemClanData.getItemClanId(idS);
                    updateItemClan(us, idS, newIt.time);
                    ms = new Message(45);
                    ds = ms.writer();
                    ds.writeUTF(GameString.buySuccess());
                    ds.flush();
                    us.sendMessage(ms);
                }
            } else {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(GameString.notClan());
                ds.flush();
                us.sendMessage(ms);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getXuClan(User us) {
        int xuclan = 0;
        if (us.getClan() > 0) {
            try {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT * FROM `clan` WHERE `id` = '" + us.getClan() + "' LIMIT 1;");
                red.first();
                xuclan = red.getInt("xu");
                red.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return xuclan;
    }

    public static int getLuongClan(User us) {
        int luongclan = 0;
        if (us.getClan() > 0) {
            try {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT * FROM `clan` WHERE `id` = '" + us.getClan() + "' LIMIT 1;");
                red.first();
                luongclan = red.getInt("luong");
                red.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return luongclan;
    }

    public static void updateXP(User us, int xp) {
        if (xp <= 0) {
            return;
        }
        if (us.getClan() > 0) {
            if (xp > (25000 * 127 * 128)) {
                xp = 25000 * 127 * 128;
            }
            try {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT `xp` FROM `clan` WHERE `id` = '" + us.getClan() + "' LIMIT 1;");
                if (red.first()) {
                    int level = (int) (((int) Math.sqrt(1 + red.getInt("xp") / 6250) + 1) >> 1);
                    level = level > 127 ? 127 : level;
                    SQLManager.getStatement().executeUpdate("UPDATE `clan` SET `level` = " + level + ",`xp` = `xp` + " + xp + "  WHERE `id` = " + us.getClan() + ";");
                    SQLManager.getStatement().executeUpdate("UPDATE `armymem` SET `clanpoint` = `clanpoint` + " + xp + " WHERE `id` = " + us.getIDDB() + ";");
                }
                red.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateXu(User us, int xuUp) {
        if (xuUp == 0) {
            return;
        }
        try {
            SQLManager.getStatement().executeUpdate("UPDATE `clan` SET  `xu` = `xu` + " + xuUp + " WHERE `id` = " + us.getClan() + ";");
            if (xuUp > 0) {
                SQLManager.getStatement().executeUpdate("UPDATE `clanmem` SET `xu` = `xu` + " + xuUp + " WHERE `user` = " + us.getIDDB() + ";");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLuong(User us, int luongUp) {
        if (luongUp == 0) {
            return;
        }
        try {
            SQLManager.getStatement().executeUpdate("UPDATE `clan` SET  `luong` = `luong` + " + luongUp + " WHERE `id` = " + us.getClan() + ";");
            if (luongUp > 0) {
                SQLManager.getStatement().executeUpdate("UPDATE `clanmem` SET `luong` = `luong` + " + luongUp + " WHERE `user` = " + us.getIDDB() + ";");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void updateItemClan(User us, int Ids, int time) {
        if (us.getClan() > 0) {
            Date Hours = new Date();
            try {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT `Item` FROM `clan` WHERE `id` = " + us.getClan() + " LIMIT 1;");
                red.first();
                JSONArray itemClan = (JSONArray) JSONValue.parse(red.getString("item"));
                //neu item con thoi gian sd -> tang gio cong don thoi gian
                if (getItemClan(us, Ids)) {
                    for (int i = 0; i < itemClan.size(); i++) {
                        JSONObject item = (JSONObject) JSONValue.parse(itemClan.get(i).toString());
                        if (((Long) item.get("id")).intValue() == Ids) {
                            Hours = Until.getDate(item.get("time").toString());
                            item.put("time", Until.addNumHours(Hours, time));
                            itemClan.set(i, item);
                        }
                    }
                    //tao moi item
                } else {
                    //con co trong ruong xoa item
                    for (int i = 0; i < itemClan.size(); i++) {
                        JSONObject item = (JSONObject) JSONValue.parse(itemClan.get(i).toString());
                        if (itemClan.get(i) != null && !getItemClan(us, ((Long) item.get("id")).intValue())) {
                            itemClan.remove(i);
                        }
                    }
                    //tao
                    JSONObject item = new JSONObject();
                    item.put("id", Ids);
                    item.put("time", Until.addNumHours(new Date(), time));
                    itemClan.add(item);
                }
                red.close();
                SQLManager.getStatement().executeUpdate("UPDATE `clan` SET `item`='" + itemClan.toJSONString() + "' WHERE `id`=" + us.getClan() + " LIMIT 1;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getItemClan(User us, int id) {
        if (us.getClan() > 0) {
            try {
                ResultSet red;
                red = SQLManager.getStatement().executeQuery("SELECT `item` FROM `clan` WHERE `id` = " + us.getClan() + " LIMIT 1;");
                red.first();
                Date itemClanArray = new Date();
                Date DateBayGio = new Date();
                JSONArray itemClan = (JSONArray) JSONValue.parse(red.getString("item"));
                red.close();
                for (int i = 0; i < itemClan.size(); i++) {
                    JSONObject item = (JSONObject) JSONValue.parse(itemClan.get(i).toString());
                    if (item != null && id == ((Long) item.get("id")).intValue()) {
                        itemClanArray = Until.getDate(item.get("time").toString());
                        if (itemClanArray.after(DateBayGio)) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected static void getTopTeam(User us, Message ms) {
        try {
            byte page = ms.reader().readByte();
            if (page > BangXHManager.topTeam.size() / 10 || page >= 10) {
                page = 0;
            }
            ClanEntry[] topClan = BangXHManager.getTopTeam(page);
            DataOutputStream ds;
            ms = new Message(116);
            ds = ms.writer();
            int level = 0;
            int xp = 0;
            ds.writeByte(page);
            for (ClanEntry clan : topClan) {
                level = ((int) Math.sqrt(1 + clan.xp / 6250) + 1) >> 1;
                xp = clan.xp;
                xp -= (level) * (level - 1) * 25000;
                ds.writeShort(clan.id);
                ds.writeUTF(clan.name);
                ds.writeByte(clan.mem);
                ds.writeByte(clan.memMax);
                ds.writeUTF(clan.masterName);
                ds.writeInt(clan.xu);
                ds.writeInt(clan.luong);
                ds.writeInt(clan.cup); // cúp
                ds.writeByte(level > 127 ? 127 : level);
                ds.writeByte((byte) (xp / level / 500));
                ds.writeUTF(clan.thongBao);
            }
            ds.flush();
            us.sendMessage(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void contributeClan(User us, Message ms) throws IOException {
        if (us.getClan() > 0) {
            try {
                byte type = ms.reader().readByte();
                int soluong = ms.reader().readInt();
                if (soluong <= 0) {
                    return;
                }
                if (type == 0) {
                    if (soluong > us.getXu()) {
                        return;
                    }
                    updateXu(us, soluong);
                    us.updateXu(-soluong);
                    SQLManager.getStatement().executeUpdate("UPDATE `clanmem` SET `n_contribute` = `n_contribute` + 1, `contribute_time` = '" + Until.toDateString(new Date()) + "', `contribute_text` = '" + Until.getStringNumber(soluong) + " xu" + "' WHERE `user` = " + us.getIDDB() + ";");
                } else if (type == 1) {
                    if (soluong > us.getLuong()) {
                        return;
                    }
                    updateLuong(us, soluong);
                    us.updateLuong(-soluong);
                    SQLManager.getStatement().executeUpdate("UPDATE `clanmem` SET `n_contribute` = `n_contribute` + 1, `contribute_time` = '" + Until.toDateString(new Date()) + "', `contribute_text` = '" + Until.getStringNumber(soluong) + " lượng" + "' WHERE `user` = " + us.getIDDB() + ";");
                }
                ms = new Message(45);
                DataOutputStream ds = ms.writer();
                ds.writeUTF("Góp thành công");
                ds.flush();
                us.sendMessage(ms);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
