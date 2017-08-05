package com.blogspot.mrodpg.lapitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    TextView mName;
    private Toolbar mToolbar;
    private RecyclerView mChatRecycler;


    @Override
    protected void onStart() {
        super.onStart();




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intent = getIntent();
        mName = (TextView)findViewById(R.id.chat_name);
        mName.setText(intent.getStringExtra("name"));
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mChatRecycler = (RecyclerView)findViewById(R.id.chat_recyclerview);


        mChatRecycler.setHasFixedSize(true);
        mChatRecycler.setLayoutManager(new LinearLayoutManager(this));
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return true;

    }
}
