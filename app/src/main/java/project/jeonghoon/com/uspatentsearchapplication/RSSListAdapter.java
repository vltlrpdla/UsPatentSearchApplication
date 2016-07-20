package project.jeonghoon.com.uspatentsearchapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeonghoon on 2016-07-14.
 */

public class RSSListAdapter extends BaseAdapter {
    private Context mContext;

    private List<SearchItem> mItems = new ArrayList<SearchItem>();

    public RSSListAdapter(Context context) {
        mContext = context;
    }

    public void removeAllItem() { mItems.clear(); }

    public void addItem(SearchItem it) {
        mItems.add(it);
    }

    public void setListItems(List<SearchItem> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        return true;
    }

    public long getItemId(int position) {
        return position;
    }

    // 리스트의 하나의 아이템을 나타냄 convertView는 재사용 가능하도록 설계가 돼 있어서 리스트뷰 아이템 하나 하나 당 인플레이션 작업을 하는
    //것을 막아줌 (하나의 view는 Ram에서 1kilobytes,2kilobytes를 차지한다.)
    public View getView(int position, View convertView, ViewGroup parent) {
        RSSSearchItemView itemView;

        if (convertView == null) {
            itemView = new RSSSearchItemView(mContext, mItems.get(position));
        } else {
            itemView = (RSSSearchItemView) convertView;

            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(0, mItems.get(position).getInventionName());
            itemView.setText(1, mItems.get(position).getLtrtno());
            itemView.setText(2, mItems.get(position).getApplicationDate());
            itemView.setText(3, mItems.get(position).getApplicationNo());
        }

        return itemView;
    }


}
