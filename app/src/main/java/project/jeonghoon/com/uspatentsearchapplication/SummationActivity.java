package project.jeonghoon.com.uspatentsearchapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
// 서지 요약정보만 가져오는 액티비티
public class SummationActivity extends AppCompatActivity {

    private static final String TAG = "SummationActivity";

    private static String summationSearchUrl = "http://plus.kipris.or.kr/openapi/rest/ForeignPatentBibliographicService/bibliographicInfo?literatureNumber=%s&countryCode=%s&accessKey=%s";
    private static String apiKey = "i6zXBgWqH2HXNuH7C=gwttRedfdBS3rd4iM2DVbgHsg=";

    ProgressDialog progressDialog;
    Handler handler = new Handler();
    TextView summation;
    TextView inventorsView;
    TextView applicantView;
    TextView inventionNameView;


    String summationStr;

    //Button bDownloadFullText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summation);

        summation = (TextView)findViewById(R.id.summation);
        inventionNameView = (TextView)findViewById(R.id.inventionTitle);
        applicantView = (TextView)findViewById(R.id.applicant);
        inventorsView = (TextView)findViewById(R.id.inventors);
        //bDownloadFullText = (Button)findViewById(R.id.bDownloadFullText);

        // 액티비티가 종료 될 때 Bundle이라는 객체를 사용해 액티비티의 상태를 저장하거나 액티비티간 데이터 전송을 할 때 Bundle 객체를 사용
        Bundle bundle = getIntent().getExtras();
        String ltrtno = bundle.getString("ltrtno");
        String contryCode = bundle.getString("contryCode");
        String inventionName = bundle.getString("inventionName");
        String applicant = bundle.getString("applicant");
        String inventors = bundle.getString("inventors");

        String url = buildKeywordSearchApiUrlString(ltrtno,contryCode,apiKey);

        showResult(url);

        inventionNameView.setText(inventionName);
        applicantView.setText(applicant);
        inventorsView.setText(inventors);

        /*파일경로가 주어졌을때 url을 통해서 pdf viewer로 연결해주는 모듈만 끝나면 완료
        bDownloadFullText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_DEFAULT);//
                i.setDataAndType(Uri.fromFile(new File("http://abpat.kipris.or.kr/abpat/remoteFile.do?method=fullText&publ_key=EP000001731204A1&cntry=EP")), "application/pdf");
                startActivity(i);//

            }
        });
        */
    }


    private String buildKeywordSearchApiUrlString(String query,String contryCode,String apikey) {
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(summationSearchUrl, encodedQuery, contryCode, apikey);
    }

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

                InputStream instream = getInputStreamUsingHTTP(urlForHttp);
                Document document = builder.parse(instream);
                processDocument(document);

                handler.post(updateRSSRunnable);

            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public InputStream getInputStreamUsingHTTP(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        int resCode = conn.getResponseCode();
        Log.d(TAG, "Response Code" + resCode);

        InputStream instream = conn.getInputStream();

        return instream;
    }

    //결과 아이템의 갯수만큼 결과값을 받아서 리스트에 저장
    private void processDocument(Document doc){
        Element docEle = doc.getDocumentElement();//객체 생성
        NodeList nodelist = docEle.getElementsByTagName("summation");

        if((nodelist != null) && (nodelist.getLength() > 0)){
            Element entry = (Element)nodelist.item(0);
            Element astrtCont = (Element)entry.getElementsByTagName("astrtCont").item(0);


            String astrtContValue = null;
            if(astrtCont != null){
                Node firstChild = astrtCont.getFirstChild();
                if(firstChild != null){
                    astrtContValue = firstChild.getNodeValue();
                }
            }

            Log.d(TAG,"astrt :" + astrtContValue);
            summationStr = astrtContValue;
        }


    }

    Runnable updateRSSRunnable = new Runnable(){
        public void run(){

            try{

                summation.setText(summationStr);
                progressDialog.dismiss();

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    };


}
