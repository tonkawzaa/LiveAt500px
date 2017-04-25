package com.tonkaw_zaa.liveat500px.manager.http;

import com.tonkaw_zaa.liveat500px.dao.PhotoItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Tonkaw_Zaa on 4/23/2017.
 */

public interface ApiService {
    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();
}
