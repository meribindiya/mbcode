package com.meribindiyaemployee.meribindiyaemployee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meribindiyaemployee.meribindiyaemployee.activities.LoginActivity;
import com.meribindiyaemployee.meribindiyaemployee.activities.LogoutActivity;
import com.meribindiyaemployee.meribindiyaemployee.activities.UserActivity;
import com.meribindiyaemployee.meribindiyaemployee.adaptors.JobAdaptor;
import com.meribindiyaemployee.meribindiyaemployee.responses.Beautician;
import com.meribindiyaemployee.meribindiyaemployee.responses.CommonResponse;
import com.meribindiyaemployee.meribindiyaemployee.responses.CustomerOrder;
import com.meribindiyaemployee.meribindiyaemployee.services.ServiceBuilder;
import com.meribindiyaemployee.meribindiyaemployee.services.Services;
import com.meribindiyaemployee.meribindiyaemployee.utils.Constants;
import com.meribindiyaemployee.meribindiyaemployee.utils.PreferenceHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final Logger LOGGER = Logger.getLogger("LoginActivity.class");

    private static final String FAILED = "failed";

    private static final String SUCCESS = "success";

    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Integer id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String username = PreferenceHandler.readString(this, Constants.USERNAME, "Welcome");
        LOGGER.info("Login Username :: " + username);
        //navheader.setText(username);
        id = getIntent().getIntExtra("id", 0);


        startActivity();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity();
        } else if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void startActivity() {

        progressBar.setVisibility(View.VISIBLE);

        LOGGER.info("Id  :: " + id);
        Services services = ServiceBuilder.buildService(Services.class);
        Call<CommonResponse> call = services.getAllOrdersByBeauticianId(id);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if (FAILED.equalsIgnoreCase(commonResponse.getStatus())) {
                    Toast.makeText(HomeActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String jsonContentResponse = gson.toJson(commonResponse.getObject());

                    Type listType = new TypeToken<List<CustomerOrder>>() {
                    }.getType();

                    List<CustomerOrder> customerOrders = gson.fromJson(jsonContentResponse, listType);
                    LOGGER.info("Customer Orders " + customerOrders);

                    RecyclerView recyclerView = findViewById(R.id.job_list);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    JobAdaptor jobAdaptor = new JobAdaptor(customerOrders, HomeActivity.this);
                    recyclerView.setAdapter(jobAdaptor);
                    jobAdaptor.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        LOGGER.info("Resume");
    }
}
