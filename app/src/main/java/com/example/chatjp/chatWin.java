package com.example.chatjp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String receiverimg, receiverUid, receiverName, SenderUID;
    CircleImageView profile;
    TextView receiverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String  receiveriimg;
    String senderRoom, receiverRoom;
    RecyclerView messangesAdpter;
    ArrayList<msgModelclass> messagessArrayList;
    messagesAdpter messagesAdpter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        /*  EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        messangesAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messangesAdpter.setLayoutManager(linearLayoutManager);
        messagesAdpter = new messagesAdpter(chatWin.this, messagessArrayList);
        messangesAdpter.setAdapter(messagesAdpter);




        receiverName = getIntent().getStringExtra("nameeee");
        receiverimg = getIntent().getStringExtra("receiverimg");
        receiverUid = getIntent().getStringExtra("uid");

        messagessArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);


        profile = findViewById(R.id.profileimgg);
        receiverNName = findViewById(R.id.receivername);

        Picasso.get().load(receiverimg).into(profile);
        receiverNName.setText(""+ receiverName);

        DatabaseReference reference = database.getInstance().getReference("User");
        Query checkuser = reference.orderByChild("username").equalTo("nameeee");
        DatabaseReference chatreference = database.getInstance().getReference("User");
        Query checkuserr = chatreference.orderByChild("username").equalTo("nameeee");


        checkuserr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagessArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);
                    messagessArrayList.add(messages);
                }
                messagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        checkuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("nameee").child("imageuri").getValue(String.class);
                receiveriimg = senderImg;
                Log.d("LOG","image rece");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//      SenderUID = firebaseAuth.getUid();
        senderRoom = SenderUID+receiverUid;
        receiverRoom = receiverUid+SenderUID;


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chatWin.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(message, SenderUID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child("senderRoom").child("messages").
                        push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child("receiverRoom").child("messages").
                                        push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
}