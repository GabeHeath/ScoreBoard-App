package scoreboard.gabeotron.com.scoreboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by gabeheath on 9/20/15.
 */
public class AnimationUtils {
    public static void animate(RecyclerView.ViewHolder holder, boolean scrollingDown) {
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY",scrollingDown==true?10:-10, 0);
//        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX",-300, 0);
        animatorTranslateY.setInterpolator(new DecelerateInterpolator());
        animatorTranslateY.setDuration(250);
        animatorTranslateY.start();


//        /**
//         * Fade in from left - Kind of works
//         */
//        AnimatorSet preAnimate = new AnimatorSet();
//        AnimatorSet runAnimate = new AnimatorSet();
//
//        ObjectAnimator preTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -holder.itemView.getRootView().getWidth() * .25f).setDuration(0);
//        ObjectAnimator preAlpha = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0).setDuration(0);
//
//        ObjectAnimator runTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", 0).setDuration(1000);
//        ObjectAnimator runAlpha = ObjectAnimator.ofFloat(holder.itemView, "alpha", 1).setDuration(1000);
//
//        preAnimate.playTogether(preTranslateX,preAlpha);
//        runAnimate.playTogether(runTranslateX,runAlpha);
//
//        preAnimate.start();
//        runAnimate.start();






    }
}
