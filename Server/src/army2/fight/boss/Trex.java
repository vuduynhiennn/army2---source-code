package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.FightManager;
import army2.server.Until;

/**
 *
 * @author văn tú
 */
public class Trex extends Boss {

    public Trex(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 45;
        super.height = 50;
        this.fly = false;
        this.XPExist = 4;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = this.fightMNG.getPlayerClosest(this.X, this.Y);
            if (pl == null) {
                return;
            }
            if (Math.abs(X - pl.X) <= 90 && Math.abs(Y - pl.Y) <= 250) {
                this.fightMNG.newShoot(this.index, (byte) 35, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
            } else {
                int randi = Until.nextInt(3);
                short[] FA = null;
                switch (randi) {
                    case 0:
                        this.fightMNG.newShoot(this.index, (byte) 37, (short) 110, (byte) 30, (byte) 0, (byte) 1, false);
                        break;
                    case 1:
                        FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, false, X, Y, pl.X, pl.Y, (short) 70, (short) (70), 110, 5, 10, 80);
                        if (FA == null) {
                            if (!fightMNG.checkWin()) {
                                fightMNG.nextTurn();
                            }
                            return;
                        }
                        this.fightMNG.newShoot(this.index, (byte) 40, FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                        break;
                    case 2:
                        FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, false, X, Y, pl.X, pl.Y, (short) (70), (short) (70), 110, 5, 10, 80);
                        if (FA == null) {
                            if (!fightMNG.checkWin()) {
                                fightMNG.nextTurn();
                            }
                            return;
                        }
                        this.fightMNG.newShoot(this.index, (byte) 41, FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
