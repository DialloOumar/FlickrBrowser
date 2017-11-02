package com.oumardiallo636.android.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        //activateToolbar is defined in BaseActivity
        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if(photo != null){
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);

            Resources resources = getResources();
            String text = resources.getString(R.string.photo_title, photo.getTitle());
//            photoTitle.setText("Title : "+photo.getTitle());
            photoTitle.setText(text);

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText("Author: "+photo.getAuthor());

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText(photo.getTags());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(photoImage);

        }
    }

}
