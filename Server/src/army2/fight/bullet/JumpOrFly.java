package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;

/**
 *
 * @author Văn Tú
 */
public class JumpOrFly extends Bullet {

    public JumpOrFly(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        super.isCanCollision = false;
        super.isXuyenPlayer = true;
        super.isXuyenMap = true;
    }

    @Override
    public void nextXY() {
        if (this.isMaxY) {
            super.isXuyenMap = false;
        }
        super.nextXY();
        if (super.collect) {
            fm.getPlayerTurn().setXY((short) X, (short) Y);
        }
    }

}
