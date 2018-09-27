package com.deecoders.meribindiya.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.deecoders.meribindiya.adapter.EarningAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.EarningModel;
import com.deecoders.meribindiya.model.UserModel;
import com.deecoders.meribindiya.util.MyPref;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    RelativeLayout titleBar;

    @BindView(R.id.myEarningList)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private LinearLayoutManager mLayoutManager;

    private EarningAdapter mEarningAdapter;
    ArrayList<EarningModel> models = new ArrayList<>();

    public static final String IS_TOTAL_EARNING = "IS_TOTAL_EARNING";
    boolean isTotalEarningScreen = false;

    @BindView(R.id.noresult)
    TextView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(getIntent().getExtras() != null){
            isTotalEarningScreen = getIntent().getBooleanExtra(IS_TOTAL_EARNING, false);
        }

        initViews();
        getEarningDetailsList();


    }


    private void initViews() {
        if(isTotalEarningScreen) {
            titleTxt.setText(getResources().getText(R.string.my_earning_details));
        } else{
            titleTxt.setText(getResources().getText(R.string.my_missed_earning_details));
        }
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    private void getEarningDetailsList() {
        progressBar.setVisibility(View.VISIBLE);
        String url = isTotalEarningScreen ? "0" : "1";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getEarningDetails + "/" + MyPref.getId(this) + "/" + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        if (progressBar == null)
                            return;
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<EarningModel>>() {}.getType();
                                ArrayList<EarningModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                models.addAll(modelsNew);
                                mEarningAdapter = new EarningAdapter(EarningActivity.this, models);
                                mRecyclerView.setAdapter(mEarningAdapter);
                                if(models.size() == 0){
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
                if (progressBar == null)
                    return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EarningActivity.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(EarningActivity.this).add(req);
    }


}