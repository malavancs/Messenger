package com.blogspot.mrodpg.lapitchat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Spinner;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity  {



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
    private FloatingActionButton mShare;
    //private Spinner mSpinner;
    private String mPrivacyOption = "";
    private AlertDialog myDialog;
    private FloatingActionButton mSettingPrivacy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Uploading your profile picture....");
        mProgress.setCanceledOnTouchOutside(false);


        circleImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.settings_image);
        mDisplayname = (TextView) findViewById(R.id.settings_display_name);
        mStatus = (TextView) findViewById(R.id.settings_status);


        mImagechange = (Button) findViewById(R.id.settings_image_btn);
        mStatuschange = (Button) findViewById(R.id.settings_status_btn);
        mSettingPrivacy = (FloatingActionButton) findViewById(R.id.setting_privacy);
        //  mSpinner = (Spinner)findViewById(R.id.spinner);

        final String[] items = {"Everbody", "onlyme", "friends"};


        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Who can see your Profile picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
                mDatabaseReference.keepSynced(true);
                mDatabaseReference.child("privacy").setValue(items[which]);


            }
        });


        builder.setCancelable(false);
        myDialog = builder.create();

        mSettingPrivacy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myDialog.show();
            }
        });


        final List<String> mPrivacy = new ArrayList<>();
        mPrivacy.add("Everyone");
        mPrivacy.add("Friends");
        mPrivacy.add("Onlyme");

        //  ArrayAdapter<String> mAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,mPrivacy);
        // mAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // mSpinner.setAdapter(mAdaptor);


        mFirebaseAuth = FirebaseAuth.getInstance();

        mShare = (FloatingActionButton) findViewById(R.id.setting_share);

        uid = mFirebaseAuth.getCurrentUser().getUid();
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText("Hey !!! add me in ChatBuddy", uid);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mDatabase.keepSynced(true);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_image").child(uid + ".jpg");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String thumb = dataSnapshot.child("thumb_image").getValue().toString();
                mPrivacyOption = dataSnapshot.child("privacy").getValue().toString();
              mPrivacyOption =   mPrivacyOption.toUpperCase();

                //     mSpinner.setSelection(getIndex(mSpinner,mPrivacyOption));
                switch (mPrivacyOption)
                {
                    case "EVERYBODY":               mSettingPrivacy.setImageResource(R.drawable.everybody);
                                                    break;
                    case "ONLYME":                  mSettingPrivacy.setImageResource(R.drawable.onlyme);
                                                    break;
                    case "FRIENDS":                 mSettingPrivacy.setImageResource(R.drawable.friends);
                                                    break;
                    default:                        mSettingPrivacy.setImageResource(R.drawable.everybody);
                                                    break;

                }
                mDisplayname.setText(displayname);
                mStatus.setText(status);
                if (!image.equals("default")) {

                    //   Picasso.with(SettingsActivity.this).load(thumb).placeholder(R.drawable.default_avatar).into(circleImageView);
                    Picasso.with(getApplicationContext())
                            .load(thumb)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(circleImageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(SettingsActivity.this).load(thumb).placeholder(R.drawable.default_avatar).into(circleImageView);
                                }
                            });
                }


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
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


            }
        });


        mStatuschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StatusActivity.class));

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
                    setMinCropWindowSize(500,500)
                    .start(this);

        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);




            if(resultCode == RESULT_OK)
            {

                mProgress.show();
                Uri resulUri = result.getUri();

                final File thumb_filepath = new File(resulUri.getPath());


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filepath);
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                final byte[] thumb_byte = baos.toByteArray();


                final StorageReference thumb_storage = FirebaseStorage.getInstance().getReference().child("profile_image").child("thumb").child(uid +".jpg");

                mStorageRef.putFile(resulUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {

                            //thumb upload


                           @SuppressWarnings("VisibleForTests") final String downloadurih = task.getResult().getDownloadUrl().toString();



                            UploadTask uploadTask = thumb_storage.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    if (thumb_task.isSuccessful())
                                    {
                                        @SuppressWarnings("VisibleForTests")   String thumb_url = thumb_task.getResult().getDownloadUrl().toString();

                                        Map update_value = new HashMap();

                                        update_value.put("image",downloadurih);
                                        update_value.put("thumb_image",thumb_url);

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                        mDatabase.updateChildren(update_value).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful())
                                                {

                                                    Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_LONG).show();
                                                    mProgress.dismiss();
                                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                                }
                                            }
                                        });



                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                        mProgress.dismiss();
                                    }
                                }
                            });


                        }




                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                    }
                });




            }

        }
    }
    public  void shareText(String subject,String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent ,"Share"));
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
