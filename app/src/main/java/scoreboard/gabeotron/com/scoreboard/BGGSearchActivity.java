package scoreboard.gabeotron.com.scoreboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Interpolator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


/**
 * Created by gabeheath on 9/19/15.
 */
public class BGGSearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BGGGameAdapter mBGGGameAdapter;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    CoordinatorLayout mCoordinator;
    NavigationView mNavigationView;

    EditText mEditText;
    private CircularProgressBar mCirProgBar;

    private String url = "http://www.boardgamegeek.com/xmlapi2/search?type=boardgame&query=";
    private HandleXML obj;

    ArrayList<String> al1 = new ArrayList<String>();
    ArrayList<String> al2 = new ArrayList<String>();
    ArrayList<String> al3 = new ArrayList<String>();
    ArrayList<String> al4 = new ArrayList<String>();//Thumbnails

    Integer minInput = 3; //Minimum number of characters to perform (non exact match) game search


    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bgg_game_search_activity);

        mToolbar = (Toolbar) findViewById(R.id.bgg_game_search_app_bar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.bgg_game_search_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.bgg_game_search_navigation_drawer);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.bgg_game_search_collapsing_toolbar_layout);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.bgg_game_search_root_coordinator);
        mCirProgBar = (CircularProgressBar) findViewById(R.id.circ_prog_bar);

        mEditText = (EditText) findViewById(R.id.bgg_game_search_field);

        //This code also exists below when search button is pressed in toolbar
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press.

                    performBGGGameSearch();
                }

                return false;
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mBGGGameAdapter = new BGGGameAdapter(this, getData(al1, al2, al3, al4));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bgg_game);
        mRecyclerView.setAdapter(mBGGGameAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));


        /**
         * Setup click events on the Navigation View Items.
         */

        // mFragmentManager = getSupportFragmentManager();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.navigation_item_1) {
                    finish(); // Ends current activity and goes back to previous one.
                }

                return false;
            }

        });


    }

    public static List<BGGGameData> getData(
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> year,
            ArrayList<String> thumbnailUrl) {

        List<BGGGameData> data = new ArrayList<>();

        for (int i = 0; i < id.size() && i < name.size() && i < year.size(); i++) {
            BGGGameData current = new BGGGameData();
            current.gameId = id.get(i);
            current.name = name.get(i);
            current.yearPublished = year.get(i);

           // Log.d("lllllllooogg","value: "+thumbnailUrl+" Position: " + i);

            //Should only be 1 thumbnail
            if(thumbnailUrl == null) {
                current.thumbnail = null;
            } else {

                    current.thumbnail = thumbnailUrl.get(i);

            }

            data.add(current);
        }

        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bgg_game_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_bgg_game_search) {


            performBGGGameSearch();


        }

        return super.onOptionsItemSelected(item);

    }

    public void performBGGGameSearch() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (mEditText.getText().toString().length() < minInput) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getString(R.string.min_search),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AnimationUtils.fadeDown(mCirProgBar);
                            //Hide Keyboard
                            InputMethodManager inputManager = (InputMethodManager) BGGSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    });

                    String mURL = url + mEditText.getText().toString();
                    String finalURL = mURL.replaceAll("\\s", "%20"); //Replace white space with this so it can be read in a URL

                    obj = new HandleXML(finalURL);
                    obj.fetchXML("game");

                    while (obj.parsingComplete) ;

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (obj.getGameId().size() == 0) {
                                AnimationUtils.fadeUp(mCirProgBar);
                                Toast.makeText(getApplicationContext(), getString(R.string.null_search),
                                        Toast.LENGTH_LONG).show();
                            } else {


                                //convertGameIdsToURLs(obj.getGameId()) --- replace argument 1 below with this
                                mBGGGameAdapter = new BGGGameAdapter(BGGSearchActivity.this, getData(obj.getGameId(), obj.getGameName(), obj.getGameYearPublished(), null));
                                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_bgg_game);
                                mRecyclerView.setAdapter(mBGGGameAdapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
                                AnimationUtils.fadeUp(mCirProgBar);

                                updateThumbnails(obj.getGameId(),obj.getGameName(),obj.getGameYearPublished());
                            }
                        }
                    });
                }

            }
        };

        Thread bggSearchThread = new Thread(r);
        bggSearchThread.start();
    }

    public void updateThumbnails(final ArrayList<String> ids, final ArrayList<String> names, final ArrayList<String> years) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                HandleXML obj;

                for (int i = 0; i < ids.size(); i++) {
                    if (i > 1) {
                        //Have to throttle api hits or BGG will return 503 http status codes
                        long futureTime = System.currentTimeMillis() + 350;
                        while (System.currentTimeMillis() < futureTime) {
                            synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    obj = new HandleXML("http://www.boardgamegeek.com/xmlapi2/thing?id=" + ids.get(i));
                    obj.fetchXML("thumbnail");
                    while (obj.parsingComplete) ;

                    al4.add(obj.getSoloThumbnail());

                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mBGGGameAdapter.setThumbnails(getData(ids,names,years,al4));
                    }
                });
            }
        };

        Thread updateThumbnailThread = new Thread(r);
        updateThumbnailThread.start();
    }
}
