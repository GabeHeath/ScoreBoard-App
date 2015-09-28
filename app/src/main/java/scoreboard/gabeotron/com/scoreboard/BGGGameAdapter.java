package scoreboard.gabeotron.com.scoreboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by gabeheath on 9/19/15.
 */
public class BGGGameAdapter extends RecyclerSwipeAdapter<BGGGameAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private int previousPosition = 0;
    List<BGGGameData> data = Collections.emptyList();
    Handler mHandler = new Handler();
    private Context mContext;



    public BGGGameAdapter (Context context, List<BGGGameData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.mContext = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_bgg_game_search_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void setThumbnails(List<BGGGameData> data, int position) {
        this.data = data;
        //notifyDataSetChanged();
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final BGGGameData current = data.get(position);

        holder.name.setText(current.name);
        holder.year.setText(current.yearPublished);

        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //Drag from Right
        holder.mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                //when the BottomView totally shows.
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {
                //You are swiping
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {
                //when user's hand released.
            }
        });

        holder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BGGActivity) mContext).getDataForGameInfo(current.gameId, current.name);
                //((BGGActivity) mContext).setCurrentItem(1, true);
            }
        });

        Runnable r = new Runnable() {

            @Override
            public void run() {

                if (current.thumbnail == null) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Context context = holder.thumbnail.getContext();
                            Picasso.with(context).load(R.drawable.ic_default_thumbnail).into(holder.thumbnail);
                        }
                    });


                } else {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Context context = holder.thumbnail.getContext();
                            Picasso.with(context).setIndicatorsEnabled(true);
                            Picasso.with(context)
                                    .load("https:" + current.thumbnail.toString())//cf.geekdo-images.com/images/pic791918_t.jpg")
                                    .placeholder(R.drawable.ic_default_thumbnail) // optional
                                    .error(R.drawable.ic_default_thumbnail)
                                    .fit().centerCrop()
                                    .into(holder.thumbnail);
                        }
                    });

                }

            }
        };

        Thread bggThumbnailSearchThread = new Thread(r);
        bggThumbnailSearchThread.start();










        /**
         * Animations work but disabled for now.
         */
//
//        if(position > previousPosition) { //Scrolling down
//            AnimationUtils.animate(holder, true);
//        } else { //Scrolling up
//            AnimationUtils.animate(holder, false);
//        }
//        previousPosition=position;


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.recycler_swipe_layout;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout mSwipeLayout;
        TextView name;
        ImageView thumbnail;
        TextView year;


        public MyViewHolder(View itemView) {
            super(itemView);

            mSwipeLayout = (SwipeLayout) itemView.findViewById(R.id.recycler_swipe_layout);
            name = (TextView) itemView.findViewById(R.id.recycler_bgg_game_list_name);
            thumbnail = (ImageView) itemView.findViewById(R.id.recycler_bgg_game_list_icon);
            year = (TextView) itemView.findViewById(R.id.recycler_bgg_game_list_year);
        }
    }
}
