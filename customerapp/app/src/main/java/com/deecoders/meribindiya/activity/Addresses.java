package com.deecoders.meribindiya.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.adapter.AddressAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.AddressModel;
import com.deecoders.meribindiya.util.MyPref;
import com.deecoders.meribindiya.view.ExpandableHeightGridView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Addresses extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.listView)
    ExpandableHeightGridView listView;
    @BindView(R.id.add)
    ViewGroup add;
    ArrayList<AddressModel> models = new ArrayList<>();
    AddressAdapter addressAdapter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noresult)
    TextView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        registerReceiver(ShowProgress, new IntentFilter("showProgress"));
        registerReceiver(HideProgress, new IntentFilter("hideProgress"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ShowProgress);
        unregisterReceiver(HideProgress);
    }

    private BroadcastReceiver ShowProgress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag", "onReceive");
            progressBar.setVisibility(View.VISIBLE);
        }
    };

    private BroadcastReceiver HideProgress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag", "onReceive");
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        models.clear();
        getAddresses();
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    public void addAddress(View view) {
        Constants.clickEffect(view);
        Intent intent = new Intent(this, AddAddress.class);
        startActivity(intent);
    }

    private void getAddresses() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.allAddresses + MyPref.getId(this), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<AddressModel>>() {}.getType();
                                ArrayList<AddressModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                models.addAll(modelsNew);
                                addressAdapter = new AddressAdapter(Addresses.this, models);
                                listView.setAdapter(addressAdapter);
                                listView.setExpanded(true);
                                if (models.size() == 0) {
                                    noresult.setVisibility(View.VISIBLE);
                                } else {
                                    noresult.setVisibility(View.GONE);
                                }
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
                Toast.makeText(Addresses.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(Addresses.this).add(req);
    }

    public void addressSelected(AddressModel selectedModel) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("address_id",selectedModel.getId());
        returnIntent.putExtra("address",selectedModel.getAddress());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
