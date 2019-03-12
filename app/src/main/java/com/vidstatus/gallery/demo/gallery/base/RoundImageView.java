package com.vidstatus.gallery.demo.gallery.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.quvideo.vivashow.library.commonutils.ComUtil;

/**
 * Created by ALiu on 2018/4/16.
 */
public class RoundImageView extends android.support.v7.widget.AppCompatImageView {


    public RoundImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Path clicPath;

    private void init(Context context) {

        r = ComUtil.dpToPixel(context, 4);
    }

    private int r;


    @Override
    protected void onDraw(Canvas canvas) {
        if (clicPath == null) {
            clicPath = new Path();
            RectF rectF = new RectF();
            rectF.left = 0;
            rectF.top = 0;
            rectF.right = getWidth();
            rectF.bottom = getHeight();
            clicPath.addRoundRect(rectF, r, r, Path.Direction.CCW);
        }
        canvas.clipPath(clicPath);
        super.onDraw(canvas);
    }
}
