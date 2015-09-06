package com.example.yangyongxin.app7.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yangyongxin.app7.R;
import com.example.yangyongxin.app7.bean.Person;

import java.util.List;

public class HaoHanAdapter extends BaseAdapter {

    private Context mContext;
    private List<Person> persons;

    public HaoHanAdapter(Context mContext, List<Person> persons) {
        this.mContext = mContext;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            view = view.inflate(mContext, R.layout.item_list, null);
        }
        ViewHolder mViewHolder = ViewHolder.getHolder(view);

        Person p = persons.get(position);
        String str = null;
        String currentLetter = p.getPinyin().charAt(0) + "";
        // 根据上一个首字母,决定当前是否显示字母
        if (position == 0) {
            str = currentLetter;
        } else {
            // 上一个人的拼音的首字母
            String preLetter = persons.get(position - 1).getPinyin().charAt(0) + "";
            if (!TextUtils.equals(preLetter, currentLetter)) {
                str = currentLetter;
            }
        }
        // 根据str是否为空,决定是否显示索引栏
        mViewHolder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
        mViewHolder.mIndex.setText(currentLetter);
        mViewHolder.mName.setText(p.getName());

        return view;
    }

    static class ViewHolder {
        TextView mIndex;
        TextView mName;

        public static ViewHolder getHolder(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.mIndex = (TextView) view.findViewById(R.id.tv_index);
                viewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            }
            return viewHolder;
        }

    }

}
