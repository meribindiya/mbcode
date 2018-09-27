package com.deecoders.meribindiya.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.deecoders.meribindiya.adapter.ProductAdapter;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ProductModel;
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
import butterknife.Unbinder;

public class SubCategoryFrag extends Fragment {
    @BindView(R.id.listView)
    ListView listView;
    Unbinder unbinder;
    ArrayList<ProductModel> productModels = new ArrayList<>();
    ArrayList<ProductModel> cartProducts = new ArrayList<>();
    ProductAdapter productAdapter;
    String catId, id, title;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.noresult)
    TextView noresult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        catId = getArguments().getString("cat_id");

        Log.e("SubCategoryFrag", "catId: "+catId);

        id = getArguments().getString("id");
        title = getArguments().getString("title");

        TinyDB tinyDB = new TinyDB(getActivity());
        cartProducts = tinyDB.getListObject("products", ProductModel.class);
        getSubCategoryProducts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getSubCategoryProducts() {
        progressBar.setVisibility(View.VISIBLE);
        Log.e("SubCategoryFrag", "cat_id: "+catId+", subcatId: "+id);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.getSubcategory + catId + "/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "" + response.toString());
                        if(progressBar == null)
                            return;
                        progressBar.setVisibility(View.GONE);
                        productModels.clear();
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("object");
                                Type listType = new TypeToken<ArrayList<ProductModel>>() {
                                }.getType();
                                ArrayList<ProductModel> modelsNew = new GsonBuilder().create().fromJson(jsonArray.toString(), listType);
                                productModels.addAll(modelsNew);
                                for (int i = 0; i < productModels.size(); i++) {
                                    productModels.get(i).setSubcatName(title);
                                }
                                updateCount();
                                productAdapter = new ProductAdapter(getActivity(), productModels);
                                listView.setAdapter(productAdapter);
                                if(productModels.size() == 0){
                                    noresult.setVisibility(View.VISIBLE);
                                }
                                else{
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
                if(progressBar == null)
                    return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(req);
    }

    private void updateCount() {
        int i = 0;
        for (ProductModel productModel : productModels) {
            for (ProductModel cartProduct : cartProducts) {
                if (productModel.getId() == cartProduct.getId()) {
                    if (cartProduct.getCount() > 0) {
                        productModels.get(i).setCount(cartProduct.getCount());
                        productModels.get(i).setShowCount(true);
                    }
                }
            }
            i++;
        }
    }
}
