package com.example.luo_pc.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.City;
import com.example.luo_pc.news.bean.County;
import com.example.luo_pc.news.bean.Province;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/6/23.
 */
public class LocationAdapter extends BaseAdapter {
    ArrayList<Province> provinces = null;
    ArrayList<City> cities = null;
    ArrayList<County> counties = null;
    LayoutInflater inflater;

    public LocationAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public void setCounties(ArrayList<County> counties) {
        this.counties = counties;
    }


    @Override
    public int getCount() {
        if (provinces != null)
            return provinces.size();

        else if (cities != null)
            return cities.size();

        else
            return counties.size();

    }

    @Override
    public Object getItem(int position) {
        if (provinces != null)
            return provinces.get(position);

        else if (cities != null)
            return cities.get(position);

        else
            return counties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_location_item, parent, false);
            holder = new ViewHolder();
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (provinces != null)
            holder.tv_location.setText(provinces.get(position).getProvinceName());

        else if (cities != null)
            holder.tv_location.setText(cities.get(position).getCityName());

        else if (counties != null)
            holder.tv_location.setText(counties.get(position).getCountyName());


        return convertView;
    }

    private class ViewHolder {
        public TextView tv_location;
    }
}
