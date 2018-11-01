package project.phi.androidcar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import project.phi.androidcar.JoystickMode.JoystickActivity;
import project.phi.androidcar.CameraMode.CameraActivity;

/*
    TODO: Modificar o layout dessa Activity

*/

public class MainActivity extends AppCompatActivity {

    public Button bnt_cam;
    public Button bnt_joy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // PERMISSION CAMERA REQUEST
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }

        bnt_cam = (Button) findViewById(R.id.bnt_camera);
        bnt_joy = (Button) findViewById(R.id.bnt_joystick);

        bnt_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        bnt_joy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoystickActivity.class);
                startActivity(intent);
            }
        });
    }
}

