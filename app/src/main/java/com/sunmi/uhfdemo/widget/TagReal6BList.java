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

import com.sunmi.rfid.entity.DataParameter;
import com.sunmi.uhfdemo.R;
import com.sunmi.uhfdemo.adapter.Real6BListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TagReal6BList extends LinearLayout {
    private Context mContext;
    private TableRow mTagReal6BRow;
    private ImageView mTagReal6BImage;
    private TextView mListTextInfo;
    private TextView mTagsCountText;
    private List<DataParameter> data;
    private Real6BListAdapter mReal6BListAdapter;
    private ListView mTagReal6BList;
    private View mTagsReal6BListScrollView;
    private WindowManager wm;
    private OnItemSelectedListener mOnItemSelectedListener;

    public interface OnItemSelectedListener {
        public void onItemSelected(View arg1, int arg2,
                                   long arg3);
    }

    public TagReal6BList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public TagReal6BList(Context context) {
        super(context);
        initContext(context);
    }

    private void initContext(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.tag_real_6b_list, this);

        data = new ArrayList<>();

        mTagsReal6BListScrollView = findViewById(R.id.tags_real_6b_list_scroll_view);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutParams lp = (LayoutParams) mTagsReal6BListScrollView.getLayoutParams();
        lp.height = wm.getDefaultDisplay().getHeight() / 3;
        mTagsReal6BListScrollView.setLayoutParams(lp);
        mTagsReal6BListScrollView.invalidate();

        mTagReal6BRow = findViewById(R.id.table_row_tag_real_6b);
        mTagReal6BImage = findViewById(R.id.image_prompt);
        mTagReal6BImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
        mListTextInfo = findViewById(R.id.list_text_info);
        mListTextInfo.setText(getResources().getString(R.string.open_tag_list));

        mTagsCountText = findViewById(R.id.tags_count_text);

        mTagReal6BRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutParams lp = (LayoutParams) mTagsReal6BListScrollView.getLayoutParams();

                if (lp.height <= wm.getDefaultDisplay().getHeight() / 3) {
                    lp.height = (int) (wm.getDefaultDisplay().getHeight() / 1.5);
                    mTagsReal6BListScrollView.setLayoutParams(lp);
                    mTagsReal6BListScrollView.invalidate();

                    mTagReal6BImage.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mListTextInfo.setText(getResources().getString(R.string.close_tag_list));
                } else {
                    lp.height = wm.getDefaultDisplay().getHeight() / 3;
                    mTagsReal6BListScrollView.setLayoutParams(lp);
                    mTagsReal6BListScrollView.invalidate();

                    mTagReal6BImage.setImageDrawable(getResources().getDrawable(R.drawable.up));
                    mListTextInfo.setText(getResources().getString(R.string.open_tag_list));
                }
            }
        });

        mTagReal6BList = findViewById(R.id.tag_real_6b_list_view);
        mReal6BListAdapter = new Real6BListAdapter(mContext, data);
        mTagReal6BList.setAdapter(mReal6BListAdapter);

        mTagReal6BList.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {

            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(arg1, arg2, arg3);
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public final void clearText() {
        mTagsCountText.setText("0");
    }

    public final void refreshText() {
        mTagsCountText.setText(String.valueOf(data.size()));
    }

    public final void setTag(List<DataParameter> tag) {
        data.clear();
        data.addAll(tag);
        mReal6BListAdapter.notifyDataSetChanged();
    }

    public final void clearTag() {
        data.clear();
        mReal6BListAdapter.notifyDataSetChanged();
    }

}
