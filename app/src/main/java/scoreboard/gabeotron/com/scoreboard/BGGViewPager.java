package scoreboard.gabeotron.com.scoreboard;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by gabeheath on 9/27/15.
 */
public class BGGViewPager extends ViewPager {
    float lastY = 0;
    float lastX = 0;
    int scrollBuffer = 250;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public BGGViewPager(Context context) {
        super(context);
    }

    public BGGViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (BGGActivity.currentFragmentPage == 0) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (BGGActivity.currentFragmentPage == 0) {
            return false;
        }
        
        return super.onInterceptTouchEvent(ev);
    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (BGGActivity.currentFragmentPage == 1) {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x1 = ev.getX();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    x2 = ev.getX();
//                    float deltaX = x2 - x1;
//                    if (Math.abs(deltaX) > MIN_DISTANCE) {
//                       this.setCurrentItem(0);
//                    }
//                    break;
//            }
//            return false;
//        }
//        return false;
//    }


}


