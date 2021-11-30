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

    /* --------------------------- sqlite에서 과목정보 가져오기 ------------------------------------- */
    public static List<SubjectInfo> getSubjectList() {
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }

    /* ------------------------------- 과목 추가, 강의추가 ----------------------------------------- */
    public static boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime, int year, int semester) {
        //startWeekNumber 1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토

        if(Integer.parseInt(startTime) > 2400) return false;
        if(Integer.parseInt(endTime) > 2400) return false;

        if(year < 1900) return false;
        else if(year > 2100) return false;

        // sqlite에 과목 추가
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);

        if(result == false) return false;


        Date startdate = getstartDate(year, semester, startWeekNumber);         // 1주차 강의 시작 날짜 구하기
        int enddate = endWeekNumber - startWeekNumber;          // 시작날짜와 종료날짜 간의 차이 구하기
        if(enddate < 0)
            enddate += 7;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strstartdate;
        String strenddate;

        // firebase에 추가할 때 Hashmap을 전달
        // key: 강의이름, value: Hashmap<String, Integer> (startDate, startTime, endDate, endTime, isDone) 정보
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        HashMap<String, Object> lecturelist = new HashMap<>();


        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);
        String lectureName;

        // 1주차부터 16주차까지 강의 생성
        for(int i = 1; i <= 16; i++)
        {
            strstartdate = dateFormat.format(cal.getTime());        // 시작날짜
            cal.add(Calendar.DATE, enddate);
            strenddate = dateFormat.format(cal.getTime());          // 종료날짜

            for(int j = 1; j <= number; j++)    //사용자가 입력한 한 주의 강의 갯수 만큼
            {
                //sqlite insert
                lectureName = subjectName+" "+i+"주차"+j;
                query = "INSERT INTO LectureList VALUES('" +
                        subjectName+"','"+lectureName+"',"+strstartdate+","+startTime+","+strenddate+","+endTime+",0);";
                result = adapter.excuteQuery(query);

                //firebase insert
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

            cal.add(Calendar.DATE, 7-enddate);  // startdate에 1주일 추가.
        }

        firebaseDB.uploadMyLecture(subjectName, lecturelist);   // firebase에 강의정보 insert

        subjectlist.add(new SubjectInfo(subjectName));
        subjectAdapter.notifyDataSetChanged();          // 화면 강의목록 새로고침

        return result;
    }

    /* --------------------------- 1주차 강의 시작날짜를 구하는 메소드 -------------------------------- */
    private static Date getstartDate(int year, int semester, int startWeekNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = Integer.toString(year);
        if(semester == 1)               // 1학기라면 3월 1일 부터
            date += "0301";
        else if(semester == 2)          // 2학기라면 9월 1일 부터
            date += "0901";

        Calendar cal = Calendar.getInstance();
        try {
            java.util.Date nDate = dateFormat.parse(date);

            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);     // 3/1, 9/1 의 요일 구하기

            while (dayNum != startWeekNumber) {             // 사용자가 입력한 시작 요일이 될 때까지 +1일
                cal.add(Calendar.DATE, 1);
                dayNum = cal.get(Calendar.DAY_OF_WEEK);
            }

        }catch(ParseException e){
            Log.e("AddSubject->AddLecure", "getstartDate >>"+ e.toString());
        }
        return cal.getTime();
    }

    /* ------------------------------------- 과목 삭제 --------------------------------------- */
    private void delSubject(String subjectName) {
        // sqlite delete
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM LectureList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM AssignmentList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM ExamList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        // 과목에 알림이 설정되어 있다면 시스템에 알림을 지운다.
        AlarmInfo alarmInfo = adapter.loadAlarm(subjectName);
        if(alarmInfo != null)
        {
            AlarmManagementActivity activity = new AlarmManagementActivity();
            activity.delSubjectAlarm(subjectName);
        }
        query = "DELETE FROM AlarmInfoList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        adapter.excuteQuery(query);

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delSubject(subjectName);

    }
}