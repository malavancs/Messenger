package com.blogspot.mrodpg.lapitchat;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import org.w3c.dom.Text;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.IllegalBlockSizeException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    TextView mName;
    private Toolbar mToolbar;
  static   Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

    @Override
    public void onResume(){
        super.onResume();
        DatabaseReference mDatabasereference = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
        mDatabasereference.child("lastseen").setValue("Online");

        // put your code here...

    }
    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference mDatabasereference = FirebaseDatabase.getInstance().getReference().child("Users").child(getUid());
        mDatabasereference.child("lastseen").setValue(getlastscene());
    }

    private RecyclerView mChatRecycler;
    String me,him,namesd,namesd1;
   // LinearLayoutManager mL;


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final String fu = intent.getStringExtra("uid");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(getUid()).child(fu);
        databaseReference.keepSynced(true);

        FirebaseRecyclerAdapter<ChatClass, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatClass, UsersViewHolder>(
                ChatClass.class,
                R.layout.single_msg_text,
                UsersViewHolder.class,
                databaseReference


        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder,  ChatClass model, int position) {

                viewHolder.setmsg(model.getMsg());
              //  viewHolder.setseen(model.getSeen());
                viewHolder.settime(model.getTime());
                String nams;

              TextView mte = (TextView) viewHolder.getmView().findViewById(R.id.single_name);
                if(model.getWho().equals("me"))
                {
                    mte.setText(namesd);
                }
                else
                {
                    mte.setText(namesd1);
                }
               viewHolder.setWho(model.getWho(),getApplicationContext(),me,him);






        }
    };


        mChatRecycler.setAdapter(firebaseRecyclerAdapter);
    }



    EditText mMessage;
    Button mSend;
    TextView LastSeen,Names;
    CircleImageView Cir;


    CircleImageView backButton,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // LastSeen.setText("Sf");

        DatabaseReference ls = FirebaseDatabase.getInstance().getReference();
        ls.keepSynced(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
       getMythumb(getUid());
        getnamesd(getUid());
        getnamesd1(getIntent().getStringExtra("uid"));
       // Toast.makeText(this, me, Toast.LENGTH_SHORT).show();
        him = getIntent().getStringExtra("thumb");
        profile = (CircleImageView)findViewById(R.id.chat_image);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprofile(getIntent().getStringExtra("uid"));
            }
        });
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("thumb")).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.NO_CACHE).into(profile);

        //mL = new LinearLayoutManager(getApplicationContext());
        LastSeen = (TextView)findViewById(R.id.chat_lastseen);

     //   Cir = (CircleImageView)findViewById()

LastSeen.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showprofile(getIntent().getStringExtra("uid"));
    }
});

        final Intent intent = getIntent();
       ls =  ls.child("Users").child(intent.getStringExtra("uid"));
        ls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LastSeen.setText(dataSnapshot.child("lastseen").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mName = (TextView)findViewById(R.id.chat_name);
        mName.setText(intent.getStringExtra("name"));
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprofile(intent.getStringExtra("uid"));
            }
        });
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        mMessage = (EditText)findViewById(R.id.chat_msg_send);
        mSend = (Button)findViewById(R.id.chat_msg_now);

        backButton = (CircleImageView)findViewById(R.id.chat_backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mMessage.getText().toString();

                msg = encryption.encryptOrNull(msg);

                if(msg.isEmpty())
                {
                    return;
                }
                String time = getTime();
                String seen = "UNSEEN";
               // String thumb = intent.getStringExtra("thumb_for_chat");
                HashMap<String,String> msgthread1 = new HashMap<String, String>();
                HashMap<String,String> msgthread2 = new HashMap<>();

                msgthread1.put("msg",msg);
                msgthread1.put("seen",seen);
                msgthread1.put("time",time);
                msgthread1.put("who","me");

                msgthread2.put("msg",msg);
                msgthread2.put("seen",seen);
                msgthread2.put("time",time);
                msgthread2.put("who","him");

                DatabaseReference mD = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mDF = FirebaseDatabase.getInstance().getReference();
                mD.child("Chats").child(intent.getStringExtra("uid")).child(getUid()).push().setValue(msgthread2);
                 mD.child("Chats").child(getUid()).child(intent.getStringExtra("uid")).push().setValue(msgthread1);
                mMessage.setText("");
                mMessage.setHint("Type something......");

            }
        });
        setSupportActionBar(mToolbar);

        mChatRecycler = (RecyclerView)findViewById(R.id.chat_recyclerview);

    LinearLayoutManager mL = new LinearLayoutManager(getApplicationContext());
        mL.setReverseLayout(true);
       // mL.setStackFromEnd(true);

      // mChatRecycler.setHasFixedSize(true);
        mChatRecycler.setLayoutManager(mL);
        
    }

    private String getMythumb(String uid) {

        final String[] finalss = new String[1];
        DatabaseReference da = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        da.addValueEventListener(new ValueEventListener() {
            String finals;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 finals =  dataSnapshot.child("thumb_image").getValue().toString();
                namesd = dataSnapshot.child("name").getValue().toString();
               // Toast.makeText(ChatActivity.this, finals, Toast.LENGTH_SHORT).show();
               me = finals;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return finalss[0];

    }

    private String getnamesd(String uid) {

        final String[] finalss = new String[1];
        DatabaseReference da = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        da.addValueEventListener(new ValueEventListener() {
            String finals;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // finals =  dataSnapshot.child("thumb_image").getValue().toString();
                namesd = dataSnapshot.child("name").getValue().toString();
                // Toast.makeText(ChatActivity.this, finals, Toast.LENGTH_SHORT).show();
              //  me = finals;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return finalss[0];

    }
    private String getnamesd1(String uid) {

        final String[] finalss = new String[1];
        DatabaseReference da = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        da.addValueEventListener(new ValueEventListener() {
            String finals;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // finals =  dataSnapshot.child("thumb_image").getValue().toString();
                namesd1 = dataSnapshot.child("name").getValue().toString();
                // Toast.makeText(ChatActivity.this, finals, Toast.LENGTH_SHORT).show();
                //  me = finals;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return finalss[0];

    }

    private String getTime() {

        String today = new Date().toString();
        String[] data =  today.split("\\s+");

        return data[3];
            //Sat Aug 05 10:24:00 GMT+05:30 2017



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.chat_clear)
        {
            Toast.makeText(this, "Available on next Update", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
      //  boolean mpPrivacy = false;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public View getmView() {
            return mView;
        }

        public void setmsg(String msg)
        {
            TextView mText = (TextView)mView.findViewById(R.id.single_msg);
            msg = encryption.decryptOrNull(msg);
            mText.setText(msg);
        }
        public void setseen(String seen)
        {
            //TextView mSeen = (TextView)mView.findViewById(R.id.single_seen);
           // mSeen.setText(seen);

        }
        public void setimage(final String image, final Context context)
        {
            final CircleImageView circleImageView = (CircleImageView)mView.findViewById(R.id.single_image);

        }
        public void settime(String time)
        {
            TextView mTime = (TextView)mView.findViewById(R.id.single_time);

            mTime.setText(time);
        }
        public void setWho(String who, final Context context, final String me, final String him)
        {

            final CircleImageView circleImageView = (CircleImageView)mView.findViewById(R.id.single_image);
            if(who.equals("me"))
            {

                Picasso.with(context).load(me).networkPolicy(NetworkPolicy.OFFLINE).into(circleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(me).into(circleImageView);

                    }
                });
            }
            else if(who.equals("him"))
            {
                Picasso.with(context).load(him).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(circleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(him).placeholder(R.drawable.default_avatar).into(circleImageView);

                    }
                });
            }
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


    public void showprofile(String uid)
    {
        Intent intent =new Intent(getApplicationContext(),FriendProfile.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
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
