package com.tonkaw_zaa.liveat500px.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inthecheesefactory.thecheeselibrary.view.SlidingTabLayout;
import com.tonkaw_zaa.liveat500px.R;
import com.tonkaw_zaa.liveat500px.dao.PhotoItemDao;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MoreInfoFragment extends Fragment {
    ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    PhotoItemDao dao;

    public MoreInfoFragment() {
        super();
    }

    public static MoreInfoFragment newInstance(PhotoItemDao dao) {
        MoreInfoFragment fragment = new MoreInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("dao",dao);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        dao = getArguments().getParcelable("dao");

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_info, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        setHasOptionsMenu(true);
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return PhotoSummaryFragment.newInstance(dao);
                    case 1:
                        return PhotoInfoFragment.newInstance(dao);
                    case 2:
                        return PhotoTagsFragment.newInstance(dao);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Summary";
                    case 1:
                        return "Info";
                    case 2:
                        return "Tags";
                    default:
                        return "";
                }
            }
        });

        slidingTabLayout = (SlidingTabLayout)rootView.findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_more_info, menu);
    }
}
