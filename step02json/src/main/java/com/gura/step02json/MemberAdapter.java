package com.gura.step02json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2017-02-15.
 */

public class MemberAdapter extends BaseAdapter{
    //레이아웃 전개자 객체
    private LayoutInflater inflater;
    //모델
    private List<MemberDto> list;
    //cell 의 레이아웃 리소스 아이디
    private int layoutRes;

    //생성자
    public MemberAdapter(Context context,
                         int layoutRes, List<MemberDto> list){
        this.list=list;
        this.layoutRes=layoutRes;
        //레이아웃 전개자 객체를 맴버필드에 저장한다.
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // 회원의 번호가 아이템의 아이디 역활을 할수 있으므로
        return list.get(position).getNum();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=inflater.inflate(layoutRes, parent, false);
        }

        TextView textNum=(TextView)
                convertView.findViewById(R.id.textNum);
        TextView textName=(TextView)
                convertView.findViewById(R.id.textName);
        MemberDto dto=list.get(position);

        textNum.setText("번호 : "+dto.getNum());
        textName.setText("이름 : "+dto.getName());

        return convertView;
    }
}












