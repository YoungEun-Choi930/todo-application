package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.todo.friends.FriendsManagementActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendsManagementActivityTest {

    @BeforeClass
    public static void setUp() throws Exception {
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

    public void 테스트01존재하는사용자추가() {   //친구신청이 성공함.

    }

    public void 테스트02존재하지않는사용자추가() {     //친구신청 실패.

    }

    public void 테스트03이미친구인사용자추가() {      //친구신청 실패해야함

    }

    public void 테스트04친구신청수락() {      //친구에 추가되어야함

    }

    public void 테스트05친구삭제() {        //친구가 삭제됨.

    }
}