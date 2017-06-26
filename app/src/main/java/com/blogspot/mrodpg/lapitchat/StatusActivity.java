package com.blogspot.mrodpg.lapitchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {



    private TextInputLayout mStatus;
    private Button mUpdate;
    private Toolbar mToolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        mToolbar = (Toolbar)findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Update status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStatus = (TextInputLayout) findViewById(R.id.status_status);
        mUpdate = (Button)findViewById(R.id.status_update);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());


        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = mStatus.getEditText().getText().toString();
                if (!status.isEmpty())
                {
                    databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please check your internet connection...",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });




                }
            }
        });





    }
}
