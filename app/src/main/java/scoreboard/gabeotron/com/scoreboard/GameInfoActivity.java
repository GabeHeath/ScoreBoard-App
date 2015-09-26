package scoreboard.gabeotron.com.scoreboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class GameInfoActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private RecyclerView recyclerView;
    private GameInfoAdapter gameInfoAdapter;
    private CircularProgressBar mCirProgBar;
    private Handler mHandler = new Handler();
    private HandleXML obj;
    private String url = "https://www.boardgamegeek.com/xmlapi2/thing?stats=1&id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_bar);
        mCirProgBar = (CircularProgressBar) findViewById(R.id.game_circ_prog_bar);
        AnimationUtils.fadeDown(mCirProgBar);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getIntent().getExtras();
                String dataID = bundle.getString("id");

                String finalURL = url + dataID;

                obj = new HandleXML(finalURL);
                obj.fetchXML("id");

                while (obj.parsingComplete) ;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.fadeDown(mCirProgBar);
                        loadGameInfo(obj);
                    }
                });
            }
        };

        Thread bggSearchThread = new Thread(r);
        bggSearchThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadGameInfo(HandleXML obj){
        setContentView(R.layout.activity_game_info);

        ImageView header = (ImageView) findViewById(R.id.header);

        //Ranks & Ratings
        TextView mAvgRating = (TextView) findViewById(R.id.game_average_rating);

        try {
            Picasso.with(this).load("http://" + obj.getIdImage().toString()).into(header);
        }catch(NullPointerException e) {
            header.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Bundle bundle = getIntent().getExtras();
        String dataName = bundle.getString("name");


        collapsingToolbar.setExpandedTitleTextAppearance(R.style.HeaderTitle);


        collapsingToolbar.setTitle(dataName);

        if(obj.getIdAvgRating().length() > 4){
            mAvgRating.append(obj.getIdAvgRating().substring(0,4) + "/10");
        } else {
            mAvgRating.append(obj.getIdAvgRating() + "/10");
        }

        NestedScrollView mNestedScrollView = (NestedScrollView) findViewById(R.id.game_scroll_view);




        //fabEdit.setShowAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up));
       // .setHideAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_down));
        //FloatingActionButton actionA = new FloatingActionButton(getBaseContext());
//        actionA.setTitle("Hide/Show Action above");
//        actionA.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//            }
//        });

        //final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.game_fab_menu);
        //menuMultipleActions.addButton(actionA);


//        ScrollView mNestedScrollView = (ScrollView) findViewById(R.id.game_scroll_view);



//        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
//
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        List<String> list = new ArrayList<String>();
//        list.add("A-Bomb (HAS)");
//        list.add("A.I.M.");
//        list.add("Abe");
//        list.add("Abin");
//        list.add("Abomination");
//        list.add("Abraxas");
//        list.add("Absorbing");
//        list.add("Adam");
//        list.add("Agent Bob");
//        list.add("Agent Zero");
//        list.add("Air Walker");
//        list.add("Ajax");
//        list.add("Alan Scott");
//        list.add("Alex Mercer");
//        list.add("Alex Woolsly");
//        list.add("Alfred Pennyworth");
//
//
//        if (gameInfoAdapter == null) {
//            gameInfoAdapter = new GameInfoAdapter(list);
//            recyclerView.setAdapter(gameInfoAdapter);
//        }

    }
}
