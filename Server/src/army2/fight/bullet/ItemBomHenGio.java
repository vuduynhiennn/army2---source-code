package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author Văn Tú
 */
public class ItemBomHenGio extends Bullet {

    public ItemBomHenGio(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        this.isCanCollision = false;
        this.isXuyenPlayer = true;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (super.collect) {
            this.isCanCollision = true;
            this.bullMNG.buls.add(new BulletManager.Bullets(this));
        }
    }

}
