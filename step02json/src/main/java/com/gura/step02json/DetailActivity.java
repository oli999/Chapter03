package com.gura.step02json;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //전달된 인텐트 객체의 참조값 얻어오기
        Intent intent=getIntent();
        //인텐트 객체에 담긴 MemberDto 객체 얻어오기
        MemberDto dto = (MemberDto)intent.getSerializableExtra("dto");
        TextView textNum=(TextView)findViewById(R.id.textNum);
        TextView textName=(TextView)findViewById(R.id.textName);
        TextView textAddr=(TextView)findViewById(R.id.textAddr);
        textNum.setText("번호 : "+dto.getNum());
        textName.setText("이름 : "+dto.getName());
        textAddr.setText("주소 : "+dto.getAddr());
    }
}









