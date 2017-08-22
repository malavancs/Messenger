package com.blogspot.mrodpg.lapitchat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FriendProfile extends AppCompatActivity {

    ImageView circleImageView,big;
    TextView name;
    TextView status;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
    mProgress  = new ProgressDialog(this);

      big = (ImageView)findViewById(R.id.friend_image);
        name = (TextView)findViewById(R.id.friend_name);
        status = (TextView)findViewById(R.id.friend_status);
    mProgress.setTitle("Please wait");
    mProgress.setMessage("Loading...");
    mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("uid"));

        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String names = dataSnapshot.child("name").getValue().toString();
                String statuss = dataSnapshot.child("status").getValue().toString();
                 String image = dataSnapshot.child("image").getValue().toString();


                name.setText(names);
                status.setText(statuss);

                Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.NO_CACHE).into(big, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgress.dismiss();
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(big);
                        mProgress.dismiss();

                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgress.dismiss();
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}