package com.meribindiyaemployee.meribindiyaemployee.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.meribindiyaemployee.meribindiyaemployee.R;
import com.meribindiyaemployee.meribindiyaemployee.utils.Constants;
import com.meribindiyaemployee.meribindiyaemployee.utils.PreferenceHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.email_id)
    EditText email_id;


    @BindView(R.id.mobile)
    EditText mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String name = PreferenceHandler.readString(this, Constants.NAME, "");

        String email = PreferenceHandler.readString(this, Constants.EMAIL, "");

        String mobile = PreferenceHandler.readString(this, Constants.MOBILE, "");

        this.name.setText(name);
        this.email_id.setText(email);
        this.mobile.setText(mobile);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }
}
