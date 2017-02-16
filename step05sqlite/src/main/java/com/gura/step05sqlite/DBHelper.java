package com.gura.step05sqlite;

/*
    DB 생성 및 업그래이드를 도와주는 도우미 클래스 만들기
    - SQLiteOpenHelper 추상 클래스를 상속 받아서 만든다.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    //생성자
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //DB 가 최초 초기화 될때 호출되는 메소드
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Data 를 저장할 테이블을 만들어 준다.
        String sql="CREATE TABLE member" +
                "(num INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, addr TEXT)";
        db.execSQL(sql);
    }
    //DB 가 업그래이드 될때 호출되는 메소드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //테이블 추가 혹은 테이블 삭제 혹은 테이블 수정등의 작업
        //혹은 테이블에 저장된 데이터 수정
        String sql="DROP TABLE member";
        db.execSQL(sql); //테이블을 삭제하고

        //다시 만들어 질수 있도록 onCreate() 를 호출한다.
        onCreate(db);
    }
}







