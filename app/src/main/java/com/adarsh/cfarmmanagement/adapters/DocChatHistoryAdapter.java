package com.adarsh.cfarmmanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh.cfarmmanagement.DoctorHomeActivity;
import com.adarsh.cfarmmanagement.ImageViewActivity;
import com.adarsh.cfarmmanagement.R;
import com.adarsh.cfarmmanagement.VideoPlayerActivity;
import com.adarsh.cfarmmanagement.model.DocChatHistoryRoot;
import com.bumptech.glide.Glide;
import com.khizar1556.mkvideoplayer.MKPlayer;

public class DocChatHistoryAdapter extends RecyclerView.Adapter<DocChatHistoryAdapter.MyViewHolder> {

    Context context;
    DocChatHistoryRoot docChatHistoryRoot;

    public DocChatHistoryAdapter(Context context, DocChatHistoryRoot docChatHistoryRoot) {
        this.context = context;
        this.docChatHistoryRoot = docChatHistoryRoot;
    }

    @NonNull
    @Override
    public DocChatHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_chat_history_layout, parent, false);
        View itemViewRight = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_chat_history_layout_right, parent, false);
        if (docChatHistoryRoot.recent_chat.get(viewType).sender.equals("doctor")) {
            return new MyViewHolder(itemViewRight);
        } else {
            return new MyViewHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (docChatHistoryRoot.recent_chat.get(position).sender.equals("doctor")) {
            return position;
        } else {
            return position;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull DocChatHistoryAdapter.MyViewHolder holder, int position) {

        holder.msgTitle.setText(docChatHistoryRoot.recent_chat.get(position).title);
        holder.msgBody.setText(docChatHistoryRoot.recent_chat.get(position).message);
        holder.chatDate.setText(docChatHistoryRoot.recent_chat.get(position).date + " , ");
        holder.chatTime.setText(docChatHistoryRoot.recent_chat.get(position).time);

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.msgImg.getLayoutParams();
//        RelativeLayout.LayoutParams params_1 = (RelativeLayout.LayoutParams) holder.chatBubble.getLayoutParams();


        // MKPlayer mkplayer = new MKPlayer((DoctorHomeActivity) context);
        if (docChatHistoryRoot.recent_chat.get(position).image.equals("")) {
            holder.msgImg.setVisibility(View.GONE);
        } else {
            holder.msgImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(docChatHistoryRoot.recent_chat.get(position).image).into(holder.msgImg);
            holder.msgImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("imageUrl", docChatHistoryRoot.recent_chat.get(position).image);
                    context.startActivity(intent);
                }
            });
        }

        if (docChatHistoryRoot.recent_chat.get(position).video.equals("")) {
            holder.msgVideo.setVisibility(View.GONE);
        } else {
            holder.msgVideo.setVisibility(View.VISIBLE);
            Glide.with(context).load(docChatHistoryRoot.recent_chat.get(position).video).into(holder.videoThumbnail);
            holder.msgVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("videoUrl", docChatHistoryRoot.recent_chat.get(position).video);
                    context.startActivity(intent);
                }
            });
        }
//        if (docChatHistoryRoot.recent_chat.get(position).sender.equals("doctor")) {
//
////            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////            params_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        } else {
//            // holder.chatHistoryLayout.setBackgroundColor(Color.MAGENTA);
////            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
////            params_1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        }

    }

    @Override
    public int getItemCount() {
        return docChatHistoryRoot.recent_chat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView msgTitle, msgBody, chatDate, chatTime;
        ImageView msgImg, videoThumbnail;
        RelativeLayout msgVideo, chatHistoryLayout, chatBubble;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            msgTitle = itemView.findViewById(R.id.chat_history_title_msg);
            msgBody = itemView.findViewById(R.id.chat_history_msg);
            msgImg = itemView.findViewById(R.id.chat_history_img);
            msgVideo = itemView.findViewById(R.id.video_layout);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
            chatDate = itemView.findViewById(R.id.chat_date);
            chatTime = itemView.findViewById(R.id.chat_time);
            chatHistoryLayout = itemView.findViewById(R.id.chat_history_layout);
            chatBubble = itemView.findViewById(R.id.chat_bubble_1);
        }
    }
}
