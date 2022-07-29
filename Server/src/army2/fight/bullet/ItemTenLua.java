package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author Văn Tú
 */
public class ItemTenLua extends Bullet {

    protected byte force;

    public ItemTenLua(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100, byte force) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        super.isCanCollision = false;
        this.force = force;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (super.frame == this.force || this.collect) {
            this.bullMNG.addBullet(new Bullet(bullMNG, (byte) 27, super.satThuong, pl, this.X + 18, this.Y - 20, 2, -1, 15, 60));
            this.bullMNG.addBullet(new Bullet(bullMNG, (byte) 27, super.satThuong, pl, this.X + 17, this.Y - 20, -3, -1, 15, 60));
            this.bullMNG.addBullet(new Bullet(bullMNG, (byte) 27, super.satThuong, pl, this.X + 16, this.Y - 23, 3, -2, 15, 60));
        }
    }
}
