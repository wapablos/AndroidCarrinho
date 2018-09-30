package project.phi.androidcar.joystick;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import project.phi.androidcar.R;

public class joystickActivity extends IOIOActivity{

    private static final String TAG = joystickActivity.class.getSimpleName();

    TextView IOIOstatus;

    public ImageButton bnt_up;
    public ImageButton bnt_down;
    public ImageButton bnt_left;
    public ImageButton bnt_right;

    public Uart uart;
    public OutputStream uart_out;
    public InputStream uart_in;

    // SERIAL VARIABLES
    int RX_PIN = 12;
    int TX_PIN = 14;
    int BAUND = 9600;

    char UP = 'u';
    char DOWN = 'd';
    char RIGHT = 'r';
    char LEFT = 'l';
    char STOP = 's';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        IOIOstatus = (TextView) findViewById(R.id.ioio_status);

        bnt_up = (ImageButton) findViewById(R.id.bnt_up);
        bnt_down = (ImageButton) findViewById(R.id.bnt_down);
        bnt_left = (ImageButton) findViewById(R.id.bnt_left);
        bnt_right = (ImageButton) findViewById(R.id.bnt_right);

    }

    class Looper extends BaseIOIOLooper {

        private DigitalOutput led;

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

            //Led for testing
            led = ioio_.openDigitalOutput(0, true);
        }

        @Override
        public void loop() throws ConnectionLostException {
            bnt_up.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(UP);
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                    }
                    return true;
                }
            });

            bnt_down.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(DOWN);
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                    }
                    return true;
                }
            });

            bnt_right.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(RIGHT);
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                    }
                    return true;
                }
            });

            bnt_left.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(LEFT);
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                    }
                    return true;
                }
            });
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

    public void SerialWrite(char message) {
        try {
            uart_out.write(message);
        } catch (IOException e) {
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

    /*
        # Serial READ example:
        try {
                int availableBytes = uart_in.available();

                if (availableBytes > 0) {
                    byte[] readBuffer = new byte[BufferSize];
                    uart_in.read(readBuffer, 0, availableBytes);
                    char[] Temp = (new String(readBuffer, 0, availableBytes).toCharArray());
                    String Temp2 = new String(Temp);
                    Log.e(TAG,"Receive: " + Temp2);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
     */
}

