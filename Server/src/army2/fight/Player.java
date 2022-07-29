package army2.fight;

import army2.fight.BulletManager.AddBoss;
import army2.fight.boss.Ghost2;
import army2.fight.boss.GiftBox;
import army2.server.ItemClanData;
import army2.server.ClanManager;
import army2.server.ServerManager;
import army2.server.SpecialItemData;
import army2.server.Until;
import army2.server.User;
import java.io.DataOutputStream;
import java.io.IOException;
import network.Message;

/**
 *
 * @author Văn Tú
 */
public class Player {

    protected FightManager fightMNG;
    protected User us;
    protected boolean team;
    protected short idBullet;
    public short X;
    public short Y;
    public short width;
    public short height;
    protected int itemInit[];
    protected int item[];
    protected byte itemUsed;
    protected boolean isUseItem;
    protected boolean isUsePow;
    public boolean isDie;
    public boolean isUpdateHP;
    protected boolean isUpdateAngry;
    protected boolean isUpdateXP;
    protected boolean isUpdateCup;
    protected int XPUp;
    protected int CupUp;
    protected int AllXPUp;
    protected int AllCupUp;

    protected byte idNV;
    public byte index;
    protected short gunId;
    protected int angry;
    protected byte pixel;
    public int HP;
    protected int HPMax;
    protected int satThuong;
    protected int phongThu;
    protected int mayMan;
    protected int dongDoi;
    protected byte ngungGioCount;
    protected byte hutMauCount;
    protected byte tangHinhCount;
    public byte voHinhCount;
    protected byte cantSeeCount;
    protected byte cantMoveCount;
    public boolean isBiDoc;
    protected boolean diX2;
    protected boolean banX2;
    protected byte buocDi;
    protected byte theLuc;
    protected boolean isMM;
    protected boolean fly;
    protected int XPExist;
    private boolean[] itemclan = new boolean[ItemClanData.entrys.size() + 1];

    public Player(FightManager fightMNG, byte location, short X, short Y, int item[], int teamPoint, User us) {
        this.fightMNG = fightMNG;
        this.index = location;
        this.team = fightMNG.type == 5 || location % 2 == 0;
        this.idBullet = -1;
        this.gunId = -1;
        this.X = X;
        this.Y = Y;
        this.theLuc = 60;
        this.width = 24;
        this.height = 24;
        this.itemInit = item;
        this.fly = false;
        this.XPExist = 0;
        if (item != null) {
            this.item = new int[item.length];
            System.arraycopy(item, 0, this.item, 0, item.length);
        }
        this.us = us;
        this.itemUsed = -1;
        this.isUseItem = false;
        this.isUsePow = false;
        this.isDie = false;
        this.angry = 0;
        this.pixel = 25;
        this.dongDoi = teamPoint;
        this.ngungGioCount = 0;
        this.hutMauCount = 0;
        this.voHinhCount = 0;
        this.cantSeeCount = 0;
        this.cantMoveCount = 0;
        this.isBiDoc = false;
        this.diX2 = false;
        this.banX2 = false;
        this.buocDi = 0;
        this.isMM = false;
        this.isUpdateAngry = false;
        this.isUpdateHP = false;
        this.isUpdateXP = false;
        this.XPUp = 0;
        this.CupUp = 0;
        this.idNV = 0;
        this.HPMax = 0;
        this.satThuong = 0;
        this.phongThu = 0;
        this.mayMan = 0;
        this.HP = 0;
        if (us == null) {
            return;
        }
        if (this.us.getClan() > 0) {
            for (byte i = 1; i <= ItemClanData.entrys.size(); i++) {
                this.itemclan[i] = ClanManager.getItemClan(this.us, i);
            }
        }
        this.idBullet = us.getIDBullet();
        this.gunId = us.getGunId();
        this.idNV = us.getNVUse();
        int[] ability = us.getAbility();
        this.HPMax = ability[0] + (teamPoint * 5);
        this.satThuong = ability[1] + (teamPoint / 1);
        this.phongThu = ability[2] + (teamPoint * 5);
        this.mayMan = ability[3] + (teamPoint * 5);
        //5% mm
        if (this.itemclan[2]) {
            this.mayMan = this.mayMan * 105 / 100;
        }
        //5% dong doi
        if (this.itemclan[3]) {
            this.dongDoi = this.dongDoi * 105 / 100;
        }
        //5% phong thu
        if (this.itemclan[4]) {
            this.phongThu = this.phongThu * 105 / 100;
        }
        //5% HP
        if (this.itemclan[6]) {
            this.HPMax = this.HPMax * 105 / 100;
        }
        //10% mm
        if (this.itemclan[9]) {
            this.mayMan = this.mayMan * 110 / 100;
        }
        //10% dong doi
        if (this.itemclan[10]) {
            this.dongDoi = this.dongDoi * 110 / 100;
        }
        this.HP = this.HPMax;
    }

    public static int[] getLuyenTapItem() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    }

    public final void setXY(short X, short Y) {
        if (X >= 0 && X < this.fightMNG.mapMNG.Width && Y < this.fightMNG.mapMNG.Height) {
            this.X = X;
            this.Y = Y;
        }
    }

    public final void updateXY(short X, short Y) {
        while (X != this.X || Y != this.Y) {
            int preX = this.X;
            int preY = this.Y;
            if (X < this.X) {
                move(false);
            } else if (X > this.X) {
                move(true);
            }
            // if ko di chuyen dc
            if (preX == this.X && preY <= this.Y) {
                return;
            }
        }
    }

    public void chuanHoaXY() {
        while (this.Y < this.fightMNG.mapMNG.Height + 200) {
            if (this.fightMNG.mapMNG.isCollision(X, Y) || this.fly) {
                return;
            }
            Y++;
        }
    }

    protected void move(boolean addX) {
        if (this.cantMoveCount > 0) {
            return;
        }
        byte step = 1;
        if (this.diX2) {
            step = 2;
        }
        if (buocDi > theLuc) {
            return;
        }
        buocDi++;
        if (addX) {
            X += step;
        } else {
            X -= step;
        }
        if (fightMNG.mapMNG.isCollision(X, (short) (Y - 5))) {
            buocDi--;
            if (addX) {
                X -= step;
            } else {
                X += step;
            }
            return;
        }
        for (int i = 4; i >= 0; i--) {
            if (this.fightMNG.mapMNG.isCollision(X, (short) (Y - i))) {
                Y -= i;
                return;
            }
        }
        this.chuanHoaXY();
    }

    public final void updateHP(int addHP) {
        this.isUpdateHP = true;
        this.HP += addHP;
        if (this.HP <= 0) {
            this.HP = 0;
        } else if (this.HP < 10) {
            this.HP = 10;
        } else if (this.HP > this.HPMax) {
            this.HP = this.HPMax;
        }
        int oldPixel = this.pixel;
        this.pixel = (byte) (this.HP * 25 / this.HPMax);
        if (addHP < 0) {
            this.updateAngry((oldPixel - pixel) * 4);
        }
        if (this.HP <= 0) {
            die();
        }
    }

    public final void updateAngry(int addAngry) {
        this.isUpdateAngry = true;
        this.angry += addAngry;
        if (this.angry < 0) {
            this.angry = 0;
        }
        if (this.angry > 100) {
            this.angry = 100;
        }
    }

    protected final void updateEXP(int addXP) throws IOException {
        if (us == null || addXP == 0) {
            return;
        }
        this.isUpdateXP = true;
        if (this.us.getClan() > 0) {
            ClanManager.updateXP(this.us, addXP / 100);
        }
        if (this.itemclan[1]) {
            addXP *= 2;
        }
        if (this.itemclan[8]) {
            addXP *= 3;
        }
        this.XPUp += addXP;
        addXP -= 2;
        if (addXP < 1) {
            return;
        }
        int i = this.team ? 0 : 1;
        int lent = this.fightMNG.type == 5 ? 1 : 2;
        for (; i < ServerManager.maxPlayers; i += lent) {
            Player pl = this.fightMNG.players[i];
            if (pl == null || pl == this) {
                continue;
            }
            pl.isUpdateXP = true;
            pl.XPUp += addXP;
            pl.AllXPUp += addXP;
        }
    }

    protected final void updateCUP(int addCup) throws IOException {
        if (us == null || addCup == 0) {
            return;
        }
        this.isUpdateCup = true;
        this.CupUp += addCup;
        this.AllCupUp += addCup;
        addCup -= 2;
        if (addCup < 1) {
            return;
        }
        int i = this.team ? 0 : 1;
        int lent = this.fightMNG.type == 5 ? 1 : 2;
        for (; i < ServerManager.maxPlayers; i += lent) {
            Player pl = this.fightMNG.players[i];
            if (pl == null || pl == this) {
                continue;
            }
            pl.isUpdateCup = true;
            pl.CupUp += addCup;
            pl.AllCupUp += addCup;
        }
    }

    private void die() {
        if (this.isMM && this.X > 0 && this.Y < this.fightMNG.mapMNG.Height && this.X < this.fightMNG.mapMNG.Width) {
            this.HP = 10;
        } else {
            this.isDie = true;
            if (us != null) {
                us.notifyNetWait();
            }
        }
    }

    public void netWait() {
        this.fightMNG.countDownMNG.second += 2;
        if (us != null) {
            us.netWait();
        }
    }

    public void notifyNetWait() {
        if (us != null) {
            us.notifyNetWait();
        }
    }

    public boolean isCollision(short X, short Y) {
        if (this.voHinhCount > 0 || this.isDie) {
            return false;
        }
        return Until.inRegion(X, Y, this.X - this.width / 2, this.Y - this.height, this.width, this.height);
    }

    public void collision(short bx, short by, Bullet bull) {
        if (this.fightMNG.ltap) {
            return;
        }

        int tamAH = Bullet.getTamAHByBullID(bull.bullId);
        if (bull.bullId == 35 && bull.pl.idNV == 15) {
            tamAH = 250;
        }
        // Neu la tz or apa or chicky or rocket dung pow-> no rong gap doi
        if (bull.pl.isUsePow && (bull.pl.idNV == 3 || bull.pl.idNV == 4 || bull.pl.idNV == 6 || bull.pl.idNV == 7 || bull.pl.idNV == 8)) {
            tamAH = tamAH * 2;
        }
        if (this.isDie || this.voHinhCount > 0 || !Until.intersecRegions(X, Y, width, height, bx, by, tamAH * 2, tamAH * 2)) {
            return;
        }
        if ((bull.bullId == 31 || bull.bullId == 32 || bull.bullId == 35) && this.index >= ServerManager.maxPlayers) {
            return;
        }
        int kcX = Math.abs(X - bx);
        int kcY = Math.abs(Y - this.height / 2 - by);
        int kc = (int) Math.sqrt(kcX * kcX + kcY * kcY);
        int dame = bull.satThuong;
        if (kc > this.width / 2) {
            dame = dame - ((dame * (kc - this.width / 2)) / tamAH);
        }
        this.chuanHoaXY();
        int PhongThu = this.phongThu;
        if (dame > 0) {
            if (bull.pl.isMM) {
                dame *= 2;
            }
            if (bull.pl.itemclan[7]) {
                dame = dame * 105 / 100;
            }
            if (bull.pl.isUsePow && bull.pl.itemclan[5]) {
                dame = dame * 105 / 100;
            }
            if (bull.typeSC > 0) {
                switch (bull.typeSC) {
                    case 1:
                        fightMNG.bullMNG.typeSC = 1;
                        dame = dame * 11 / 10; // x1.1
                        fightMNG.bullMNG.XSC = bull.XmaxY;
                        fightMNG.bullMNG.YSC = bull.maxY;
                        break;
                    case 2:
                        fightMNG.bullMNG.typeSC = 1;
                        dame = dame * 6 / 5; // x1.2
                        fightMNG.bullMNG.XSC = bull.XmaxY;
                        fightMNG.bullMNG.YSC = bull.maxY;
                        break;
                    case 4:
                        fightMNG.bullMNG.typeSC = 2;
                        dame = dame * 13 / 10; // x1.3
                        break;
                    default:
                        break;
                }
            }
        }
        PhongThu *= isMM ? 2 : 1;
        int maxPhongThu = 100000;
        PhongThu = PhongThu > 95000 ? 95000 : PhongThu;
        dame = Math.round(dame - (dame * (PhongThu * 100 / maxPhongThu) / 100));
        if (dame > 0) {
            int oldHP = this.HP;
            this.updateHP(-dame);
            if (bull.pl instanceof Boss) {
                return;
            }
            bull.pl.us.updateMission(1, oldHP - this.HP);
            if (bull.pl.hutMauCount > 0) {
                bull.pl.updateHP(dame / 2);
            }
            // Neu ban chet + xp va dvong
            if (this.isDie) {
                // Tarzan
                if (this.idNV == 7) {
                    bull.pl.us.updateMission(6, 1);
                }
                if (this.idNV == 6) {
                    bull.pl.us.updateMission(7, 1);
                }
                if (this.idNV == 9) {
                    bull.pl.us.updateMission(8, 1);
                }
                try {
                    if (this.idNV == 26) {
                        for (int i = 0; i < 2; i++) {
                            Player players = new Ghost2(fightMNG, (byte) 26, "Ghost II", (byte) (fightMNG.allCount + fightMNG.bullMNG.addboss.size()), 2000 + (fightMNG.getLevelTeam() * 20), (short) (Until.nextInt(100, fightMNG.mapMNG.Width - 100)), (short) Until.nextInt(150));
                            fightMNG.bullMNG.addboss.add(new AddBoss(players, fightMNG.getisLH() ? 50 : 6));
                        }
                    }
                    // Ban dong doi -5xp -5cup
                    if (this.fightMNG.type != 5 && this.team == bull.pl.team && this.idNV != 23 && this.idNV != 24 && this.index != bull.pl.index) {
                        bull.pl.updateCUP(-5);
                        bull.pl.updateEXP(-5);
                        return;
                    }
                    if (index == bull.pl.index || this.fightMNG.type == 5 && !(this instanceof Boss)) {
                        return;
                    }
                    if (this instanceof Boss) {

                        //kinh nghiệm 
                        bull.pl.updateEXP(this.XPExist * 100);

                    } else {
                        int cupCL = bull.pl.us.getDvong() - this.us.getDvong();
                        int cupAdd = ((3000 - cupCL) / 100);
                        int levelPL = this.us.getLevel();
                        if (levelPL > 255) {
                            levelPL = 255;
                        }
                        if (cupAdd > 60) {
                            cupAdd = 60;
                        }
                        if (cupAdd < 0) {
                            cupAdd = 0;
                        }
                        bull.pl.updateCUP(cupAdd);
                        bull.pl.updateEXP((levelPL / 2) + 2);
                        updateCUP(-cupAdd);
                        updateEXP(-((levelPL / 2) + 2));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void nextMM() {
        if (this.mayMan > 7500) {
            this.mayMan = 7500;
        }
        this.isMM = Until.nextInt(10000) <= this.mayMan;
    }

    public void flyNotice(String text) {
        try {
            Message ms = new Message(119);
            DataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeByte(0);
            ds.writeByte(4);
            ds.writeUTF(text);
            ds.flush();
            fightMNG.sendToTeam(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
