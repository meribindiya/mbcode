package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.deecoders.meribindiya.fragment.SubCategoryFrag;
import com.deecoders.meribindiya.model.SubCategoryModel;

import java.util.ArrayList;

public class CategoryPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    ArrayList<SubCategoryModel> subCategoryModels;
    String catId;

    public CategoryPagerAdapter(Context context, FragmentManager fm, String catId, ArrayList<SubCategoryModel> models) {
        super(fm);
        mContext = context;
        this.subCategoryModels = models;
        this.catId = catId;
        Log.e("CategoryPagerAdapter", "catId: "+catId);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("title", subCategoryModels.get(position).getName());
        bundle.putString("id", ""+subCategoryModels.get(position).getId());
        bundle.putString("cat_id", ""+catId);
        SubCategoryFrag frag = new SubCategoryFrag();
        frag.setArguments(bundle);
        return frag;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return subCategoryModels.size();
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        return subCategoryModels.get(position).getName();
    }
}
