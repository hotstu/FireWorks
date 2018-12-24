package github.hotstu.fireworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by hotstuNg on 2016/8/29.
 */

public class ColorfullView extends View  {
    private static final String TAG = "ColorfullView";
    ArrayList<Firework> fireworks;
    private Paint mParticlePaint;
    private Paint mBitmapPaint;
    private Canvas bitmapCanvas;
    private GestureDetectorCompat mGesture;
    private Bitmap canvasBitmap;
    private BitmapShader mBitmapShader;

    public ColorfullView(Context context) {
        super(context);
        init();
    }

    public ColorfullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorfullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        fireworks = new ArrayList<>();
        mBitmapPaint = new Paint();
        mParticlePaint = new Paint();
        mGesture = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                fireworks.add(new Firework(e.getX(), e.getY()));
                ViewCompat.postInvalidateOnAnimation(ColorfullView.this);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    long counter = SystemClock.elapsedRealtime();
    @Override
    protected void onDraw(Canvas canvas) {
        //enableHardwareLayer(this);
        long start1  = SystemClock.elapsedRealtime();
        if (canvasBitmap == null || canvasBitmap.isRecycled()) {
            canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(canvasBitmap);
            mBitmapShader = new BitmapShader(canvasBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setShader(mBitmapShader);
            mBitmapPaint.setDither(false);
        }
        Log.d(TAG, "onDraw: cost0 " + (SystemClock.elapsedRealtime() - start1));
        start1  = SystemClock.elapsedRealtime();
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.run(bitmapCanvas, mParticlePaint);
            if (f.done()) {
                fireworks.remove(i);
            }
        }
        Log.d(TAG, "onDraw: cost1 " + (SystemClock.elapsedRealtime() - start1));
        start1  = SystemClock.elapsedRealtime();
        canvas.drawPaint(mBitmapPaint);
        Log.d(TAG, "onDraw: cost2 " + (SystemClock.elapsedRealtime() - start1));
        if (!fireworks.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        Log.d(TAG, "onDraw: elapsed: " + (SystemClock.elapsedRealtime() - counter));
        counter = SystemClock.elapsedRealtime();
    }


}
