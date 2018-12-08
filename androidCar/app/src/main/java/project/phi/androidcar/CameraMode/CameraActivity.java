package project.phi.androidcar.CameraMode;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import project.phi.androidcar.R;

public class CameraActivity extends IOIOActivity {

    private Camera camera;
    public FrameLayout frameLayout;
    public CameraView CameraView;
    public TextView serverStatus;
    public TextView socketin;

    // CONFIGURACAO SOCKETS
    public static String SERVERIP = "localhost";
    public static final int SERVERPORTS = 9191;
    public static final int SERVERPORTR = 9192;
    private Handler handler = new Handler();

    // SERIAL IOIO
    public Uart uart;
    public OutputStream uart_out;
    public InputStream uart_in;

    // CONTROL
    public char command = 'x';
    // VETOR PARA O ALGORTMO ROTEAMENTO
    public static char[] ida;
    public static char[] volta;
    public String resp = " ";
    String OK = "s";

    // SERIAL VARIABLES
    int RX_PIN = 12;
    int TX_PIN = 14;
    int BAUND = 9600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_streaming);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        serverStatus = (TextView) findViewById(R.id.textView);
        socketin = (TextView) findViewById(R.id.textView);

        SERVERIP = getLocalIpAddress();
        camera = getCameraInstance();

        CameraView = new CameraView(this, camera);
        preview.addView(CameraView);

        //Socket to send imagens
        Thread sThread = new Thread(new CameraServerThread(this, SERVERIP, SERVERPORTS, handler));
        sThread.start();

        //Socket to receive commands
        Thread rThread = new Thread(new CameraCommandsThread(this, SERVERIP, SERVERPORTR, handler));
        rThread.start();

    }

    class Looper extends BaseIOIOLooper {

        @Override
        protected void setup() throws ConnectionLostException {
            showVersion(ioio_, "IOIO Connected!");
            // Serial Setup
            try {
                uart = ioio_.openUart(RX_PIN, TX_PIN, BAUND, Uart.Parity.NONE, Uart.StopBits.ONE);
                uart_in = uart.getInputStream();
                uart_out = uart.getOutputStream();

            } catch (ConnectionLostException e){
                e.printStackTrace();
            }
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
//            Log.e("TEST", "Entrando no running de ida");
            Running(ida);
            Running(volta);
            while(true){
                Log.e("TEST", "FIM");
                Thread.sleep(1000);
            }
//            Log.e("TEST", "Saindo no running de ida");
        }

        @Override
        public void disconnected(){
            toast("IOIO disconnected");
        }

        @Override
        public void incompatible() {
            showVersion(ioio_, "Incompatible firmware version!");
        }
    }

    public void Running(char[] path) throws InterruptedException {
        for (int i = 0; i<=path.length; i++) {
            Log.e("TEST", "Interação: "+ i);

            if (command != 'x') {
                if (command == 'W') {
                    Log.e("TEST3", String.valueOf("PyMSG: "+command));
                    SerialWrite('s');
                    Thread.sleep(2000);
                }
                else if ( command == 'R') {
                    SerialWrite('s');
                    while (command != 'G') {
                        Thread.sleep(100);
                    }
                }
                command = 'x';
            }

            // TODO: DEBUG: Enviando o path[i] direto o arduino não reconhece (motivo desconhecido).
            if (path[i] != ' ') {
                if (path[i] == 'f'){
                    Log.e("TEST", "Enviado: "+ path[i]);
                    SerialWrite('f');

                } else if (path[i] == 'r'){
                    Log.e("TEST", "Enviado: "+ path[i]);
                    SerialWrite('r');

                } else if (path[i] == 'l'){
                    Log.e("TEST", "Enviado: "+ path[i]);
                    SerialWrite('l');

                } else if (path[i] == 'L'){
                    Log.e("TEST", "Enviado: "+ path[i]);
                    SerialWrite('L');
                }
            } else {
                break;
            }

            // Aplicação fica esperando a resposta do HARDWARE
            while (!resp.equals(OK)) {
                try {
                    int availableBytes = uart_in.available();
                    if (availableBytes > 0) {
                        byte[] readBuffer = new byte[10];
                        uart_in.read(readBuffer, 0, availableBytes);
                        char[] Temp = (new String(readBuffer, 0, availableBytes).toCharArray());
                        resp = new String(Temp);
                        Log.e("TEST", "Recebido: "+resp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resp = "x";
        }
        // Tempo que o carrinho vai ficar esperando ao chegar no destino final
        Thread.sleep(5000);
    }

    public void SerialWrite(char message) {
        try {
            uart_out.write(message);
        } catch (IOException e) {
            Log.e("TEST", "Entrando na exception");
            e.printStackTrace();
        }
    }

    @Override
    protected IOIOLooper createIOIOLooper(){
        return new Looper();
    }

    private void showVersion(IOIO ioio, String title){
        toast(String.format(
                "%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmaware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));
    }

    private void toast (final String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
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