package scoreboard.gabeotron.com.scoreboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gabeheath on 9/26/15.
 */
public class BGGActivity extends AppCompatActivity {
    BGGViewPager mViewPager;
    static String gameId;
    static String gameName;
    static int currentFragmentPage;
    private BGGPagerAdapter bggPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgg_activity);
        mViewPager = (BGGViewPager) findViewById(R.id.bgg_view_pager);
        mViewPager.setAdapter(new BGGPagerAdapter(getSupportFragmentManager(),this.getApplicationContext()));
//        mViewPager = new ViewPager(this);
//        mViewPager.setId(R.id.bgg_view_pager);
//        setContentView(mViewPager);
//        bggPagerAdapter = new BGGPagerAdapter(getSupportFragmentManager(),this.getApplicationContext());
//        mViewPager.setAdapter(bggPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragmentPage = position;
                Fragment fragment = ((BGGPagerAdapter)mViewPager.getAdapter()).getFragment(position);

                if(position == 1 && fragment != null) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class BGGPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
        private Context mContext;

        public BGGPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
            mContext = context;

        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new BGGSearchFragment();
            } else {
                return new GameInfoFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if(obj instanceof Fragment) {
                //record fragment tag here
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if(tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        mViewPager.setCurrentItem(item, smoothScroll);
    }

    //Send the game id and name that was clicked in search recyclerview to
    //the game info fragment
    public void getDataForGameInfo(String id, String name) {
        gameId = id;
        gameName = name;

        setCurrentItem(1, true);
    }
}
