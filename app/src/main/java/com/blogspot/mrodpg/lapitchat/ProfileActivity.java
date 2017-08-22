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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView mName;
    TextView mStatus;
    CircleImageView mImage;
    Intent intent;


    String ouid,cuid,oname,othump;
    Button sendReq;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent = getIntent();
        getOurName();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendLists");


        sendReq = (Button)findViewById(R.id.profile_request);


       // cuid = mUserid.getText().toString();
        ouid = intent.getStringExtra("uid");
     //   mProgress = new ProgressDialog(this);
        mName = (TextView)findViewById(R.id.profile_name);
        mStatus = (TextView)findViewById(R.id.profile_status);
        mImage = (CircleImageView)findViewById(R.id.profile_image);
       // mProgress.setTitle("Loading.......");
        //mProgress.setMessage("Please wait");

        mName.setText(intent.getStringExtra("name"));
        //mStatus.setText(intent.getStringExtra("status"));








        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFriend(ouid,getUid(),oname,othump);


                }
        });

    }

    private String getOurName() {

        final DatabaseReference dabaseRefer = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
        dabaseRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oname = dataSnapshot.child("name").getValue().toString();
                othump=dataSnapshot.child("thumb_image").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
return  "sdf";

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
    public void requestFriend(String his,String me,String name,String thumb)
    {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(his).child(me);
        HashMap<String,String> requestCut = new HashMap<>();
        requestCut.put("name",name);
        requestCut.put("uid",me);
        requestCut.put("thumb",thumb);
        db.setValue(requestCut).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
