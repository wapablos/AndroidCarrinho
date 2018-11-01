package project.phi.androidcar.CameraMode;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import project.phi.androidcar.R;

/*
    TODO: Colocar um textview no activity_streaming ao lado do ip, para mostrar as mensagens recebidas via socket.
 */

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    public FrameLayout frameLayout;
    public CameraView CameraView;
    public TextView serverStatus;
    public TextView socketin;


    public static String SERVERIP = "localhost";
    public static final int SERVERPORT = 9191;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        serverStatus = (TextView) findViewById(R.id.textView);
        socketin = (TextView) findViewById(R.id.textView);

        SERVERIP = getLocalIpAddress();
        camera = getCameraInstance();

        CameraView = new CameraView(this, camera);
        preview.addView(CameraView);

        Thread sThread = new Thread(new CameraServerThread(this, SERVERIP, SERVERPORT, handler));
        sThread.start();

        //Thread cThread = new Thread(new CameraCommands());
    }

    private String getLocalIpAddress () {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

    public static Camera getCameraInstance()
    {
        Camera c=null;
        try{
            c=Camera.open();
        }catch(Exception e){
            e.printStackTrace();
        }
        return c;
    }
}