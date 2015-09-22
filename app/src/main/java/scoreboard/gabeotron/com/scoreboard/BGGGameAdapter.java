package scoreboard.gabeotron.com.scoreboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by gabeheath on 9/19/15.
 */
public class BGGGameAdapter extends RecyclerView.Adapter<BGGGameAdapter.MyViewHolder> {

    private HandleXML obj;
    private LayoutInflater inflater;
    private int previousPosition = 0;
    List<BGGGameData> data = Collections.emptyList();



    public BGGGameAdapter (Context context, List<BGGGameData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_bgg_game_search_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BGGGameData current = data.get(position);

        holder.name.setText(current.name);
        holder.year.setText(current.yearPublished);

       // String bggApiUrl = "http://www.boardgamegeek.com/xmlapi2/thing?id=";
       // String finalURL = bggApiUrl + current.gameId;


//        /*
//        Doesn't work very well. Crashes a lot ond slows scrolling
//         */
//        obj = new HandleXML("http://www.boardgamegeek.com/xmlapi2/thing?id=" + current.gameId.toString());
//        obj.fetchXMLThumbnail();
//        while (obj.parsingComplete);
//
//        if (obj.getSoloThumbnail() == null) {
//            Context context = holder.icon.getContext();
//            Picasso.with(context).load(R.drawable.ic_add_white).into(holder.icon);
//        } else {
//
//            Context context = holder.icon.getContext();
//            Picasso.with(context)
//                    .load("http:" + obj.getSoloThumbnail().toString())//cf.geekdo-images.com/images/pic791918_t.jpg")
//                    .placeholder(R.drawable.ic_search_white) // optional
//                    .error(R.drawable.ic_sort_white)
//                    .fit().centerCrop()
//                    .into(holder.icon);
//        }

          if(current.gameId == null) {
              Context context = holder.icon.getContext();
              Picasso.with(context).load(R.drawable.ic_add_white).into(holder.icon);
          } else {
              Context context = holder.icon.getContext();
              Picasso.with(context)
                    .load("https:" + current.gameId.toString())//cf.geekdo-images.com/images/pic791918_t.jpg")
                    .placeholder(R.drawable.ic_search_white) // optional
                    .error(R.drawable.ic_sort_white)
                    .fit().centerCrop()
                    .into(holder.icon);
          }







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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        TextView year;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.recycler_bgg_game_list_name);
            icon = (ImageView) itemView.findViewById(R.id.recycler_bgg_game_list_icon);
            year = (TextView) itemView.findViewById(R.id.recycler_bgg_game_list_year);
        }
    }
}
