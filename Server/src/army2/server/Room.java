package army2.server;

import army2.fight.FightWait;
import java.util.ArrayList;

/**
 *
 *
 * @author văn tú
 */
public class Room {

    public byte id;
    public byte type;
    public String name;
    public int maxXu;
    public int minXu;
    public int minMap;
    public int maxMap;
    public final FightWait[] entrys;
    public int nType;

    public ArrayList<Byte> slMap = new ArrayList<>();

    public Room(int id, int type, int maxEntrys, int ntype) {
        this.id = (byte) id;
        this.type = (byte) type;
        this.nType = ntype;
        this.slMap = new ArrayList<>();
        byte maxPlayerInit = 0, map = 0;
        boolean isLH = false;

        byte[] continuityType = new byte[]{5, 5};
        byte[] continuityNumbers = new byte[]{8, 9};
        for (int i = 0; i < continuityNumbers.length; i++) {
            if (type == continuityType[i] && ntype == continuityNumbers[i]) {
                isLH = true;
                break;
            }
        }
        if (!isLH) {
            byte[] slMapId = new byte[]{30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
            byte[] slMapType = new byte[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
            byte[] slMapNumbers = new byte[]{0, 0, 1, 2, 3, 4, 5, 6, 7, 7};
            for (int i = 0; i < slMapId.length; i++) {
                if (type != slMapType[i] || slMapNumbers[i] != ntype) {
                    continue;
                }
                this.slMap.add(slMapId[i]);
            }
        }
        switch (type) {
            case 0:
                this.minXu = 0;
                this.maxXu = 1000;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;

            case 1:
                this.minXu = 1000;
                this.maxXu = 10000;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;

            case 2:
                this.minXu = 10000;
                this.maxXu = 100000;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;

            case 3:
                this.minXu = 100000;
                this.maxXu = 1000000;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;

            case 4:
                this.minXu = 10000;
                this.maxXu = 10000;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;

            case 5:
                this.minXu = 100;
                this.maxXu = 500000;
                this.minMap = 30;
                this.maxMap = 39;
                maxPlayerInit = ServerManager.maxPlayers;
                map = ServerManager.initMapBoss;
                break;

            case 6:
                this.minXu = 0;
                this.maxXu = Integer.MAX_VALUE;
                this.minMap = 0;
                this.maxMap = 29;
                maxPlayerInit = ServerManager.nPlayersInitRoom;
                map = ServerManager.initMap;
                break;
        }
        if (this.slMap.size() > 0) {
            map = this.slMap.get(0);
        }
        this.entrys = new FightWait[maxEntrys];
        for (int i = 0; i < maxEntrys; i++) {
            this.entrys[i] = new FightWait(this, this.type, (byte) i, ServerManager.maxPlayers, maxPlayerInit, map, (byte) Until.nextInt(0, 2), isLH);
        }
    }

    protected int getFully() {
        int maxPlayers = 0;
        int player = 0;
        synchronized (entrys) {
            for (FightWait fw : entrys) {
                maxPlayers += fw.maxSetPlayer;
                if (fw.started) {
                    player += fw.maxSetPlayer;
                } else {
                    player += fw.numPlayer;
                }
            }
            int perCent = (player * 100) / maxPlayers;
            return (perCent < 50) ? 2 : ((perCent < 75) ? 1 : 0);
        }
    }

}
