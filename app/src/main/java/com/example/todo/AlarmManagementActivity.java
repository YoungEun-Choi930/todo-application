package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.GregorianCalendar;
import java.util.List;

public class AlarmManagementActivity extends AppCompatActivity {
    private List<AlarmInfo> alarmInfoList;
    Button btn_del_alarm;
    private int ck=0;
    alarmAdapter alarmAdapter;
    public static AlarmManagementActivity mContext;

    public AlarmManager alarmManager;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_management);
        mContext=this;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        alarmInfoList = getAlarmList();

        RecyclerView recyclerView = findViewById(R.id.recy_alarm);
        alarmAdapter = new alarmAdapter((ArrayList<AlarmInfo>) alarmInfoList);
        recyclerView.setAdapter(alarmAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        alarmAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        btn_del_alarm = (Button)findViewById(R.id.btn_del_alarm);
        btn_del_alarm.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<alarmAdapter.getcheckedList().size();i++){
                alarmInfoList.remove(alarmAdapter.getcheckedList().get(i)); //체크된목록 과목목록에서 제거
                delAlarm(alarmAdapter.getcheckedList().get(i).getSubjectName());
            }
            if(alarmAdapter.getcheckedList().size()>0){
                Toast.makeText(this, "알림 삭제 완료", Toast.LENGTH_SHORT).show();
            }
            btn_del_alarm.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            alarmAdapter.notifyDataSetChanged();
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.alarm_menu, menu); //툴바에 메뉴 설정
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm: // 알림추가버튼 누른 경우
                Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.del_alarm:
                if(ck==0){
                    btnCheck(1);
                    ck=1;
                    btn_del_alarm.setVisibility(View.VISIBLE);
                    break;
                }
                else if(ck==1){
                    btnCheck(0);
                    ck=0;
                    btn_del_alarm.setVisibility(View.GONE);
                    break;
                }

        }
        alarmAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void btnCheck(int i) {
        alarmAdapter.updateCheckBox(i);
        alarmAdapter.notifyDataSetChanged();;
    }


    public List<SubjectInfo> getSubjectList() {
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }

    public List<AlarmInfo> getAlarmList(){
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<AlarmInfo> list = adapter.loadAlarmList();
        return list;
    }

    // 리얼빼고 파라미터 4개인걸로 넘겼어용
    public boolean addAlarm(String subjectName, String exam, String assignment, String video){
        String query = "INSERT INTO AlarmInfoList VALUES('"+
                subjectName+"','"+exam+"','"+assignment+"','"+video+"');";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);

        AlarmInfo alarmInfo = new AlarmInfo(false,subjectName,exam,assignment,video);
        alarmInfoList.add(alarmInfo);
        alarmAdapter.notifyDataSetChanged();
        setSystemAlarm(subjectName, exam, assignment, video);
        return result;
    }
    private boolean delAlarm(String subjectName) {
        String query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);
        delSubjectAlarm(subjectName);
        return result;
    }


    private void setSystemAlarm(String subjectName, String exam, String assignment, String video) {
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        int examnum = Integer.parseInt(exam.substring(0,1));
        int assignmentnum;
        if(assignment.equals("1일 전"))
            assignmentnum=24;
        else
            assignmentnum=Integer.parseInt(assignment.substring(0,1));
        int videonum;
        if(video.equals("1일 전"))
            videonum=24;
        else {
            videonum = Integer.parseInt(video.substring(0, 1));
        }

        // 여기서 에러뜸
       Intent receiverIntent = new Intent(TodoManagementActivity.mContext, AlarmRecevier.class);


        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<String> list = helper.loadLectureDateList(subjectName);            // sqlite에서 강의목록 들고오기

        //lecture
        for(String date: list) {
            String from = date;
            String name = date.substring(12,from.length());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

            Date datetime = null;
            try {
                datetime = dateFormat.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);
            calendar.add(Calendar.HOUR_OF_DAY, -videonum); //몇시간전인지 빼
            int alarmNum = helper.setAlarmNum(name, subjectName);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, receiverIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }

        //assignment
        list = helper.loadAssignmentDateList(subjectName);
        for(String date: list) {
            String from = date;
            String name = date.substring(12,from.length());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date datetime = null;
            try {
                datetime = dateFormat.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);
            calendar.add(Calendar.HOUR_OF_DAY, -assignmentnum);
            int alarmNum = helper.setAlarmNum(name, subjectName);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, receiverIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }

        //exam
        list = helper.loadExamDateList(subjectName);
        for(String date: list) {
            String from = date;
            String name = date.substring(12,from.length());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date datetime = null;
            try {
                datetime = dateFormat.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);
            calendar.add(Calendar.DATE, -examnum);
            int alarmNum = helper.setAlarmNum(name,subjectName);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, receiverIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    public void addSystemAlarm(String subjectName, String alarmName){ //할일 체크여부에 따라 개별로 추가하기 위한 메소드
        Calendar calendar = Calendar.getInstance();
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper();
        int alarmNum = sqLiteDBHelper.setAlarmNum(alarmName,subjectName);
        Intent receiverIntent = new Intent(TodoManagementActivity.mContext, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, receiverIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
    public void delSubjectAlarm(String subjectName){ //과목에 대한 알람을 한꺼번에 삭제
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper();
        List<Integer> alarmNumber = sqLiteDBHelper.loadAlarmSubjectList(subjectName);
        for(int i=0;i<alarmNumber.size();i++){
            delSystemAlarmNum(alarmNumber.get(i));
        }
    }
    public void delSystemAlarmNum(int num){ //delSubjectAlarm에서 사용함. 번호로 삭제
        alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext.getApplicationContext(), TodoManagementActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, num, intent, 0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    public void delSystemAlarm(String alarmName) { //할일 체크 여부에 따라 개별로 삭제하기 위한 메소드
        SQLiteDBHelper helper = new SQLiteDBHelper();
        int alarmNum = helper.loadAlarmNum(alarmName);
        if(alarmNum != -1) {
            alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mContext.getApplicationContext(), TodoManagementActivity.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, intent, 0);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
    /*
    void creatNotification(String channelId, int id, String title, String text){
        Intent intent2 = new Intent(this, TodoManagementActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,101,intent2, PendingIntent.FLAG_UPDATE_CURRENT);


        builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id,builder.build());
    }*/
}