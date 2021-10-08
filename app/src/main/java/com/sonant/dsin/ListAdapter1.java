package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemAdapter> mList;
    public Context mContext;

    public ListAdapter1(List<ItemAdapter> list, Context context) {
        super();
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ItemAdapter itemAdapter = mList.get(position);
        ((ViewHolder) viewHolder).mTv_name.setText(itemAdapter.getName());
        ((ViewHolder) viewHolder).heading.setText(itemAdapter.getHeading());

        Glide
                .with(mContext)
                .load(itemAdapter.getBookurl())
                .placeholder(R.drawable.default_book)
                .into(((ListAdapter1.ViewHolder) viewHolder).mImg);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTv_name, heading;

        public ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv_name = (TextView) itemView.findViewById(R.id.textt);
            mImg = (ImageView) itemView.findViewById(R.id.image_3);
            heading = (TextView) itemView.findViewById(R.id.tite);


            // on item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        int pos = getAdapterPosition();
                        // check if item still exists
                        if (pos != RecyclerView.NO_POSITION) {


                            ItemAdapter clickedDataItem = mList.get(pos);
                            if (mList.size() > 1) {
                                Intent intent = new Intent(mContext, SubjectInternalActivity.class);
                                intent.putExtra("name", clickedDataItem.getKey() + "/" + clickedDataItem.getHeading());
                                mContext.startActivity(intent);

                            } else {
                                Intent intent = new Intent(mContext, SubjectInternalActivity.class);
                                intent.putExtra("heading", clickedDataItem.getHeading());
                                intent.putExtra("name", clickedDataItem.getHeading());
                                mContext.startActivity(intent);
                            }
                        }
                    }
                    // get position
                catch (Exception e)
                {}

                }
            });
        }
    }
}

