package github.hotstu.fireworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;



public class ColorfullView2 extends View  {
    private static final String TAG = "ColorfullView";
    ArrayList<Firework> fireworks;
    private Paint mParticlePaint;
    private Paint mBitmapPaint;
    private Canvas bitmapCanvas;
    private GestureDetectorCompat mGesture;
    private Bitmap canvasBitmap;

    public ColorfullView2(Context context) {
        super(context);
        init();
    }

    public ColorfullView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorfullView2(Context context, AttributeSet attrs, int defStyleAttr) {
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
                ViewCompat.postInvalidateOnAnimation(ColorfullView2.this);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //enableHardwareLayer(this);
        if (canvasBitmap == null || canvasBitmap.isRecycled()) {
            canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(canvasBitmap);
        }
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.run(bitmapCanvas, mParticlePaint);
            if (f.done()) {
                fireworks.remove(i);
            }
        }
        canvas.drawBitmap(canvasBitmap,0, 0, mBitmapPaint);
        if (!fireworks.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


}
