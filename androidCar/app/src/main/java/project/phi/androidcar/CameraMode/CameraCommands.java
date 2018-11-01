package project.phi.androidcar.CameraMode;

import android.content.Context;

import java.util.logging.Handler;

public class CameraCommands implements Runnable {

    private int ServerPort;
    private String ServerIP;
    private Context context;
    private Handler handler;
    private CameraActivity CameraActivityInstance;

    public CameraCommands(Context context, String serverip, int serverport, Handler handler){
        this.context = context;
        this.handler = handler;
        this.ServerIP = serverip;
        this.ServerPort = serverport;
        this.CameraActivityInstance = (CameraActivity) this.context;
    }

    @Override
    public void run() {


    }

}
