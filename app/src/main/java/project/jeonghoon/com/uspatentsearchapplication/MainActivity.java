package project.jeonghoon.com.uspatentsearchapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static String freeKeywordSearchUrl ="http://plus.kipris.or.kr/openapi/rest/ForeignPatentGeneralSearchService/wordSearch?searchWord=%s&collectionValues=%s&accessKey=%s";
    private static String apiKey = "i6zXBgWqH2HXNuH7C=gwttRedfdBS3rd4iM2DVbgHsg=";

    EditText editSearchText;
    Button bSearch;
    Spinner s;

    ProgressDialog progressDialog;
    Handler handler = new Handler();

    RSSListView list;
    RSSListAdapter adapter;
    ArrayList<SearchItem> searchItemList;

    String contryCode = "US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a ListView instance
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        list = new RSSListView(this);

        adapter = new RSSListAdapter(this);
        list.setAdapter(adapter);

        // 리스트뷰의 아이템이 선택 될 때 다른 액티비티로 문헌번호 전송과 전환
        // 선택이라는 이벤트가 발생했을 때 동작하는 콜백 메소드의 실질적인 구현
        list.setOnDataSelectionListener(new OnDataSelectionListener() {
            public void onDataSelected(AdapterView parent, View v, int position, long id) {

                SearchItem curItem = (SearchItem) adapter.getItem(position);
                String curTitle = curItem.getInventionName();
                Toast.makeText(getApplicationContext(), "Selected : " + curTitle, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this , SummationActivity.class);
                intent.putExtra("contryCode",curItem.getContryCode());
                intent.putExtra("ltrtno", curItem.getLtrtno());
                intent.putExtra("inventionName",curItem.getInventionName());
                intent.putExtra("applicant", curItem.getApplicant());
                intent.putExtra("inventors", curItem.getInventors());
                startActivity(intent);
            }
        });


        searchItemList = new ArrayList<SearchItem>();

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.addView(list, params);//리스트 뷰 추가



        bSearch = (Button)findViewById(R.id.bSearch);
        editSearchText = (EditText)findViewById(R.id.editSearchText);
        editSearchText.setText("ROSENMAN");

        // 검색시 기존의 모든 아이템을 삭제하고 api입력값으로 국가 코드와 검색어로 api url을 만들고 showResult 메소드의 입력값으로 줌
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.removeAllItem();
                String query = editSearchText.getText().toString();
                String url = buildKeywordSearchApiUrlString(query,contryCode,apiKey);
                showResult(url);
            }
        });

        // 국가 코드가 선택이 되지 않을 시에는 초기 값으로 US를 가짐 그 이외에는 res/values/array 밑의 아이템 값들을 선택시 가짐.
        s = (Spinner)findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                       contryCode = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    contryCode = parent.getItemAtPosition(0).toString();
            }
        });
    }

    //입력된 검색값과 contryCode로 apikey의 빈 값들을 채워넣어 url을 만듦
    private String buildKeywordSearchApiUrlString(String query,String contryCode,String apikey) {
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(freeKeywordSearchUrl, encodedQuery, contryCode, apikey);
    }

    //main thread만이 UI를 건드릴 수 있음, 지금 같은 경우 화면 작업과 외부에서 데이터를 가져오는 작업은 나눠져서 실행 되어야 함
    //
    private void showResult(String inputStr){
        try{

            progressDialog = ProgressDialog.show(this, "검색 중", "검색 하는 중입니다.",true,true);

            SearchThread thread = new SearchThread(inputStr);
            thread.start();

        }catch(Exception e){
            Log.e(TAG, "Error",e);
        }
    }

    // Test때는 Url을 그냥 주고 검색 실제 검색에서는 String 으로 자유 검색어 받아서 구현
    class SearchThread extends Thread{
        String urlStr;

        public SearchThread(String str){
            urlStr = str;
        }

        public void run(){

            try{

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();//팩토리 객체 생성
                DocumentBuilder builder = builderFactory.newDocumentBuilder();

                URL urlForHttp = new URL(urlStr);//url

                // 웹에 뿌려진 xml 데이터 포맷을 읽어 들이는 작업
                InputStream instream = getInputStreamUsingHTTP(urlForHttp);
                Document document = builder.parse(instream);
                int countItem = processDocument(document); //가져오려고 하는 item의 갯수를 체크
                Log.d(TAG, countItem + "search  item processed.");

                handler.post(updateRSSRunnable);

            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public InputStream getInputStreamUsingHTTP(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");//
        conn.setDoInput(true);
        conn.setDoOutput(true);

        int resCode = conn.getResponseCode();
        Log.d(TAG, "Response Code" + resCode);

        InputStream instream = conn.getInputStream();

        return instream;
    }

    //결과 아이템의 갯수만큼 결과값을 받아서 리스트에 저장
    private int processDocument(Document doc){
        searchItemList.clear();// 기존에 리스트 어댑터에 적용 됐던 아이템들 클리어

        Element docEle = doc.getDocumentElement();//객체 생성
        NodeList nodelist = docEle.getElementsByTagName("searchResult");
        int count = 0;

        if((nodelist != null) && (nodelist.getLength() > 0)){
            for(int i = 0; i < nodelist.getLength(); i++){
                SearchItem searchItem = dissectNode(nodelist, i);
                if(searchItem != null){
                    searchItemList.add(searchItem);
                    count++;
                }
            }
        }
        return count;
    }

    //결과 아이템의 하위 노드의 아이템의 값을 가져오는 메소드, 결과값 받아오는 데이터를 수정하려면 여기와 SearchItem 수정
    private SearchItem dissectNode(NodeList nodelist, int index){
        SearchItem searchItem = null;

        try{
            Element entry = (Element)nodelist.item(index);

            Element ltrtno = (Element)entry.getElementsByTagName("ltrtno").item(0);
            Element inventionName = (Element)entry.getElementsByTagName("inventionName").item(0);
            Element applicationDate = (Element)entry.getElementsByTagName("applicationDate").item(0);
            Element applicationNo = (Element)entry.getElementsByTagName("applicationNo").item(0);
            Element applicant = (Element)entry.getElementsByTagName("applicant").item(0);
            Element inventors = (Element)entry.getElementsByTagName("inventors").item(0);


            String ltrtnoValue = null;
            //
            if(ltrtno != null){
                Node firstChild = ltrtno.getFirstChild();
                if(firstChild != null){
                    ltrtnoValue = firstChild.getNodeValue();
                }
            }

            String inventionNameValue = null;
            if(inventionName != null){
                Node firstChild = inventionName.getFirstChild();
                if(firstChild != null){
                    inventionNameValue = firstChild.getNodeValue();
                }
            }

            String applicationDateValue = null;
            if(applicationDate != null){
                Node firstChild = applicationDate.getFirstChild();
                if(firstChild != null){
                    applicationDateValue = firstChild.getNodeValue();
                }
            }

            String applicationNoValue = null;
            if(applicationNo != null){
                Node firstChild = applicationNo.getFirstChild();
                if(firstChild != null){
                    applicationNoValue = firstChild.getNodeValue();
                }
            }

            String applicantValue = null;
            if(applicant != null){
                Node firstChild = applicant.getFirstChild();
                if(firstChild != null){
                    applicantValue = firstChild.getNodeValue();
                }
            }

            String inventorsValue = null;
            if(inventors != null){
                Node firstChild = inventors.getFirstChild();
                if(firstChild != null){
                    inventorsValue = firstChild.getNodeValue();
                }
            }

            Log.d(TAG, "item node :" + ltrtnoValue + ", " + inventionNameValue +", " +applicationDateValue
                    + ", " + applicationNoValue );


            searchItem = new SearchItem(ltrtnoValue,inventionNameValue,applicationDateValue,applicationNoValue,contryCode,applicantValue,inventorsValue);


        }catch (DOMException e){
            e.printStackTrace();
        }

        return searchItem;
    }

    // 실질적으로 화면변경 작업은 여기서 실행 , 완료된 작업을 화면에 적용하기 위한 코드
    Runnable updateRSSRunnable = new Runnable(){
        public void run(){

            try{

                Resources res = getResources();
                Drawable rssIcon = res.getDrawable(R.drawable.patent_icon);
                for (int i = 0; i < searchItemList.size(); i++){
                    SearchItem searchItem = (SearchItem) searchItemList.get(i);
                    searchItem.setIcon(rssIcon);
                    adapter.addItem(searchItem);
                }

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    };




}
