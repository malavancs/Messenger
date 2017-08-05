package com.blogspot.mrodpg.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsFragment extends Fragment {


    private  RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    public FriendsFragment()
    {

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FriendLists,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendLists,UsersViewHolder>(
                FriendLists.class,
                R.layout.users_single_friends,
                UsersViewHolder.class,
                mDatabase


        ) {

            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, final FriendLists model, int position) {

                //viewHolder.setName(model.getUsername());
                //viewHolder.setUid(model.getUid());
                final String  uid = model.getUid();



               DatabaseReference userDetails  = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                userDetails.keepSynced(true);

                userDetails.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        View mView = viewHolder.getView();

                        final String name = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        final String status = dataSnapshot.child("status").getValue().toString();
                        final String thumb = dataSnapshot.child("thumb_image").getValue().toString();
                       String mPrivacyOption = dataSnapshot.child("privacy").getValue().toString();


                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserChat").child(getUid()).child(uid);

                                databaseReference.child("name").setValue(name);
                                databaseReference.child("lastmessage").setValue(status);
                                Intent intent = new Intent(getActivity(),ChatActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("uid",uid);
                                startActivity(intent);



                            }
                        });



                        final CircleImageView mCirculerImage =  (CircleImageView)mView.findViewById(R.id.friends_single_image);
                        TextView mStatus = (TextView)mView.findViewById(R.id.friends_single_status);
                        TextView mName = (TextView)mView.findViewById(R.id.friends_single_name);

                        mName.setText(name);
                        mStatus.setText(status);
                        if(!mPrivacyOption.equalsIgnoreCase("Onlyme")) {

                            Picasso.with(getActivity())
                                    .load(thumb)
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.default_avatar)
                                    .into(mCirculerImage, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(getActivity())
                                                    .load(thumb)
                                                    .placeholder(R.drawable.default_avatar)
                                                    .into(mCirculerImage);
                                        }
                                    });
                        }
                        else
                        {
                           Picasso.with(getActivity()).load(R.drawable.default_avatar).into(mCirculerImage);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });







            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.friends_recyclerview);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendLists").child(getUid());
        mDatabase.keepSynced(true);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));






    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
       public void setName(String name)
       {
           TextView mName = (TextView)mView.findViewById(R.id.friends_single_name);
           mName.setText(name);
       }
       public void setUid(String uid)
       {
           TextView mName = (TextView)mView.findViewById(R.id.friends_single_name);
           mName.setText(uid);
       }
       public View getView()
       {
           return mView;
       }


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
