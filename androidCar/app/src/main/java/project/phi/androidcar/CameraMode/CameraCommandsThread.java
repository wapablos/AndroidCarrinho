package project.phi.androidcar.CameraMode;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class CameraCommandsThread implements Runnable {
    private int ServerPort;
    private String ServerIP;
    private Context context;
    private Handler handler;
    private CameraActivity cameraActivityInstance;
    public char command;

    public CameraCommandsThread(Context context, String serverip, int serverport, Handler handler) {
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

            while (true) {
                Socket s = ss.accept();
                new Thread(new ServerSocketThread(s)).start();
                SerialOIOIThread ioio = new SerialOIOIThread(command);
                ioio.setup();
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
                try {
                    s.setKeepAlive(true);
                    is = s.getInputStream();

                    while(true){
                        DataInputStream dis = new DataInputStream(is);
                        command = dis.readChar();
                        Log.e("TEST", String.valueOf(command));
                        Thread.sleep(1000/5);
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

    public class SerialOIOIThread extends BaseIOIOLooper{
        char command;

        public SerialOIOIThread(char command) {
            super();
            this.command = command;
        }

        public Uart uart;
        public OutputStream uart_out;
        public InputStream uart_in;

        // SERIAL VARIABLES
        int RX_PIN = 12;
        int TX_PIN = 14;
        int BAUND = 9600;

        @Override
        protected void setup() throws ConnectionLostException {

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
        public void loop() throws ConnectionLostException {
            try {
                uart_out.write(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}