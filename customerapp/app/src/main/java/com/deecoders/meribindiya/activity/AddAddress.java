package com.deecoders.meribindiya.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.MyPref;
import com.devspark.appmsg.AppMsg;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddress extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.bname)
    EditText bname;
    @BindView(R.id.landmark)
    EditText landmark;
    @BindView(R.id.spinnerType)
    BetterSpinner spinnerType;
    @BindView(R.id.gpsImg)
    ImageView gpsImg;
    @BindView(R.id.gps)
    RelativeLayout gps;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(gpsImg.getDrawable(), ContextCompat.getColor(this, R.color.welcome));
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                Intent intent = new Intent(AddAddress.this, SelectAddress.class);
                startActivityForResult(intent, 0);
            }
        });

        String[] tempArr = new String[]{"Work", "Home"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tempArr);
        spinnerType.setAdapter(adapter);
    }

    public void save(View view) {
        Constants.clickEffect(view);
        if(bname.getText().toString().isEmpty()){
            AppMsg.makeText(this, "Enter building name!", AppMsg.STYLE_ALERT).show();
        }
        else if(landmark.getText().toString().isEmpty()){
            AppMsg.makeText(this, "Enter landmark!", AppMsg.STYLE_ALERT).show();
        }
        else if(spinnerType.getText().toString().isEmpty()){
            AppMsg.makeText(this, "Select address type!", AppMsg.STYLE_ALERT).show();
        }
        else if(address.getText().toString().isEmpty()){
            AppMsg.makeText(this, "Select address!", AppMsg.STYLE_ALERT).show();
        }
        else{
            sendRequest();
        }
    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("userid", MyPref.getId(this));
            object.put("address", address.getText().toString());
            object.put("addressType", spinnerType.getText().toString());
            object.put("buildingName", bname.getText().toString());
            object.put("landmark", landmark.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("tag", "sending: "+object.toString());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.addAddress, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", ""+response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                Toast.makeText(AddAddress.this, "Address added!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                AppMsg.makeText(AddAddress.this, ""+msg, AppMsg.STYLE_ALERT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddAddress.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String address_ = data.getStringExtra("address");
                Log.e("tag", "address: " + address_);
                address.setText(address_);
            }
        }
    }
}
