package project.phi.androidcar.joystick;

import android.content.Context;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }

    class Looper extends BaseIOIOLooper {

        @Override
        protected void setup() throws ConnectionLostException {
            showVersion(ioio_, "IOIO Connected!");
        }

        @Override
        public void loop() throws ConnectionLostException {
        }

        @Override
        public void disconnected(){

        }

        @Override
        public void incompatible() {
            showVersion(ioio_, "Incompatible firmware version!");
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

