package com.example.hello.futurehome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class FanActivity extends AppCompatActivity implements OnDataSendToActivity {

    private static ImageView fan_image;
    private static Button btn_fan;
    private int current_image, current_tombol;
    int[] tombol={R.drawable.switch_on,R.drawable.switch_off};
    int[] images={R.drawable.fan_out,R.drawable.fan_in};
    String url = "http://192.168.43.93/"; //Define your NodeMCU IP Address here Ex: http://192.168.1.4/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);

        btn_fan = (Button) findViewById(R.id.button_fan);
        fan_image = (ImageView) findViewById(R.id.fan);

        btn_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image++;
                current_image = current_image % images.length;
                fan_image.setImageResource(images[current_image]);
                String url_rl = url + "in_lamp";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
                current_tombol++;
                current_tombol = current_tombol % images.length;
                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,tombol[current_tombol]);
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

            String fan = json.getString("Fan");

            if (fan.equals("1")) {
                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_on);
            } else {
                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.switch_off);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}