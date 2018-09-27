package com.deecoders.meribindiya.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.deecoders.meribindiya.listeners.ServiceDataListener;
import com.deecoders.meribindiya.util.LocationHelper;
import com.devspark.appmsg.AppMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Signup extends AppCompatActivity {
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.male)
    ImageView male;
    @BindView(R.id.female)
    ImageView female;
    @BindView(R.id.dob)
    TextView dob;
    @BindView(R.id.dobPanel)
    LinearLayout dobPanel;
    @BindView(R.id.anniversary)
    TextView anniversary;
    @BindView(R.id.anniversaryPanel)
    LinearLayout anniversaryPanel;
    String gender;
    String userLocation = "";

    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mobile.setText(""+getIntent().getStringExtra("mobile"));
        selectGender(1);

        locationHelper = new LocationHelper(this, new ServiceDataListener<String>() {
            @Override
            public void onData(String data) {
                userLocation = data;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.onResume(true);
    }

    public void registerUser(View view) {
        Constants.clickEffect(view);
        if (mobile.getText().toString().isEmpty()) {
            AppMsg.makeText(this, "Enter mobile number!", AppMsg.STYLE_ALERT).show();
        } else if (mobile.getText().toString().length() < 10) {
            AppMsg.makeText(this, "Invalid mobile number!", AppMsg.STYLE_ALERT).show();
        } else if (name.getText().toString().isEmpty()) {
            AppMsg.makeText(this, "Enter name!", AppMsg.STYLE_ALERT).show();
        } else if (email.getText().toString().isEmpty()) {
            AppMsg.makeText(this, "Enter email!", AppMsg.STYLE_ALERT).show();
        } else if (!checkEmail(email.getText().toString())) {
            AppMsg.makeText(this, "Invalid email!", AppMsg.STYLE_ALERT).show();
        } else if (gender == null) {
            AppMsg.makeText(this, "Please select gender!", AppMsg.STYLE_ALERT).show();
        } else {
            sendRequest();
        }
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
            "(" +
            "." +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
            ")+");

    public static boolean checkEmail(String email) {
        if (email.isEmpty())
            return false;
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public void finish(View view) {
        Constants.clickEffect(view);
        finish();
    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("email", email.getText().toString());
            object.put("name", name.getText().toString());
            object.put("mobile", mobile.getText().toString());
            object.put("gender", gender);
            object.put("location", userLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.userRegister, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                JSONObject object = response.getJSONObject("object");
                                String otpCode = object.getString("otp");
                                Intent intent = new Intent(Signup.this, OtpVerification.class);
                                intent.putExtra("code", otpCode);
                                intent.putExtra("mobile", mobile.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                AppMsg.makeText(Signup.this, "" + msg, AppMsg.STYLE_ALERT).show();
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
                Toast.makeText(Signup.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    @OnClick({R.id.male, R.id.female, R.id.dobPanel, R.id.anniversaryPanel})
    public void onViewClicked(View view) {
        Constants.clickEffect(view);
        switch (view.getId()) {
            case R.id.male:
                selectGender(0);
                break;
            case R.id.female:
                selectGender(1);
                break;
            case R.id.dobPanel:
                showDatePicker(dob);
                break;
            case R.id.anniversaryPanel:
                showDatePicker(anniversary);
                break;
        }
    }

    public void showDatePicker(final TextView textView){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textView.setText(sdformat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void selectGender(int i) {
        if(i==0){
            gender = "male";
            male.setAlpha(1f);
            female.setAlpha(0.3f);
        }
        else{
            gender = "female";
            male.setAlpha(0.3f);
            female.setAlpha(1f);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationHelper = null;
    }
}
