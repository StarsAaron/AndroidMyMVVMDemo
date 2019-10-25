package com.aaron.widgetlibrary.view.noconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 名称：AbOuterScrollView.java
 * 描述：这个ScrollView与内部的滑动不冲突
 */
public class NoConflictOuterScrollView extends ScrollView {

    /**
     * The m gesture detector.
     */
    private GestureDetector mGestureDetector;

    /**
     * Instantiates a new ab pager scroll view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public NoConflictOuterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * 描述：TODO.
     *
     * @param ev the ev
     * @return true, if successful
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    /**
     * The Class YScrollDetector.
     */
    class YScrollDetector extends SimpleOnGestureListener {

        /**
         * 描述：TODO.
         *
         * @param e1        the e1
         * @param e2        the e2
         * @param distanceX the distance x
         * @param distanceY the distance y
         * @return true, if successful
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY != 0 && distanceX != 0) {

            }
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }


}
