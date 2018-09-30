package project.phi.androidcar.streaming;

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

public class streamingActivity extends AppCompatActivity {

    private Camera camera;
    public FrameLayout frameLayout;
    public streamingView streamingView;
    public TextView serverStatus;

    public static String SERVERIP = "localhost";
    public static final int SERVERPORT = 9191;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        serverStatus = (TextView) findViewById(R.id.textView);

        SERVERIP = getLocalIpAddress();
        camera = getCameraInstance();

        streamingView = new streamingView(this, camera);
        preview.addView(streamingView);
        Thread cThread = new Thread(new streamingServerThread(this, SERVERIP, SERVERPORT, handler));
        cThread.start();
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