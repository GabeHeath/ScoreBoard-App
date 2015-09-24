package scoreboard.gabeotron.com.scoreboard;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gabeheath on 9/23/15.
 */
public class GameActivity extends AppCompatActivity {
    private CoordinatorLayout mCoordinator;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private TextView mTextView;

    private String url = "https://www.boardgamegeek.com/xmlapi2/thing?stats=1&id=";
    private HandleXML obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        mCoordinator = (CoordinatorLayout) findViewById(R.id.game_root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.game_collapsing_toolbar_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.game_drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.game_app_bar);
        mNavigationView = (NavigationView) findViewById(R.id.game_navigation_drawer) ;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        //mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerToggle.syncState();


        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("message");
//        mTextView = (TextView) findViewById(R.id.game_id);
//        mTextView.setText(data);

        fetchGame(data);
    }

    private void fetchGame(String id) {

        String finalURL = url + id;
        obj = new HandleXML(finalURL);
        obj.fetchXML("id");

        while (obj.parsingComplete) ;

        Log.d("kkkkkkkkk","value: "+obj.getIdThumbnail());

        Toast.makeText(getApplicationContext(), obj.getIdThumbnail().toString(),
                Toast.LENGTH_LONG).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            GameActivity.this.overridePendingTransition(R.animator.slide2, R.animator.slide);
        }
        return super.onOptionsItemSelected(item);
    }
}
