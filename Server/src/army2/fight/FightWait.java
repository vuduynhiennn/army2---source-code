package army2.fight;

import army2.server.ClientEntry;
import army2.server.ItemData;
import army2.server.MapData;
import army2.server.Room;
import army2.server.ServerManager;
import army2.server.Until;
import army2.server.User;
import army2.server.GameString;
import network.Message;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author Văn Tú
 */
public class FightWait {

    public final User[] players;
    public FightManager fight;
    final Room parent;
    public final byte id;
    public final boolean[] readys;
    public final int[][] item;
    public boolean started;
    public int numReady;
    public int maxSetPlayer;
    public int maxPlayerInit;
    public int maxPlayer;
    public int numPlayer;
    public boolean passSet;
    public String pass;
    public int money;
    public String name;
    public byte type;
    public byte teaFree;
    public byte map;
    public int boss;
    private Thread kickBoss;
    protected long timeStart;
    public boolean isLH;
    public byte ntLH;
    public byte[] LHMap = new byte[]{30, 31, 32, 33, 34, 35, 36, 37, 38, 39};

    public FightWait(Room parent, byte type, byte id, byte maxPlayers, byte maxPlayerInit, byte map, byte teaFree, boolean isLH) {
        this.parent = parent;
        this.id = id;
        this.maxPlayer = maxPlayers;
        this.maxPlayerInit = maxPlayerInit;
        this.maxSetPlayer = maxPlayerInit;
        this.numPlayer = 0;
        this.numReady = 0;
        this.players = new User[maxPlayers];
        this.readys = new boolean[maxPlayers];
        this.item = new int[maxPlayers][8];
        this.type = type;
        this.teaFree = teaFree;
        this.money = this.parent.minXu;
        this.name = "";
        this.pass = "";
        this.isLH = isLH;
        this.ntLH = (byte) (isLH ? 0 : -1);
        this.map = isLH ? LHMap[ntLH] : map;
        this.fight = new FightManager(this);
        this.started = false;
        this.boss = -1;
        this.timeStart = 0L;
    }

    private void refreshFightWait() {
        this.maxSetPlayer = maxPlayerInit;
        this.numPlayer = 0;
        this.money = this.parent.minXu;
        this.name = "";
        this.pass = "";
        this.passSet = false;
        this.boss = -1;
        if (this.isLH) {
            this.ntLH = 0;
            this.map = LHMap[this.ntLH];
        }
    }

    public int getIndexByIDDB(int iddb) {
        for (int i = 0; i < this.players.length; i++) {
            User pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.getIDDB() == iddb) {
                return i;
            }
        }
        return -1;
    }

    private void changeBoss(final int index) {
        this.boss = index;
        if (this.kickBoss != null) {
            this.kickBoss.interrupt();
        }
        this.kickBoss = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Sleep 5 min
                    int sco = 300;
                    boolean stkick = true;
                    while (stkick && !started && boss != -1) {
                        Thread.sleep(1000L);
                        sco--;
                        if (sco == 0) {
                            kick(boss);
                            stkick = false;
                        }
                    }
                } catch (InterruptedException | IOException e) {
                }
            }
        });
        this.kickBoss.start();
    }

    public void enterFireOval(User us) throws IOException {
        Message ms;
        DataOutputStream ds;
        if (this.type == 6 && us.getClan() == 0) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.notClan());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        if (this.started || (this.isLH && this.ntLH > 0)) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.joinKVError0());
            ds.flush();
            us.sendMessage(ms);
            return;
        } else if (this.money > us.getXu()) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.joinKVError2());
            ds.flush();
            us.sendMessage(ms);
            return;
        } else if (this.numPlayer >= this.maxSetPlayer) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.joinKVError3());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        synchronized (this.players) {
            int bestLocation = -1;
            for (byte i = 0; i < this.players.length; i++) {
                if (this.players[i] == null) {
                    bestLocation = i;
                    break;
                }
            }
            if (bestLocation == -1) {
                return;
            }
            us.setFightWait(this);
            ServerManager.removeWait(us);
            if (this.numPlayer == 0) {
                this.changeBoss(bestLocation);
            }
            // Send to team message 12
            ms = new Message(12);
            ds = ms.writer();
            // Location
            ds.writeByte(bestLocation);
            // iddb
            ds.writeInt(us.getIDDB());
            // clanId
            ds.writeShort(us.getClan());
            // name
            ds.writeUTF(us.getUserName());
            // level
            ds.writeByte(us.getLevel() > 255 ? 255 : us.getLevel());
            // nv
            ds.writeByte(us.getNVUse());
            // equip
            for (int i = 0; i < 5; i++) {
                ds.writeShort(us.getEquip()[i]);
            }
            ds.flush();
            this.sendToTeam(ms);

            this.players[bestLocation] = us;
            this.readys[bestLocation] = false;
            numPlayer++;

            // Send mss 8
            ms = new Message(8);
            ds = ms.writer();
            // Chu phong
            ds.writeInt(this.players[this.boss].getIDDB());
            // Tien
            ds.writeInt(this.money);
            // 2 null byte
            ds.writeByte(0);
            ds.writeByte(0);
            for (byte i = 0; i < this.players.length; i++) {
                if (this.players[i] != null) {
                    User us0 = players[i];

                    // IDDB
                    ds.writeInt(us0.getIDDB());
                    // Clan id
                    ds.writeShort(us0.getClan());
                    // Ten 
                    ds.writeUTF(us0.getUserName());
                    // 
                    ds.writeInt(0);
                    // level
                    ds.writeByte(us0.getLevel() > 255 ? 2555 : us0.getLevel());
                    // Nhan vat
                    ds.writeByte(us0.getNVUse());
                    // Nhan vat data
                    for (byte k = 0; k < 5; k++) {
                        ds.writeShort(us0.getEquip()[k]);
                    }
                    // San sang
                    ds.writeBoolean(this.readys[i]);
                } else {
                    ds.writeInt(-1);
                }
            }
            ds.flush();
            us.sendMessage(ms);

            // Update khu vuc
            ms = new Message(76);
            ds = ms.writer();
            ds.writeByte(this.parent.id);
            ds.writeByte(this.id);
            ds.writeUTF(this.name);
            // Kieu ban do
            ds.writeByte(this.parent.type);
            ds.flush();
            us.sendMessage(ms);

            // Send map
            ms = new Message(75);
            ds = ms.writer();
            ds.writeByte(this.map);
            ds.flush();
            us.sendMessage(ms);
        }
    }

    protected void kick(int index) throws IOException {
        Message ms;
        DataOutputStream ds;
        ms = new Message(11);
        ds = ms.writer();
        ds.writeShort(index);
        ds.writeInt(this.players[index].getIDDB());
        ds.writeUTF(GameString.kickString());
        ds.flush();
        sendToTeam(ms);
        leave(this.players[index]);
    }

    public void kickMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
            return;
        }
        int iddb = ms.reader().readInt();
        int index = getIndexByIDDB(iddb);
        if (index == -1) {
            return;
        }
        if (this.players[index].startQua) {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(String.format(GameString.openingGift(), this.players[index].getUserName()));
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        if (this.readys[index]) {
            return;
        }
        kick(index);
    }

    public void leave(User us) throws IOException {
        if (this.started) {
            this.fight.leave(us);
        }
        this.leaveBoard(us);
        if (this.numPlayer == 0) {
            return;
        }
        Message ms = new Message(14);
        DataOutputStream ds = ms.writer();
        ds.writeInt(us.getIDDB());
        ds.writeInt(this.players[this.boss].getIDDB());
        ds.flush();
        this.sendToTeam(ms);
    }

    public void leaveBoard(User us) throws IOException {
        synchronized (this.players) {
            byte max = ServerManager.maxPlayers, i;
            for (i = 0; i < max; i++) {
                if (this.players[i] != null && this.players[i].getIDDB() == us.getIDDB()) {
                    ServerManager.enterWait(this.players[i]);
                    this.players[i] = null;
                    if (this.boss == i) {
                        for (i = 0; i < max; i++) {
                            if (this.players[i] != null) {
                                this.changeBoss(i);
                                break;
                            }
                        }
                    }
                    this.numPlayer--;
                    if (this.numPlayer == 0) {
                        refreshFightWait();
                    }
                    break;
                }
            }
        }
    }

    public void fightComplete() throws IOException {
        // Chien xong, refresh fight wait
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < this.players.length; i++) {
            this.readys[i] = false;
            User us = this.players[i];
            if (us == null) {
                continue;
            }
            ms = new Message(112);
            ds = ms.writer();
            for (byte j = 0; j < 4; j++) {
                ds.writeByte(us.getItemNum(12 + j));
            }
            ds.flush();
            us.sendMessage(ms);
            us.setFightWait(this);
        }
        this.numReady = 0;
        if (this.boss != -1) {
            changeBoss(this.boss);
        }
        // Send map
        ms = new Message(75);
        ds = ms.writer();
        ds.writeByte(this.map);
        ds.flush();
        this.sendToTeam(ms);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (timeStart > 0L) {
                        Thread.sleep(1000L);
                        timeStart -= 1000L;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendToTeam(Message ms) throws IOException {
        for (User pl : players) {
            if (pl != null) {
                pl.sendMessage(ms);
            }
        }
    }

    public void chatMessage(User us, Message ms) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1) {
            return;
        }
        String s = ms.reader().readUTF();
        DataOutputStream ds;
        if (s.equalsIgnoreCase("chúc mừng năm mới") || s.equalsIgnoreCase("chuc mung nam moi") || s.equalsIgnoreCase("happy new year")) {
            ms = new Message(45);
            ds = ms.writer();
            try {
                if (Until.compare_Day(new Date(), us.getBaodanhsk())) {
                    us.updateEventScore(1);
                    us.updateBaoDanhSk(new Date());
                    ds.writeUTF("Happy New Year bạn nhận được 1 điểm giáng sinh");
                } else {
                    ds.writeUTF("Bạn đã báo danh hôm nay rồi!");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            ds.flush();
            us.sendMessage(ms);
        }
        ms = new Message(9);
        ds = ms.writer();
        ds.writeInt(us.getIDDB());
        ds.writeUTF(s);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void setPassMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
            return;
        }
        String matkhau = ms.reader().readUTF();
        if (matkhau == null || matkhau.equals("")) {
            this.passSet = false;
            this.pass = "";
        } else {
            this.passSet = true;
            this.pass = matkhau;
        }
    }

    public void setMoneyMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
            return;
        }
        int xu = Math.abs(ms.reader().readInt());
        ms = new Message(45);
        DataOutputStream ds = ms.writer();
        if (xu < this.parent.minXu || xu > this.parent.maxXu) {
            ds.writeUTF(String.format(GameString.datCuocError1(), this.parent.minXu, this.parent.maxXu));
            ds.flush();
            us.sendMessage(ms);
            return;
        } else if (xu > us.getXu()) {
            ds.writeUTF(String.format(GameString.xuNotEnought()));
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        for (byte i = 0; i < this.players.length; i++) {
            User pl = this.players[i];
            if (pl == null || i == this.getIndexByIDDB(us.getIDDB())) {
                continue;
            }
            this.readys[i] = false;
        }
        this.numReady = 0;

        this.money = xu;
        ms = new Message(19);
        ds = ms.writer();
        ds.writeShort(0);
        ds.writeInt(xu);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void startMessage(ClientEntry cl) throws IOException {
        Message ms;
        DataOutputStream ds;
        if (this.players[this.boss].getIDDB() != cl.user.getIDDB() || this.started) {
            return;
        }
        //kiem tra thoi gian cho het chua
        if (this.timeStart > 0L) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(String.format(GameString.Wait_click(), Until.getStrungTime(timeStart)));
            ds.flush();
            cl.sendMessage(ms);
            return;
        }
        //neu phong dau doi kiem tra phe con lai co cung clan ko
        if (this.type == 6) {
            for (byte i = 0; i < this.players.length; i++) {
                if (this.players[i] == null) {
                    continue;
                }
                for (byte j = 0; j < this.players.length; j++) {
                    if (j == i || this.players[j] == null || (j % 2 == 0 && i % 2 == 0) || (j % 2 != 0 && i % 2 != 0)) {
                        continue;
                    }
                    if (this.players[j].getClan() == this.players[i].getClan()) {
                        ms = new Message(45);
                        ds = ms.writer();
                        ds.writeUTF("Đội phải cùng phe!");
                        ds.flush();
                        cl.sendMessage(ms);
                        return;
                    }
                }
            }

        }
        // Kiem tra neu ko ai ss
//        if(this.map == 37) {
//            ms = new Message(45); ds = ms.writer();
//            ds.writeUTF("Bản đồ đang nâng cấp");
//            ds.flush();
//            cl.sendMessage(ms);
//            return;
//        }
        if (this.numReady == 0 && this.type != 5) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.startGameError1());
            ds.flush();
            cl.sendMessage(ms);
            return;
        }
        // kiem tra san sang va item
        int nRed = 0, nBlue = 0, nTeamPointRed = 0, nTeamPointBlue = 0;
        int[][] itemMap = new int[this.players.length][];
        for (byte i = 0; i < this.players.length; i++) {
            User pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.startQua) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(String.format(GameString.openingGift(), this.players[i].getUserName()));
                ds.flush();
                cl.sendMessage(ms);
                return;
            }
            if (this.type == 5) {
                nBlue++;
                if (this.numPlayer > 1) {
                    nTeamPointBlue += pl.getAbility()[4];
                }
            } else {
                if (i % 2 == 0) {
                    nBlue++;
                    if (this.numPlayer > 2) {
                        nTeamPointBlue += pl.getAbility()[4];
                    }
                } else {
                    nRed++;
                    if (this.numPlayer > 2) {
                        nTeamPointRed += pl.getAbility()[4];
                    }
                }
            }
            if (this.boss != i && !readys[i]) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(String.format(GameString.startGameError2(), pl.getUserName()));
                ds.flush();
                cl.sendMessage(ms);
                return;
            } else if (pl.getXu() < this.money) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(String.format(GameString.startGameError3(), pl.getUserName()));
                ds.flush();
                cl.sendMessage(ms);
                return;
            }
            int lent = ItemData.entrys.size();
            itemMap[i] = new int[lent];
            for (byte j = 0; j < lent; j++) {
                itemMap[i][j] = 0;
            }
            for (byte j = 0; j < 8; j++) {
                itemMap[i][this.item[i][j]]++;
            }
            for (byte j = 0; j < 8; j++) {
                byte itemSelect = (byte) item[i][j];
                if (itemSelect <= 1 || itemSelect >= lent) {
                    continue;
                }
                if ((j >= 4 && itemSelect > 0 && pl.getItemNum(12 + j - 4) <= 0) || itemMap[i][itemSelect] > ItemData.nItemDcMang[itemSelect] || itemMap[i][itemSelect] > pl.getItemNum(itemSelect)) {
                    ms = new Message(45);
                    ds = ms.writer();
                    ds.writeUTF(String.format(GameString.startGameError4(), pl.getUserName(), j));
                    ds.flush();
                    this.sendToTeam(ms);
                    return;
                }
            }
        }
        if (this.type != 5 && nBlue != nRed) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.startGameError4());
            ds.flush();
            cl.sendMessage(ms);
            return;
        }
        this.started = true;
        this.timeStart = 5000L;
        this.fight.startGame(Until.getTeamPoint(nTeamPointBlue, nBlue), Until.getTeamPoint(nTeamPointRed, nRed));
    }

    public void setNameMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
            return;
        }
        String nameS = ms.reader().readUTF();
        if (nameS == null || nameS.equals("")) {
            this.name = "";
        } else {
            this.name = nameS;
        }
    }

    public void setMaxPlayerMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB()) {
            return;
        }
        byte numPL = ms.reader().readByte();
        if ((numPL > 0) && (numPL < 9) && (numPL % 2 == 0) && (numPlayer < numPL)) {
            this.maxSetPlayer = numPL;
        }
    }

    public void doiPheMessage(User us, Message ms) throws IOException {
        if (this.started) {
            return;
        }
        byte i, j = 0;
        synchronized (this.players) {
            for (i = (byte) (this.players.length - 1); i >= 0; i--) {
                if ((this.players[i] != null) && (this.players[i].getIDDB() == us.getIDDB())) {
                    if (i % 2 == 0) {
                        j = 1;
                    } else {
                        j = 0;
                    }
                    for (; j < this.players.length; j += 2) {
                        if (this.players[j] == null) {
                            this.players[j] = us;
                            this.players[i] = null;
                            if (i == this.boss) {
                                this.boss = j;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
        if (i >= 0) {
            ms = new Message(71);
            DataOutputStream ds = ms.writer();
            ds.writeInt(us.getIDDB());
            ds.writeByte(j);
            ds.flush();
            this.sendToTeam(ms);
        }
    }

    public void setMapMessage(User us, Message ms) throws IOException {
        if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
            return;
        }
        byte mapup = ms.reader().readByte();
        DataOutputStream ds;
        if (this.isLH) {
            ms = new Message(45);
            ds = ms.writer();
            ds.writeUTF(GameString.selectMapError1_3());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        for (byte i = 0; i < this.players.length; i++) {
            User pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.startQua) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(String.format(GameString.openingGift(), this.players[i].getUserName()));
                ds.flush();
                us.sendMessage(ms);
                return;
            }
        }
        if (this.parent.slMap.size() > 0) {
            for (byte i = 0; i < this.parent.slMap.size(); i++) {
                if (mapup == this.parent.slMap.get(i)) {
                    break;
                }
                if (i == this.parent.slMap.size() - 1) {
                    ms = new Message(45);
                    ds = ms.writer();
                    ds.writeUTF(GameString.selectMapError1_3());
                    ds.flush();
                    us.sendMessage(ms);
                    return;
                }
            }
        } else if (this.map < this.parent.minMap || this.map > this.parent.maxMap) {
            ms = new Message(45);
            ds = ms.writer();
            if (this.parent.maxMap == this.parent.minMap) {
                ds.writeUTF(String.format(GameString.selectMapError1_1(), MapData.entrys.get(this.parent.minMap).name));
            } else if (this.parent.maxMap == this.parent.minMap + 1) {
                ds.writeUTF(String.format(GameString.selectMapError1_2(), MapData.entrys.get(this.parent.minMap).name, MapData.entrys.get(this.parent.maxMap).name));
            } else {
                ds.writeUTF(GameString.selectMapError1_3());
            }
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        this.map = mapup;
        if (this.map == 27) {
            int numbMap = MapData.entrys.size() - ServerManager.numMapBoss;
            while (this.map == 27) {
                this.map = (byte) Until.nextInt(numbMap);
            }
        }
        for (byte i = 0; i < this.players.length; i++) {
            User pl = this.players[i];
            if (pl == null || i == boss) {
                continue;
            }
            this.readys[i] = false;
        }
        this.numReady = 0;
        ms = new Message(75);
        ds = ms.writer();
        ds.writeByte(this.map);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void findPlayerMessage(User us, Message ms) throws IOException {
        boolean find = ms.reader().readBoolean();
        int iđdb = ms.reader().readInt();
        DataOutputStream ds;
        if (find) {
            if (this.players[this.boss].getIDDB() != us.getIDDB() || this.started) {
                return;
            }
            User[] uss = ServerManager.findWaitPlayers(us);
            ms = new Message(78);
            ds = ms.writer();
            ds.writeBoolean(true);
            ds.writeByte(uss.length);
            for (User us0 : uss) {
                ds.writeUTF(us0.getUserName());
                ds.writeInt(us0.getIDDB());
                ds.writeByte(us0.getNVUse());
                ds.writeInt(us0.getXu());
                int lvS = us0.getLevel();
                ds.writeByte("2.2.3".equals(us.client.versionARM) ? (lvS > 127 ? 127 : lvS) : (lvS > 255 ? 255 : lvS));
                ds.writeByte(us0.getLevelPercen());

                for (byte j = 0; j < 5; j++) {
                    ds.writeShort(us0.getEquip()[j]);
                }
            }
            ds.flush();
            us.sendMessage(ms);
        } else {
            User us1 = ServerManager.getUser(iđdb);
            if (us1 == null) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(GameString.inviteError1());
                ds.flush();
                us.sendMessage(ms);
                ServerManager.removeWait(iđdb);
                return;
            }
            if (us1.getState() != User.State.Waiting) {
                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(GameString.inviteError2());
                ds.flush();
                us.sendMessage(ms);
                return;
            }
            ms = new Message(78);
            ds = ms.writer();
            ds.writeBoolean(false);
            ds.writeUTF(String.format(GameString.inviteMessage(), us.getUserName()));
            ds.writeByte(this.parent.id);
            ds.writeByte(this.id);
            ds.writeUTF(this.pass);
            ds.flush();
            us1.sendMessage(ms);
        }
    }

    public void readyMessage(User us, Message ms) throws IOException {
        boolean ready = ms.reader().readBoolean();
        if (this.players[this.boss].getIDDB() == us.getIDDB() || this.started) {
            return;
        }
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1) {
            return;
        }
        if (this.readys[index] != ready) {
            this.readys[index] = ready;
            if (ready) {
                this.numReady++;
            } else {
                this.numReady--;
            }
        }
        ms = new Message(16);
        DataOutputStream ds = ms.writer();
        ds.writeInt(us.getIDDB());
        ds.writeBoolean(ready);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void setItemMessage(User us, Message ms) throws IOException {
        if (this.started) {
            return;
        }
        synchronized (this.players) {
            int index = -1;
            for (byte i = 0; i < this.players.length; i++) {
                if ((this.players[i] != null) && (this.players[i].getIDDB() == us.getIDDB())) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return;
            }
            for (int i = 0; i < 8; i++) {
                byte id = ms.reader().readByte();
                if (us.getItemNum(id) > 0) {
                    this.item[index][i] = id;
                }
            }
        }
    }

}
