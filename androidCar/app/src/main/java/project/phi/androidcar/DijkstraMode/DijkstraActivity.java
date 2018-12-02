package project.phi.androidcar.DijkstraMode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.phi.androidcar.R;

/* TODO: Chama a activity da Camera apos a resposta do Algoritmo */

public class DijkstraActivity extends AppCompatActivity {

    public Button bnt_start;
    public TextView init_point, final_point;
    public int fp, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstra);


        bnt_start = (Button) findViewById(R.id.bnt_start);
        init_point = (TextView) findViewById(R.id.init_point);
        final_point = (TextView) findViewById(R.id.final_point);

        bnt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f_point = final_point.getText().toString();
                String i_point = init_point.getText().toString();

                if (!f_point.equals("") && !i_point.equals("")) {
                    fp = Integer.parseInt(f_point);
                    ip = Integer.parseInt(i_point);
                }

                

            }
        });
    }
}

