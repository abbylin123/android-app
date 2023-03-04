package com.example.test_lognin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test_lognin.account.FragmentAccountSet;
import com.example.test_lognin.calendar.FragmentCalender;
import com.example.test_lognin.calendar.FragmentWeek;
import com.example.test_lognin.event.FragmentEventEdit;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentCalender.onFgbtnWeek, FragmentWeek.onFgbtnMonth, FragmentWeek.onbtnevent, FragmentEventEdit.onbtnSave{
    DrawerLayout drawerLayout; //抽屜布局
    ActionBarDrawerToggle actionBarDrawerToggle;  //動作切換
    Toolbar toolbar;  //工具欄
    NavigationView navigationView;  //導航視圖
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    String userID;
    int id;
    DocumentReference documentReference;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        //Navigation Drawer
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView); //導航欄
        navigationView.setNavigationItemSelectedListener(this);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Navigation Drawer Menu
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true); //漢堡標誌
        actionBarDrawerToggle.syncState();  //同步導航抽屜的狀態
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.list_text);// set your own icon

        //預設主畫面
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction()
                .add(R.id.content_framgmant,new FragmentHome());
        fragmentTransaction.commit();
        id = getIntent().getIntExtra("id", 0);

        //Drawer Header
        View headerView=navigationView.getHeaderView(0);
        TextView name=headerView.findViewById(R.id.nav_user_name);
        TextView mail=headerView.findViewById(R.id.nav_user_email);
        Button edit=headerView.findViewById(R.id.btn_edit);

        //抓取用戶資料
        userID=firebaseAuth.getCurrentUser().getUid();
        documentReference=firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("UserName"));
                mail.setText(documentSnapshot.getString("Email"));
            }
        });

        //利用 button 進行跳頁
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_framgmant,new FragmentAccountSet());
                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
    }


        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem ) {
        drawerLayout.closeDrawer(GravityCompat.START);
        //首頁
        if(menuitem.getItemId()==R.id.home){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentHome());
            fragmentTransaction.commit();
        }
        //新增裝置
        if(menuitem.getItemId()==R.id.AddCamera){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentAddCamera());

            fragmentTransaction.commit();

        }

        //協助
        if(menuitem.getItemId()==R.id.Assist){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentAssist());
            fragmentTransaction.commit();

        }
        //關於
        if(menuitem.getItemId()==R.id.AboutAPP){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentAbout());
            fragmentTransaction.commit();

        }
        /*//帳戶
        if(menuitem.getItemId()==R.id.AccountSet){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentAccountSet());
            fragmentTransaction.commit();

        }*/
        //跌倒影像
        if(menuitem.getItemId()==R.id.Video){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentVideo());
            fragmentTransaction.commit();

        }
        //月跌倒警示紀錄
        if(menuitem.getItemId()==R.id.Warning){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentCalender());
            fragmentTransaction.commit();

        }
        /*//週跌倒警示紀錄
        if(menuitem.getItemId()==R.id.Week){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FragmentWeek());
            fragmentTransaction.commit();

        }*/
        //全螢幕
        if(menuitem.getItemId()==R.id.Fullscreen){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_framgmant,new FullscreenFragment());
            fragmentTransaction.commit();

        }

        return true;
    }

    public void onWeekSelected() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framgmant,new FragmentWeek());
        fragmentTransaction.commit();
    }

    public void onMonthSelected() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framgmant,new FragmentCalender());
        fragmentTransaction.commit();
    }

    @Override
    public void onAddEvent() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framgmant,new FragmentEventEdit());
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveEvent() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_framgmant,new FragmentWeek());
        fragmentTransaction.commit();
    }
}