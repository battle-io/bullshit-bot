/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javawrapper;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

/**
 *
 * @author Andrew
 */
public class BotConnector implements Runnable {

    private Thread serverListener;
    private Socket socket;
    private String name;
    private String email;
    private String key;
    private String room;
    private String language;
    private InetAddress serverIP;
    private int serverPort;
    static private final BlockingQueue<String> MessageQueue = new LinkedBlockingQueue();
    private static PrintWriter out;
    static private long startTime;
    int debug = 0;

    public BotConnector() {
        this.name       = "null";
        this.email      = "null";
        this.key        = "null";
        this.room       = "null";
        this.language   = "null";
        try {            
            this.serverPort = 4000;
            //this.serverIP = InetAddress.getLocalHost();
            this.serverIP = InetAddress.getByName("www.code-wars.com");
        } catch (Exception e) {
            if (debug == 1) {
                e.printStackTrace();
            }
        }
    }

    public void setBotName(String in){
        this.name=in;
    }

    public void setEmail(String in){
        this.email=in;
    }

    public void setAuthenticationKey(String in){
        this.key=in;
    }

    public void setRoom(String in){
        this.room=in;
    }

    /* Initialize connection using authentication parameters */
    public void connect() {
        BotConnector newBot = new BotConnector();
        newBot.setEmail(this.email);
        newBot.setAuthenticationKey(key);
        newBot.setBotName(name);
        newBot.setRoom(room);
        //Start Listener Thread
        this.serverListener = new Thread(newBot, "serverListener");
        this.serverListener.start();
    }


    /* getMsg() is a BLOCKING routine that returns the next message sent by
       the server.  If no message is in the queue, this routine will wait
     * for a server response.
     */
    public String[] getMsg() {
        String[] parts = null;
        try {
            String cmd = MessageQueue.take();
            parts = cmd.split("<<");
        } catch (Exception e) {
            if (debug == 1) {
                e.printStackTrace();
            }
        }
        return parts;
    }


    /* sendReply forwards a command and its metaData to the server.
     The timing parameter is not used by most challenges and will likely
     be removed from a public, downloadable wrapper.

     All commands have the following structure:
       COMMAND_NAME<<data1:data2:data3 etc
     */
    public void sendReply(String cmd, String reply) {
        if (cmd.equals("ACTION_REPLY")){
            long now = System.currentTimeMillis();
            long processTime = now - startTime;
            out.println(cmd + "<<"+processTime+":"+ reply);
        }else{
            //out.println(cmd + "<<null" + reply);
            out.println(cmd + "<<" + reply); //changed per drew's suggestion
        }
        
    }

    /* Main Routine which maintains connection to the server. */
    public void run() {
        while (true) {
            try {
                System.out.println("Attempting to connect @ " + serverIP + ":" + serverPort);
                this.socket = new Socket(serverIP, serverPort);
                System.out.println("Connected to Server!");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                PingThread pt = new PingThread(out);
                pt.start();
                //Send LOGIN_INFORM Command
                System.out.println("Transmitting Login Credentials...");
                String loginInfo = "LOGIN_INFORM<<" + name + ":" + email + ":" +key + ":"+ room + ":" + language;
                out.println( loginInfo );
                try {
                    while (true) {
                        String cmd = in.readLine();
                        String[] parts = cmd.split("<<");
                        String cmdType = parts[0];
                        String metaData = parts[1];

                      
                        // All GAME_INITIALIZE commands must be echoed to "accept" a challenge
                        if (cmdType.equals("GAME_INITIALIZE")) {
                            out.println(cmd);
                        }
                        // The timing parameter is not used by most challenges and will likely
                        // be removed from a public, downloadable wrapper.
                        if (cmdType.equals("ACTION_REQUEST")){
                            startTime = System.currentTimeMillis();
                        }
                        // Server Messages  should be displayed to the console.
                        // Game data is never sent using this command
                        if (cmdType.equals("SERVER_MESSAGE")) {
                            System.out.println("ServerMSG : " + metaData);
                        } else {
                            MessageQueue.add(cmd);
                        }
                    }
                } catch (IOException e) {
                    if (debug == 1) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    if (debug == 1) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        this.socket.close();
                        pt.interrupt();
                        System.out.println("Disconnected From Server.  Retrying in 5s...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                if (debug == 1) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* The Game Server needs to maintain a list of active bots.  It is the bot's
       responsibility to periodically ping the game server.  Interrupting this
       thread will cause the connection to drop.  Pings must be sent every 20s
     */
    private class PingThread extends Thread {

        private PrintWriter out;
        private boolean stop;

        public PingThread(PrintWriter out) {
            this.out = out;
            this.stop = false;
        }

        public void run() {
            while (!stop) {
                try {
                    Thread.sleep(20 * 1000);
                    out.println("SERVER_PING<<null");
                } catch (InterruptedException e) {
                    stop = true;
                }
            }
        }
    }
}
