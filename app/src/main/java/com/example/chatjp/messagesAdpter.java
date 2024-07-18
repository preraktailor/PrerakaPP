package com.example.chatjp;

import static com.example.chatjp.chatWin.receiveriimg;
import static com.example.chatjp.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdpter extends RecyclerView.Adapter {

    Context context;
    ArrayList<msgModelclass> messagesAdpterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtext.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);


        }
        else{
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtext.setText(messages.getMessage());
            Picasso.get().load(receiveriimg).into(viewHolder.circleImageView);
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }

    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtext;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtext = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtext;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtext = itemView.findViewById(R.id.recivertextset);
        }
    }


}
