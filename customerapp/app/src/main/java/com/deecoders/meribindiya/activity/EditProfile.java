package com.deecoders.meribindiya.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.deecoders.meribindiya.util.MyPref;
import com.devspark.appmsg.AppMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfile extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));

        mobile.setText(""+MyPref.getMobile(this));
        name.setText(MyPref.getName(this));
        email.setText(MyPref.getEmail(this));
        if (MyPref.getGender(this).equalsIgnoreCase("male")) {
            selectGender(0);
        } else {
            selectGender(1);
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

    public void updateProfile(View view) {
        Constants.clickEffect(view);
        if (!checkEmail(email.getText().toString())) {
            AppMsg.makeText(this, "Invalid email!", AppMsg.STYLE_ALERT).show();
            return;
        }
        sendRequest();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Constants.userUpdate, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("tag", "" + response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");
                            if (status.equals("success")) {
                                Toast.makeText(EditProfile.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                MyPref.setEmail(EditProfile.this, email.getText().toString());
                                MyPref.setGender(EditProfile.this, gender);
                                MyPref.setName(EditProfile.this, name.getText().toString());
                                finish();
                            } else {
                                AppMsg.makeText(EditProfile.this, "" + msg, AppMsg.STYLE_ALERT).show();
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
                Toast.makeText(EditProfile.this, "Network Problem!", Toast.LENGTH_SHORT).show();
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
}
