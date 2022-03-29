package com.example.db_listview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ExpandableListView listView;
    ListviewAdapter adapter;
    ArrayList<ParentItem> groupList = new ArrayList<>(); //부모 리스트
    ArrayList<ArrayList<ChildItem>> childList = new ArrayList<>(); //자식 리스트
    ArrayList<ArrayList<ChildItem>> monthArray = new ArrayList<>(); //1월 ~ 12월을 관리하기 위한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ExpandableListView) findViewById(R.id.expandable_list);

        //monthArray에 1월~12월 배열을 모두 가
        for (int i = 0; i < 12; i++) {
            monthArray.add(new ArrayList<>());
        }



        //어댑터에 각각의 배열 등록
        adapter = new ListviewAdapter();
        adapter.parentItems = groupList;
        adapter.childItems = childList;

        listView.setAdapter(adapter);
        listView.setGroupIndicator(null); //리스트뷰 기본 아이콘 표시 여부

        setListItems();

        //리스트 클릭시 지출 항목이 토스트로 나타난다
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), adapter.getChild(groupPosition, childPosition).getDevice(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    public void addBtnOnClicked(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_add, null);

        final EditText monthEdit = (EditText) v.findViewById(R.id.month_edit);
        final EditText titleEdit = (EditText) v.findViewById(R.id.title_edit);

        new AlertDialog.Builder(this).setTitle("새로운 항목 입력").setView(v)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //입력받은 월 값에 맞는 month 배열을 찾아 새로운 요소를 등록한다
                        ChildItem item = new ChildItem(titleEdit.getText().toString());
                        monthArray.get(Integer.parseInt(monthEdit.getText().toString()) - 1).add(item);
                        setListItems();
                    }
                }).show();
    }

    //리스트 초기화 함수
    public void setListItems() {
        groupList.clear();
        childList.clear();

        childList.addAll(monthArray);

        //부모 리스트 내용 추가
        for (int i = 1; i <= 12; i++) {
            groupList.add(new ParentItem(i + "월", "울산광역시 남구","203.250.77.240",childList.get(i - 1).size() + "건"));
        }

        adapter.notifyDataSetChanged();
    }
}