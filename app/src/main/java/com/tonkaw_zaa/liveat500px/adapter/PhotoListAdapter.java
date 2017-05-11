package com.tonkaw_zaa.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tonkaw_zaa.liveat500px.R;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemCollectionDao;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemDao;
import com.tonkaw_zaa.liveat500px.datatype.MutableInteger;
import com.tonkaw_zaa.liveat500px.manager.PhotoListManager;
import com.tonkaw_zaa.liveat500px.view.PhotoListItem;

/**
 * Created by Tonkaw_Zaa on 4/22/2017.
 */

public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;

    MutableInteger lastPositionInteger;

    public PhotoListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
    }

    public void setDao(PhotoItemCollectionDao dao) {

        this.dao = dao;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 1;
        if(dao.getData() == null)
            return 1;
        return dao.getData().size() +1 ;
    }

    @Override
    public Object getItem(int position) {

       return dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position == getCount() -1 ? 1:0;
    }

    @Override
    public int getViewTypeCount() {
//        return super.getViewTypeCount();
        return 2;
    }

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

        if(position == getCount() -1){
            // Progress Bar
            ProgressBar item;
            if (convertView != null){
                item = (ProgressBar) convertView;
            }else {
                item = new ProgressBar(parent.getContext());
            }
            return item;
        }

        PhotoListItem item;
        if(convertView != null)
            item = (PhotoListItem)convertView;
        else
            item = new PhotoListItem(parent.getContext());

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescription(dao.getUserName()  + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        if(position > lastPositionInteger.getValue()){
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPositionInteger.setValue(position);
        }

        return item;

    }

    public void increaseLastPosition(int amount){
        lastPositionInteger.setValue(lastPositionInteger.getValue() + amount);
    }
}
