package com.example.muhammadashfaq.snippet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muhammadashfaq.snippet.MessageAdapter.MessageAdapter;
import com.example.muhammadashfaq.snippet.Model.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    ImageButton btnAddNewType,btnSendMessage;
    EditText edtTxtMsgContent;

    private static String mCharUser,mChatUserName,mChatUserThumb;
    Toolbar chatToolbar;
    DatabaseReference mRootRef,mRootRefrence;

    TextView txtVuUserName,txtVuLastSeen;
    CircleImageView mProfileImgVu;
    FirebaseAuth mAuth;
    String mCurruntUid;

    DatabaseReference mUserDbRef,mChatRef;

    private final static int TOTAL_ITEMS_TO_LOAD_EACH_TIME =10;
    private int mCurrentPage=1;

    //New solotyin
    private int itemPostion=0;
    private String mLastKey;
    private String mPrevKey;
    String messageKey;

    //Messages Working
    RecyclerView mMsgesRecylerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private final List<MessageModel> mMessageList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    public int GELLARY_PICK=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        chatToolbar=findViewById(R.id.app_bar_chat);
        setSupportActionBar(chatToolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        btnAddNewType=findViewById(R.id.msg_add_new_type);
        btnSendMessage=findViewById(R.id.msg_send);
        edtTxtMsgContent=findViewById(R.id.msg_content);

        mCharUser=getIntent().getStringExtra("UserId");
        mChatUserName=getIntent().getStringExtra("user_name");
        mChatUserThumb=getIntent().getStringExtra("user_image");

        mAuth=FirebaseAuth.getInstance();
        mCurruntUid=mAuth.getCurrentUser().getUid();
        mRootRefrence=FirebaseDatabase.getInstance().getReference();
        mRootRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mCharUser);


        LayoutInflater inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView=inflater.inflate(R.layout.custom_chat_toolbar,null);
        actionBar.setCustomView(actionBarView);

        mUserDbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mCurruntUid);
        mChatRef=FirebaseDatabase.getInstance().getReference().child("Chats").child(mCurruntUid);

        //MessagesWorking
        mMsgesRecylerView=findViewById(R.id.recyceler_view_messages_list);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        linearLayoutManager=new LinearLayoutManager(this);

        mMsgesRecylerView.setHasFixedSize(true);
        mMsgesRecylerView.setLayoutManager(linearLayoutManager);

        messageAdapter=new MessageAdapter(mMessageList);

        mMsgesRecylerView.setAdapter(messageAdapter);

        loadMessages();








        //Custom actionBar items
        txtVuUserName=actionBarView.findViewById(R.id.custom_bar_user_name);
        txtVuLastSeen=actionBarView.findViewById(R.id.custom_bar_last_seen);
        mProfileImgVu=actionBarView.findViewById(R.id.custom_bar_image);

        txtVuUserName.setText(mChatUserName);
        Glide.with(getApplicationContext()).load(mChatUserThumb).into(mProfileImgVu);

        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null){
                    if(dataSnapshot.hasChild("online")){
                        String online=dataSnapshot.child("online").getValue().toString();

                        if(online.equals("true")){
                            txtVuLastSeen.setText("Online");
                        }else{
                            GetTimeAgo getTimeAgo=new GetTimeAgo();
                            long lasttime=Long.parseLong(online);
                            String lasttimeSeen=GetTimeAgo.getTimeAgo(lasttime,getBaseContext());
                            txtVuLastSeen.setText(lasttimeSeen);
                        }
                    }else{
                        Toast.makeText(ChatActivity.this, "User not online", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Adding Chat to Our App
      mRootRefrence.child("Chat").child(mCurruntUid).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              if(!dataSnapshot.hasChild(mCharUser)){
                  Map chatAddMap=new HashMap();
                  chatAddMap.put("seen",false);
                  chatAddMap.put("timestamp",ServerValue.TIMESTAMP);

                  Map chatUserMap=new HashMap();
                  chatUserMap.put("Chats/"+mCurruntUid+"/"+mCharUser,chatAddMap);
                  chatUserMap.put("Chats/"+mCharUser+"/"+mCurruntUid,chatAddMap);

                  mRootRefrence.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                      @Override
                      public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                          if(databaseError!=null){
                              Toast.makeText(ChatActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  });

              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

      btnSendMessage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              sendMessage();
          }
      });

      swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              mCurrentPage++;
              itemPostion=0;
              loadMoreMessages();
          }
      });

    }

    private void loadMoreMessages() {
        DatabaseReference messageRef=mRootRefrence.child("messages").child(mCurruntUid).child(mCharUser);

        Query messageQuery=messageRef.orderByKey().endAt(mLastKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessageModel model=dataSnapshot.getValue(MessageModel.class);
                messageKey=dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){
                    mMessageList.add(itemPostion++,model);
                }else{
                    mPrevKey=mLastKey;
                }
                if(itemPostion==1){
                    mLastKey=messageKey;
                }



                Log.d("Total Keys","Last Key :"+mLastKey+"Prev Key :"+mPrevKey+"Message Key: "+messageKey);



                messageAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
                linearLayoutManager.scrollToPositionWithOffset(10,0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadMessages() {
        DatabaseReference messageRef=mRootRefrence.child("messages").child(mCurruntUid).child(mCharUser);

        Query messageQuery=messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD_EACH_TIME);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessageModel model=dataSnapshot.getValue(MessageModel.class);

                itemPostion++;
                if(itemPostion==1){
                    String messageKey=dataSnapshot.getKey();
                    mLastKey=messageKey;
                    mPrevKey=messageKey;
                }
                mMessageList.add(model);
                messageAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage() {
        String message=edtTxtMsgContent.getText().toString();

        if(!TextUtils.isEmpty(message)){
            String currentUserRef="messages/"+mCurruntUid+"/"+mCharUser;
            String chatUserRef="messages/"+mCharUser+"/"+mCurruntUid;

            DatabaseReference userMsgPush=mRootRefrence.child("messages").child(mCurruntUid).child(mCharUser).push();
            String pushID=userMsgPush.getKey();

            Map messageMap=new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurruntUid);

            Map messageUserMap=new HashMap();
            messageUserMap.put(currentUserRef+"/"+pushID,messageMap);
            messageUserMap.put(chatUserRef+"/"+pushID,messageMap);

            edtTxtMsgContent.setText("");

            mRootRefrence.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Toast.makeText(ChatActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            edtTxtMsgContent.setError("Enter Some Message Content");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserDbRef.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserDbRef.child("online").setValue("true");
    }

    public void sendImageMessage(View view) {
        Intent gallaryIntent=new Intent();
        gallaryIntent.setType("image/*");
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallaryIntent,"Select image"),GELLARY_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GELLARY_PICK && resultCode== Activity.RESULT_OK){
            Uri imageUri=data.getData();

            final String currentUserRef="messages/"+mCurruntUid+"/"+mCharUser;
            final String chatUserRef="messages/"+mCharUser+"/"+mCurruntUid;

            DatabaseReference userMsgPush=mRootRefrence.child("messages").child(mCurruntUid).child(mCharUser).push();
            final String pushID=userMsgPush.getKey();

            StorageReference filePathRef= FirebaseStorage.getInstance().getReference().child("image_messages").child(pushID+ ".jpg");

            filePathRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        String downloadImageUrl=task.getResult().getDownloadUrl().toString();

                        Map messageMap=new HashMap();
                        messageMap.put("message",downloadImageUrl);
                        messageMap.put("seen",false);
                        messageMap.put("type","image");
                        messageMap.put("time",ServerValue.TIMESTAMP);
                        messageMap.put("from",mCurruntUid);

                        Map messageUserMap=new HashMap();
                        messageUserMap.put(currentUserRef+"/"+pushID,messageMap);
                        messageUserMap.put(chatUserRef+"/"+pushID,messageMap);


                        mRootRefrence.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!=null){
                                    Toast.makeText(ChatActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });
        }
    }
}
