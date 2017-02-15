package com.gura.step02json;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
            Util.RequestListener,
            AdapterView.OnItemClickListener{
    ListView listView;
    MemberAdapter adapter;
    List<MemberDto> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        Button getBtn=(Button)findViewById(R.id.getMemberBtn);
        getBtn.setOnClickListener(this);
        list=new ArrayList<>();
        adapter=new MemberAdapter(this,
                R.layout.listview_cell, list);
        listView.setAdapter(adapter);
        //ListView 에 아이템 클릭 리스너 등록하기
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // sendGetRequest(요청 id, 요청 url, 요청 parameter, 리스너 )
        Util.sendGetRequest(0, MyConstants.REQUEST_URL, null, this);
    }

    @Override
    public void onSuccess(int requestId, Map<String, Object> result) {
        if(requestId==0){
            int code=(int)result.get("code");
            switch (code){
                case 200:
                    //응답되는 문자열은 json 이다.
                    String data=(String)result.get("data");
                    Toast.makeText(this, data, Toast.LENGTH_SHORT ).show();
                    parseJson(data);
                    break;
            }
        }
    }

    @Override
    public void onFail(int requestId, Map<String, Object> result) {

    }
    //
    public void parseJson(String json){

        try {
            JSONObject jsonObj=new JSONObject(json);
            JSONArray jsonArr=jsonObj.getJSONArray("members");
            for(int i=0; i<jsonArr.length(); i++){
                // i 번째 JSONObject 객체를 얻어온다.
                JSONObject tmp = (JSONObject)jsonArr.get(i);

                int num=tmp.getInt("num"); //번호
                String name=tmp.getString("name"); //이름
                String addr=tmp.getString("addr"); //주소
                //모델에 추가 한다.
                MemberDto dto=new MemberDto(num, name, addr);
                list.add(dto);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "JSON 문서에 오류가 있습니다.",
                    Toast.LENGTH_SHORT).show();
        }
        //아답타에 알린다.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, DetailActivity.class);
        //인텐트 객체에 MemberDto 객체를 담는다.
        intent.putExtra("dto", list.get(position));
        //액티비티 시작 시키기
        startActivity(intent);
    }
}








