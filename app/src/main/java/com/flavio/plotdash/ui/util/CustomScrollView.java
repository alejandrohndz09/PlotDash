package com.flavio.plotdash.ui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private boolean enableScrolling = true;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return enableScrolling && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return enableScrolling && super.onInterceptTouchEvent(ev);
    }
}
