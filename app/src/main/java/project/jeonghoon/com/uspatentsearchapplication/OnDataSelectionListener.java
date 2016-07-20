package project.jeonghoon.com.uspatentsearchapplication;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by jeonghoon on 2016-07-14.
 */
// 데이터 선택 이벤트가 발생했을때 사용하는 인터페이스
public interface OnDataSelectionListener {

    /**
     * Method that is called when an item is selected in DataListView
     *
     * @param parent Parent View
     * @param v Target View
     * @param row Row Index
     * @param column Column Index
     * @param id ID for the View
     */

    public void onDataSelected(AdapterView parent, View v, int position, long id);

}
