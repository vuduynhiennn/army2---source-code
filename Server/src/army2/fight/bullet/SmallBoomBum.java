package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;
import army2.fight.boss.SmallBoom;

/**
 *
 * @author Văn Tú
 */
public class SmallBoomBum extends Bullet {

    public SmallBoomBum(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
        super(bullMNG, bullId, satThuong, pl, pl.X, pl.Y - 12, 0, 0, 0, 0);
    }

    @Override
    public void nextXY() {
        this.collect = true;
        this.XArray.add((short) X);
        this.YArray.add((short) Y);
        this.Y += 2;
        this.XArray.add((short) X);
        this.YArray.add((short) Y);
        ((SmallBoom) pl).bomAction();
        if (this.isCanCollision) {
            fm.mapMNG.collision(X, Y, this);
        }
    }

}
