package project.jeonghoon.com.uspatentsearchapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;


/**
 * Created by jeonghoon on 2016-07-14.
 *
 * AdapterView<?> parentView
 parent는 클릭된 항목의 부모 뷰인 어댑터 뷰입니다. 리스트 뷰의 항목을 클릭했다면, parent는 ListView 뷰입니다.

 View view
 view는 사용자가 클릭한 항목에 해당되는 뷰입니다.

 int position
 안드로이드 개발자 사이트에서 position의 본래 의미는 'The position of the view in the adapter'입니다. ListView의 경우 이것은 선택된 항목의 위치입니다.

 long id
 개발자 사이트에 의하면 id의 본래 의미는 'The row id of the item that was clicked.'입니다. ListView의 경우 id는 position과 동일하다고 보시면 됩니다.
 *
 *
 */
public class RSSListView extends ListView {
    /**
     * DataAdapter for this instance
     */
    private RSSListAdapter adapter;

    /**
     * Listener for data selection
     */
    private OnDataSelectionListener selectionListener;

    public RSSListView(Context context) {
        super(context);

        init();
    }

    public RSSListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    /**
     * set initial properties
     */
    private void init() {
        // set OnItemClickListener for processing OnDataSelectionListener
        setOnItemClickListener(new OnItemClickAdapter());
    }

    /**
     * set DataAdapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);

    }

    /**
     * get DataAdapter
     *
     * @return
     */
    public BaseAdapter getAdapter() {
        return (BaseAdapter)super.getAdapter();
    }

    /**
     * set OnDataSelectionListener
     *
     * @param listener
     */
    public void setOnDataSelectionListener(OnDataSelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * get OnDataSelectionListener
     *
     * @return
     */
    public OnDataSelectionListener getOnDataSelectionListener() {
        return selectionListener;
    }


    class OnItemClickAdapter implements OnItemClickListener {

        public OnItemClickAdapter() {
        }


        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (selectionListener == null) {
                return;
            }

            // get row and column
            int rowIndex = -1;
            int columnIndex = -1;

            // call the OnDataSelectionListener method
            selectionListener.onDataSelected(parent, v, position, id);

        }

    }
}
