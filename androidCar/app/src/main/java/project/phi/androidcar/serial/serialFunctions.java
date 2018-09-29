package project.phi.androidcar.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class serialFunctions extends BaseIOIOLooper {

    private Uart uart;
    private InputStream uart_in;
    private OutputStream uart_out;

    public void serialSetup(int rx, int tx, int baud, Uart.Parity parity, Uart.StopBits stopBits) {
        try {
            uart = ioio_.openUart(rx, tx, baud, parity, stopBits);

        } catch (ConnectionLostException e) {
            e.printStackTrace();
        }
    }

    public String serialRead() {
        String read = null;

        try {
            uart_in = uart.getInputStream();
            int BufferSize = 1000;

            int availableBytes = uart_in.available();

            if (availableBytes > 0){
                byte[] readBuffer = new byte[BufferSize];
                uart_in.read(readBuffer, 0, availableBytes);
                char[] Temp = (new String(readBuffer, 0, availableBytes).toCharArray());
                read = new String(Temp);
            }

        } catch (IOException e){
            e.printStackTrace();
            read = "Error!";
        }
        return read;
    }

    public boolean serialWrite(char w) {
        boolean write = false;
        try {
            uart_out.write(w);
            write = true;

        } catch (IOException e){
            e.printStackTrace();
        }

        return write;
    }
}
