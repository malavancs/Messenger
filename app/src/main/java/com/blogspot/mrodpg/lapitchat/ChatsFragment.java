package com.blogspot.mrodpg.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment {
    RecyclerView chats_recyclerview;

    public ChatsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chats_recyclerview  =(RecyclerView)view.findViewById(R.id.chats_recyclerview);



        chats_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        chats_recyclerview.setHasFixedSize(true);


    }

    @Override
    public void onStart() {


        super.onStart();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("UserChat").child(getUid());
        Query query = mDatabase.orderByChild("name");
        query.keepSynced(true);

        FirebaseRecyclerAdapter<ChatFragmentRecycler, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatFragmentRecycler, UsersViewHolder>(
                ChatFragmentRecycler.class,
                R.layout.users_single_friends,
                UsersViewHolder.class,
                query


        ) {

            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, final ChatFragmentRecycler model, int position) {

                //viewHolder.setName(model.getUsername());
                //viewHolder.setUid(model.getUid());
                 String uid = model.getUid();
                viewHolder.setName(model.getName());
                viewHolder.setlastmessage(model.getLastmessage());
                viewHolder.setThump(model.getThumb(), getActivity());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   final String[] thumb = new String[1];

                        final DatabaseReference thumbChanger = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid());
                        thumbChanger.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DatabaseReference thumbUpdate = FirebaseDatabase.getInstance().getReference().child("UserChat").child(getUid()).child(model.uid);
                                thumbUpdate.child("thumb").setValue(dataSnapshot.child("thumb_image").getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserChat").child(getUid()).child(model.getUid());

                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("uid", model.getUid());
                        //intent.putExtra("thumb_for_chat", thumbs);
                        intent.putExtra("thumb", model.getThumb());

                        startActivity(intent);
                        // DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());





                    }
                });
            }
        };


                chats_recyclerview.setAdapter(firebaseRecyclerAdapter);


            }




    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String name)
        {
            TextView mName = (TextView)mView.findViewById(R.id.request_name);
            mName.setText(name);
        }
        public void setlastmessage(String uid)
        {
            TextView mName = (TextView)mView.findViewById(R.id.friends_single_status);
            mName.setText(uid);
        }
        public void setThump(final String thumb, final Context context)
        {
            final CircleImageView mCirculerImage =  (CircleImageView)mView.findViewById(R.id.request_image);

            Picasso.with(context)
                    .load(thumb)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.default_avatar)
                    .into(mCirculerImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(thumb)
                                    .placeholder(R.drawable.default_avatar)
                                    .into(mCirculerImage);
                        }
                    });
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
    public String getlastscene()
    {
        String date = new Date().toString();
        String[] data = date.split("\\s+");
        date = data[3];
        return data[1].concat(" " + data[2] + " ").concat(date);

        //Sat Aug 05 10:24:00 GMT+05:30 2017
    }
}
