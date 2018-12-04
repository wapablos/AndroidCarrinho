package project.phi.androidcar.DijkstraMode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.phi.androidcar.CameraMode.CameraActivity;
import project.phi.androidcar.R;

/* TODO: Chama a activity da Camera apos a resposta do Algoritmo */

public class DijkstraActivity extends AppCompatActivity {

    public Button bnt_start;
    public TextView final_point;
    public int fp;
    public int[][] maxt;
    public ArrayList ida, volta;
    public Map<String,String> MapCommands;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstra);

        ida = new ArrayList();
        volta = new ArrayList();

        bnt_start = (Button) findViewById(R.id.bnt_start);
        final_point = (TextView) findViewById(R.id.final_point);

        bnt_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String f_point = final_point.getText().toString();

                if (!f_point.equals("")) {
                    fp = Integer.parseInt(f_point);
                }

                // IDA
                getPathVector(0, fp, ida);
                CameraActivity.ida = (ArrayList) ida.clone();
                System.out.println("LISTA IDA:" + ida);

                // VOLTA
                getPathVector(fp - 1, 0, volta);
                CameraActivity.volta = (ArrayList) volta.clone();
                System.out.println("LISTA VOLTA:" + volta);
            }
        });

        // MAP COMMANDS
        MapCommands = new HashMap<String, String>();
        MapCommands.put("KEY","VALUE");

    }

    public void getPathVector(int init_point, int final_point, ArrayList list){
        list.clear();
        DijkstraAlgorithm dj = new DijkstraAlgorithm();
        dj.dijkstra(init_point);
        maxt = dj.getPathVect();

        for (int i =0; i < 15; i ++){
            int value = maxt[final_point - 1][i];
            if (value == -1) break;
            list.add(value);
        }
    }
}


