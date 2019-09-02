package com.example.hello.futurehome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hello.futurehome.OnDataSendToActivity;
import com.example.hello.futurehome.R;
import com.example.hello.futurehome.SelectTask;
import com.example.hello.futurehome.StatusTask;

import org.json.JSONException;
import org.json.JSONObject;

public class DoorActivity extends AppCompatActivity implements OnDataSendToActivity {

    private static ImageView Door, Lock;
    private static Button btn_door, btn_lock;
    private int current_image, current_image1, current_tombol, current_tombol1;
    int[] tombol={R.drawable.switch_on,R.drawable.switch_off};
    int[] images={R.drawable.door_close,R.drawable.door_open};
    int[] images1={R.drawable.lock_close,R.drawable.lock_open};
    String url = "http://192.168.43.93/"; //Define your NodeMCU IP Address here Ex: http://192.168.1.4/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        btn_door = (Button) findViewById(R.id.button_door_close);
        btn_lock = (Button) findViewById(R.id.button_lock_close);
        Door = (ImageView) findViewById(R.id.door_close);
        Lock = (ImageView) findViewById(R.id.lock_close);

        btn_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image1++;
                current_image1 = current_image1 % images.length;
                Door.setImageResource(images1[current_image1]);
                String url_rl = url + "in_lamp";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
                current_tombol++;
                current_tombol = current_tombol % images.length;
                btn_door.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,tombol[current_tombol]);
            }
        });

        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image++;
                current_image = current_image % images.length;
                Lock.setImageResource(images[current_image]);
                String url_rl = url + "out_lamp";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
                current_tombol1++;
                current_tombol1 = current_tombol1 % images.length;
                btn_lock.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,tombol[current_tombol1]);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendData(String str) {
        updateButtonStatus(str);
    }

    private void updateStatus(){
        String url_rl = url+"status";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }

    //Function for updating Button Status
    private void updateButtonStatus(String jsonStrings) {
        try {
            JSONObject json = new JSONObject(jsonStrings);

            String out_lamp = json.getString("ol");
            String in_lamp = json.getString("il");

            if (out_lamp.equals("1")) {
                btn_door.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_on);
            } else {
                btn_door.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_off);
            }
            if (in_lamp.equals("1")) {
                btn_lock.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_on);
            } else {
                btn_lock.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_off);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



 /*
    private static ImageView Lamp_in;
    private static ImageView Lamp_out;
    private static Button but_in;
    private static Button but_out;
    private int current_image;
    private int current_image1;
    int[] images={R.drawable.lamp_out_off,R.drawable.lamp_out_on};
    int[] images1={R.drawable.lamp_in_off,R.drawable.lamp_in_on};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);

        buttonclick();
    }
        public void buttonclick()
    {
        Lamp_in = (ImageView) findViewById(R.id.lamp_in);
        Lamp_out = (ImageView) findViewById(R.id.lamp_out);
        but_in = (Button) findViewById(R.id.button_in);
        but_out = (Button) findViewById(R.id.button_out);

        but_in.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    current_image1++;
                    current_image1 = current_image1 % images.length;
                    Lamp_in.setImageResource(images1[current_image1]);
                    }
                }
        );

        but_out.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        current_image++;
                        current_image = current_image % images.length;
                        Lamp_out.setImageResource(images[current_image]);
                    }
                });
    }
}*/
