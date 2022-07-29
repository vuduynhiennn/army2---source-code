package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.server.Until;
import army2.fight.Player;

/**
 *
 * @author ASD
 */
public class MGTBulletNew extends Bullet {

    protected byte force;

    public MGTBulletNew(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100, byte force) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        bullMNG.mgtAddX = 0;
        bullMNG.mgtAddY = 0;
        this.force = force;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (isMaxY) {
            this.collect = true;
            int nextX = X - XArray.get(0);
            int nextY = Y - YArray.get(0);
            int arg = Until.getArg(nextX, nextY);
            nextX = ((force + 5) * Until.cos(arg)) >> 10;
            nextY = ((force + 5) * Until.sin(arg)) >> 10;
            short x = XArray.get(XArray.size() - 1);
            short y = YArray.get(YArray.size() - 1);
            while (true) {
                if ((x < -100) || (x > fm.mapMNG.getWidth() + 100) || (y > fm.mapMNG.getHeight() + 100)) {
                    break;
                }
                short[] XYVC = bullMNG.getCollisionPoint(x, y, (short) (x + nextX), (short) (y - nextY), false, false);
                if (XYVC != null) {
                    x = XYVC[0];
                    y = XYVC[1];
                    break;
                }
                x += nextX;
                y -= nextY;
            }

            fm.mapMNG.collision(x, y, this);
            XArray.add((short) x);
            YArray.add((short) y);
            bullMNG.mgtAddX = (byte) nextX;
            bullMNG.mgtAddY = (byte) nextY;
        }
    }

}
