package army2.fight;

import army2.fight.BulletManager.AddBoss;
import army2.fight.BulletManager.BomHenGio;
import army2.fight.BulletManager.Bullets;
import army2.fight.BulletManager.VoiRong;
import army2.fight.boss.Balloon;
import army2.fight.boss.Balloon_FanBack;
import army2.fight.boss.Balloon_Gun;
import army2.fight.boss.Balloon_GunBig;
import army2.server.GameString;
import army2.server.ItemData;
import army2.server.ServerManager;
import army2.server.Until;
import army2.server.User;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import network.Message;
import army2.fight.boss.BigBoom;
import army2.fight.boss.Ghost;
import army2.fight.boss.Ghost2;
import army2.fight.boss.Robot;
import army2.fight.boss.SpiderPoisonous;
import army2.fight.boss.SpiderMachine;
import army2.fight.boss.Trex;
import army2.fight.boss.UFO;
import army2.fight.boss.UFOFire;
import army2.fight.bullet.ItemBomHenGio;
import army2.server.SpecialItemData;
import java.util.Date;

/**
 *
 * @author Văn Tú
 */
public class FightManager {

    final FightWait wait;
    private final User userLt;
    private boolean isShoot;
    private int teamlevel;
    public boolean isNextTurn;
    private Date matchTime;
    protected boolean ltap;
    protected byte type;
    protected boolean isBossTurn;
    protected byte bossTurn;
    protected byte playerTurn;
    protected int nTurn;
    protected byte nHopQua;
    protected int playerCount;
    public int allCount;
    protected int WindX;
    protected int WindY;
    protected boolean isFight;
    protected final byte timeCountMax = 30;
    public Player[] players;
    public MapManager mapMNG;
    public BulletManager bullMNG;
    public CountDownMNG countDownMNG;

    public FightManager(User us, byte map) {
        this.wait = null;
        this.ltap = true;
        this.type = 0;
        this.playerCount = 1;
        this.playerTurn = -1;
        this.nTurn = 0;
        this.isBossTurn = false;
        this.bossTurn = 0;
        this.allCount = 1;
        this.WindX = 0;
        this.WindY = 0;
        this.isFight = false;
        this.nHopQua = 0;
        this.mapMNG = new MapManager(this);
        this.bullMNG = new BulletManager(this);
        this.countDownMNG = null;
        this.userLt = us;
        this.mapMNG.setMapId(map);
    }

    public FightManager(FightWait fo) {
        this.isShoot = false;
        this.isNextTurn = true;
        this.wait = fo;
        this.userLt = null;
        this.ltap = false;
        this.type = fo.type;
        this.playerCount = 0;
        this.allCount = 0;
        this.playerTurn = -1;
        this.isBossTurn = false;
        this.bossTurn = 0;
        this.WindX = 0;
        this.WindY = 0;
        this.nHopQua = 0;
        this.players = new Player[ServerManager.maxElementFight];
        this.isFight = false;
        this.mapMNG = new MapManager(this);
        this.bullMNG = new BulletManager(this);
        this.countDownMNG = new CountDownMNG(this, timeCountMax);
    }

    protected void setMap(byte map) {
        this.mapMNG.setMapId(map);
    }

    void sendToTeam(Message ms) throws IOException {
        if (this.ltap) {
            this.userLt.sendMessage(ms);
            return;
        }
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            pl.us.sendMessage(ms);
        }
    }

    public void removeUser(User us) {
        synchronized (this.players) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                if (this.players[i].us.getIDDB() == us.getIDDB()) {
                    this.players[i] = null;
                    break;
                }
            }
        }
    }

    public int getIndexByIDDB(int iddb) {
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            if (this.players[i] != null && this.players[i].us != null && this.players[i].us.getIDDB() == iddb) {
                return i;
            }
        }
        return -1;
    }

    public int getIDTurn() {
        if (ltap) {
            return 0;
        } else if (isBossTurn && type == 5) {
            return this.bossTurn;
        } else {
            return this.playerTurn;
        }
    }

    public Player getPlayerTurn() {
        if (isBossTurn) {
            return this.players[this.bossTurn];
        }
        return this.players[this.playerTurn];
    }

    public Player getPlayerClosest(short X, short Y) {
        int XClosest = -1;
        Player plClosest = null;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.isDie) {
                continue;
            }
            int kcX = Math.abs(pl.X - X);
            if (XClosest == -1 || kcX < XClosest) {
                XClosest = kcX;
                plClosest = pl;
            }
        }
        return plClosest;
    }

    public int getWindX() {
        return this.WindX;
    }

    public int getWindY() {
        return this.WindY;
    }

    public int getLevelTeam() {
        return this.teamlevel;
    }

    protected boolean getisLH() {
        return this.wait.isLH;
    }

    private void nextBoss() throws IOException {
        //Map Bom 1
        if (this.wait.map == 30) {
            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 8, 8, 9, 9})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) ((i % 2 == 0) ? Until.nextInt(95, 315) : Until.nextInt(890, 1070));
                short Y = (short) (50 + 40 * Until.nextInt(3));
                players[allCount] = new BigBoom(this, (byte) 12, "BigBoom", (byte) allCount, 1500 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }
        //Map Bom 2
        if (this.wait.map == 31) {
            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 8, 8, 9, 9})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) (Until.nextInt(445, 800) + i * 50);
                short Y = 180;
                players[allCount] = new BigBoom(this, (byte) 12, "SmallBoom", (byte) allCount, 1500 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }
        //map nhen may
        if (this.wait.map == 32) {
            byte numBoss = (new byte[]{2, 2, 3, 3, 4, 4, 5, 5, 5})[playerCount];
            short[] tempX = new short[]{505, 1010, 743, 425, 1068};
            short[] tempY = new short[]{221, 221, 198, 369, 369, 369};
            for (byte i = 0; i < numBoss; i++) {
                players[allCount] = new SpiderMachine(this, (byte) 13, "Spider Robot", (byte) allCount, 4785 + (getLevelTeam() * 15), (short) tempX[i], (short) tempY[i]);
                allCount++;
            }
        }
        //map thanh pho may
        if (this.wait.map == 33) {
            byte numBoss = (new byte[]{2, 2, 3, 3, 4, 4, 5, 5, 6})[playerCount];
            short[] tempX = new short[]{420, 580, 720, 240, 55, 900};
            for (int i = 0; i < numBoss; i++) {
                short X = tempX[i];
                short Y = (short) 200;
                players[allCount] = new Robot(this, (byte) 14, "Robot", (byte) allCount, 3700 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }
        //Map T-rex Máy
        if (this.wait.map == 34) {
            short X = 880;
            short Y = 400;
            players[allCount] = new Trex(this, (byte) 15, "T-rex", (byte) allCount, 15000 + (this.getLevelTeam() * 10), X, Y);
            allCount++;

            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 6, 7, 7, 8})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                X = (short) (Until.nextInt(470, 755));
                Y = 400;
                players[allCount] = new BigBoom(this, (byte) 12, "BigBooom", (byte) allCount, 1500 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }
        //Map KV cam
        if (this.wait.map == 35) {
            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 8, 8, 9, 9})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) (Until.nextInt(300, 800));
                short Y = (short) Until.nextInt(-350, 100);
                players[allCount] = new UFO(this, (byte) 16, "UFO", (byte) allCount, 4500 + (this.getLevelTeam() * 12), X, Y);
                allCount++;
            }
        }
        //Map HMLS
        if (this.wait.map == 36) {
            short X = (short) (Until.nextInt(300, 800));
            short Y = (short) Until.nextInt(-350, 100);
            players[allCount] = new Balloon(this, (byte) 17, "Balloon", (byte) allCount, 1, X, Y);
            allCount++;
            players[allCount] = new Balloon_Gun(this, (byte) 18, "Balloon Gun", (byte) allCount, 2000 + (this.getLevelTeam() * 10), (short) (X + 51), (short) (Y + 19));
            allCount++;
            players[allCount] = new Balloon_GunBig(this, (byte) 19, "Balloon Gun Big", (byte) allCount, 2500 + (this.getLevelTeam() * 10), (short) (X - 5), (short) (Y + 30));
            allCount++;
            players[allCount] = new Balloon_FanBack(this, (byte) 20, "Fan Back", (byte) allCount, 1000 + (this.getLevelTeam() * 10), (short) (X - 67), (short) (Y - 6));
            allCount++;
        }

        //map nhen doc
        if (this.wait.map == 37) {
            byte numBoss = (new byte[]{2, 3, 3, 4, 4, 5, 5, 6, 6})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) Until.nextInt(20, this.mapMNG.Width - 20);
                short Y = (short) 250;
                players[allCount] = new SpiderPoisonous(this, (byte) 22, "Spider Poisonous", (byte) allCount, 3800 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }

        //map Nghia trang 1
        if (this.wait.map == 38) {
            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 8, 8, 9, 9})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) ((short) 700 - i * 80);
                short Y = (short) (Until.nextInt(30));
                players[allCount] = new Ghost(this, (byte) 25, "Ghost", (byte) allCount, 1800 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }

        //map Nghia trang 2
        if (this.wait.map == 39) {
            byte numBoss = (new byte[]{4, 4, 5, 5, 6, 8, 8, 9, 9})[playerCount];
            for (byte i = 0; i < numBoss; i++) {
                short X = (short) (700 - i * 80);
                short Y = (short) Until.nextInt(30);
                players[allCount] = new Ghost2(this, (byte) 26, "Ghost II", (byte) allCount, 1800 + (this.getLevelTeam() * 10), X, Y);
                allCount++;
            }
        }

        int bossLen = this.allCount - ServerManager.maxPlayers;
        Message ms = new Message(89);
        DataOutputStream ds = ms.writer();
        ds.writeByte(bossLen);
        for (byte i = 0; i < bossLen; i++) {
            Boss boss = (Boss) this.players[ServerManager.maxPlayers + i];
            ds.writeInt(-1);
            ds.writeUTF(boss.name);
            ds.writeInt(boss.HPMax);
            ds.writeByte(boss.idNV);
            ds.writeShort(boss.X);
            ds.writeShort(boss.Y);
        }
        ds.flush();
        this.sendToTeam(ms);
    }

    private void nextAngry() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.isUpdateAngry) {
                ms = new Message(113);
                ds = ms.writer();
                ds.writeByte(i);
                ds.writeByte(pl.angry);
                ds.flush();
                this.sendToTeam(ms);
                pl.isUpdateAngry = false;
            }
        }
    }

    private void calcMM() {
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            pl.nextMM();
        }
    }

    private void nextMM() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.isMM) {
                if (i == this.playerTurn || pl.isUpdateHP) {
                    ms = new Message(100);
                    ds = ms.writer();
                    ds.writeByte(i);
                    ds.flush();
                    this.sendToTeam(ms);
                }
                pl.isMM = false;
            }
        }
    }

    private void nextBiDoc() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.isBiDoc) {
                ms = new Message(108);
                ds = ms.writer();
                ds.writeByte(pl.index);
                ds.flush();
                this.sendToTeam(ms);
            }
        }
    }

    private void nextCantSee() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.cantSeeCount > 0) {
                ms = new Message(106);
                ds = ms.writer();
                ds.writeByte(0);
                ds.writeByte(pl.index);
                ds.flush();
                this.sendToTeam(ms);
            }
        }
    }

    private void nextCantMove() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.cantMoveCount > 0) {
                ms = new Message(107);
                ds = ms.writer();
                ds.writeByte(0);
                ds.writeByte(pl.index);
                ds.flush();
                this.sendToTeam(ms);
            }
        }
    }

    private void nextHP() throws IOException {
        Message ms;
        DataOutputStream ds;
        for (byte i = 0; i < this.allCount; i++) {
            Player pl = this.players[i];
            if (pl == null) {
                continue;
            }
            if (pl.isUpdateHP) {
                ms = new Message(51);
                ds = ms.writer();
                ds.writeByte(i);
                ds.writeShort(pl.HP);
                ds.writeByte(pl.pixel);
                ds.flush();
                this.sendToTeam(ms);
                pl.isUpdateHP = false;
            }
        }
        this.nextXP();
        this.nextCUP();
        this.nextAngry();
    }

    private void nextXP() {
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            if (pl.isUpdateXP) {
                int oldXP = pl.us.getXP();
                pl.us.updateXP(pl.XPUp, true);
                int newXP = pl.us.getXP();
                pl.AllXPUp += newXP - oldXP;
                pl.XPUp = 0;
                pl.isUpdateXP = false;
            }
        }
    }

    private void nextCUP() {
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            if (pl.isUpdateCup) {
                try {
                    pl.us.updateDvong(pl.CupUp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pl.CupUp = 0;
                pl.isUpdateCup = false;
            }
        }
    }

    private void nextWind() throws IOException {
        Player pl = this.players[this.getIDTurn()];
        if (pl.ngungGioCount > 0) {
            this.WindX = 0;
            this.WindY = 0;
            pl.ngungGioCount--;
        } else {
            if (Until.nextInt(0, 100) > 25) {
                this.WindX = Until.nextInt(-70, 70);
                this.WindY = Until.nextInt(-70, 70);
            }
        }
        Message ms = new Message(25);
        DataOutputStream ds = ms.writer();
        ds.writeByte(WindX);
        ds.writeByte(WindY);
        ds.flush();
        this.sendToTeam(ms);
    }

    private void huyVoHinh(byte index) throws IOException {
        Message ms = new Message(80);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.flush();
        this.sendToTeam(ms);
    }

    private void huyCantSee(byte index) throws IOException {
        Message ms = new Message(106);
        DataOutputStream ds = ms.writer();
        ds.writeByte(1);
        ds.writeByte(index);
        ds.flush();
        this.sendToTeam(ms);
    }

    /*
    private void huyCantMove(int index) throws IOException {
        Message ms = new Message(107); DataOutputStream ds = ms.writer();
        ds.writeByte(1);
        ds.writeByte(index);
        ds.flush();
        this.sendToTeam(ms);
    }
     */
    public void nextTurn() throws IOException {
        if (!this.isNextTurn) {
            return;
        }
        this.nTurn++;
        // Update XY Player
        for (byte i = 0; i < this.allCount; i++) {
            Player pl = players[i];
            if (pl == null) {
                continue;
            }
            pl.chuanHoaXY();
        }
        if (this.ltap) {
            this.playerTurn = 0;
        } else {
            if (this.countDownMNG != null) {
                this.countDownMNG.stopCount();
            }
            if (this.playerTurn == -1) {
                while (true) {
                    int next = 0;
                    if (this.type != 5) {
                        next = Until.nextInt(ServerManager.maxPlayers);
                    } else {
                        next = Until.nextInt(this.allCount);
                    }
                    if (this.players[next] != null && this.players[next].idNV != 18 && this.players[next].idNV != 19 && this.players[next].idNV != 20 && this.players[next].idNV != 21 && this.players[next].idNV != 23 && this.players[next].idNV != 24) {
                        if (next < ServerManager.maxPlayers) {
                            this.playerTurn = (byte) next;
                            this.isBossTurn = false;
                            this.bossTurn = ServerManager.maxPlayers;
                        } else {
                            this.bossTurn = (byte) next;
                            this.isBossTurn = true;
                            this.playerTurn = 0;
                        }
                        break;
                    }
                }
            } else {
                this.nextAngry();
                if (!isBossTurn) {
                    Player plTurn = null;
                    if (this.playerTurn >= 0 && this.playerTurn < this.players.length) {
                        plTurn = this.players[this.playerTurn];
                    }
                    if (plTurn != null) {
                        this.players[this.playerTurn].isUsePow = false;
                        this.players[this.playerTurn].isUseItem = false;
                        this.players[this.playerTurn].itemUsed = -1;
                    }
                }
                if (this.type == 5) {
                    if (this.isBossTurn) {
                        this.isBossTurn = false;
                        int turn = this.playerTurn + 1;
                        while (turn != this.playerTurn) {
                            if (turn == ServerManager.maxPlayers) {
                                turn = 0;
                            }
                            if (this.players[turn] != null && !this.players[turn].isDie && this.players[turn].idNV != 18 && this.players[turn].idNV != 19 && this.players[turn].idNV != 20 && this.players[turn].idNV != 21 && this.players[turn].idNV != 23 && this.players[turn].idNV != 24) {
                                this.playerTurn = (byte) turn;
                                break;
                            }
                            turn++;
                        }
                    } else {
                        this.isBossTurn = true;
                        byte turn = (byte) (this.bossTurn + 1);
                        while (turn != this.bossTurn) {
                            if (turn >= this.allCount) {
                                turn = ServerManager.maxPlayers;
                            }
                            if (this.players[turn] != null && !this.players[turn].isDie && this.players[turn].idNV != 18 && this.players[turn].idNV != 19 && this.players[turn].idNV != 20 && this.players[turn].idNV != 21 && this.players[turn].idNV != 23 && this.players[turn].idNV != 24) {
                                this.bossTurn = turn;
                                break;
                            }
                            turn++;
                        }
                    }
                } else {
                    byte turn = (byte) (this.playerTurn + 1);
                    while (turn != this.playerTurn) {
                        if (turn == this.allCount) {
                            turn = 0;
                        }
                        if (this.players[turn] != null && !this.players[turn].isDie && this.players[turn].idNV != 18 && this.players[turn].idNV != 19 && this.players[turn].idNV != 20 && this.players[turn].idNV != 21 && this.players[turn].idNV != 23 && this.players[turn].idNV != 24) {
                            this.playerTurn = (byte) turn;
                            break;
                        }
                        turn++;
                    }
                }
            }
        }
        if (!isBossTurn) {
            this.isShoot = false;
            Player pl = this.players[this.playerTurn];
            pl.buocDi = 0;
            if (pl.hutMauCount > 0) {
                pl.hutMauCount--;
            }
            if (pl.voHinhCount > 0) {
                pl.voHinhCount--;
                if (pl.voHinhCount == 0) {
                    huyVoHinh(this.playerTurn);
                }
            }
            if (pl.tangHinhCount > 0) {
                pl.tangHinhCount--;
                if (pl.tangHinhCount == 0) {
                    huyVoHinh(this.playerTurn);
                }
            }
            if (pl.cantSeeCount > 0) {
                pl.cantSeeCount--;
                if (pl.cantSeeCount == 0) {
                    huyCantSee(this.playerTurn);
                }
            }
            if (pl.cantMoveCount > 0) {
                pl.cantMoveCount--;
                if (pl.cantMoveCount == 0) {
                    huyCantSee(this.playerTurn);
                }
            }
            if (pl.isBiDoc) {
                pl.updateHP(-150);
            }
            pl.updateAngry(10);
        } else {
            Player pl = this.players[this.bossTurn];
            pl.buocDi = 0;
        }
        if (this.bullMNG.hasVoiRong) {
            for (byte i = 0; i < this.bullMNG.voiRongs.size(); i++) {
                VoiRong vr = this.bullMNG.voiRongs.get(i);
                vr.count--;
                if (vr.count < 0) {
                    this.bullMNG.voiRongs.remove(i);
                    i--;
                }
            }
            if (this.bullMNG.voiRongs.isEmpty()) {
                this.bullMNG.hasVoiRong = false;
            }
        }
        if (this.bullMNG.boms.size() > 0) {
            for (byte i = 0; i < this.bullMNG.boms.size(); i++) {
                BomHenGio bom = this.bullMNG.boms.get(i);
                bom.count--;
                if (bom.count == 1) {
                    this.bullMNG.exploreBom(i);
                    i--;
                }
            }
        }
        if (this.bullMNG.addboss.size() > 0) {
            for (byte i = 0; i < this.bullMNG.addboss.size(); i++) {
                AddBoss bos = this.bullMNG.addboss.get(i);
                this.addBoss(bos.players);
                players[allCount - 1].XPExist = bos.XPE;
            }
            this.bullMNG.addboss.clear();
        }
        if (this.bullMNG.buls.size() > 0) {
            for (byte i = 0; i < this.bullMNG.buls.size(); i++) {
                Bullets bul = this.bullMNG.buls.get(i);
                this.bullMNG.addBom((ItemBomHenGio) bul.bull);
            }
            this.bullMNG.buls.clear();
        }
        if (!checkWin() && players[this.getIDTurn()].isDie) {
            nextTurn();
            return;
        }
        Message ms = new Message(24);
        DataOutputStream ds = ms.writer();
        ds.writeByte(this.isBossTurn ? this.bossTurn : this.playerTurn);
        ds.flush();
        this.sendToTeam(ms);
        this.nextWind();
        if (this.ltap) {
            return;
        }
        this.countDownMNG.resetCount();
        if (this.isBossTurn) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((Boss) players[bossTurn]).turnAction();
                }
            }).start();
        }
    }

    public boolean checkWin() throws IOException {
        if (this.ltap) {
            return true;
        }
        if (!isFight) {
            return true;
        }
        // Next HP
        nextHP();
        nextCantSee();
        nextCantMove();
        nextBiDoc();
        if (FightManager.this.type == 5) {
            byte nPlayerAlive = 0, nBossAlive = 0, i = 0;
            while (i < ServerManager.maxPlayers) {
                Player pl2 = players[i];
                if (pl2 != null && !pl2.isDie) {
                    nPlayerAlive++;
                }
                i++;
            }
            while (i < allCount) {
                Player pl2 = players[i];
                if (pl2 != null && !pl2.isDie) {
                    nBossAlive++;
                }
                i++;
            }
            if (nPlayerAlive == 0 || nBossAlive == 0) {
                if (nPlayerAlive == nBossAlive && nPlayerAlive == 0) {
                    if (isBossTurn) {
                        fightComplete((byte) -1);
                    } else {
                        fightComplete((byte) 1);
                    }
                } else if (nPlayerAlive == 0) {
                    fightComplete((byte) -1);
                } else {
                    fightComplete((byte) 1);
                }
            } else {
                return false;
            }
        } else {
            int nRedAlive = 0, nBlueAlive = 0;
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl2 = players[i];
                if (pl2 == null) {
                    continue;
                }
                if (!pl2.isDie) {
                    if (pl2.team) {
                        nBlueAlive++;
                    } else {
                        nRedAlive++;
                    }
                }
            }
            if (nRedAlive == 0 || nBlueAlive == 0) {
                if ((nRedAlive == nBlueAlive) && (nRedAlive == 0)) {
                    fightComplete((byte) 0);
                } else if (nRedAlive == 0) {
                    fightComplete((byte) 1);
                } else {
                    fightComplete((byte) -1);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private void fightComplete(byte checkWin) throws IOException {
        this.isFight = false;
        this.WindX = 0;
        this.WindY = 0;
        this.nHopQua = 0;
        this.bullMNG.hasVoiRong = false;
        this.bullMNG.voiRongs.clear();
        this.bullMNG.boms.clear();
        this.bullMNG.addboss.clear();
        boolean LHfinish = false;
        boolean LHSuccess = false;
        if (wait.isLH && wait.numPlayer > 0) {
            if (checkWin == 1) {
                wait.ntLH++;
                if (wait.ntLH == wait.LHMap.length) {
                    wait.ntLH = 0;
                    LHfinish = true;
                } else {
                    LHSuccess = true;
                }
            } else {
                wait.ntLH = 0;
            }
            wait.map = wait.LHMap[wait.ntLH];
        }
        if (this.type == 5 && checkWin == 1) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl != null && pl.us != null) {
                    pl.us.updateXP(10, true);
                }
            }
        }
        Message ms;
        DataOutputStream ds;
        // Update Win
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            byte win = (byte) (pl.team ? checkWin : -checkWin);
            if (win == 1 && nTurn > 2) {
                if (this.playerCount == 2) {
                    pl.us.updateMission(0, 1);
                } else if (this.playerCount >= 5) {
                    pl.us.updateMission(17, 1);
                }
                switch (pl.idNV) {
                    case 0:
                        pl.us.updateMission(13, 1);
                        break;
                    case 1:
                        pl.us.updateMission(14, 1);
                        break;
                    case 2:
                        pl.us.updateMission(15, 1);
                        break;
                    default:
                        break;
                }
                // UFO
                switch (this.mapMNG.Id) {
                    case 35:
                        pl.us.updateMission(2, 1);
                        break;
                    case 36:
                        pl.us.updateMission(3, 1);
                        break;
                    case 38:
                    case 39:
                        pl.us.updateMission(4, 1);
                        break;
                    default:
                        break;
                }
            }
            ms = new Message(50);
            ds = ms.writer();
            // Team win->0: hoa 1: win -1: thua
            ds.writeByte(win);
            // Null byte
            ds.writeByte(0);
            // money Bonus
            ds.writeInt(this.wait.money);
            ds.flush();
            pl.us.sendMessage(ms);
        }
        long milisecond = (new Date()).getTime() - this.matchTime.getTime();
        int m = (int) (milisecond / 1000 / 60);
        if (!this.wait.isLH && this.type == 5 && checkWin == 1) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl == null || pl.us == null) {
                    continue;
                }
                byte idSpItem = new byte[]{62, 62, 63, 64, 63, 68, 66, 67, 65, 65}[this.mapMNG.Id - 30];
                int numSpItem = Until.nextInt(1, 20);
                byte idItem = (byte) Until.nextInt(2, ItemData.entrys.size() - 1);
                byte numItem = (byte) Until.nextInt(1, 5);
                pl.us.updateSpecialItem(idSpItem, numSpItem);
                pl.us.updateItem(idItem, numItem);

                byte numSpItemsk = (byte) Until.nextInt(5, 20);
                pl.us.updateSpecialItem((byte) 51, numSpItemsk);

                String strItem = numItem + "x " + ItemData.entrys.get(idItem).name + " " + numSpItem + "x " + SpecialItemData.getSpecialItemById(idSpItem).name + " " + numSpItemsk + "x Bông tuyết";

                ms = new Message(45);
                ds = ms.writer();
                ds.writeUTF(String.format(GameString.giftFightWin(), strItem));
                ds.flush();
                this.sendToTeam(ms);
            }
        }
        // Update All XP and CUP
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            ms = new Message(97);
            ds = ms.writer();
            ds.writeInt(pl.AllXPUp);
            ds.writeInt(pl.us.getXP());
            ds.writeInt(pl.us.getLevel() * (pl.us.getLevel() + 1) * 1000);
            ds.writeByte(0);
            ds.writeByte(pl.us.getLevelPercen());
            ds.flush();
            pl.us.sendMessage(ms);
        }
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            ms = new Message(-24);
            ds = ms.writer();
            ds.writeByte(pl.AllCupUp);
            ds.writeInt(pl.us.getDvong());
            ds.flush();
            pl.us.sendMessage(ms);
        }
        // Update Xu
        if (this.wait.money > 0) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl == null || pl.us == null) {
                    continue;
                }
                byte win = (byte) (pl.team ? checkWin : -checkWin);
                if (win >= 0) {
                    pl.us.updateXu(this.wait.money * (win == 1 ? 2 : 1));
                    ms = new Message(52);
                    ds = ms.writer();
                    ds.writeInt(pl.us.getIDDB());
                    ds.writeInt(this.wait.money * (win == 1 ? 2 : 1));
                    ds.writeInt(pl.us.getXu());
                    ds.flush();
                    sendToTeam(ms);
                }
            }
        }
        this.wait.started = false;
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
        }
        this.wait.fightComplete();
        if (wait.isLH) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl == null || pl.us == null) {
                    continue;
                }
                if (pl.isDie) {
                    wait.kick(i);
                    continue;
                }
                String strItem = "";
                int[] arXP = new int[]{0, 1000, 5000, 10000, 20000, 35000, 45000, 65000, 80000, 100000, 1000000};
                int[] arXu = new int[]{0, 1000, 5000, 10000, 15000, 20000, 25000, 30000, 35000, 40000, 100000};
                if (LHfinish) {
                    byte xXu = (byte) Until.nextInt(1, 10);
                    pl.us.sendMSSToUser(null, String.format(GameString.LHFinish(), Until.getStringNumber(arXP[arXP.length - 1] * xXu) + " XP +" + Until.getStringNumber(arXu[arXu.length - 1]) + " Xu"));
                    pl.us.updateXP(arXP[arXP.length - 1], false);
                    pl.us.updateXu(arXu[arXu.length - 1] * xXu);
                } else if (LHSuccess) {
                    strItem = Until.getStringNumber(arXP[wait.ntLH]) + " XP +" + Until.getStringNumber(arXu[wait.ntLH]) + " Xu";
                    pl.us.updateXP(arXP[wait.ntLH], false);
                    pl.us.updateXu(arXu[wait.ntLH]);
                    //vòng
                    switch (wait.ntLH) {
                        //win vòng 1
                        case 1:
                            break;
                        //win vòng 2
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
                    pl.us.sendMSSToUser(null, String.format(GameString.LHSuccess(), wait.ntLH, strItem));
                } else {
                    pl.us.sendMSSToUser(null, GameString.LHfailde());
                }
            }
        }
        if (nTurn > 2 && wait.type < 5) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
            }
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl == null || pl.us == null) {
                    continue;
                }
                byte win = (byte) (pl.team ? checkWin : -checkWin);
                if (win == 1) {
                    pl.us.updateQua_Start(2, 30, true);
                }
            }
        }
        this.nTurn = 0;
    }

    protected void startGame(int nTeamPointBlue, int nTeamPointRed) throws IOException {
        if (this.isFight) {
            return;
        }
        if (!this.ltap) {
            this.mapMNG.entrys.clear();
            this.setMap(this.wait.map);
        } else {
            this.mapMNG.setMapId(this.mapMNG.Id);
        }
        this.playerTurn = -1;
        this.nTurn = 0;
        this.WindX = 0;
        this.WindY = 0;
        this.isFight = true;
        if (this.ltap) {
            this.playerCount = 1;
        } else {
            this.playerCount = this.wait.numPlayer;
        }
        this.allCount = ServerManager.maxPlayers;
        if (this.ltap) {
            this.userLt.setFightManager(this);
            this.players = new Player[ServerManager.maxPlayers];
            this.players[0] = new Player(this, (byte) 0, ServerManager.Xltap[0], ServerManager.Yltap[0], Player.getLuyenTapItem(), (byte) 0, this.userLt);
            this.players[1] = new Player(this, (byte) 1, ServerManager.Xltap[1], ServerManager.Yltap[1], Player.getLuyenTapItem(), (byte) 0, this.userLt);
            for (int i = 2; i < 8; i++) {
                this.players[i] = null;
            }
        } else {
            if (this.type == 5) {
                this.nHopQua = (byte) (this.playerCount / 2);
            }
            int[] location = new int[8];
            int count = 0;
            this.teamlevel = 0;
            for (byte i = 0; i < this.wait.maxPlayer; i++) {
                User us = this.wait.players[i];
                if (us == null) {
                    this.players[i] = null;
                    continue;
                }
                this.teamlevel += us.getLevel();
                us.updateXu(-this.wait.money);
                us.setFightManager(this);
                short X, Y;
                int item[];
                int teamPoint;
                boolean exists;
                int locaCount = -1;
                do {
                    locaCount = Until.nextInt(this.mapMNG.XPlayerInit.length);
                    exists = false;
                    for (int j = 0; j < count; j++) {
                        if (location[j] == locaCount) {
                            exists = true;
                            break;
                        }
                    }
                } while (exists);
                location[count++] = locaCount;
                X = this.mapMNG.XPlayerInit[locaCount];
                Y = this.mapMNG.YPlayerInit[locaCount];
                item = this.wait.item[i];
                for (byte j = 0; j < 4; j++) {
                    if (item[4 + j] > 0) {
                        if (12 + j > 1) {
                            us.updateItem((byte) (12 + j), -1);
                        }
                    }
                }
                if (this.type == 5 || i % 2 == 0) {
                    teamPoint = nTeamPointBlue;
                } else {
                    teamPoint = nTeamPointRed;
                }
                this.players[i] = new Player(this, (byte) i, X, Y, item, teamPoint, us);
            }
        }
        this.bullMNG.mangNhenId = 200;
        this.sendFightInfoMessage();
        if (this.type == 5) {
            nextBoss();
        }
        this.matchTime = new Date();
        this.nextTurn();
    }

    public void leave(User us) throws IOException {
        if (!this.isFight) {
            return;
        }
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1) {
            return;
        }
        Player pl = this.players[index];
        int upXP = us.getXP();
        us.updateXP(-5, false);
        upXP -= us.getXP();
        Message ms = new Message(9);
        DataOutputStream ds = ms.writer();
        ds.writeInt(us.getIDDB());
        ds.writeUTF(upXP > 0 ? String.format(GameString.leave1(), upXP) : GameString.leave2());
        ds.flush();
        sendToTeam(ms);
        if (!pl.isDie) {
            pl.HP = 0;
            pl.isUpdateHP = true;
            pl.isDie = true;
        }
        pl.us = null;
        if (!this.ltap) {
            this.wait.leave(us);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!checkWin()) {
                            if (index == getIDTurn()) {
                                nextTurn();
                            } else {
                                Message ms = new Message(24);
                                DataOutputStream ds = ms.writer();
                                ds.writeByte(isBossTurn ? bossTurn : playerTurn);
                                ds.flush();
                                sendToTeam(ms);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }

    protected void sendFightInfoMessage() throws IOException {
        if (!this.isFight) {
            return;
        }
        // Update Xu
        if (!this.ltap && this.wait.money > 0) {
            for (byte i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = this.players[i];
                if (pl == null || pl.us == null) {
                    continue;
                }
                Message ms = new Message(52);
                DataOutputStream ds = ms.writer();
                ds.writeInt(pl.us.getIDDB());
                ds.writeInt(-this.wait.money);
                ds.writeInt(pl.us.getXu());
                ds.flush();
                this.sendToTeam(ms);
            }
        }
        for (byte i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = this.players[i];
            if (pl == null || pl.us == null) {
                continue;
            }
            Message ms = new Message(20);
            DataOutputStream ds = ms.writer();
            if (ltap) {
                short[] aw = this.userLt.getEquip();
                for (byte j = 0; j < 5; j++) {
                    ds.writeShort(aw[j]);
                }
            }
            // Null byte
            ds.writeByte(0);
            // Time Count
            if (ltap) {
                ds.writeByte(0);
            } else {
                ds.writeByte(this.timeCountMax);
            }
            // Team point
            ds.writeShort(pl.dongDoi);
            if (!this.ltap && this.wait.type == 7) {
                ds.writeByte(8);
            }
            // X, Y, HP
            for (byte j = 0; j < this.players.length; j++) {
                Player pl2 = this.players[j];
                if (pl2 == null) {
                    ds.writeShort(-1);
                    continue;
                }
                ds.writeShort(pl2.X);
                ds.writeShort(pl2.Y);
                ds.writeShort(pl2.HPMax);
            }
            ds.flush();
            pl.us.sendMessage(ms);
        }
    }

    protected void countOut() throws IOException {
        if (this.isFight && !this.checkWin()) {
            nextTurn();
        }
    }

    public void changeLocation(int index) throws IOException {
        Player pl = this.players[index];
        ServerManager.log("Player " + index + " change location X=" + pl.X + " Y=" + pl.Y);
        Message ms = new Message(21);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeShort(pl.X);
        ds.writeShort(pl.Y);
        ds.flush();
        sendToTeam(ms);
        if (pl.Y > this.mapMNG.Height) {
            pl.isDie = true;
            pl.HP = 0;
            pl.isUpdateHP = true;
            if (!checkWin() && index == getIDTurn()) {
                nextTurn();
            }
        }
    }

    public void flyChangeLocation(int index) throws IOException {
        Player pl = this.players[index];
        Message ms = new Message(93);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeShort(pl.X);
        ds.writeShort(pl.Y);
        ds.flush();
        sendToTeam(ms);
    }

    public void addBoss(Player pl) throws IOException {
        if (allCount >= ServerManager.maxElementFight) {
            return;
        }
        players[allCount] = pl;
        Boss boss = (Boss) this.players[allCount];
        Message ms = new Message(89);
        DataOutputStream ds = ms.writer();
        ds.writeByte(1);
        ds.writeInt(-1);
        ds.writeUTF(boss.name);
        ds.writeInt(boss.HPMax);
        ds.writeByte(boss.idNV);
        ds.writeShort(boss.X);
        ds.writeShort(boss.Y);
        ds.flush();
        this.sendToTeam(ms);
        allCount++;
    }

    public void newShoot(int index, byte bullId, short arg, byte force, byte force2, byte nshoot, boolean ltap) throws IOException {
        ServerManager.log("New shoot index=" + index + " bullId: " + bullId + " arg: " + arg + " force: " + force + " force2: " + force2 + " nshoot: " + nshoot);
        final Player pl = this.players[index];
        short x = pl.X, y = pl.Y;
        if (!ltap) {
            this.calcMM();
        }
        bullMNG.addShoot(pl, bullId, arg, force, force2, nshoot);
        bullMNG.fillXY();
        if (!this.ltap) {
            this.nextMM();
        }
        ArrayList<Bullet> bullets = bullMNG.entrys;
        if (bullets.isEmpty()) {
            return;
        }
        bullId = bullMNG.entrys.get(0).bullId;
        Message ms = new Message(ltap ? 84 : 22);
        DataOutputStream ds = ms.writer();
        // typeshoot
        byte typeshoot = 0;
        // Type shoot 0: pem buoc nhay 1: pem tang dan
        ds.writeByte(typeshoot);
        // Ban pow
        ds.writeByte(pl.isUsePow ? 1 : 0);
        // id trong phong
        ds.writeByte(index);
        // id dan
        ds.writeByte(bullId);
        // x, y, goc
        ds.writeShort(x);
        ds.writeShort(y);
        ds.writeShort(arg);
        // Apa or chicky: send force 2
        if (bullId == 17 || bullId == 19) {
            ds.writeByte(bullMNG.force2);
        }
        // dan laser
        if (bullId == 14 || bullId == 40) {
            // Goc
            ds.writeByte(0);
            // Null byte
            ds.writeByte(0);
        }
        // Send goc
        if (bullId == 44 || bullId == 45 || bullId == 47) {
            ds.writeByte(0);
        }
        // So lan ban
        ds.writeByte(nshoot);
        // So dan
        ds.writeByte(bullets.size());

        for (Bullet bull : bullets) {
            if (bullMNG.typeSC > 0 && pl.us != null) {
                pl.us.updateMission(12, 1);
            }
            ArrayList<Short> X = bull.XArray;
            ArrayList<Short> Y = bull.YArray;

            // Length
            ds.writeShort(X.size());

            if (typeshoot == 0) {
                for (int j = 0; j < X.size(); j++) {
                    if (j == 0) {
                        // Toa do x, y dau
                        ds.writeShort(X.get(0));
                        ds.writeShort(Y.get(0));
                    } else {
                        if ((j == X.size() - 1) && bullId == 49) {
                            ds.writeShort(X.get(j));
                            ds.writeShort(Y.get(j));
                            ds.writeByte(bullMNG.mgtAddX);
                            ds.writeByte(bullMNG.mgtAddY);
                            break;
                        }
                        // Buoc nhay
                        ds.writeByte((byte) (X.get(j) - X.get(j - 1)));
                        ds.writeByte((byte) (Y.get(j) - Y.get(j - 1)));
                    }
                }
            } else if (typeshoot == 1) {
                for (int j = 0; j < X.size(); j++) {
                    // Toa do x, y thu j
                    ds.writeShort(X.get(j));
                    ds.writeShort(Y.get(j));
                }
            }
            if (bullId == 48) {
                // Lent
                ds.writeByte(1);
                for (int j = 0; j < 1; j++) {
                    // xHit, yHit
                    ds.writeShort(0);
                    ds.writeShort(0);
                }
            }
        }

        // Type Sieu cao
        if (bullId == 42) {
            bullMNG.typeSC = 0;
        }
        ds.writeByte(bullMNG.typeSC);
        if (bullMNG.typeSC == 1 || bullMNG.typeSC == 2) {
            // X, Y super
            ds.writeShort(bullMNG.XSC);
            ds.writeShort(bullMNG.YSC);
        }
        ds.flush();
        bullMNG.reset();
        this.sendToTeam(ms);
        pl.isUseItem = false;
        pl.itemUsed = -1;
        if (this.ltap) {
            nextTurn();
        } else if (this.isNextTurn) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (pl != null && !(pl instanceof Boss)) {
                            pl.netWait();
                        }
                        if (!checkWin()) {
                            nextTurn();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void chatMessage(User us, Message ms) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1) {
            return;
        }
        String s = ms.reader().readUTF();
        ms = new Message(9);
        DataOutputStream ds = ms.writer();
        ds.writeInt(us.getIDDB());
        ds.writeUTF(s);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void luyenTapMessage(User us) throws IOException {
        Message ms = new Message(-6);
        DataOutputStream ds = ms.writer();
        ds.writeByte(this.mapMNG.Id);
        ds.flush();
        us.sendMessage(ms);
    }

    public void startLuyenTapMessage(User us, Message ms) throws IOException {
        byte typeS = ms.reader().readByte();
        // 0: start game, 1: roi ltap
        if (typeS == 0) {
            if (us.getState() == User.State.Waiting) {
                this.startGame((byte) 0, (byte) 0);
            }
        } else if (typeS == 1) {
            if (us.getState() == User.State.Fighting) {
                this.isFight = false;
                ServerManager.enterWait(userLt);
                ms = new Message(83);
                userLt.sendMessage(ms);
            }
        }
    }

    public void changeLocationMessage(User us, Message ms) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1) {
            return;
        }
        Player pl = this.players[index];
        short x = ms.reader().readShort();
        short y = ms.reader().readShort();
        pl.updateXY(x, y);
        changeLocation(index);
    }

    public void shootMessage(User us, Message ms) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1 || index != this.playerTurn || this.isShoot || !wait.started) {
            return;
        }
        this.isShoot = true;
        Player pl = this.players[index];
        DataInputStream dis = ms.reader();
        // id dan
        byte bullId = dis.readByte();
        short x = dis.readShort();
        short y = dis.readShort();
        short arg = dis.readShort();
        // 2 luc
        byte force = dis.readByte();
        byte force2 = 0;
        // Neu la apa or chicky -> 2 luc
        if (bullId == 17 || bullId == 19) {
            force2 = dis.readByte();
        }
        // so lan ban
        byte nshoot = dis.readByte();
        if (pl.banX2) {
            nshoot = 2;
            pl.banX2 = false;
        } else {
            nshoot = 1;
        }
        if (this.ltap) {
            pl.setXY(x, y);
        } else if (x != pl.X && y != pl.Y) {
            pl.updateXY(x, y);
        }
        newShoot(index, bullId, (arg > 360 ? 360 : (arg < -360 ? -360 : arg)), (force > 30 ? 30 : (force < 0 ? 0 : force)), (force2 > 30 ? 30 : (force2 < 0 ? 0 : force2)), nshoot, ltap);
    }

    public void boLuotMessage(User us) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1 || index != this.playerTurn || checkWin()) {
            return;
        }
        // Set next turn
        nextTurn();
    }

    public void useItemMessage(User us, Message ms) throws IOException {
        int index = this.getIndexByIDDB(us.getIDDB());
        if (index == -1 || index != this.playerTurn) {
            return;
        }
        byte idItem = ms.reader().readByte();
        if (idItem < 0 || idItem > ItemData.nItemDcMang.length - 1 && idItem != 100) {
            return;
        }
        Player pl = this.players[index];
        if (pl == null || pl.isUseItem) {
            return;
        }
        if (this.type == 5 && (idItem == 9 || idItem == 26 || idItem == 23 || idItem == 27 || idItem == 28 || idItem == 30 || idItem == 31)) {
            ms = new Message(45);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.unauthorized_Item());
            ds.flush();
            us.sendMessage(ms);
            return;
        }
        int indexItem = -1;
        if (idItem != 100) {
            for (byte i = 0; i < pl.item.length; i++) {
                if (pl.item[i] == idItem) {
                    indexItem = i;
                }
            }
            if (indexItem == -1) {
                return;
            }
        }
        ms = new Message(26);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeByte(idItem);
        ds.flush();
        this.sendToTeam(ms);
        pl.isUseItem = true;
        pl.itemUsed = idItem;
        if (indexItem >= 0) {
            if (idItem > 1) {
                pl.us.updateItem(idItem, -1);
            }
            pl.item[indexItem] = -1;
        }
        // HP
        if (idItem == 0) {
            pl.updateHP(350);
            this.nextHP();
        }
        //ban X2
        if (idItem == 2) {
            pl.banX2 = true;
        }
        // Di X2
        if (idItem == 3) {
            pl.diX2 = true;
        }
        // Tang hinh
        if (idItem == 4) {
            pl.tangHinhCount = 5;
        }
        // Ngung gio
        if (idItem == 5) {
            pl.ngungGioCount = 5;
            this.nextWind();
        }
        // HP dong doi
        if (idItem == 10) {
            byte lent = (byte) (type == 5 ? 1 : 2);
            byte i = (byte) (pl.team ? 0 : 1);
            for (; i < ServerManager.maxPlayers; i += lent) {
                Player pl2 = this.players[i];
                if (pl2 == null || pl2.isDie) {
                    continue;
                }
                pl2.updateHP(300);
            }
            this.nextHP();
        }
        // Tu sat
        if (idItem == 24) {
            newShoot(index, (byte) 50, (short) 0, (byte) 0, (byte) 0, (byte) 1, this.ltap);
        }
        //item UFO
        if (idItem == 27) {
            this.addBoss(new UFOFire(this, (byte) 16, "UFO", (byte) allCount, 980 + (pl.us.getLevel() * 20), (short) 100, (short) 0, pl, (byte) 3));
            if (!checkWin()) {
                nextTurn();
            }
        }
        // HP 50%
        if (idItem == 32) {
            pl.updateHP(pl.HPMax / 2);
            this.nextHP();
        }
        // HP 100%
        if (idItem == 33) {
            pl.updateHP(pl.HPMax);
            this.nextHP();
        }
        // Vo hinh
        if (idItem == 34) {
            pl.voHinhCount = 3;
        }
        // Ma ca rong
        if (idItem == 35) {
            pl.hutMauCount = 3;
        }
        // Pow
        if (idItem == 100) {
            if (pl.angry == 100) {
                pl.updateAngry(-100);
                pl.isUsePow = true;
            }
        }
        if (idItem == 0 || idItem == 2 || idItem == 3 || idItem == 4 || idItem == 5 || idItem == 10 || idItem == 32 || idItem == 33 || idItem == 34 || idItem == 35 || idItem == 100) {
            pl.itemUsed = -1;
        }
    }

    public void removeBullMessage(User us, Message ms) throws IOException {
        int[] X, Y;
        int lent = ms.reader().readByte();
        X = new int[lent];
        Y = new int[lent];
        for (byte i = 0; i < lent; i++) {
            X[i] = ms.reader().readInt();
            Y[i] = ms.reader().readInt();
        }
    }

    public void addBom(int id, int X, int Y) throws IOException {
        Message ms = new Message(109);
        DataOutputStream ds = ms.writer();
        ds.writeByte(0);
        ds.writeByte(id);
        ds.writeInt(X);
        ds.writeInt(Y);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void exploreBom(int id, int X, int Y, Bullet bull) throws IOException {
        this.mapMNG.collision((short) X, (short) Y, bull);
        Message ms = new Message(109);
        DataOutputStream ds = ms.writer();
        ds.writeByte(1);
        ds.writeByte(id);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void updateCantSee(Player pl) throws IOException {
        pl.cantSeeCount = 5;
    }

    public void updateCantMove(Player pl) throws IOException {
        pl.cantMoveCount = 5;
    }

    public void updateBiDoc(Player pl) throws IOException {
        pl.isBiDoc = true;
    }

    public short[] getForceArgXY(int idGun, BulletManager bull, boolean isXuyenMap, short X, short Y, short toX, short toY, short Mx, short My, int arg, int force, int msg, int g100) {
        byte i = (byte) (Until.nextInt(2) == 0 ? -1 : 1);
        short argS = (short) (i == 1 ? arg : 180 - arg);
        byte forceS = (byte) force;
        do {
            short x, y, vx, vy;
            x = (short) (X + (20 * Until.cos(argS) >> 10));
            y = (short) (Y - 12 - (20 * Until.sin(argS) >> 10));
            vx = (short) (forceS * Until.cos(argS) >> 10);
            vy = (short) -(forceS * Until.sin(argS) >> 10);
            short ax100 = (short) (bull.fm.WindX * msg / 100);
            short ay100 = (short) (bull.fm.WindY * msg / 100);
            short vxTemp = 0, vyTemp = 0, vyTemp2 = 0;

            if (idGun == 13) {
                y -= 25;
            }
            while (true) {
                if ((x < -200) || (x > bull.fm.mapMNG.Width + 200) || (y > bull.fm.mapMNG.Height + 200)) {
                    break;
                }
                short preX = x, preY = y;
                x += vx;
                y += vy;
                byte collision = getCollisionPoint(bull, preX, preY, x, y, toX, toY, Mx, My, isXuyenMap);
                if (collision == 1) {
                    return new short[]{argS, forceS};
                } else if (collision == 2) {
                    break;
                }
                vxTemp += Math.abs(ax100);
                vyTemp += Math.abs(ay100);
                vyTemp2 += g100;
                if (Math.abs(vxTemp) >= 100) {
                    if (ax100 > 0) {
                        vx += vxTemp / 100;
                    } else {
                        vx -= vxTemp / 100;
                    }
                    vxTemp %= 100;
                }
                if (Math.abs(vyTemp) >= 100) {
                    if (ay100 > 0) {
                        vy += vyTemp / 100;
                    } else {
                        vy -= vyTemp / 100;
                    }
                    vyTemp %= 100;
                }
                if (Math.abs(vyTemp2) >= 100) {
                    vy += vyTemp2 / 100;
                    vyTemp2 %= 100;
                }
            }
            forceS++;
            if (forceS > 30) {
                argS += i;
                forceS = (byte) force;
                argS = (short) Until.toArg0_360(argS);
                if (argS == arg) {
                    break;
                }
            }
        } while (true);

        return null;
    }

    private byte getCollisionPoint(BulletManager bull, short X1, short Y1, short X2, short Y2, short toX, short toY, short Mx, short My, boolean isXuyenMap) {
        int Dx = X2 - X1;
        int Dy = Y2 - Y1;
        byte x_unit = 0;
        byte y_unit = 0;
        byte x_unit2 = 0;
        byte y_unit2 = 0;
        if (Dx < 0) {
            x_unit = x_unit2 = -1;
        } else if (Dx > 0) {
            x_unit = x_unit2 = 1;
        }
        if (Dy < 0) {
            y_unit = y_unit2 = -1;
        } else if (Dy > 0) {
            y_unit = y_unit2 = 1;
        }
        int k1 = Math.abs(Dx);
        int k2 = Math.abs(Dy);
        if (k1 > k2) {
            y_unit2 = 0;
        } else {
            k1 = Math.abs(Dy);
            k2 = Math.abs(Dx);
            x_unit2 = 0;
        }
        int k = k1 >> 1;
        short X = X1, Y = Y1;
        for (int i = 0; i <= k1; i++) {
            if (Math.abs(X - toX) <= Mx && Math.abs(Y - toY) <= My) {
                return 1;
            }
            if (!isXuyenMap) {
                if (bull.fm.mapMNG.isCollision(X, Y)) {
                    return 2;
                }
            }
            k += k2;
            if (k >= k1) {
                k -= k1;
                X += x_unit;
                Y += y_unit;
            } else {
                X += x_unit2;
                Y += y_unit2;
            }
        }
        return 0;
    }

    public void GhostBullet(int index, int toIndex) throws IOException {
        Message ms = new Message(124);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeByte(toIndex);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void capture(byte index, byte toindex) throws IOException {
        Message ms = new Message(95);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeByte(toindex);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void thadocBullet(byte index, byte toindex) throws IOException {
        Message ms = new Message(96);
        DataOutputStream ds = ms.writer();
        ds.writeByte(index);
        ds.writeByte(toindex);
        ds.flush();
        this.sendToTeam(ms);
    }

    public void cLocation(byte toIndex) throws IOException {
        Player pl = players[toIndex];
        Message ms = new Message(21);
        DataOutputStream ds = ms.writer();
        ds.writeByte(toIndex);
        ds.writeShort(pl.X);
        ds.writeShort(pl.Y);
        ds.flush();
        pl.us.sendMessage(ms);
    }
}
