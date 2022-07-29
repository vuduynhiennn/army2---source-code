package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.Bullet;
import army2.fight.FightManager;
import army2.server.Until;

/**
 *
 * @author Văn Tú
 */
public class BigBoom extends Boss {

    public BigBoom(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 100;
        super.width = 28;
        super.height = 28;
        this.XPExist = 4;
    }

    public void bomAction() {
        this.isDie = true;
        this.height = 0;
        this.width = 0;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = this.fightMNG.getPlayerClosest(this.X, this.Y);
            if (pl == null) {
                return;
            }
            //45% add bom
            if (Until.nextInt(100) <= 45) {
                short Vgoc = 0;
                short Vsuc;
                if (pl.X > X) {
                    Vgoc = (short) Until.nextInt(70, 75);
                }
                if (pl.X <= X) {
                    Vgoc = (short) Until.nextInt(110, 115);
                }
                Vsuc = (short) (Math.abs(X - pl.X) / 20);
                if (Vsuc < 8) {
                    Vsuc = 8;
                }
                if (Vsuc > 30) {
                    Vsuc = 30;
                }
                this.fightMNG.newShoot(this.index, (byte) 34, (short) Vgoc, (byte) Vsuc, (byte) 0, (byte) 1, false);
            } else {
                while (X != pl.X || Y != pl.Y) {
                    int preX = this.X;
                    int preY = this.Y;
                    if (pl.X < this.X) {
                        super.move(false);
                    } else if (pl.X > this.X) {
                        super.move(true);
                    } else if (!fightMNG.mapMNG.isCollision(this.X, this.Y)) {
                        this.Y++;
                    }
                    // if ko di chuyen dc
                    if (preX == this.X && preY == this.Y) {
                        break;
                    }
                }
                this.fightMNG.changeLocation(super.index);
                if (Math.abs(X - pl.X) <= 35 && Math.abs(Y - pl.Y) <= 35) {
                    this.fightMNG.newShoot(this.index, (byte) 31, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
                } else if (this.buocDi < this.theLuc) {
                    this.itemUsed = 7;
                    this.fightMNG.newShoot(this.index, (byte) 7, (short) Until.getArgXY(X, Y, pl.X, pl.Y), (byte) 30, (byte) 0, (byte) 1, false);
                } else if (!fightMNG.checkWin()) {
                    this.fightMNG.nextTurn();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
