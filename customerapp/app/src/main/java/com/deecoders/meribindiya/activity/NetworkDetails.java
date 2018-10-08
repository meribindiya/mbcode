package com.deecoders.meribindiya.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.MyPref;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkDetails extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;

    @BindView(R.id.viewSharedContactBtn)
    Button viewSharedContactBtn;
    @BindView(R.id.sharePointsTV)
    TextView sharePointsTV;

    @BindView(R.id.viewTotalPointsBtn)
    Button viewTotalPointsBtn;
    @BindView(R.id.totalPointsTV)
    TextView totalPointsTV;

    @BindView(R.id.viewNetworkBtn)
    Button viewNetworkBtn;
    @BindView(R.id.activeNetworkMemberTV)
    TextView activeNetworkMemberTV;

    @BindView(R.id.viewMissedAmountBtn)
    Button viewMissedAmountBtn;
    @BindView(R.id.missedPointsTV)
    TextView missedPointsTV;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_details);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        viewSharedContactBtn.setOnClickListener(this);
        viewTotalPointsBtn.setOnClickListener(this);
        viewNetworkBtn.setOnClickListener(this);
        viewMissedAmountBtn.setOnClickListener(this);

        this.getNetworkDetails ();
    }

    private void initViews(JSONObject network) {
        try {
            sharePointsTV.setText(String.valueOf(network.getInt("referContactsNo")));
            totalPointsTV.setText("\u20B9 " + String.valueOf(network.getDouble("totalEarnings")));
            activeNetworkMemberTV.setText(String.valueOf(network.getInt("activeMembersNo")));
            missedPointsTV.setText("\u20B9 " + String.valueOf(network.getDouble("missedEarning")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }


    @Override
    public void onClick(View view) {
        Constants.clickEffect(view);
        switch(view.getId()) {
            case R.id.viewSharedContactBtn: {
                Intent intent = new Intent(NetworkDetails.this, MySharedContact.class);
                startActivity(intent);
            }
            break;
            case R.id.viewTotalPointsBtn: {
                Intent intent = new Intent(NetworkDetails.this, EarningActivity.class);
                intent.putExtra(EarningActivity.IS_TOTAL_EARNING, true);
                startActivity(intent);
            }
            break;
            case R.id.viewNetworkBtn: {
                Intent intent = new Intent(NetworkDetails.this, MyActiveMembers.class);
                startActivity(intent);
            }
            break;
            case R.id.viewMissedAmountBtn: {
                Intent intent = new Intent(NetworkDetails.this, EarningActivity.class);
                intent.putExtra(EarningActivity.IS_TOTAL_EARNING, false);
                startActivity(intent);
            }
            break;
        }
    }

    private void getNetworkDetails () {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        Log.e("tag", "getting wallet info ");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getNetworkDetails + MyPref.getId(this), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("wallet info", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                initViews(response.getJSONObject("object"));
                            } else {
                                Toast.makeText(NetworkDetails.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(NetworkDetails.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }
}
