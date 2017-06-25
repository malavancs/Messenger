package com.blogspot.mrodpg.lapitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {


    private Button start_reg_button;
    private Button start_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        start_reg_button = (Button)findViewById(R.id.start_create_acc);
        start_login = (Button)findViewById(R.id.start_login);


        start_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
                //finish();
            }
        });

        start_reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
             //   finish();

            }
        });


    }
}
