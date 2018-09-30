package project.phi.androidcar.streaming;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class streamingServerThread implements Runnable {
    private int ServerPort;
    private String ServerIP;
    private Context context;
    private Handler handler;
    private streamingActivity streamingActivityInstance;


    public streamingServerThread(Context context, String serverip, int serverport, Handler handler) {
        super();
        this.context = context;
        this.handler = handler;
        this.ServerIP = serverip;
        this.ServerPort = serverport;
        this.streamingActivityInstance = (streamingActivity) this.context;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(ServerPort);
            handler.post(new Runnable(){
                @Override
                public void run() {
                    streamingActivityInstance.serverStatus.setText("Listening on IP:" + ServerIP);
                }
            });

            while (true) {
                Socket s = ss.accept();
                new Thread(new ServerSocketThread(s)).start();
            }

        } catch (Exception e) {
            Log.d("ServerThread", "Run: ERROR");
        }
    }

    public class ServerSocketThread implements Runnable {
        Socket s = null;
        OutputStream os = null;

        public ServerSocketThread(Socket s) throws IOException {
            this.s = s;
        }

        @Override
        public void run() {
            if(s !=null){
                String clientIp = s.getInetAddress().toString().replace("/", "");
                int clientPort = s.getPort();
                System.out.println("====client ip====="+clientIp);
                System.out.println("====client port====="+clientPort);
                try {

                    s.setKeepAlive(true);
                    os = s.getOutputStream();
                    while(true){
                        DataOutputStream dos = new DataOutputStream(os);
                        dos.writeInt(4);
                        dos.writeUTF("#@@#");
                        dos.writeInt(streamingActivityInstance.streamingView.FrameBuffer.size());
                        dos.writeUTF("-@@-");
                        dos.flush();
                        System.out.println(streamingActivityInstance.streamingView.FrameBuffer.size());
                        dos.write(streamingActivityInstance.streamingView.FrameBuffer.toByteArray());
                        dos.flush();
                        Thread.sleep(1000/15);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (os!= null)
                            os.close();

                    } catch (Exception e2) {
                        e.printStackTrace();
                    }

                }
            } else{
                System.out.println("socket is null");
            }
        }
    }
}