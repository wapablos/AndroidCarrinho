package project.phi.androidcar.CameraMode;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraCommandsThread implements Runnable {
    private int ServerPort;
    private String ServerIP;
    private Context context;
    private android.os.Handler handler;
    private CameraActivity cameraActivityInstance;

    public CameraCommandsThread(Context context, String serverip, int serverport, android.os.Handler handler) {
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
                    cameraActivityInstance.socketin.setText("Receved data:" + ServerIP);
                    //TODO: VER SE ESSA ATUALIZAÇÃO ACONTECE SÓ UMA VEZ
                }
            });

            while (true) {
                Socket s = ss.accept();
                new Thread(new CameraCommandsThread.ServerSocketThread(s)).start();
            }
        } catch (Exception e) {
            Log.d("CommandThread", "Run: ERROR");
        }
    }

    public class ServerSocketThread implements Runnable {
        Socket s = null;
        InputStream is = null;

        public ServerSocketThread(Socket s) throws IOException {
            this.s = s;
        }

        @Override
        public void run() {
            if(s != null){
                try {
                    s.setKeepAlive(true);
                    is = s.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    while(true){
                        byte c = dis.readByte();
                        Log.e("Receved_COMMANDTHREAD","Received: " + c);
                        //TODO: VER SE ISSO TÁ FUNCIONANDO
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                System.out.println("socket is null");
            }
        }
    }
}
