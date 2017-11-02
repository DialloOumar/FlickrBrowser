package com.oumardiallo636.android.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oumar on 7/27/17.
 */

class FlickerRecyclerViewAdapter extends RecyclerView.Adapter<FlickerRecyclerViewAdapter.FlickerImageViewHolder> {

    private static final String TAG = "FlickerRecyclerViewAdap";
    List<Photo> mPhotosList;
    Context mContext;

    public FlickerRecyclerViewAdapter(Context mContext,List<Photo> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosList = mPhotosList;
    }


    @Override
    public FlickerImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //called by layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new View Requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browser, parent, false);

        return new FlickerImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickerImageViewHolder holder, int position) {
        // called by the layout manager when it wants new data in an existing row

        // telling the user that there was no match to his query
        if (mPhotosList == null || (mPhotosList.size() ==0)){
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        }else {
            Photo photoItem = mPhotosList.get(position);
            Log.d(TAG, "onBindViewHolder: "+ photoItem.getTitle() + " --> "+position);
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size() : 1 );
    }

    void loadNewData (List<Photo> newPhoto){
        mPhotosList = newPhoto;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return ((mPhotosList != null) && mPhotosList.size() != 0 ? mPhotosList.get(position):null);
    }

    static class FlickerImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickerImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickerImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickerImageViewHolder: starts");
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.browser_title);
        }
    }
}
