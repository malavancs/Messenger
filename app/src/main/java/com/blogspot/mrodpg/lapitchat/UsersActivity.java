package com.blogspot.mrodpg.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        mToolbar = (Toolbar) findViewById(R.id.users_app_bar);
        mUsersList = (RecyclerView)findViewById(R.id.users_recyclerview);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("Users");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, final Users model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setPrivacy(model.getPrivacy());
                viewHolder.setImage(getApplicationContext(),model.getThumb_image());
                final String userid = getRef(position).getKey();
                final String names = model.getName();
                final String images = model.getImage();
                final String status = model.getStatus();



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("name",names);
                        intent.putExtra("image",images);
                        intent.putExtra("status",status);
                        intent.putExtra("privacy",model.getPrivacy());
                        intent.putExtra("uid",userid);
                        startActivity(intent);


                    }
                });

            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }




    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;
        boolean mpPrivacy = false;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setPrivacy(String privacy)
        {
            if(privacy.equals("Everyone"))
            {
                mpPrivacy = true;
            }
            else
            {
                mpPrivacy = false;

            }
        }

        public void setName(String name)
        {
            TextView mName = (TextView)mView.findViewById(R.id.users_single_name);

            mName.setText(name);
        }
        public void setStatus(String status)
        {
            TextView mStatus = (TextView)mView.findViewById(R.id.users_single_status);
            mStatus.setText(status);
        }
        public void setImage(final Context context, final String url)
        {
            if(mpPrivacy) {
                final CircleImageView mImage = (CircleImageView) mView.findViewById(R.id.users_single_image);
                Picasso.with(context)
                        .load(url)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(mImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(url).placeholder(R.drawable.default_avatar).into(mImage);
                            }
                        });
            }

            else
            {
                ImageView mImage = (ImageView) mView.findViewById(R.id.users_single_image);
                Picasso.with(context).load(R.drawable.default_avatar).into(mImage);
            }
        }

    }
}
