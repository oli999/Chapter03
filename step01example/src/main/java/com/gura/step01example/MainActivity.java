package com.gura.step01example;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
            implements View.OnClickListener{
    private EditText inputUrl, console;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // res/layout/activity_main.xml 에 정의되 UI 의 참조값
        inputUrl=(EditText)findViewById(R.id.inputUrl);
        console=(EditText)findViewById(R.id.console);

        //Button 의 참조값 얻어와서 리스너 등록하기
        Button requestBtn=(Button)findViewById(R.id.requestBtn);
        requestBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //입력한 url 주소를 읽어와서
        String inputText=inputUrl.getText().toString();
        //비동기 작업객체를 생성해서 작업 시킨다.
        new HttpRequestTask().execute(inputText);
        //메소드는 바로 리턴된다.
    }

    public class HttpRequestTask extends
            AsyncTask<String, Void, Map<String, Object>>{

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            //params 는 배열이다
            String urlAddr=params[0];
            //서버가 http 요청에 대해서 응답하는 문자열을 누적할 객체
            StringBuilder builder=new StringBuilder();
            HttpURLConnection conn=null;
            InputStreamReader isr=null;
            BufferedReader br=null;
            //결과값을 담을 Map Type 객체
            Map<String,Object> map=new HashMap<String,Object>();
            try{
                //URL 객체 생성
                URL url=new URL(urlAddr);
                //HttpURLConnection 객체의 참조값 얻어오기
                conn=(HttpURLConnection)url.openConnection();
                if(conn!=null){//연결이 되었다면
                    conn.setConnectTimeout(20000); //응답을 기다리는 최대 대기 시간
                    conn.setUseCaches(false);//케쉬 사용 여부
                    //응답 코드를 읽어온다.
                    int responseCode=conn.getResponseCode();
                    //Map 객체에 응답 코드를 담는다.
                    map.put("code", responseCode);
                    if(responseCode==200){//정상 응답이라면...
                        //서버가 출력하는 문자열을 읽어오기 위한 객체
                        isr=new InputStreamReader(conn.getInputStream());
                        br=new BufferedReader(isr);
                        //반복문 돌면서 읽어오기
                        while(true){
                            //한줄씩 읽어들인다.
                            String line=br.readLine();
                            //더이상 읽어올 문자열이 없으면 반복문 탈출
                            if(line==null)break;
                            //읽어온 문자열 누적 시키기
                            builder.append(line);
                        }
                        //출력받은 문자열 전체 얻어내기
                        String str=builder.toString();
                        //아래 코드는 수행 불가
                        //console.setText(str);
                        //Map 객체에 결과 문자열을 담는다.
                        map.put("data", str);
                    }
                }
            }catch(Exception e){//예외가 발생하면
                //에러 정보를 담는다.
                map.put("code",-1);
                map.put("data", e.getMessage());
            }finally {
                try{
                    if(isr!=null)isr.close();
                    if(br!=null)br.close();
                    if(conn!=null)conn.disconnect();
                }catch(Exception e){}
            }
            //결과 Data 리턴해주기
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            //응답 코드를 읽어온다.
            int code=(int) stringObjectMap.get("code");
            switch (code){
                case 404:
                    console.setText("url 주소를 확인 하세요!");
                    break;
                case 500:
                    console.setText("해당 서버에 오류가 있습니다!");
                    break;
                case 200:
                    String result=(String)stringObjectMap.get("data");
                    console.setText(result);
                    break;
                case -1:
                    String errMsg=(String) stringObjectMap.get("data");
                    console.setText(errMsg);
                    break;
                default:
                    console.setText("기타 오류 발생!");
                    break;
            }// switch{}
        }//onPostExecute()
    }//class HttpRequestTask
}//class MainActivity







