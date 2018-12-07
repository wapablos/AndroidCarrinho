package project.phi.androidcar.RoutingMode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.phi.androidcar.CameraMode.CameraActivity;
import project.phi.androidcar.MainActivity;
import project.phi.androidcar.R;

public class RoutingActivity extends AppCompatActivity {

    public Button bnt_start;
    public TextView final_point;
    public char[] ida, volta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);

        bnt_start   = (Button) findViewById(R.id.bnt_start);
        final_point = (TextView) findViewById(R.id.final_point);

        bnt_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int f_point = Integer.parseInt(String.valueOf(final_point.getText()));

                if (isValidate(f_point)){
                    RoutingAlgorithm cmd = new RoutingAlgorithm();
                    cmd.FinalPoint(f_point);

                    ida = cmd.getCmdIda();
                    volta = cmd.getCmdVolta();

                    CameraActivity.ida = ida;
                    CameraActivity.volta = volta;

                    // Chamando Activity da Camera
                    Intent intent = new Intent(RoutingActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public Boolean isValidate(int fp){
        final Context context = this;
        boolean valid = true;

        if (fp == 0){
            Toast.makeText(context,
                    "Adicione um ponto final!", Toast.LENGTH_LONG).show();
            valid = false;

        } else if ( !(fp == 1 || fp == 3 || fp == 6 || fp == 8 || fp == 11 || fp == 13) ){
            Toast.makeText(context,
                    "Ponto final inválido! Pontos finais disponíveis: 1, 3, 6, 8, 11 e 13!", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }
}


