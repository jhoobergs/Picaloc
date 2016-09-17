package be.tomberndjesse.picaloc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class SwitcherActivity extends BaseActivity {
    MyPageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);

        enableLocationUpdates(2000, 5000); //Fastest updateInterval and updateInterval


        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager =
                (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();
        fList.add(new OverviewFragment());
        fList.add(new PostFragment());
        fList.add(new StatisticsFragment());
        return fList;
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }



}
