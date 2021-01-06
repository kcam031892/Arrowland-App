package com.arrowland.arrowland.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.arrowland.arrowland.Adapter.ReservationAdapter;
import com.arrowland.arrowland.Classes.CheckInternet;
import com.arrowland.arrowland.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mhack Bautista on 8/7/2018.
 */

public class ReservationListFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation_list,null);
        getActivity().setTitle("My Reservation");

        viewPager = view.findViewById(R.id.reservation_list_viewpager);

        setupViewPage(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        CheckInternet ci = new CheckInternet(getContext());

        if(!ci.isConnected()) {
            ci.displayNoInternetMessage();
        }





        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPage(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ReservationListPendingFragment(), "Pending");
        adapter.addFragment(new ReservationAccceptedFragment(), "Accepted");
        adapter.addFragment(new ReservationListCompletedFragment(), "Completed");
        viewPager.setAdapter(adapter);






    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }





}
