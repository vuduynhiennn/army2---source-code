package army2.fight.bullet;

import army2.fight.Bullet;
import army2.fight.BulletManager;
import army2.fight.Player;
import army2.server.Until;

/**
 *
 * @author Văn Tú
 */
public class ApaBullet extends Bullet {

    protected byte force;
    protected byte force2;
    protected int satThuongGoc;

    public ApaBullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int X, int Y, int vx, int vy, int msg, int g100, byte force, byte force2) {
        super(bullMNG, bullId, satThuong, pl, X, Y, vx, vy, msg, g100);
        this.force = force;
        this.force2 = force2;
        this.satThuongGoc = satThuong;
    }

    @Override
    public void nextXY() {
        super.nextXY();
        if (this.force2 == frame) {
            this.collect = true;
            this.isCanCollision = false;
            this.satThuong = 0;
            fm.mapMNG.collision(this.X, this.Y, this);
            int arg = bullMNG.arg + Until.toArg0_360(Until.getArg(this.pl.X - this.X, this.pl.Y - this.Y));
            if (bullMNG.arg < 90) {
                arg = 180 - arg;
            }
            arg = arg - 15;
            for (int i = 0; i < 3; i++, arg += 15) {
                int x = this.X + (20 * Until.cos(arg) >> 10);
                int y = (this.Y - 12) - (20 * Until.sin(arg) >> 10);
                int vxn = (this.force * Until.cos(arg) >> 11);
                int vyn = -(this.force * Until.sin(arg) >> 11);
                bullMNG.addBullet(new Bullet(bullMNG, (byte) 18, this.satThuongGoc, super.pl, x, y, vxn, vyn, 30, 100));
            }
        }
    }

}
