package com.sunmi.uhfdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.sunmi.rfid.constant.ParamCts;
import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.R;
import com.sunmi.uhfdemo.adapter.AccessListAdapter;

import java.util.ArrayList;
import java.util.List;


public class TagAccessList extends LinearLayout {
    private Context mContext;
    private TableRow mTagAccessRow;
    private ImageView mTagAccessImage;
    private TextView mListTextInfo;


    private List<DataParameter> data;
    private AccessListAdapter mAccessListAdapter;
    private ListView mTagAccessList;

    private View mTagsAccessListScrollView;
    private WindowManager wm;

    public TagAccessList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public TagAccessList(Context context) {
        super(context);
        initContext(context);
    }

    private void initContext(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.tag_access_list, this);

        data = new ArrayList<>();

        mTagsAccessListScrollView = findViewById(R.id.tags_access_list_scroll_view);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutParams lp = (LayoutParams) mTagsAccessListScrollView.getLayoutParams();
        lp.height = 0;
        mTagsAccessListScrollView.setLayoutParams(lp);
        mTagsAccessListScrollView.invalidate();

        mTagAccessRow = (TableRow) findViewById(R.id.table_row_tag_access);
        mTagAccessImage = (ImageView) findViewById(R.id.image_prompt);
        mTagAccessImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
        mListTextInfo = (TextView) findViewById(R.id.list_text_info);
        mListTextInfo.setText(getResources().getString(R.string.open_tag_list));

        mTagAccessRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutParams lp = (LayoutParams) mTagsAccessListScrollView.getLayoutParams();
                if (lp.height <= 0) {
                    lp.height = wm.getDefaultDisplay().getHeight() / 3;
                    mTagsAccessListScrollView.setLayoutParams(lp);
                    mTagAccessImage.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mListTextInfo.setText(getResources().getString(R.string.close_tag_list));
                } else {
                    lp.height = 0;
                    mTagsAccessListScrollView.setLayoutParams(lp);
                    mTagAccessImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
                    mListTextInfo.setText(getResources().getString(R.string.open_tag_list));
                }
                requestLayout();
            }
        });

        mTagAccessList = (ListView) findViewById(R.id.tag_real_list_view);
        mAccessListAdapter = new AccessListAdapter(mContext, data);
        mTagAccessList.setAdapter(mAccessListAdapter);
    }

    public final void clearText() {}

    public final void refreshText() {}

    public final void refreshList(List<DataParameter> data) {
        this.data.clear();
        this.data.addAll(data);
        mAccessListAdapter.notifyDataSetChanged();
    }

    private int lengthestData(String str) {
        int widest = 0;
        if ("epc".equals(str)) {
            for (DataParameter otm : data) {
                if (widest < otm.getString(ParamCts.TAG_EPC).length()) {
                    widest = otm.getString(ParamCts.TAG_EPC).length();
                }
            }
        }
        if ("data".equals(str)) {
            for (DataParameter otm : data) {
                if (widest < otm.getString(ParamCts.TAG_EPC).length()) {
                    widest = otm.getString(ParamCts.TAG_EPC).length();
                }
            }
        }
        return widest * 16;
    }
}
