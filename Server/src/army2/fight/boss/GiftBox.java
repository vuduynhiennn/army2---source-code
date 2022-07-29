/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package army2.fight.boss;

import army2.fight.Boss;
import army2.fight.FightManager;

/**
 *
 * @author pika
 */
public class GiftBox extends Boss {
    
    public byte[] item;

    public GiftBox(FightManager fightMNG, byte idGun, String name, byte location, int HPMax, short X, short Y, byte[] item) {
        super(fightMNG, idGun, name, location, HPMax, X, Y);
        super.theLuc = 100;
        super.width = 24;
        super.height = 24;
        this.XPExist = 0;
        this.item = item;
        this.fly = (this.idNV == 24);
    }
    
    @Override
    protected void turnAction() {
        
    }
    
}
