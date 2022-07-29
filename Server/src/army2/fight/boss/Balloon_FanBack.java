package army2.fight.boss;

import army2.fight.Boss;
import army2.fight.FightManager;
import java.io.IOException;

/**
 *
 * @author Văn Tú
 */
public class Balloon_FanBack extends Boss {

    public Balloon_FanBack(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y) throws IOException {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 0;
        super.width = 10;
        super.height = 19;
        this.fly = true;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
    }

}
