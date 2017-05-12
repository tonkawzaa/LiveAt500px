package com.tonkaw_zaa.liveat500px.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.tonkaw_zaa.liveat500px.R;
import com.tonkaw_zaa.liveat500px.adapter.PhotoListAdapter;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemCollectionDao;
import com.tonkaw_zaa.liveat500px.datatype.MutableInteger;
import com.tonkaw_zaa.liveat500px.manager.HttpManager;
import com.tonkaw_zaa.liveat500px.manager.PhotoListManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {

    // Variables
    boolean isLoadingMore = false;

    ListView listView;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    PhotoListManager photoListManager;
    Button btnNewPhotos;

    MutableInteger lastPositionInteger;

    /************  Function **************/

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Fragment level's variables

        init(savedInstanceState);


        if(savedInstanceState != null){
            // Restore Instance State
            onRestoreInstanceState(savedInstanceState);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);

        File dir = getContext().getDir("Hello", Context.MODE_PRIVATE);
        Log.d("Storage", String.valueOf(dir));
        File file = new File(dir, "testfile.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("hello".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here

        btnNewPhotos = (Button)rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhotos.setOnClickListener(buttonClickListener);

        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new PhotoListAdapter(lastPositionInteger);
        listAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(listAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);
        listView.setOnScrollListener(listViewScrollListener);

        if(savedInstanceState == null)
        {
            refreshData();
        }

    }

    private void refreshData(){
        if(photoListManager.getCount() == 0)
            reloadData();
        else
            reloadDataNewer();
//            reloadData();
    }



    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));
    }



    private void loadMoreData() {
        if(isLoadingMore)
            return;
        isLoadingMore = true;
        int minId = photoListManager.getMinimunId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListBeforeId(minId);
        call.enqueue(
                new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE));
    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putBundle("photoListManager",
                photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger",
                lastPositionInteger.onSaveInstanceState());
    }

    private void onRestoreInstanceState(Bundle savedInstanceState){
        // Restore instance state here
        photoListManager.onRestoreInstanceState(
                savedInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(
                savedInstanceState.getBundle("lastPositionInteger"));
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showButtonNewPhotos(){
        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_in
        );
        btnNewPhotos.startAnimation(anim);
    }

    private void hideButtonNewPhotos(){
        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_out
        );
    }

    private void showToast(String text){
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

    /***************  Listener Zone ************/
    final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnNewPhotos){
                listView.smoothScrollToPosition(0);
                hideButtonNewPhotos();
            }

        }
    };

    final SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    final AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view,
                             int firstVisibleItem,
                             int visibleItemCount,
                             int totalItemCount) {
            if(view == listView){
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        // Load More
//                        Log.d("ListView", "Load More Trigged");
                        loadMoreData();
                    }
                }
            }
        }
    };

    /****************  Inner Class **************/

    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao>{

        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;

        public PhotoListLoadCallback(int mode){
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if(response.isSuccessful()){
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);

                int top = c == null ? 0 : c.getTop();
                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                }else if (mode == MODE_LOAD_MORE){
                    photoListManager.appendDaoAtBottomPosition(dao);
                }else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagIfCapable(mode);

                listAdapter.setDao(photoListManager.getDao());
                listAdapter.notifyDataSetChanged();

                if ( mode == MODE_RELOAD_NEWER){
                    // Maintain Scroll Position
                    int additionalSize =
                            (dao != null && dao.getData() != null)? dao.getData().size() : 0 ;
                    listAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionalSize,
                            top);
                    if(additionalSize > 0){
                        showButtonNewPhotos();
                    }
                }else {

                }
                showToast("Load Completed");
            }else {
                // Handle
                clearLoadingMoreFlagIfCapable(mode);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            // Handle
            clearLoadingMoreFlagIfCapable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagIfCapable(int mode){
            if(mode == MODE_LOAD_MORE){
                isLoadingMore = false;
            }
        }
    }
}
