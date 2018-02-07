/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.vallyse.test.volvo.adaptater;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vallyse.test.volvo.R;

import java.util.List;

/**
 * Created by princejackes on 28/01/2018.
 */

public class travel_pager extends PagerAdapter {

    List<Integer> dataList;

    @Override
    public int getCount() {
        return dataList.size();
    }

    public travel_pager(List<Integer> dataList) {
        this.dataList = dataList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view;

        if (dataList.get(position) == 0) {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.try_volvo_berline, container, false);
        } else {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.try_volvo_suv, container, false);
        }


        container.addView(view);
        return view;
    }
}
