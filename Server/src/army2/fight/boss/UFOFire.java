package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.FightManager;
import army2.server.ServerManager;
import army2.server.Until;
import java.io.IOException;

public class UFOFire extends Boss {

    private boolean acll;
    private Player pl;
    private byte turnDie;

    public UFOFire(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y, Player pl, byte tdie) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 51;
        super.height = 46;
        this.fly = true;
        this.pl = pl;
        this.turnDie = tdie;
        this.XPExist = 0;
        this.team = pl.index % 2 == 0;
    }

    @Override
    public void turnAction() {
        try {
            if (this.turnDie == 0) {
                this.HP = 0;
                this.isUpdateHP = true;
                this.isDie = true;
                if (fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                }
                return;
            }
            short ys = this.Y, xs = this.X;
            while (this.acll && ys < this.fightMNG.mapMNG.Height + 200 && !this.fightMNG.mapMNG.isCollision(xs, ys)) {
                if (ys > this.fightMNG.mapMNG.Height) {
                    this.acll = false;
                }
                ys++;
            }
            if (!this.acll) {
                this.acll = true;
                Player pl2 = null;
                int i = this.team ? 1 : 0;
                int lent = 2;
                for (; i < ServerManager.maxPlayers; i += lent) {
                    Player pl3 = this.fightMNG.players[i];
                    if (pl3 == null || pl3.isDie) {
                        continue;
                    }
                    pl2 = pl3;
                }
                if (pl2 != null) {
                    this.Y = (short) (pl2.Y - Until.nextInt(150, 500));
                    this.X = pl2.X;
                    this.fightMNG.flyChangeLocation(super.index);
                }
            } else {
                this.acll = false;
                this.turnDie--;
                this.fightMNG.newShoot(this.index, (byte) 42, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
                return;
            }
            if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
