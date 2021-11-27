package com.example.todo;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {
  //  private static final int REQUEST_CODE = 0;
    public static List<SubjectInfo> subjectlist;
    public static subjectAdapter subjectAdapter;
    Button btn_del_sub;
    int ck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        subjectlist = getSubjectList();

        RecyclerView recyclerView = findViewById(R.id.recy_sub);
        subjectAdapter = new subjectAdapter((ArrayList<SubjectInfo>) subjectlist);
        recyclerView.setAdapter(subjectAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        subjectAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        btn_del_sub= (Button)findViewById(R.id.btn_del_sub);
        btn_del_sub.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<subjectAdapter.getcheckedList().size();i++){
                subjectlist.remove(subjectAdapter.getcheckedList().get(i)); //체크된목록 과목목록에서 제거
                delSubject(subjectAdapter.getcheckedList().get(i).getSubjectName());
            }
            if(subjectAdapter.getcheckedList().size()>0){
                Toast.makeText(this, "과목 삭제 완료", Toast.LENGTH_SHORT).show();
            }
            btn_del_sub.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            subjectAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subject_menu, menu); //툴바에 ㅇ메뉴 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_subject: // 과목추가버튼 누른 경우
                Intent intent = new Intent(getApplicationContext(), AddSubjectActivity.class);
                startActivity(intent);

                break;
            case R.id.del_subject:
                if(ck==0){
                    btnCheck(1);
                    ck=1;
                    btn_del_sub.setVisibility(View.VISIBLE);
                    break;
                }
                else if(ck==1){
                   btnCheck(0);
                   ck=0;
                    btn_del_sub.setVisibility(View.GONE);
                    break;
                }

        }
        subjectAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            subjectAdapter.notifyDataSetChanged();

        }
    }
*/
    public void btnCheck(int n){
        subjectAdapter.updateCheckBox(n);
        subjectAdapter.notifyDataSetChanged();;

    }


    public static List<SubjectInfo> getSubjectList() {
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }
    //+2021년(int) 2학기(int:1 or 2) 정보도 받아와야 할 듯
    //startWeekNumber 넘겨줄때 1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토 로 넘겨주세요
    public static boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime, int year, int semester) {

        if(Integer.parseInt(startTime) > 2400) return false;
        if(Integer.parseInt(endTime) > 2400) return false;



        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);

        if(result == false) return false;



        Date startdate = getstartDate(year, semester, startWeekNumber);
        int enddate = endWeekNumber - startWeekNumber;
        if(enddate <= 0)
            enddate += 7;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strstartdate;
        String strenddate;

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        HashMap<String, Object> lecturelist = new HashMap<>();


        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);
        String lectureName;

        //강의 추가
        for(int i = 1; i <= 16; i++)
        {
            strstartdate = dateFormat.format(cal.getTime());
            cal.add(Calendar.DATE, enddate);
            strenddate = dateFormat.format(cal.getTime());

            for(int j = 1; j <= number; j++)
            {
                //sqlite
                lectureName = subjectName+" "+i+"주차"+j;
                query = "INSERT INTO LectureList VALUES('" +
                        subjectName+"','"+lectureName+"',"+strstartdate+","+startTime+","+strenddate+","+endTime+",0);";
                result = adapter.excuteQuery(query);
                System.out.println(query);

                //firebase
                HashMap<String, Object> map = new HashMap<>();
                map.put("startDate", Integer.parseInt(strstartdate));
                map.put("startTime", Integer.parseInt(startTime));
                map.put("endDate", Integer.parseInt(strenddate));
                map.put("endTime", Integer.parseInt(endTime));
                map.put("isDone", 0);

                lecturelist.put(lectureName, map);

                if(result == false) {
                    query = "DELETE FROM SubjectList WHERE subjectName = '" + subjectName + "';";
                    adapter.excuteQuery(query);
                    return false;
                }
            }

            cal.add(Calendar.DATE, 7-enddate);
        }

        firebaseDB.uploadMyLecture(subjectName, lecturelist);

        subjectlist.add(new SubjectInfo(subjectName));
        subjectAdapter.notifyDataSetChanged();

        return result;
    }
    private static Date getstartDate(int year, int semester, int startWeekNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = Integer.toString(year);
        if(semester == 1)
            date += "0301";
        else if(semester == 2)
            date += "0901";

        Calendar cal = Calendar.getInstance();
        try {
            java.util.Date nDate = dateFormat.parse(date);


            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            while (dayNum != startWeekNumber) {
                cal.add(Calendar.DATE, 1);
                dayNum = cal.get(Calendar.DAY_OF_WEEK);
            }

            SimpleDateFormat resultFormat = new SimpleDateFormat("yyyyMMdd");
            String result = resultFormat.format(cal.getTime());
            Integer.parseInt(result);


        }catch(ParseException e){
            Log.e("AddSubject->AddLecure", "getstartDate >>"+ e.toString());
        }
        return cal.getTime();
    }

    private void delSubject(String subjectName) {
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        adapter.excuteQuery(query);

        query = "DELETE FROM LectureList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM AssignmentList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM ExamList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delSubject(subjectName);

    }
}