package com.gura.step04fileio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class MainActivity extends AppCompatActivity {
    EditText inputMsg, console;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputMsg=(EditText)findViewById(R.id.inputMsg);
        console=(EditText)findViewById(R.id.console);
    }
    //파일에 저장하기 버튼을 눌렀을때
    public void saveToFile(View v){
        //입력한 문자열을 읽어온다.
        String str=inputMsg.getText().toString();
        //필요한 객체 초기화 하기
        FileOutputStream fos=null;
        PrintWriter pw=null;
        try{
            // 내부 저장장치에 저장할수있는 FileOutputStream 객체
            fos=openFileOutput("memo.txt", MODE_PRIVATE);
            pw=new PrintWriter(fos);
            pw.write(str);
            Toast.makeText(this, "내부 저장장치에 저장성공!",
                    Toast.LENGTH_SHORT).show();
        }catch(IOException ie){
            Toast.makeText(this, ie.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }finally {
            try{
                if(pw!=null)pw.close();
                if(fos!=null)fos.close();
            }catch(Exception e){}
        }
    }
    //읽어오기 버튼을 눌었을때
    public void loadFromFile(View v){
        //필요한 객체 초기화 하기
        FileInputStream fis=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        StringBuilder builder=new StringBuilder();
        try{
            fis=openFileInput("memo.txt");
            isr=new InputStreamReader(fis);
            br=new BufferedReader(isr);
            while (true) {
                String line=br.readLine();
                if(line==null)break;
                builder.append(line+"\r\n");
            }
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }finally {
            try{
                if(br!=null)br.close();
                if(isr!=null)isr.close();
                if(fis!=null)fis.close();
            }catch(Exception e){}
        }
        //EditText 에 읽어온 내용 출력하기
        console.setText(builder.toString());
    }
    // 외부저장장치(Sdcard) 에 저장하기
    public void saveToSdcardFile(View v){
        checkStoragePermission();
    }
    // 외부저장장치(Sdcard) 에서 읽어오기
    public void loadFromSdcardFile(View v){
        //외부 저장장치의 상태값 읽어오기
        String state= Environment.getExternalStorageState();
        if(! state.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "외부 저장장치를 사용할수 없음",
                    Toast.LENGTH_SHORT).show();
            return; //메소드 종료
        }
        //외부 저장 장치의 경로 얻어오기
        String externalPath=Environment
                .getExternalStorageDirectory().getAbsolutePath();
        //저장할 디렉토리 만들기
        String dirName=getPackageName(); //App 페키지명을 디렉토리로
        //파일을 저장할 경로를 구성한다.
        String path=externalPath+"/"+dirName+"/memo.txt";
        //필요한 객체 초기화 하기
        FileInputStream fis=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        StringBuilder builder=new StringBuilder();
        try{
            fis=new FileInputStream(path);
            isr=new InputStreamReader(fis);
            br=new BufferedReader(isr);
            while (true) {
                String line=br.readLine();
                if(line==null)break;
                builder.append(line+"\r\n");
            }
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }finally {
            try{
                if(br!=null)br.close();
                if(isr!=null)isr.close();
                if(fis!=null)fis.close();
            }catch(Exception e){}
        }
        //EditText 에 읽어온 내용 출력하기
        console.setText(builder.toString());

    }

    // 외부 저장장치에 기록을 할수 있도록 앱 사용자가 체크 했는지
    // 확인 해서 체크 하지 않았다면 체크하도록 요청하는 메소드
    public void checkStoragePermission(){
        //퍼미션을 체크 했는지 여부
        boolean isGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED ;
        if(!isGranted){ //체크 하지 않았다면
            //퍼미션을 체크해 달라고 요청한다.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    999);
        }else{//이미 퍼미션이 체크 되어 있다면
            sdcardProcess();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 999:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //여기가 수행되면 퍼미션을 on 한 것이다.
                    sdcardProcess();
                }else{
                    //여기가 수행되면 퍼미션을 on 하지 않은 것이다.
                    Toast.makeText(this, "Sdcard 에 파일을 저장하기 위해서는" +
                            "퍼미션을 체크 해야 합니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void sdcardProcess(){
        //입력한 문자열을 읽어온다.
        String msg=inputMsg.getText().toString();
        //외부 저장장치의 상태값 읽어오기
        String state= Environment.getExternalStorageState();
        if(! state.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "외부 저장장치를 사용할수 없음",
                    Toast.LENGTH_SHORT).show();
            return; //메소드 종료
        }
        //외부 저장 장치의 경로 얻어오기
        String externalPath=Environment
                .getExternalStorageDirectory().getAbsolutePath();
        //저장할 디렉토리 만들기
        String dirName=getPackageName(); //App 페키지명을 디렉토리로
        File file=new File(externalPath+"/"+dirName+"/");
        file.mkdir();//디렉토리 만들고
        //파일을 저장할 경로를 구성한다.
        String path=externalPath+"/"+dirName+"/memo.txt";
        //필요한 객체 초기화 하기
        FileOutputStream fos=null;
        PrintWriter pw=null;
        try{
            fos=new FileOutputStream(path);
            pw=new PrintWriter(fos);
            pw.write(msg);
            Toast.makeText(this, "Sdcard 에 저장성공!",
                    Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }finally{
            try{
                if(pw!=null)pw.close();
                if(fos!=null)fos.close();
            }catch(Exception e){}
        }
    }
}









