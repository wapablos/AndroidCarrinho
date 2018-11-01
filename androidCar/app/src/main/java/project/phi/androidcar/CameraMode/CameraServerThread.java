package project.phi.androidcar.CameraMode;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraServerThread implements Runnable {
    private int ServerPort;
    private String ServerIP;
    private Context context;
    private Handler handler;
    private CameraActivity cameraActivityInstance;

    public CameraServerThread(Context context, String serverip, int serverport, Handler handler) {
        super();
        this.context = context;
        this.handler = handler;
        this.ServerIP = serverip;
        this.ServerPort = serverport;
        this.cameraActivityInstance = (CameraActivity) this.context;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(ServerPort);
            handler.post(new Runnable(){
                @Override
                public void run() {
                    cameraActivityInstance.serverStatus.setText("Listening on IP:" + ServerIP);
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
        InputStream is = null;

        public ServerSocketThread(Socket s) throws IOException {
            this.s = s;
        }

        @Override
        public void run() {
            if(s != null){
                String clientIp = s.getInetAddress().toString().replace("/", "");
                int clientPort = s.getPort();
                System.out.println("Client ip: "+ clientIp);
                System.out.println("Client port: " +clientPort);
                try {
                    s.setKeepAlive(true);
                    os = s.getOutputStream();
                    is = s.getInputStream();
                    while(true){
                        DataInputStream dis = new DataInputStream(is);
                        cameraActivityInstance.socketin.setText(dis.readUTF());
                        DataOutputStream dos = new DataOutputStream(os);
                        //dos.writeInt(4);
                        //dos.writeUTF("#@@#");
                        dos.writeInt(cameraActivityInstance.CameraView.FrameBuffer.size());
                        dos.writeUTF("-@@-");  //TOKEN UTF 02 -> AFTER IMG LENGTH
                        dos.flush();
                        System.out.println(cameraActivityInstance.CameraView.FrameBuffer.size());
                        dos.write(cameraActivityInstance.CameraView.FrameBuffer.toByteArray());
                        dos.writeUTF("FEND");
                        dos.flush();
                        Thread.sleep(1000/15); // 15 FRAMES PER SECOND
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