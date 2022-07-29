package army2.server;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import network.ISession;
import network.IMessageHandler;
import network.Message;

/**
 *
 * @author Văn Tú
 */
public class ClientEntry implements ISession {

    private class Sender implements Runnable {

        private final ArrayList<Message> sendingMessage;

        public Sender() {
            sendingMessage = new ArrayList<>();
        }

        public void AddMessage(Message message) {
            sendingMessage.add(message);
        }

        @Override
        public void run() {
            try {
                while (isConnected() && dis != null) {
                    while (sendingMessage != null && sendingMessage.size() > 0) {
                        Message m = sendingMessage.get(0);
                        ServerManager.log("Send mss " + m.getCommand() + " to " + ClientEntry.this.toString());
                        doSendMessage(m);
                        sendingMessage.remove(0);
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                System.out.println("Socket is closed!");
            } 
        }
    }

    class MessageCollector implements Runnable {

        @Override
        public void run() {
            try {
                while (true && dis != null) {
                    Message message = readMessage();
                    if (message != null) {
                        if (onWork != null && onWork.isAlive()) {
                            onWork.stop();
                        }
                        onWork = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(180000);
                                    closeMessage();
                                } catch (InterruptedException ex) {
                                }
                            }
                        });
                        onWork.start();
                        ServerManager.log(ClientEntry.this.toString() + " send mss " + message.getCommand());
                        messageHandler.onMessage(message);
                        message.cleanup();
                    } else {
                        break;
                    }
                }
            } catch (Exception ex) {
                closeMessage();
            }
        }

        private Message readMessage() throws Exception {
            // read message command
            byte cmd = dis.readByte();
            if (connected) {
                cmd = readKey(cmd);
            }
            // read size of data
            int size;
            if (connected) {
                byte b1 = dis.readByte();
                byte b2 = dis.readByte();
                size = (readKey(b1) & 0xff) << 8 | readKey(b2) & 0xff;
            } else {
                size = dis.readUnsignedShort();
            }
            byte data[] = new byte[size];
            int len = 0;
            int byteRead = 0;
            while (len != -1 && byteRead < size) {
                len = dis.read(data, byteRead, size - byteRead);
                if (len > 0) {
                    byteRead += len;
                }
            }
            if (connected) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = readKey(data[i]);
                }
            }
            Message msg = new Message(cmd, data);
            return msg;
        }
    }

    private static final byte[] key = "bth.army2.ml".getBytes();
    public Socket sc;
    public DataInputStream dis;
    public DataOutputStream dos;
    public int id;
    public User user;
    private IMessageHandler messageHandler;
    protected boolean connected, login;
    private byte curR, curW;
    private final Sender sender;
    private Thread collectorThread;
    protected Thread sendThread;
    protected final Object obj = new Object();
    protected String plastfrom;
    public String versionARM;
    public Thread onWork;
    public String IPAddress;

    public ClientEntry(Socket sc, int id) throws IOException {
        this.sc = sc;
        this.id = id;
        this.dis = new DataInputStream(sc.getInputStream());
        this.dos = new DataOutputStream(sc.getOutputStream());
        setHandler(new MessageHandler(this));
        sendThread = new Thread(sender = new Sender());
        collectorThread = new Thread(new MessageCollector());
        collectorThread.start();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void sendMessage(Message message) {
        sender.AddMessage(message);
    }

    protected synchronized void doSendMessage(Message m) throws IOException {
        byte[] data = m.getData();
        try {
            if (connected) {
                byte b = (writeKey(m.getCommand()));
                dos.writeByte(b);
            } else {
                dos.writeByte(m.getCommand());
            }
            if (data != null) {
                int size = data.length;
                if (m.getCommand() == 90) {
                    dos.writeInt(size);
                    dos.write(data);
                } else {
                    if (connected) {
                        int byte1 = writeKey((byte) (size >> 8));
                        dos.writeByte(byte1);
                        int byte2 = writeKey((byte) (size & 0xFF));
                        dos.writeByte(byte2);
                        // System.out.println("l1=" + byte1 + " l2=" + byte2 + " k1"+key1+" k2="+key2);
                    } else {
                        dos.writeShort(size);
                    }
                    //
                    if (connected) {
                        for (int i = 0; i < data.length; i++) {
                            data[i] = writeKey(data[i]);
                        }
                    }
                    dos.write(data);
                }
//                sendByteCount += (5 + data.length);
            } else {
                dos.writeShort(0);
//                sendByteCount += 5;
            }
            dos.flush();
            m.cleanup();
        } catch (IOException e) {
            closeMessage();
            e.printStackTrace();
        }
    }

    private byte readKey(byte b) {
        byte i = (byte) ((key[curR++] & 0xff) ^ (b & 0xff));
        if (curR >= key.length) {
            curR %= key.length;
        }
        return i;
    }

    private byte writeKey(byte b) {
        byte i = (byte) ((key[curW++] & 0xff) ^ (b & 0xff));
        if (curW >= key.length) {
            curW %= key.length;
        }
        return i;
    }

    @Override
    public void close() {
        try {
            if (user != null) {
                user.close();
            }
            ServerManager.disconnect(this);
            cleanNetwork();
            user = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanNetwork() {
        curR = 0;
        curW = 0;
        try {
            connected = false;
            login = false;
            if (sc != null) {
                sc.close();
                sc = null;
            }
            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            sendThread = null;
            collectorThread = null;
            onWork = null;
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (this.user != null) {
            return this.user.toString();
        }
        return "Client " + this.id;
    }

    public void hansakeMessage() throws IOException {
        Message ms = new Message(-27);
        DataOutputStream ds = ms.writer();
        ds.writeByte(key.length);
        ds.writeByte(key[0]);
        for (int i = 1; i < key.length; i++) {
            ds.writeByte(key[i] ^ key[i - 1]);
        }
        ds.flush();
        doSendMessage(ms);
        connected = true;
        sendThread.start();
    }

    public void loginMessage(Message ms) throws IOException {
        if (this.login) {
            return;
        }
        if (!BangXHManager.isComplete) {
            ms = new Message(4);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.getString("NOT_FINISHED_LOADING_RANKING", 1000));
            ds.flush();
            this.sendMessage(ms);
            return;
        }
        String userS = ms.reader().readUTF().trim();
        String pass = ms.reader().readUTF().trim();
        String version = ms.reader().readUTF().trim();
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m1 = p.matcher(userS);
        Matcher m2 = p.matcher(pass);
        if (!m1.find() || !m2.find()) {
            ms = new Message(4);
            DataOutputStream ds = ms.writer();
            ds.writeUTF(GameString.reg_Error1());
            ds.flush();
            this.sendMessage(ms);
            return;
        }
        System.out.println("Client: " + id + " name: " + userS + " pass: " + pass + " version: " + version);
        this.versionARM = version;
        User us = User.login(this, userS, pass);
        if (us != null) {
            System.out.println("Login Success!");
            this.login = true;
            this.user = us;
            ServerManager.sendNVData(user);
            ServerManager.sendRoomInfo(user);
            ServerManager.sendMapCollisionInfo(user);
        } else {
            System.out.println("Login Failse!");
            this.login = false;
        }
    }

    public void closeMessage() {
        if (isConnected()) {
            if (messageHandler != null) {
                messageHandler.onDisconnected();
            }
            close();
        }
    }

    public void regMessage(Message ms) throws IOException {
        ms = new Message(4);
        DataOutputStream ds = ms.writer();
        ds.writeUTF("Vui lòng truy cập http://army2.ml để đăng ký");
        ds.flush();
        this.sendMessage(ms);
//        try {
//            String name = ms.reader().readUTF().replaceAll(" ", "").trim();
//            String pass = ms.reader().readUTF().replaceAll(" ", "");
//
//            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
//            Matcher m1 = p.matcher(name);
//            Matcher m2 = p.matcher(pass);

//            if (!m1.find() || !m2.find()) {
//                ms = new Message(4);
//                DataOutputStream ds = ms.writer();
//                ds.writeUTF(GameString.reg_Error1());
//                ds.flush();
//                this.sendMessage(ms);
//                return;
//            } else if (name.length() < 5 || name.length() > 16) {
//                ms = new Message(4);
//                DataOutputStream ds = ms.writer();
//                ds.writeUTF(GameString.reg_Error2());
//                ds.flush();
//                this.sendMessage(ms);
//                return;
//            } else if (pass.length() < 1 || pass.length() > 40) {
//                ms = new Message(4);
//                DataOutputStream ds = ms.writer();
//                ds.writeUTF(GameString.reg_Error3());
//                ds.flush();
//                this.sendMessage(ms);
//                return;    
//            }
//            try (ResultSet red = SQLManager.getStatement().executeQuery("SELECT * FROM `user` WHERE user=\"" + name + "\";")) {
//                if (red.first()) {
//                    ms = new Message(4);
//                    DataOutputStream ds = ms.writer();
//                    ds.writeUTF(GameString.reg_Error4());
//                    ds.flush();
//                    this.sendMessage(ms);
//                } else {
//                    SQLManager.getStatement().executeUpdate("INSERT INTO user(`user`, `password`) VALUES ('" + name + "', '" + pass + "');");
//
//                    ms = new Message(4);
//                    DataOutputStream ds = ms.writer();
//                    ds.writeUTF(GameString.reg_Error5());
//                    ds.flush();
//                    this.sendMessage(ms);
//                    System.out.println("regtry True name: "+ name +" pass: "+ pass);
//                }
//                red.close();
//            }
//        } catch(IOException | SQLException e) {
//            e.printStackTrace();
//            System.out.println("regtry False");
//        }
    }

}
