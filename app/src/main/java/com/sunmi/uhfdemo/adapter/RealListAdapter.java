package com.sunmi.uhfdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunmi.rfid.constant.ParamCts;
import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.R;
import com.sunmi.uhfdemo.R.id;

import java.util.List;

public class RealListAdapter extends BaseAdapter {
    public static int mWidthest = 0;

    private LayoutInflater mInflater;

    private Context mContext;

    private List<DataParameter> listMap;

    public final class ListItemView {
        public TextView mIdText;
        public TextView mEpcText;
        public TextView mPcText;
        public TextView mTimesText;
        public TextView mRssiText;
        public TextView mFreqText;
    }

    public RealListAdapter(Context context, List<DataParameter> listMap) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.listMap = listMap;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listMap.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = mInflater.inflate(R.layout.tag_real_list_item, null);
            listItemView.mIdText = (TextView) convertView.findViewById(id.id_text);
            listItemView.mEpcText = (TextView) convertView.findViewById(id.epc_text);
            listItemView.mPcText = (TextView) convertView.findViewById(id.pc_text);
            listItemView.mTimesText = (TextView) convertView.findViewById(id.times_text);
            listItemView.mRssiText = (TextView) convertView.findViewById(id.rssi_text);
            listItemView.mFreqText = (TextView) convertView.findViewById(id.freq_text);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        DataParameter map = listMap.get(position);

        listItemView.mIdText.setText(String.valueOf(position + 1));
        listItemView.mEpcText.setText(map.getString(ParamCts.TAG_EPC, ""));
        listItemView.mPcText.setText(map.getString(ParamCts.TAG_PC, ""));
        listItemView.mTimesText.setText(String.valueOf(map.getInt(ParamCts.TAG_READ_COUNT)));
        try {
            listItemView.mRssiText.setText((Integer.parseInt(map.getString(ParamCts.TAG_RSSI, "129")) - 129) + "dBm");
        } catch (Exception e) {
            listItemView.mRssiText.setText("");
        }
        listItemView.mFreqText.setText(map.getString(ParamCts.TAG_FREQ, ""));

        return convertView;

    }

    /**
     * get lengthest data in listMap
     *
     * @return the max show area
     */
    public int lengthestData() {
        int length = 0;
        for (DataParameter itm : listMap) {
            if (length < itm.getString(ParamCts.TAG_EPC, "").length()) {
                length = itm.getString(ParamCts.TAG_EPC, "").length();
            }
        }
        return length * 16;
    }
}
