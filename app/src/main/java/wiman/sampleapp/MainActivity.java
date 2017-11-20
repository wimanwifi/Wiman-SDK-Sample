package wiman.sampleapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import me.wiman.design.ThemeStyle;
import me.wiman.listener.WimanSDK;
import me.wiman.logger.Logger;


public class MainActivity extends AppCompatActivity {

    public static ArrayList<Logger.LogEvent> logStream = new ArrayList<>();
    private ViewPager viewPager;
    private ProfileAdapter pagerAdapter;
    private int previousPage;

    public static boolean askPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return false;

        } else {

            return true;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        askPermission(this);
        WimanSDK.showOptin(MainActivity.this, ThemeStyle.LIGHT);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_tab_layout);
        assert tabLayout != null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SDK Free WiFi");
        toolbar.setTitleTextColor(Color.WHITE);

        final int[] tabTitles = new int[4];
        tabTitles[0] = R.string.prefs;
        tabTitles[1] = R.string.connected;
        tabTitles[2] = R.string.events;
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[2]));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager = findViewById(R.id.pager);
        assert viewPager != null;
        viewPager.setPageMargin(8);
        viewPager.setAdapter(pagerAdapter = new ProfileAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    previousPage = viewPager.getCurrentItem();
                } else if (previousPage >= 0 && previousPage < pagerAdapter.getCount()) {
                    previousPage = -1;
                }
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private class ProfileAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public ProfileAdapter(FragmentManager manager) {
            super(manager);
            fragments = new Fragment[4];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {


            Fragment fragment = fragments[position];
            if (fragment == null) {
                switch (position) {
                    default:
                    case 0:
                        fragment = new PrefsFragment();
                        break;
                    case 1:
                        fragment = new ConnectedFragment();
                        break;
                    case 2:
                        fragment = new EventsFragment();
                        break;

                }
                fragments[position] = fragment;
            }
            if(fragment instanceof ConnectedFragment){
                ((ConnectedFragment) fragment).update(MainActivity.this);
            }
            return fragment;
        }
    }


}