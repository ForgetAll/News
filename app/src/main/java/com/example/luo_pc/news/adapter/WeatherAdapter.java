package com.example.luo_pc.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.WeatherBean;
import com.example.luo_pc.news.utils.WeatherImageUtils;


/**
 * Created by luo-pc on 2016/6/22.
 */
public class WeatherAdapter extends BaseAdapter {
    String[] weather;
    LayoutInflater inflater;

    public WeatherAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setWeather(WeatherBean wb) {
        this.weather = wb.getWeather();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (weather == null) {
            return 0;
        }

        return weather.length;
    }

    @Override
    public Object getItem(int position) {
        return weather[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_weather_item, parent, false);
            holder = new ViewHolder();
            holder.iv_weather = (ImageView) convertView.findViewById(R.id.iv_weather);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_weather = (TextView) convertView.findViewById(R.id.tv_weather);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//            holder.iv_weather
        if (position == 0) {
            holder.tv_date.setText("今天");
        } else if (position == 1) {
            holder.tv_date.setText("明天");
        } else {
            holder.tv_date.setText("后天");
        }
        holder.tv_weather.setText(weather[position]);
        WeatherImageUtils.setImage(holder.iv_weather, holder.tv_weather);
        return convertView;
    }

    private class ViewHolder {
        public ImageView iv_weather;
        public TextView tv_date;
        public TextView tv_weather;
    }

}
