package com.blogspot.mrodpg.lapitchat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    RecyclerView mRecyclerView;
    private AdView mAdView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(getActivity(),"ca-app-pub-2455279011703306~6844025785");
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.request_recyclerview);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(getUid());


        FirebaseRecyclerAdapter<RequestClass, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestClass, UsersViewHolder>(
                RequestClass.class,
                R.layout.request_row,
                UsersViewHolder.class,
                mDatabase


        ) {

            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, final RequestClass model, int position) {


                viewHolder.setName(model.getName());

                viewHolder.setThump(model.getThumb(), getActivity());

                View view = viewHolder.getView();
                Button accept = (Button) view.findViewById(R.id.request_accept);
                Button decline = (Button) view.findViewById(R.id.request_decline);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addfriend(model.getUid());
                     //   requestFriend(model.getUid(),getUid(),model.getName(),model.getThumb());
                    }
                });

                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleterequest(model.getUid());
                    }
                });

            }
        };


        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    private void deleterequest(String ouid) {
        DatabaseReference sd = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(getUid()).child(ouid);
        sd.removeValue();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView mName = (TextView) mView.findViewById(R.id.request_name);
            mName.setText(name.toUpperCase());
        }


        public void setThump(final String thumb, final Context context) {
            final CircleImageView mCirculerImage = (CircleImageView) mView.findViewById(R.id.request_image);

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

        public View getView() {
            return mView;
        }


    }

    public String getUid() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = "";
        try {
            uid = mFirebaseAuth.getCurrentUser().getUid();

        } catch (NullPointerException e) {
            //who cares
        }

        return uid;
    }


    private void addfriend(final String ouid) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("FriendLists").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ouid).child("uid");
        DatabaseReference sd = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(getUid()).child(ouid);
        sd.removeValue();

        mDatabase.setValue(ouid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("FriendLists").child(ouid).child(getUid()).child("uid");
                    md.setValue(getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Now you are friends", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });
    }




}
