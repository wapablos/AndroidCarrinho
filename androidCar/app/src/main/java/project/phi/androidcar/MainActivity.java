package project.phi.androidcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import project.phi.androidcar.joystick.joystickActivity;
import project.phi.androidcar.streaming.streamingActivity;

/*
    TODO: Modificar o layout dessa Activity

*/

public class MainActivity extends AppCompatActivity {

    public Button bnt_cam;
    public Button bnt_joy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnt_cam = (Button) findViewById(R.id.bnt_camera);
        bnt_joy = (Button) findViewById(R.id.bnt_joystick);

        bnt_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, streamingActivity.class);
                startActivity(intent);
            }
        });

        bnt_joy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, joystickActivity.class);
                startActivity(intent);
            }
        });
    }
}

