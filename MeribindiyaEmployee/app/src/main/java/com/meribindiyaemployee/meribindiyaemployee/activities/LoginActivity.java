package com.meribindiyaemployee.meribindiyaemployee.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meribindiyaemployee.meribindiyaemployee.HomeActivity;

import com.meribindiyaemployee.meribindiyaemployee.R;
import com.meribindiyaemployee.meribindiyaemployee.responses.Beautician;
import com.meribindiyaemployee.meribindiyaemployee.responses.CommonResponse;
import com.meribindiyaemployee.meribindiyaemployee.services.ServiceBuilder;
import com.meribindiyaemployee.meribindiyaemployee.services.Services;
import com.meribindiyaemployee.meribindiyaemployee.utils.Constants;
import com.meribindiyaemployee.meribindiyaemployee.utils.PreferenceHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private static final Logger LOGGER = Logger.getLogger("LoginActivity.class");

    private static final String FAILED = "failed";

    @BindView(R.id.username)
    EditText usernameEditText;

    @BindView(R.id.password)
    EditText passwordEditText;

    @BindView((R.id.progressBar))
    ProgressBar progressBar;

    boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);

        boolean isUserLogin = PreferenceHandler.readBoolean(this, Constants.ISUSERLOGIN, false);
        if (isUserLogin) {
            Integer id = PreferenceHandler.readInteger(this, Constants.USERID, 0);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    @OnClick(R.id.loginbutton)
    public void clickOnLoginButton(View view) {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if ("".equalsIgnoreCase(username.trim()) || "".equalsIgnoreCase(password.trim())) {
            Toast.makeText(LoginActivity.this, "Username and Password not empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        Services services = ServiceBuilder.buildService(Services.class);

        Map<String, String> filter = new HashMap<>();
        filter.put("mobile", username);
        filter.put("password", password);

        retrofit2.Call<CommonResponse> call = services.getLogin(filter);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CommonResponse> call, Response<CommonResponse> response) {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                CommonResponse commonResponse = response.body();
                if (FAILED.equalsIgnoreCase(commonResponse.getStatus())) {
                    Toast.makeText(LoginActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String jsonContentResponse = gson.toJson(commonResponse.getObject());
                    Beautician beautician = gson.fromJson(jsonContentResponse, Beautician.class);
                    PreferenceHandler.writeString(LoginActivity.this, Constants.USERNAME, beautician.getName());
                    PreferenceHandler.writeBoolean(LoginActivity.this, Constants.ISUSERLOGIN, true);
                    PreferenceHandler.writeInteger(LoginActivity.this, Constants.USERID, beautician.getId());
                    PreferenceHandler.writeString(LoginActivity.this, Constants.NAME, beautician.getName());
                    PreferenceHandler.writeString(LoginActivity.this, Constants.MOBILE, beautician.getMobile() + "");
                    PreferenceHandler.writeString(LoginActivity.this, Constants.EMAIL, beautician.getEmail());
                    LOGGER.info(" Beautician beautician " + beautician);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("id", beautician.getId());
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Something going wrong. Please try after sometime.", Toast.LENGTH_LONG).show();

            }
        });


    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            System.exit(0);

        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
