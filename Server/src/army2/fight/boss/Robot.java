package army2.fight.boss;

import army2.fight.Boss;
import army2.fight.FightManager;
import army2.fight.Player;
import army2.server.ServerManager;
import army2.server.Until;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Văn Tú
 */
public class Robot extends Boss {

    public Robot(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 24;
        super.height = 25;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(X, Y);
            if (pl == null) {
                return;
            }
            if (Math.abs(X - pl.X) <= 40 && Math.abs(Y - pl.Y) <= 40) {
                fightMNG.isNextTurn = false;
                this.fightMNG.newShoot(this.index, (byte) 35, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
                fightMNG.isNextTurn = true;
            }
            if (Math.abs(X - pl.X) <= 40) {
                byte force = (byte) Until.nextInt(15, 30);
                short arg = (short) Until.nextInt(80, 100);
                this.fightMNG.newShoot(this.index, (byte) 36, (short) arg, (byte) force, (byte) 0, (byte) 1, false);
                return;
            }
            ArrayList<Player> ar = new ArrayList();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (this.fightMNG.players[i] != null && !this.fightMNG.players[i].isDie) {
                    ar.add(this.fightMNG.players[i]);
                }
            }
            if (ar.size() > 0) {
                pl = ar.get(Until.nextInt(ar.size()));
            }
            if (pl == null) {
                return;
            }
            short[] FA = null;
            switch (Until.nextInt(9)) {
                case 0:
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, (short) (pl.width / 2), (short) (pl.height / 2), 50, 5, 80, 100);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 0, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
                case 1:
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, (short) (pl.width / 2), (short) (pl.height / 2), 50, 5, 80, 60);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 2, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
                case 2:
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, (short) (pl.width / 2), (short) (pl.height / 2), 50, 5, 50, 80);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 10, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
                case 3:
                    this.itemUsed = 6;
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, (short) (pl.width / 2), (short) (pl.height / 2), 50, 5, 70, 90);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 6, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
                case 4:
                    this.itemUsed = 7;
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, (short) (pl.width / 2), (short) (pl.height / 2), 50, 5, 70, 80);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 7, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    FA = fightMNG.getForceArgXY(idNV, fightMNG.bullMNG, true, X, Y, pl.X, pl.Y, pl.width, pl.height, 50, 5, 0, 80);
                    if (FA == null) {
                        if (!fightMNG.checkWin()) {
                            fightMNG.nextTurn();
                        }
                        return;
                    }
                    this.fightMNG.newShoot(this.index, (byte) 36, (short) FA[0], (byte) FA[1], (byte) 0, (byte) 1, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
