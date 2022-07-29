package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.FightManager;
import army2.server.Until;
import java.io.IOException;

public class UFO extends Boss {

    private boolean turnShoot;

    public UFO(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 51;
        super.height = 46;
        this.fly = true;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        try {
            short ys = this.Y, xs = this.X;
            while (this.turnShoot && ys < this.fightMNG.mapMNG.Height + 200 && !this.fightMNG.mapMNG.isCollision(xs, ys)) {
                if (ys > this.fightMNG.mapMNG.Height) {
                    this.turnShoot = false;
                }
                ys++;
            }
            if (!this.turnShoot) {
                this.turnShoot = true;
                Player pl = this.fightMNG.getPlayerClosest(this.X, this.Y);
                if (pl != null) {
                    this.Y = (short) (pl.Y - Until.nextInt(150, 500));
                    this.X = pl.X;
                    this.fightMNG.flyChangeLocation(super.index);
                    if (!fightMNG.checkWin()) {
                        fightMNG.nextTurn();
                    }
                } else if (!fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                }
            } else {
                this.turnShoot = false;
                this.fightMNG.newShoot(this.index, (byte) 42, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
