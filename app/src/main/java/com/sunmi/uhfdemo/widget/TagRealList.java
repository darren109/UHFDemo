package com.sunmi.uhfdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.sunmi.rfid.constant.ParamCts;
import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.R;
import com.sunmi.uhfdemo.adapter.RealListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TagRealList extends LinearLayout {
    private Context mContext;
    private TableRow mTagRealRow;
    private ImageView mTagRealImage;
    private TextView mListTextInfo;
    private TextView mMinRSSIText, mMaxRSSIText;
    private List<DataParameter> data;
    private RealListAdapter mRealListAdapter;
    private ListView mTagRealList;
    private View mTagsRealListScrollView;
    private TextView mUIDText;

    //show the list height.
    private static final float SECTOR_FACTOR = 3.7f;
    private WindowManager wm;
    private OnItemSelectedListener mOnItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(View arg1, int arg2, long arg3);
    }

    public TagRealList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public TagRealList(Context context) {
        super(context);
        initContext(context);
    }

    @SuppressWarnings("deprecation")
    private void initContext(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.tag_real_list, this);
        data = new ArrayList<>();
        mTagsRealListScrollView = findViewById(R.id.tags_real_list_scroll_view);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutParams lp = (LayoutParams) mTagsRealListScrollView.getLayoutParams();
        lp.height = (int) (wm.getDefaultDisplay().getHeight() / SECTOR_FACTOR); //2.7
        mTagsRealListScrollView.setLayoutParams(lp);
        mTagsRealListScrollView.invalidate();
        mUIDText = findViewById(R.id.uid_text);

        mTagRealRow = findViewById(R.id.table_row_tag_real);
        mTagRealImage = findViewById(R.id.image_prompt);
        mTagRealImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
        mListTextInfo = findViewById(R.id.list_text_info);
        mListTextInfo.setText(getResources().getString(R.string.open_tag_list));

        mMinRSSIText = findViewById(R.id.min_rssi_text);
        mMaxRSSIText = findViewById(R.id.max_rssi_text);

        mTagRealRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutParams lp = (LayoutParams) mTagsRealListScrollView.getLayoutParams();

                if (lp.height <= wm.getDefaultDisplay().getHeight() / 2) {
                    lp.height = (int) (wm.getDefaultDisplay().getHeight() / 1.5);
                    mTagsRealListScrollView.setLayoutParams(lp);
                    mTagsRealListScrollView.invalidate();

                    mTagRealImage.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mListTextInfo.setText(getResources().getString(R.string.close_tag_list));
                } else {
                    lp.height = (int) (wm.getDefaultDisplay().getHeight() / SECTOR_FACTOR);
                    mTagsRealListScrollView.setLayoutParams(lp);
                    mTagsRealListScrollView.invalidate();

                    mTagRealImage.setImageDrawable(getResources().getDrawable(
                            R.drawable.up));
                    mListTextInfo.setText(getResources().getString(
                            R.string.open_tag_list));
                }
            }
        });

        mTagRealList = (ListView) findViewById(R.id.tag_real_list_view);
        mRealListAdapter = new RealListAdapter(mContext, data);
        mTagRealList.setAdapter(mRealListAdapter);

        mTagRealList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(arg1, arg2, arg3);
                }
            }

        });
    }

    public void setOnItemSelectedListener(
            OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public final void clearText() {
        mMinRSSIText.setText("0dBm");
        mMaxRSSIText.setText("0dBm");
    }

    public final void refreshText(int nMinRSSI, int nMaxRSSI) {
        if (nMinRSSI == 0 && nMaxRSSI == 0) {
            mMinRSSIText.setText("0dBm");
            mMaxRSSIText.setText("0dBm");
        } else {
            mMinRSSIText.setText((nMinRSSI - 129) + "dBm");
            mMaxRSSIText.setText((nMaxRSSI - 129) + "dBm");
        }
    }

    public final void refreshList(List<DataParameter> data) {
        this.data.clear();
        this.data.addAll(data);
        mRealListAdapter.notifyDataSetChanged();
        invaildate();
    }

    private void invaildate() {
        if (mUIDText != null) {
            mUIDText.setWidth(mRealListAdapter.mWidthest);
            Log.e("change the width", mUIDText.getWidth() + "::::::::::::::");
            mUIDText.invalidate();
        }
    }

    private int lengthestData() {
        int widest = 0;
        for (DataParameter itm : data) {
            if (widest < itm.getString(ParamCts.TAG_EPC, "").length()) {
                widest = itm.getString(ParamCts.TAG_EPC, "").length();
            }
        }

        return widest * 16;
    }
}
