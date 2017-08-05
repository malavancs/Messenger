package com.blogspot.mrodpg.lapitchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionPager mSectionPager;
    private TabLayout mTablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar  =(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat on");

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabasereference = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
        mDatabasereference.child("lastseen").setValue("Online");
        mViewPager = (ViewPager)findViewById(R.id.main_tabPager);
        mSectionPager = new SectionPager(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPager);



        mTablayout = (TabLayout)findViewById(R.id.main_tabs);
        mTablayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == R.id.main_log_out)
        {
          FirebaseAuth.getInstance().signOut();
          startActivity(new Intent(getApplicationContext(),StartActivity.class));
            finish();

        }
        if (item.getItemId()==R.id.main_setting)
        {

            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));

        }
        if(item.getItemId()==R.id.main_all_users)
        {
            startActivity(new Intent(getApplicationContext(),UsersActivity.class));
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference mDatabasereference = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
        mDatabasereference.child("lastseen").setValue(getlastscene());

    }
    public String getlastscene()
    {
        String date = new Date().toString();
        String[] data = date.split("\\s+");
        date = data[3];
        return data[1].concat(" " + data[2] + " ").concat(date);

        //Sat Aug 05 10:24:00 GMT+05:30 2017
    }
    public String getUid()
    {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String uid="";
        try
        {
            uid = mFirebaseAuth.getCurrentUser().getUid();

        }
        catch (NullPointerException e)
        {
            //who cares
        }

        return uid;
    }
}
