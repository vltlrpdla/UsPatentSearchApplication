package project.jeonghoon.com.uspatentsearchapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jeonghoon on 2016-07-14.
 */
// 리스트 아이템뷰 인플레이션 및 값 할당
public class RSSSearchItemView extends LinearLayout {


    /**
     * Icon
     */
    private ImageView mIcon;

    /**
     * TextView 01
     */
    private TextView mText01;

    /**
     * TextView 02
     */
    private TextView mText02;

    /**
     * TextView 03
     */
    private TextView mText03;

    /**
     * WebView 04
     */
    private TextView mText04;

    String applicationNo;
    String ltrtNo;
    String applicationDate;

    public RSSSearchItemView(Context context, SearchItem aItem) {
        super(context);

        // Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listitem, this, true);

        // Set Icon
        mIcon = (ImageView) findViewById(R.id.iconItem);
        mIcon.setImageDrawable(aItem.getIcon());

        // Set Text 01
        mText01 = (TextView) findViewById(R.id.dataItem01);
        mText01.setText(aItem.getInventionName());

        // Set Text 02
        mText02 = (TextView) findViewById(R.id.dataItem02);
        ltrtNo = "문헌번호: "+aItem.getLtrtno();
        mText02.setText(ltrtNo);

        // Set Text 03
        mText03 = (TextView) findViewById(R.id.dataItem03);
        applicationDate = "출원일: "+ aItem.getApplicationDate();
        mText03.setText(applicationDate);


        // Set Text 04
        mText04 = (TextView) findViewById(R.id.dataItem04);
        applicationNo = "출원번호: "+aItem.getApplicationNo();
        mText04.setText(applicationNo);
        Log.d("MainActivity", "데이터 :" + aItem.getApplicationNo());
        //setTextToWebView(aItem.getApplicationNo());

    }

    /**
     * set Text
     *
     * @param index
     * @param data
     */
    public void setText(int index, String data) {

        String output;

        if (index == 0) {
            mText01.setText(data);
        } else if (index == 1) {
            output = "문헌번호: "+data;
            mText02.setText(output);
        } else if (index == 2) {
            output ="출원일: "+ data;
            mText03.setText(output);
        } else if (index == 3) {
            output = "출원번호: "+data;
            mText04.setText(output);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * set Icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }


}
