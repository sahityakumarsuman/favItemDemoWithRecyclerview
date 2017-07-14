package an.favlistapp;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import an.favlistapp.app.CustomApplication;
import an.favlistapp.fragments.FavFragment;
import an.favlistapp.fragments.ListFragment;
import an.favlistapp.receiver.NetworkConnectionReceiver;

public class MainActivity extends AppCompatActivity implements NetworkConnectionReceiver.NetworkConnectionReceiverListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onResume() {
        super.onResume();
        CustomApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.setOffscreenPageLimit(0);

    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_contacts,
                R.drawable.ic_tab_favourite
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ListFragment(), "List");
        adapter.addFrag(new FavFragment(), "Favourites");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                viewPager.setCurrentItem(position);
                adapter.refreshFragment(position);
                adapter.notifyDataSetChanged();
                viewPager.destroyDrawingCache();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.parentRelativeLayout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ListFragment _listFragment;
        private FavFragment _favFragment;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    _listFragment = new ListFragment();
                    return _listFragment;
                case 1:
                    _favFragment = new FavFragment();
                    return _favFragment;
            }
            return null;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void refreshFragment(int position) {

            switch (position) {
                case 0:
                    if (_listFragment != null) {
                        _listFragment.getListDataFromNetwork();
                    }
                    break;
                case 1:
                    if (_favFragment != null) {
                        _favFragment.setFavDataTolist();
                    }
                    break;

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
