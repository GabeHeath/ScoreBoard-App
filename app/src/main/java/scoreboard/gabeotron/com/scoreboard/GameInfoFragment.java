package scoreboard.gabeotron.com.scoreboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by gabeheath on 9/26/15.
 */
public class GameInfoFragment extends Fragment {
    private View view;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private RecyclerView recyclerView;
    private GameInfoAdapter gameInfoAdapter;
    private CircularProgressBar mCirProgBar;
    private Handler mHandler = new Handler();
    private ImageView header;
    private Toolbar toolbar;
    private HandleXML obj;
    private TextView mAvgRating;
    private LinearLayout mRoot;
    private String url = "https://www.boardgamegeek.com/xmlapi2/thing?stats=1&id=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("created", "created");
        view = inflater.inflate(R.layout.game_info_fragment, container, false);
        setHasOptionsMenu(true); //Makes the onCreateOptionsMenu show

        mCirProgBar = (CircularProgressBar) view.findViewById(R.id.game_info_circ_prog_bar);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.fadeDown(mCirProgBar);
                    }
                });
            }
        };

        Thread bggSearchThread = new Thread(r);
        bggSearchThread.start();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        String id = BGGActivity.gameId;
        String name = BGGActivity.gameName;

        String finalURL = url + id;

        obj = new HandleXML(finalURL);
        obj.fetchXML("id");

        while (obj.parsingComplete);

        header = (ImageView) view.findViewById(R.id.game_header);
        toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mAvgRating = (TextView) view.findViewById(R.id.game_average_rating);

        AnimationUtils.fadeUp(mCirProgBar);

        try {
            Picasso.with(getContext()).load("http://" + obj.getIdImage().toString()).into(header);
        } catch (NullPointerException e) {
            header.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.HeaderTitle);

        collapsingToolbar.setTitle(name);

        //Ranks & Ratings
        try {
            if (obj.getIdAvgRating().length() > 4) {
                mAvgRating.append(obj.getIdAvgRating().substring(0, 4) + "/10");
            } else {
                mAvgRating.append(obj.getIdAvgRating() + "/10");
            }
        } catch (NullPointerException e) {}

        super.onResume();
    }
}
