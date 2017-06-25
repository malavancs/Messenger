package com.blogspot.mrodpg.lapitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar  =(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LapitChat");

        mAuth = FirebaseAuth.getInstance();



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



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

}
