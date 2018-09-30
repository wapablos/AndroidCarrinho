package project.phi.androidcar.streaming;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;

public class streamingView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Camera camera;
    private SurfaceHolder holder;
    private int width;
    private int height;
    public ByteArrayOutputStream FrameBuffer;
    private Context con;

    public streamingView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(320, 240);
            this.width = parameters.getPreviewSize().width;
            this.height = parameters.getPreviewSize().height;
            parameters.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(parameters);
            camera.setPreviewCallback(this);
            camera.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            // Convert YuVImage(NV21) to JPEG Image Data
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, this.width, this.height, null);
            System.out.println("WidthandHeight" + yuvImage.getHeight() + "::" + yuvImage.getWidth());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0,0,this.width, this.height), 100, baos);
            FrameBuffer = baos;
        } catch (Exception e){
            Log.d("Parse", "errpr");
        }
    }
}
