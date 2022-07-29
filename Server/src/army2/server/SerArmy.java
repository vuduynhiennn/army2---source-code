package army2.server;

/**
 *
 * @author ASD
 */
public class SerArmy {

    public static void main(String args[]) {
        System.out.println("Start server!");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Shutdown Server!");
                ServerManager.stop();
            }
        }));
        ServerManager.init();
        ServerManager.start();
    }
}
