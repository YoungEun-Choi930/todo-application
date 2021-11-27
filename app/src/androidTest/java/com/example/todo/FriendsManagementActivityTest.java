package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FriendsManagementActivityTest {

    @Before
    public void setUp() throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();


        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();


        ActivityScenario.launch(FriendsManagementActivity.class);
    }

    public void addFriend(String id) {
        onView(ViewMatchers.withId(R.id.add_friend)).perform(click());

        onView(ViewMatchers.withHint("신청을 보낼 아이디를 입력하세요.")).perform(typeText(id));

        onView(ViewMatchers.withText("확인")).perform(click());
    }

    @Test
    public void 존재하는사용자추가() {   //친구신청이 성공함.
        addFriend("test1");


    }

    @Test
    public void 존재하지않는사용자추가() {     //친구신청 실패.
        addFriend("test3");
    }

    @Test
    public void 이미친구인사용자추가() {      //친구신청 실패해야함

    }

    @Test
    public void 친구신청수락() {      //친구에 추가되어야함

    }

    @Test
    public void 친구삭제() {        //친구가 삭제됨.

    }
}