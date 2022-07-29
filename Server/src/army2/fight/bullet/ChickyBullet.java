package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author ASD
 */
public class ChickyBullet extends Bullet {

    protected byte force2;
    private int satThuongGoc;

    public ChickyBullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100, byte force2) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        this.force2 = force2;
        this.satThuongGoc = satThuong;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (this.force2 == super.frame) {
            bullMNG.addBullet(new ChickyTrung(bullMNG, (byte) 20, this.satThuongGoc, super.pl, super.lastX, super.lastY, 0, 0, 20, 50));
        }
        if (this.isCollect()) {
            return;
        }
    }

}
