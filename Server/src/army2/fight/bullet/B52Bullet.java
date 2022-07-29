package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author Văn Tú
 */
public class B52Bullet extends Bullet {

    private short toX, toY;

    public B52Bullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100, short toX, short toY) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        super.isXuyenMap = true;
        super.isXuyenPlayer = true;
        this.toX = toX;
        this.toY = toY;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (toY <= this.Y) {
            super.isXuyenMap = false;
            super.isXuyenPlayer = false;
            this.X = toX;
            this.Y = toY;
        }
    }

}
