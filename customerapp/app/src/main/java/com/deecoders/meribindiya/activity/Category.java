package com.deecoders.meribindiya.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.deecoders.meribindiya.adapter.CategoryPagerAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.CategoryModel;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.model.SubCategoryModel;
import com.deecoders.meribindiya.util.TinyDB;
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

public class Category extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.arrowDown)
    ImageView arrowDown;
    @BindView(R.id.titlePanel)
    LinearLayout titlePanel;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewCart)
    LinearLayout viewCart;
    @BindView(R.id.cartPanel)
    LinearLayout cartPanel;
    CategoryPagerAdapter pagerAdapter;
    ArrayList<SubCategoryModel> models = new ArrayList<>();
    ArrayList<CategoryModel> allCategoryModels = new ArrayList<>();
    CategoryModel categoryModel;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.cart)
    ImageView cart;
    @BindView(R.id.arrowRight)
    ImageView arrowRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        DrawableCompat.setTint(arrowDown.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(cart.getDrawable(), ContextCompat.getColor(this, R.color.white));
        DrawableCompat.setTint(arrowRight.getDrawable(), ContextCompat.getColor(this, R.color.white));

        categoryModel = (CategoryModel) getIntent().getSerializableExtra("model");
        allCategoryModels = (ArrayList<CategoryModel>) getIntent().getSerializableExtra("all_models");
        titleTxt.setText(categoryModel.getName());
        titlePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tempArr = new String[allCategoryModels.size()];
                for (int i = 0; i < allCategoryModels.size(); i++) {
                    tempArr[i] = allCategoryModels.get(i).getName();
                }
                showDialog(tempArr);
            }
        });

        registerReceiver(AddToCartReceiver, new IntentFilter("cart"));

        models.clear();
        getSubCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCartData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(AddToCartReceiver);
    }

    private BroadcastReceiver AddToCartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag", "onReceive");
            showCartData();
        }
    };

    private void showCartData() {
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        int itemCount = 0, priceCount = 0;
        for (ProductModel model : models) {
            itemCount = itemCount + model.getCount();
            priceCount = priceCount + (model.getCount() * model.getPrice());
        }
        count.setText("" + itemCount);
        price.setText(priceCount + " INR");
        if (itemCount == 0) {
            count.setVisibility(View.GONE);
        } else {
            count.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(String[] options) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Category");
        builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryModel = allCategoryModels.get(which);
                Intent intent = new Intent(Category.this, Category.class);
                intent.putExtra("model", categoryModel);
                intent.putExtra("all_models", allCategoryModels);
                startActivity(intent);
                finish();
                dialog.dismiss();
                clearCart();
            }
        });
        builder.create();
        builder.show();
    }

    @OnClick({R.id.back, R.id.viewCart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Constants.clickEffect(view);
                if (!isCartEmpty()) {
                    showAlert(this);
                } else {
                    finish();
                }
                break;
            case R.id.viewCart:
                viewCart(view);
                break;
        }
    }

    public void viewCart(View view) {
        Constants.clickEffect(view);
        Intent intent = new Intent(this, Cart.class);
        startActivity(intent);
    }

    private void getSubCategories() {
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getSubcategories + categoryModel.getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        progressBar.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<SubCategoryModel>>() {}.getType();
                                ArrayList<SubCategoryModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                models.addAll(modelsNew);
                                pagerAdapter = new CategoryPagerAdapter(Category.this, getSupportFragmentManager(), "" + categoryModel.getId(), models);
                                viewpager.setAdapter(pagerAdapter);
                                slidingTabs.setupWithViewPager(viewpager);
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
                Toast.makeText(Category.this, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Going back will clear your cart!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearCart();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clearCart() {
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putListObject("products", new ArrayList<ProductModel>());
    }

    private boolean isCartEmpty() {
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        int itemCount = 0;
        for (ProductModel model : models) {
            itemCount = itemCount + model.getCount();
        }
        if (itemCount == 0)
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        if (!isCartEmpty()) {
            showAlert(this);
        } else {
            finish();
        }
    }
}
