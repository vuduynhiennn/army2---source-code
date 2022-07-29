package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.FightManager;
import army2.server.Until;
import java.io.IOException;

public class Ghost extends Boss {

    public Ghost(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 35;
        super.height = 31;
        this.fly = true;
        this.XPExist = 5;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = this.fightMNG.getPlayerClosest(this.X, this.Y);
            if (pl == null) {
                return;
            }
            //mò tới nguoi chơi
            if (this.X > pl.X) {
                this.X = (short) (pl.X + 30);
            } else {
                this.X = (short) (pl.X - 30);
            }
            this.Y = (short) (pl.Y - 15);
            this.fightMNG.flyChangeLocation(super.index);
            this.fightMNG.GhostBullet(this.index, pl.index);
            short wmap = this.fightMNG.mapMNG.Width;
            short hmap = this.fightMNG.mapMNG.Height;
            this.X = (short) Until.nextInt(100, wmap - 100);
            this.Y = (short) Until.nextInt(0, hmap - 200);
            this.fightMNG.flyChangeLocation(super.index);
            pl.updateHP(-Until.nextInt(300, 600));
            if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
