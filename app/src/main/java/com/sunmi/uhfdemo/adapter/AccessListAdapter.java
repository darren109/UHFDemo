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

import java.util.List;

public class AccessListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DataParameter> listMap;

    public final class ListItemView {
        public TextView mIdText;
        public TextView mPCText;
        public TextView mCRCText;
        public TextView mEpcText;
        public TextView mDataText;
        public TextView mDataLenText;
        public TextView mAntennaText;
        public TextView mTimesText;
    }

    public AccessListAdapter(Context context, List<DataParameter> listMap) {
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
            convertView = mInflater.inflate(R.layout.tag_access_list_item, null);
            listItemView.mIdText = (TextView) convertView.findViewById(R.id.id_text);
            listItemView.mPCText = (TextView) convertView.findViewById(R.id.pc_text);
            listItemView.mCRCText = (TextView) convertView.findViewById(R.id.crc_text);
            listItemView.mEpcText = (TextView) convertView.findViewById(R.id.epc_text);
            listItemView.mDataText = (TextView) convertView.findViewById(R.id.data_text);
            listItemView.mDataLenText = (TextView) convertView.findViewById(R.id.data_len_text);
            listItemView.mAntennaText = (TextView) convertView.findViewById(R.id.antenna_text);
            listItemView.mTimesText = (TextView) convertView.findViewById(R.id.times_text);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        DataParameter tag = listMap.get(position);

        listItemView.mIdText.setText(String.valueOf(position + 1));
        listItemView.mPCText.setText(tag.getString(ParamCts.TAG_PC, ""));
        listItemView.mCRCText.setText(tag.getString(ParamCts.TAG_CRC, ""));
        listItemView.mEpcText.setText(tag.getString(ParamCts.TAG_EPC, ""));
        listItemView.mDataText.setText(tag.getString(ParamCts.TAG_DATA, ""));
        listItemView.mDataLenText.setText(String.valueOf(tag.getInt(ParamCts.TAG_DATA_LEN, 0)));
        listItemView.mAntennaText.setText(String.valueOf(tag.getByte(ParamCts.ANT_ID, (byte) 0x00) & 0xFF));
        listItemView.mTimesText.setText(String.valueOf(tag.getInt(ParamCts.TAG_READ_COUNT, 0) & 0xFF));

        return convertView;
    }

    /**
     * get lengthest data in listMap
     *
     * @return the max show area
     */
    private int lengthestEPC() {
        int length = 0;
        for (DataParameter itm : listMap) {
            if (length < itm.getString(ParamCts.TAG_EPC, "").length()) {
                length = itm.getString(ParamCts.TAG_EPC, "").length();
            }
        }
        return length * 16;
    }

    private int lengthestData() {
        int length = 0;
        for (DataParameter itm : listMap) {
            if (length < itm.getString(ParamCts.TAG_EPC, "").length()) {
                length = itm.getString(ParamCts.TAG_EPC, "").length();
            }
        }
        return length * 16;
    }
}
