package com.tonkaw_zaa.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tonkaw_zaa.liveat500px.R;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemCollectionDao;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemDao;
import com.tonkaw_zaa.liveat500px.manager.PhotoListManager;
import com.tonkaw_zaa.liveat500px.view.PhotoListItem;

/**
 * Created by Tonkaw_Zaa on 4/22/2017.
 */

public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;
    int lastPosition = -1;

    public void setDao(PhotoItemCollectionDao dao) {

        this.dao = dao;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        if(dao.getData() == null)
            return 0;
        return dao.getData().size();
    }

    @Override
    public Object getItem(int position) {

       return dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

/*    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position % 2 == 0? 0:1;
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 2;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*if(getItemViewType(position) == 0){
            PhotoListItem item;
            if(convertView != null)
                item = (PhotoListItem)convertView;
            else
                item = new PhotoListItem(parent.getContext());
            return item;
        }else {
            TextView item;
            if(convertView != null)
                item = (TextView)convertView;
            else
                item = new TextView(parent.getContext());
            item.setText("Position "+ position);
            return item;
        }*/

        PhotoListItem item;
        if(convertView != null)
            item = (PhotoListItem)convertView;
        else
            item = new PhotoListItem(parent.getContext());

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescription(dao.getUserName()  + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        if(position > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPosition = position;
        }

        return item;

    }

    public void increaseLastPosition(int amount){
        lastPosition += amount;
    }
}
