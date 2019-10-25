package com.aaron.widgetlibrary.view.noconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * 描述：这个ListView不会与父亲是个ScrollView与List的产生事件冲突
 */
public class NoConflictInnerListView extends ListView {

    /**
     * The parent scroll view.
     */
    private ScrollView parentScrollView;

    /**
     * The max height.
     */
    private int maxHeight;

    /**
     * Gets the parent scroll view.
     *
     * @return the parent scroll view
     */
    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    /**
     * Sets the parent scroll view.
     *
     * @param parentScrollView the new parent scroll view
     */
    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    /**
     * Gets the max height.
     *
     * @return the max height
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Sets the max height.
     *
     * @param maxHeight the new max height
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * Instantiates a new ab inner list view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public NoConflictInnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 描述：TODO.
     *
     * @param widthMeasureSpec  the width measure spec
     * @param heightMeasureSpec the height measure spec
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:48
     * @see ListView#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
                    MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 描述：TODO.
     *
     * @param ev the ev
     * @return true, if successful
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:48
     * @see android.widget.AbsListView#onInterceptTouchEvent(MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                setParentScrollAble(false);
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Sets the parent scroll able.
     *
     * @param flag the new parent scroll able
     */
    private void setParentScrollAble(boolean flag) {
        parentScrollView.requestDisallowInterceptTouchEvent(!flag);
    }

}
