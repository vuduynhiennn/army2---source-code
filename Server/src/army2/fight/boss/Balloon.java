package army2.fight.boss;

import army2.fight.Boss;
import army2.fight.Bullet;
import army2.fight.FightManager;
import army2.fight.Player;
import army2.server.Until;
import java.io.IOException;

/**
 *
 * @author Văn Tú
 */
public class Balloon extends Boss {

    private byte[] part;
    private int turns;

    public Balloon(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 40;
        super.width = 138;
        super.height = 0;
        this.fly = true;
        this.part = new byte[]{super.index, (byte) (super.index + 1), (byte) (super.index + 2), (byte) (super.index + 3), -1};
        this.turns = -1;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = this.fightMNG.getPlayerClosest(super.X, super.Y);
            if (pl == null) {
                return;
            }
            boolean fd = true;
            //turn
            int turn = this.turns + 1;
            while (turn != this.turns) {
                if (turn == this.part.length) {
                    turn = 0;
                }
                Player boss = null;
                if (this.part[turn] != -1) {
                    boss = this.fightMNG.players[this.part[turn]];
                }
                if (boss != null && !boss.isDie && turn != 1 && turn != 3) {
                    if (turn != 2 || !fightMNG.players[part[3]].isDie) {
                        this.turns = turn;
                        break;
                    }
                }
                turn++;
            }
            for (int i = 0; i < 5; i++) {
                if (this.part[i] == -1) {
                    continue;
                }
                Player boss = this.fightMNG.players[this.part[i]];
                if (boss != null && !boss.isDie && (i == 1 || i == 2 || i == 4)) {
                    fd = false;
                }
            }
            if (((this.fightMNG.players[this.part[1]].isDie && this.fightMNG.players[this.part[2]].isDie) || this.fightMNG.players[this.part[3]].isDie) && this.part[4] == -1) {
                this.part[4] = (byte) this.fightMNG.allCount;
                this.fightMNG.addBoss(new Balloon_Eye(this.fightMNG, (byte) 21, "Balloon Eye", (byte) this.fightMNG.allCount, 1500 + (this.fightMNG.getLevelTeam() * 10), (short) (super.X + 55), (short) (super.Y - 27)));
            } else if (fd) {

                this.fightMNG.players[part[0]].isDie = true;
                this.fightMNG.players[part[0]].HP = 0;
                this.fightMNG.players[part[0]].isUpdateHP = true;

                this.fightMNG.players[part[3]].isDie = true;
                this.fightMNG.players[part[3]].HP = 0;
                this.fightMNG.players[part[3]].isUpdateHP = true;

                if (!this.fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                    return;
                }
            }
            if (this.turns == 0) {
                if (!fd) {
                    short toX = (short) Until.nextInt(100, this.fightMNG.mapMNG.Width - 100);
                    short toY = (short) Until.nextInt(-150, 50);
                    for (int i = 0; i < 5; i++) {
                        if (this.part[i] == -1) {
                            continue;
                        }
                        Player boss = (Boss) this.fightMNG.players[this.part[i]];
                        if (boss == null || boss.isDie) {
                            continue;
                        }
                        switch (i) {
                            case 0:
                                boss.X = toX;
                                boss.Y = toY;
                                break;
                            case 1:
                                boss.X = (short) (toX + 51);
                                boss.Y = (short) (toY + 19);
                                break;
                            case 2:
                                boss.X = (short) (toX - 5);
                                boss.Y = (short) (toY + 30);
                                break;
                            case 3:
                                boss.X = (short) (toX - 67);
                                boss.Y = (short) (toY - 6);
                                break;
                            case 4:
                                boss.X = (short) (toX + 57);
                                boss.Y = (short) (toY - 27);
                                break;
                        }
                    }
                    this.fightMNG.flyChangeLocation(super.index);
                }
                if (!this.fightMNG.players[this.part[1]].isDie) {
                    this.fightMNG.newShoot(this.index, (byte) 44, (short) Until.getArgXY(X, Y, pl.X, pl.Y), (byte) 10, (byte) 0, (byte) 1, false);
                    return;
                }
            } else if (this.turns == 2 && !this.fightMNG.players[this.part[2]].isDie) {
                this.fightMNG.newShoot(this.index, (byte) 43, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
                return;
            } else if (this.turns == 4 && !this.fightMNG.players[this.part[4]].isDie) {
                this.fightMNG.newShoot(this.index, (byte) 45, (short) Until.getArgXY(X, Y, pl.X, pl.Y), (byte) 20, (byte) 0, (byte) 1, false);
                return;
            }
            if (!fightMNG.checkWin()) {
                this.fightMNG.nextTurn();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
