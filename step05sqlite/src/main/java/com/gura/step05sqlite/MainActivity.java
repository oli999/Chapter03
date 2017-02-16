package com.gura.step05sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                implements View.OnClickListener{
    //필요한 맴버필드 정의하기
    ListView listView;
    EditText inputName, inputAddr;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    List<String> members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        inputName=(EditText)findViewById(R.id.inputName);
        inputAddr=(EditText)findViewById(R.id.inputaddr);
        //DBHelper 객체를 생성한다.
        dbHelper=new DBHelper(this, "mydb.sqlite", null, 2);
        //버튼에 리스너 등록
        Button saveBtn=(Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        //모델
        members=new ArrayList<>();
        //아답타
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                members);
        //ListView 에 아답타 연결하기
        listView.setAdapter(adapter);
        updateModel();
        adapter.notifyDataSetChanged();
    }

    public void updateModel(){
        members.clear();
        //DB 에서 회원 목록 불러오기
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="SELECT num,name,addr FROM member " +
                "ORDER BY num ASC";
        // SELECT 문을 수행하고 결과값을 Cursor Type 으로 얻어내기
        Cursor resultSet=db.rawQuery(sql, null);
        // 커서를 하나씩 내리면서 결과값 추출하기
        while(resultSet.moveToNext()){
            int num=resultSet.getInt(0);
            String name=resultSet.getString(1);
            String addr=resultSet.getString(2);
            members.add(num+" | "+name+" | "+addr);
        }
    }

    @Override
    public void onClick(View v) {
        //입력한 이름과 주소 읽어오기
        String name=inputName.getText().toString();
        String addr=inputAddr.getText().toString();
        //SQLiteDataBase 객체 얻어오기
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //실행할 sql 문
        String sql="INSERT INTO member (name,addr) " +
                "VALUES( ?, ?)";
        // ? 에 바인딩할 내용을 Object[] 에 준비한다.
        //Object[] args={name, addr};
        Object[] args=new Object[2];
        args[0]=name;
        args[1]=addr;
        // DB 에 저장하기
        db.execSQL(sql, args);
        // 반듯이 close() 해야 저장이된다.
        db.close();
        // 새로 들어간 정보를 확인 할수 있도록
        updateModel();
        adapter.notifyDataSetChanged();
        Toast.makeText
                (this, "저장했습니다.", Toast.LENGTH_SHORT).show();
    }
}










