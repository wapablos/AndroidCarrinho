package project.phi.androidcar.JoystickMode;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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

public class JoystickActivity extends IOIOActivity{

    private static final String TAG = JoystickActivity.class.getSimpleName();

    public TextView IOIOstatus;
    public TextView front_log, bottom_log, left_log, right_log;
    public ImageButton bnt_up, bnt_down, bnt_left, bnt_right;

    public Uart uart;
    public OutputStream uart_out;
    public InputStream uart_in;

    public long init_f,now_f,time_f;
    public long init_b,now_b,time_b;
    public long init_r,now_r,time_r;
    public long init_l,now_l,time_l;

    // SERIAL VARIABLES
    int RX_PIN = 12;
    int TX_PIN = 14;
    int BAUND = 9600;

    //COMMANDS
    char UP = 'f';
    char DOWN = 't';
    char RIGHT = 'r';   //d
    char LEFT = 'l';    //e
    char STOP = 's';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_joystick);

        IOIOstatus  = (TextView) findViewById(R.id.ioio_status);
        front_log   = (TextView) findViewById(R.id.front_log);
        bottom_log  = (TextView) findViewById(R.id.bottom_log);
        left_log    = (TextView) findViewById(R.id.left_log);
        right_log   = (TextView) findViewById(R.id.right_log);

        bnt_up      = (ImageButton) findViewById(R.id.bnt_up);
        bnt_down    = (ImageButton) findViewById(R.id.bnt_down);
        bnt_left    = (ImageButton) findViewById(R.id.bnt_left);
        bnt_right   = (ImageButton) findViewById(R.id.bnt_right);

    }

    class Looper extends BaseIOIOLooper {

        private DigitalOutput led;

        @Override
        protected void setup() throws ConnectionLostException {
            showVersion(ioio_, "IOIO Connected!");

            // Serial Setup
            try {
                uart        = ioio_.openUart(RX_PIN, TX_PIN, BAUND, Uart.Parity.NONE, Uart.StopBits.ONE);
                uart_in     = uart.getInputStream();
                uart_out    = uart.getOutputStream();

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
                        init_f = System.currentTimeMillis();
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                        now_f = System.currentTimeMillis();
                        time_f = now_f - init_f;
                        front_log.setText("Front: " + time_f + " ms");
                    }
                    return true;
                }
            });

            bnt_down.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(DOWN);
                        init_b = System.currentTimeMillis();

                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                        now_b = System.currentTimeMillis();
                        time_b = now_b - init_b;
                        bottom_log.setText("Down: " + time_b + " ms");
                    }
                    return true;
                }
            });

            bnt_right.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(RIGHT);
                        init_r = System.currentTimeMillis();
                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                        now_r = System.currentTimeMillis();
                        time_r = now_r - init_r;
                        right_log.setText("Right: " + time_r + " ms");
                    }
                    return true;
                }
            });

            bnt_left.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_DOWN){
                        SerialWrite(LEFT);
                        init_l = System.currentTimeMillis();

                    }
                    if (event.getAction() == event.ACTION_UP){
                        SerialWrite(STOP);
                        now_l = System.currentTimeMillis();
                        time_l = now_l - init_l;
                        left_log.setText("Left: " + time_l + " ms");
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
}

