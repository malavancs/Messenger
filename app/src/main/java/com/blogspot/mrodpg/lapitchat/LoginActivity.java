package com.blogspot.mrodpg.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {



    private TextInputLayout login_email;
    private TextInputLayout login_password;
    private Button login_btn;
    private Toolbar mToolbar;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgressDialogue;
    private TextView already;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        already = (TextView)findViewById(R.id.textViewSignup);
        login_btn  = (Button)findViewById(R.id.login_signin);
        login_email = (TextInputLayout)findViewById(R.id.login_email);
        login_password = (TextInputLayout)findViewById(R.id.login_password);

        mToolbar = (Toolbar)findViewById(R.id.login_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mProgressDialogue = new ProgressDialog(this);
        mProgressDialogue.setTitle("Please wait...");

       login_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               mProgressDialogue.show();

               try {
                   String email = login_email.getEditText().getText().toString();
                   String password = login_password.getEditText().getText().toString();

                   login(email, password);
               }
               catch (Exception e)
               {
                   Toast.makeText(getApplicationContext(),"Check the form and try again",Toast.LENGTH_LONG).show();
               }

           }
       });




        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });




    }

    private void login(String email,String password) {



        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                        mProgressDialogue.dismiss();
                    Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {


                    Toast.makeText(getApplicationContext(),"Check the form and try again",Toast.LENGTH_LONG).show();
                    mProgressDialogue.hide();
                }
            }
        });




    }
}
