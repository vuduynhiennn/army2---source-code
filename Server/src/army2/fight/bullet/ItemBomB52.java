package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author Văn Tú
 */
public class ItemBomB52 extends Bullet {

    public ItemBomB52(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        super.isCanCollision = false;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (super.collect) {
            this.bullMNG.addBullet(new B52Bullet(bullMNG, (byte) 3, this.satThuong, super.pl, this.X - 50, this.Y - 260, 2, 0, 0, 80, this.X, this.Y));
        }
    }

}
