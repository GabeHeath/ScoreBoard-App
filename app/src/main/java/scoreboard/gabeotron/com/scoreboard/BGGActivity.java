package scoreboard.gabeotron.com.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gabeheath on 9/26/15.
 */
public class BGGActivity extends AppCompatActivity {
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgg_activity);
        mViewPager = (ViewPager) findViewById(R.id.bgg_view_pager);
        mViewPager.setAdapter(new BGGPagerAdapter(
                getSupportFragmentManager()));
    }

    public class BGGPagerAdapter extends FragmentPagerAdapter {

        public BGGPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new BGGSearchFragment();
            } else
                return new GameInfoFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
