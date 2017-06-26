package com.blogspot.mrodpg.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {



    private TextView mDisplayname;
    private TextView mStatus;
    private Button mImagechange;
    private Button mStatuschange;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private static final int GALLERY_PICK =1;
    private StorageReference mStorageRef;
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Uploading your profile picture....");
        mProgress.setCanceledOnTouchOutside(false);




        circleImageView = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.settings_image);
        mDisplayname = (TextView)findViewById(R.id.settings_display_name);
        mStatus = (TextView)findViewById(R.id.settings_status);


        mImagechange = (Button)findViewById(R.id.settings_image_btn);
        mStatuschange = (Button) findViewById(R.id.settings_status_btn);


        mFirebaseAuth = FirebaseAuth.getInstance();

        uid = mFirebaseAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_image").child(uid+".jpg");






        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb = dataSnapshot.child("thumb_image").getValue().toString();



                mDisplayname.setText(displayname);
                mStatus.setText(status);

                Picasso.with(SettingsActivity.this)
                        .load(image)
                        .into(circleImageView);









            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mImagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"), GALLERY_PICK);


            }
        });


        mStatuschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StatusActivity.class));

            }
        });











    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode ==RESULT_OK)
        {

            Uri imageuri = data.getData();

            CropImage.activity(imageuri).setAspectRatio(1,1).
                    start(this);

        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);




            if(resultCode == RESULT_OK)
            {
                Uri resulUri = result.getUri();

                mProgress.show();
                mStorageRef.putFile(resulUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                           @SuppressWarnings("VisibleForTests") String downloadurih = task.getResult().getDownloadUrl().toString();
                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("image");
                            mDatabase.setValue(downloadurih).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_LONG).show();
                                        mProgress.dismiss();


                                    }
                                    else
                                    {
                                        mProgress.dismiss();


                                    }

                                }
                            });
                        }


                        else
                        {
                            Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }

                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                    }
                });




            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
