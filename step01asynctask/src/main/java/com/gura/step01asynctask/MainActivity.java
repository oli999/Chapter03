package com.gura.step01asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
            implements View.OnClickListener{
    //필요한 맴버필드 정의하기
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
        //입력한 url 주소를 읽어온다.
        String inputText=inputUrl.getText().toString();
        // 비동기 작업에서 http 요청을 한다.
        DownTask task=new DownTask();
        task.execute(inputText);

        //메소드 바로 리턴 하기
    }
    /*
        AsyncTask<파라미터 Type, 진행중 Type, 결과 Type>
     */
    public class DownTask
            extends AsyncTask<String, Integer, Map<String,Object>>{

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            // 인자로 전달된 params 는 배열이다.
            String urlAddr = params[0];
            // 가상의 다운로드 작업을 하는데 5초가 걸린다고 가정한다.

            for(int i=10; i<=100; i=i+10){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //진행 상황을 발생시킨다.
                publishProgress(i);
            }

            // 결과 데이터라고 가정
            String result="<html></html>";
            // 결과 데이터를 Map 에 담아서 리턴하다.
            Map<String, Object> map=new HashMap<>();
            map.put("data", result);

            return map;
        }
        // publishProgress() 메소드를 호출하면 호출되는 메소드
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //여기는 UI 스레드 이다.
            console.setText(values[0]+" % 진행됨...");
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            // doInBackground() 메소드가 리턴하고 나서 호출되는 메소드
            // 여기는 UI 스레드 이다.
            String data = (String)stringObjectMap.get("data");
            console.setText(data);
        }
    }
}












