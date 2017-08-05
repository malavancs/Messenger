package com.blogspot.mrodpg.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView mName;
    TextView mStatus;
    CircleImageView mImage;
    Intent intent;
    ProgressDialog mProgress;
    EditText mUserid;
    String ouid,cuid;
    Button sendReq;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent = getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendLists");

        mUserid = (EditText)findViewById(R.id.profile_uid);
        sendReq = (Button)findViewById(R.id.profile_request);


        cuid = mUserid.getText().toString();
        ouid = intent.getStringExtra("uid");
        mProgress = new ProgressDialog(this);
        mName = (TextView)findViewById(R.id.profile_name);
        mStatus = (TextView)findViewById(R.id.profile_status);
        mImage = (CircleImageView)findViewById(R.id.profile_image);
        mProgress.setTitle("Loading.......");
        mProgress.setMessage("Please wait");

        mName.setText(intent.getStringExtra("name"));
        mStatus.setText(intent.getStringExtra("status"));


        mProgress.show();
        if(intent.getStringExtra("privacy").equals("true")) {
            Picasso.with(getApplicationContext()).load(intent.getStringExtra("image")).placeholder(R.drawable.default_avatar).into(mImage);
            mProgress.dismiss();
        }
        else
        {
            mImage.setImageResource(R.drawable.default_avatar);
            mProgress.dismiss();
        }




        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ouid.equals(mUserid.getText().toString())) {


                    addfriend(ouid);
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Something went wrong ask for your friend for id", Toast.LENGTH_SHORT).show();
                }
                }
        });

    }

    private void addfriend(String ouid) {
    mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendLists").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ouid).child("uid");


        mDatabase.setValue(ouid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this, "Now you and " + mName.getText().toString() + " are friends", Toast.LENGTH_SHORT).show();

                    DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("FriendLists").child(mUserid.getText().toString()).child(getUid()).child("uid");
                    md.setValue(getUid());


                }
            }
        });

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
