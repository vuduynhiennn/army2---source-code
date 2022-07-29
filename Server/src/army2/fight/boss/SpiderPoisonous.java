package army2.fight.boss;

import army2.fight.Player;
import army2.fight.Boss;
import army2.fight.FightManager;
import army2.server.ServerManager;
import army2.server.Until;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Văn Tú
 */
public class SpiderPoisonous extends Boss {

    protected byte nturn;
    public Player target;

    public SpiderPoisonous(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        this.fly = true;
        super.width = 45;
        super.height = 48;
        this.XPExist = 8;
        this.nturn = 0;
    }

    @Override
    public void turnAction() {
        try {
            short xpre = this.X;
            ArrayList<Player> player = new ArrayList<>();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (this.fightMNG.players[i] != null && !this.fightMNG.players[i].isDie && this.fightMNG.players[i].Y - 150 > this.Y) {
                    player.add(this.fightMNG.players[i]);
                }
            }
            //kéo
            if (player.size() > 0 && nturn == 0) {
                nturn = 3;
                Player pl = (player.get(Until.nextInt(0, player.size() - 1)));
                this.X = pl.X;
                this.fightMNG.flyChangeLocation(super.index);
                this.fightMNG.capture(super.index, pl.index);
                fightMNG.isNextTurn = false;
                this.target = pl;
                this.fightMNG.newShoot(this.index, (byte) 8, (short) 270, (byte) 10, (byte) 0, (byte) 1, false);
                fightMNG.isNextTurn = true;
                this.X = xpre;
                this.fightMNG.flyChangeLocation(super.index);
                if (!fightMNG.checkWin()) {
                    this.fightMNG.nextTurn();
                }
            } else {
                if (nturn > 0) {
                    nturn--;
                }
                Player pl = fightMNG.getPlayerClosest(X, Y);
                if (!pl.isBiDoc) {
                    this.X = pl.X;
                    this.fightMNG.flyChangeLocation(super.index);
                    this.fightMNG.thadocBullet(super.index, pl.index);
                    this.fightMNG.updateBiDoc(pl);
                    this.X = xpre;
                    this.fightMNG.flyChangeLocation(super.index);
                    if (!fightMNG.checkWin()) {
                        this.fightMNG.nextTurn();
                    }
                } else {
                    this.X = (short) Until.nextInt(50, fightMNG.mapMNG.Width - 50);
                    this.fightMNG.flyChangeLocation(super.index);
                    this.fightMNG.newShoot(this.index, (byte) 47, (short) Until.getArgXY(X, Y, pl.X, pl.Y), (byte) 10, (byte) 0, (byte) 1, false);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
