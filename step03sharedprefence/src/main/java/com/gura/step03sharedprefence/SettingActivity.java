package com.gura.step03sharedprefence;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {
    //필요한 맴버필드 정의하기
    EditText name, email, phoneNumber, password;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //필요한 객체의 참조값 얻어오기
        //객체의 참조값을 얻어온다.
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        phoneNumber=(EditText)findViewById(R.id.phoneNumber);
        password=(EditText)findViewById(R.id.password);
        radioGroup=(RadioGroup)findViewById(R.id.sex);

        //SharedPrefences 객체의 참조값 얻어오기
        SharedPreferences pref=
                getSharedPreferences("information", MODE_PRIVATE);
        //이름이 저장되어 있는지 읽어와 본다.
        String savedName=pref.getString("name", "empty");
        if(savedName.equals("empty")){//만일 비어 있다면
            return; //메소드 종료
        }

        //나머지 정보를 얻어와서 저장된 정보를 출력해준다.
        name.setText(pref.getString("name", ""));
        email.setText(pref.getString("email", ""));
        phoneNumber.setText(pref.getString("phoneNumber", ""));
        password.setText(pref.getString("password", ""));
        //성별 체크하기
        String gender=pref.getString("sex", "");
        //라디어 버튼의 참조값을 얻어온다.
        RadioButton btnMan=(RadioButton)findViewById(R.id.man);
        RadioButton btnWoman=(RadioButton)findViewById(R.id.woman);
        //현재 성별에 따라서 다른 라디오 버튼을 체크하게 한다.
        if(gender.equals("남")){
            btnMan.setChecked(true);
        }else{
            btnWoman.setChecked(true);
        }

    }
    //저장 버튼을 눌렀을때 호출되는 메소드
    public void save(View v){
        //입력한 정보를 읽어온다.
        String inputName=name.getText().toString();
        String inputEmail=email.getText().toString();
        String inputPhoneNumber=phoneNumber.getText().toString();
        String inputPassword=password.getText().toString();
        //선택한 라디오 버튼의 아이디 값을 얻어온다.
        int buttonId=radioGroup.getCheckedRadioButtonId();
        //3항 연산자를 이용해서 성별에 관련된 문자열 얻어오기
        String inputSex = buttonId==R.id.man ? "남" : "여";
		/*
		 *  String inputSex;
		 *  if(buttonId==R.id.man){
		 *   	inputSex="남";
		 *  }else{
		 *  	inputSex="여";
		 *  }
		 */
        //informaion.xml 문서를 사용할 준비를 한다.
        SharedPreferences pref=
                getSharedPreferences("information", MODE_PRIVATE);
        //정보를 저장할수 있는 에디터 객체 얻어오기
        SharedPreferences.Editor editor=pref.edit();
        //에디터 객체를 이용해서 key : value 의 쌍으로 정보를 저장한다.
        editor.putString("name", inputName);
        editor.putString("email", inputEmail);
        editor.putString("phoneNumber", inputPhoneNumber);
        editor.putString("password", inputPassword);
        editor.putString("sex", inputSex);
        //저장하기 => informaion.xml 문서에 저장된다.
        editor.commit();
        //알림 띄우기
        new AlertDialog.Builder(this)
                .setMessage("저장하였습니다.")
                .setNeutralButton("확인", null)
                .show();
    }
}









