package army2.server;

import army2.server.ClanManager.ClanEntry;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;

/**
 *
 * @author Văn Tú
 */
public class BangXHManager {

    public static class BangXHEntry {

        int index;
        int iddb;
        byte nv;
        int nXH;
    }

    protected static final String[] bangXHString = new String[]{"DANH DỰ", "CAO THỦ", "ĐẠI GIA XU", "ĐẠI GIA LƯỢNG", "CHUYỂN SINH", "ĐIỂM SỰ KIỆN"};
    protected static final String[] bangXHString1 = new String[]{"Danh dự", "xp", "xu", "lượng", "tổng số lần", "điểm"};
    protected static Date giocapnhat;
    @SuppressWarnings("unchecked")
    protected static final ArrayList<BangXHEntry> bangXH[] = new ArrayList[bangXHString.length];
    protected static final Timer t = new Timer(true);
    protected static final ArrayList<ClanEntry> topTeam = new ArrayList<>();
    public static boolean isComplete;

    protected static void init() {
        for (int i = 0; i < bangXH.length; i++) {
            bangXH[i] = new ArrayList<>();
        }
        Calendar cl = GregorianCalendar.getInstance();
        Date d = new Date();
        giocapnhat = new Date();
        cl.setTime(d);
        cl.set(Calendar.HOUR_OF_DAY, 0);
        cl.set(Calendar.MINUTE, 0);
        cl.set(Calendar.SECOND, 0);
        cl.add(Calendar.MILLISECOND, 0);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                isComplete = false;
                for (int i = 0; i < bangXHString.length; i++) {
                    refreshXH(i);
                }
                refreshTopTeam();
                isComplete = true;
                System.out.println("Refresh BXH + TopTeam");
            }
        }, cl.getTime(), 86400000L);
    }

    protected static void refreshXH(int type) {
        bangXH[type].clear();
        ArrayList<BangXHEntry> bxh = bangXH[type];
        switch (type) {
            case 0:
                try {
                    ResultSet res = SQLManager.getStatement().executeQuery("SELECT `id`,`dvong` FROM `armymem` ORDER BY `dvong` DESC LIMIT 0, 100;");
                    int i = 1;
                    while (res.next()) {
                        int iddb = res.getInt("id");
                        int dvong = res.getInt("dvong");
                        if (iddb < 3) {
                            continue;
                        }
                        if (i < 1000) {
                            BangXHEntry bXHE = new BangXHEntry();
                            bXHE.iddb = iddb;
                            bXHE.index = i;
                            bXHE.nXH = dvong;
                            bxh.add(bXHE);
                        }
                        i++;
                    }
                    res.close();
                    for (int j = 0; j < bxh.size(); j++) {
                        BangXHEntry bXHE = bxh.get(j);
                        SQLManager.getStatement().executeUpdate("UPDATE `armymem` SET `top`='" + bXHE.index + "' WHERE `id`=" + bXHE.iddb + " LIMIT 1;");
                        int[] xuUp = new int[]{30000, 20000, 10000};
                        if (bXHE.index <= 3) {
                            if (ServerManager.getUser(bXHE.iddb) != null) {
                                ServerManager.getUser(bXHE.iddb).updateXu(xuUp[bXHE.index - 1]);
                            } else {
                                SQLManager.getStatement().executeUpdate("UPDATE `armymem` SET `xu`= `xu` + " + xuUp[bXHE.index - 1] + " WHERE (`id`=" + bXHE.iddb + " AND `xu` < 2000000000)LIMIT 1;");
                            }
                        }
                        if (j > 99) {
                            bxh.remove(j);
                            j--;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    int i = 1;
                    ResultSet red = SQLManager.getStatement().executeQuery("SELECT `id`,`xpMax` FROM `armymem` ORDER BY `xpMax` DESC LIMIT 0, 100;");
                    while (red.next()) {
                        int iddb = red.getInt("id");
                        int xpMax = red.getInt("xpMax");
                        if (iddb < 3 || iddb == 792) {
                            continue;
                        }
                        BangXHEntry bXHE = new BangXHEntry();
                        bXHE.iddb = iddb;
                        bXHE.index = i++;
                        bXHE.nXH = xpMax;
                        bxh.add(bXHE);
                    }
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    int i = 1;
                    ResultSet red = SQLManager.getStatement().executeQuery("SELECT `id`,`xu` FROM `armymem` ORDER BY `xu` DESC LIMIT 0, 100;");
                    while (red.next()) {
                        int iddb = red.getInt("id");
                        int xu = red.getInt("xu");
                        if (iddb < 3) {
                            continue;
                        }
                        BangXHEntry bXHE = new BangXHEntry();
                        bXHE.iddb = iddb;
                        bXHE.index = i++;
                        bXHE.nXH = xu;
                        bxh.add(bXHE);
                    }
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                try {
                    int i = 1;
                    ResultSet red = SQLManager.getStatement().executeQuery("SELECT `id`,`luong` FROM `armymem` ORDER BY `luong` DESC LIMIT 0, 100;");
                    while (red.next()) {
                        int iddb = red.getInt("id");
                        int luong = red.getInt("luong");
                        if (iddb < 3) {
                            continue;
                        }
                        BangXHEntry bXHE = new BangXHEntry();
                        bXHE.iddb = iddb;
                        bXHE.index = i++;
                        bXHE.nXH = luong;
                        bxh.add(bXHE);
                    }
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    int i = 1;
                    ResultSet red = SQLManager.getStatement().executeQuery("SELECT `id`,`CSinh` FROM `armymem` WHERE `CSinh` > 0 ORDER BY `CSinh` DESC LIMIT 0, 100;");
                    while (red.next()) {
                        int iddb = red.getInt("id");
                        int cs = red.getInt("CSinh");
                        if (iddb < 3) {
                            continue;
                        }
                        BangXHEntry bXHE = new BangXHEntry();
                        bXHE.iddb = iddb;
                        bXHE.index = i++;
                        bXHE.nXH = cs;
                        bxh.add(bXHE);
                    }
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 5:
                try {
                    int i = 1;
                    ResultSet red = SQLManager.getStatement().executeQuery("SELECT `id`,`point_event` FROM `armymem` ORDER BY `point_event` DESC LIMIT 0, 100;");
                    while (red.next()) {
                        int iddb = red.getInt("id");
                        int point = red.getInt("point_event");
                        if (iddb < 3 || point < 1) {
                            continue;
                        }
                        BangXHEntry bXHE = new BangXHEntry();
                        bXHE.iddb = iddb;
                        bXHE.index = i++;
                        bXHE.nXH = point;
                        bxh.add(bXHE);
                    }
                    red.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    protected static final BangXHEntry[] getBangXH(int type, int page) {
        ArrayList<BangXHEntry> bxh = bangXH[type];
        int index = page * 10, lent = 0;
        if (index >= bxh.size() || index < 0) {
            index = 0;
        }
        if (index < bxh.size()) {
            lent = (bxh.size() - index) > 10 ? 10 : (bxh.size() - index);
        }
        BangXHEntry[] bxhA = new BangXHEntry[lent];
        for (int i = 0; i < lent; i++) {
            if (bxh.size() <= index + i && index + i >= 0) {
                continue;
            }
            bxhA[i] = bxh.get(index + i);
        }
        return bxhA;
    }

    public static void refreshTopTeam() {
        try {
            ClanEntry clan = null;
            ResultSet red = SQLManager.getStatement().executeQuery("SELECT * FROM `clan` ORDER BY `xp` DESC, `cup` DESC LIMIT 100;");
            while (red.next()) {
                clan = new ClanEntry();
                clan.xp = red.getInt("xp");
                clan.level = ((int) Math.sqrt(1 + red.getInt("xp") / 6250) + 1) >> 1;
                clan.id = red.getShort("id");
                clan.name = red.getString("name");
                clan.mem = red.getInt("Mem");
                clan.memMax = red.getInt("MemMax");
                clan.masterName = red.getString("masterName");
                clan.xu = red.getInt("xu");
                clan.luong = red.getInt("luong");
                clan.cup = red.getInt("cup");
                clan.thongBao = red.getString("desc");
                topTeam.add(clan);
            }
        } catch (SQLException e) {

        }
    }

    public static ClanEntry[] getTopTeam(int page) {
        ArrayList<ClanEntry> bxh = topTeam;
        int index = page * 10, lent = 0;
        if (index >= bxh.size()) {
            index = 0;
        }
        if (index < bxh.size()) {
            lent = (bxh.size() - index) > 10 ? 10 : (bxh.size() - index);
        }
        ClanEntry[] bxhA = new ClanEntry[lent];
        for (int i = 0; i < lent; i++) {
            if (bxh.size() <= index + i && index + i >= 0) {
                continue;
            }
            bxhA[i] = bxh.get(index + i);
        }
        return bxhA;
    }

}
