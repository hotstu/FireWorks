package github.hotstu.fireworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static github.hotstu.fireworks.PContext.dist;
import static github.hotstu.fireworks.PContext.lerpColor;
import static github.hotstu.fireworks.PContext.min;
import static github.hotstu.fireworks.PContext.random;


public class ParticleTextView extends View {
    private static final String TAG = "ParticleTextView";
    private int width = 320;
    private int height = 640;
    private Paint mParticlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ArrayList<Particle> particles = new ArrayList<>();
    int pixelSteps = 6; // Amount of pixels to skip
    private float scaleX;
    private float scaleY;
    private Bitmap canvasBitmap;
    private Paint mBitmapPaint;
    private Canvas bitmapCanvas;

    //
    class Particle {
        PVector pos = new PVector(0, 0);
        PVector vel = new PVector(0, 0);
        PVector acc = new PVector(0, 0);
        PVector target = new PVector(0, 0);

        float closeEnoughTarget = 50;
        float maxSpeed = 4.0f;
        float maxForce = 0.1f;
        float particleSize = 5;
        RectF ovalRect = new RectF();
        boolean isKilled = false;

        int startColor = Color.BLACK;
        int targetColor = Color.BLACK;
        float colorWeight = 0;
        float colorBlendRate = 0.025f;

        void move() {
            // Check if particle is close enough to its target to slow down
            float proximityMult = 1.0f;
            float distance = dist(this.pos.x, this.pos.y, this.target.x, this.target.y);
            if (distance < this.closeEnoughTarget) {
                proximityMult = distance/this.closeEnoughTarget;
            }

            // Add force towards target
            PVector towardsTarget = new PVector(this.target.x, this.target.y);
            towardsTarget.sub(this.pos);
            towardsTarget.normalize();
            towardsTarget.mult(this.maxSpeed*proximityMult);

            PVector steer = new PVector(towardsTarget.x, towardsTarget.y);
            steer.sub(this.vel);
            steer.normalize();
            steer.mult(this.maxForce);
            this.acc.add(steer);

            // Move particle
            this.vel.add(this.acc);
            this.pos.add(this.vel);
            this.acc.mult(0);
        }

        void draw(Canvas canvas) {
            // Draw particle
            int currentColor = lerpColor(this.startColor, this.targetColor, this.colorWeight);
            mParticlePaint.setStyle(Paint.Style.FILL);
            mParticlePaint.setColor(currentColor);
            ovalRect.set(this.pos.x, this.pos.y, this.pos.x + this.particleSize, this.pos.y + this.particleSize);
            canvas.drawOval(ovalRect, mParticlePaint);
            // Blend towards its target color
            if (this.colorWeight < 1.0) {
                this.colorWeight = min(this.colorWeight+this.colorBlendRate, 1.0f);
            }
        }

        boolean reached() {
            return dist(this.pos.x, this.pos.y, this.target.x, this.target.y) < 1;
        }

        void kill() {
            if (! this.isKilled) {
                // Set its target outside the scene
                PVector randomPos = generateRandomPos(width/2, height/2, (width+height)/2);
                this.target.x = randomPos.x;
                this.target.y = randomPos.y;

                // Begin blending its color to black
                this.startColor = lerpColor(this.startColor, this.targetColor, this.colorWeight);
                this.targetColor = Color.BLACK;
                this.colorWeight = 0;

                this.isKilled = true;
            }
        }
    }

    public ParticleTextView(Context context) {
        super(context);
        init();
    }

    public ParticleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParticleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        nextWord("Android");
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.scaleX = w/(float)width;
        this.scaleY = h/(float)height;
        Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");

    }

    // Picks a random position from a point's radius
    PVector generateRandomPos(int x, int y, float mag) {
        PVector sourcePos = new PVector(x, y);
        PVector randomPos = new PVector(random(0, width), random(0, height));

        PVector direction = PVector.sub(randomPos, sourcePos);
        direction.normalize();
        direction.mult(mag);
        sourcePos.add(direction);

        return sourcePos;
    }

    // Makes all particles draw the next word
    void nextWord(String word) {
        // Draw word in memory
        Bitmap tempBtimpap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBtimpap);
        canvas.drawColor(0);
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(96);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create("Arial Bold", Typeface.BOLD));
        canvas.drawText(word, width>>1, height>>1, textPaint);

        // Next color for all pixels to change to
        int newColor = Color.rgb(((int) random(0.0f, 255.0f)), (int)random(0.0f, 255.0f), (int)random(0.0f, 255.0f));

        int particleCount = particles.size();
        int particleIndex = 0;

        // Collect coordinates as indexes into an array
        // This is so we can randomly pick them to get a more fluid motion
        ArrayList<Integer> coordsIndexes = new ArrayList<>();
        for (int i = 0; i < (width*height)-1; i+= pixelSteps) {
            coordsIndexes.add(i);
        }

        for (int i = 0; i < coordsIndexes.size (); i++) {
            // Pick a random coordinate
            int randomIndex = (int)random(0, coordsIndexes.size());
            int coordIndex = coordsIndexes.get(randomIndex);
            coordsIndexes.remove(randomIndex);

            int x = coordIndex % width;
            int y = coordIndex / width;
            // Only continue if the pixel is not blank
            if (tempBtimpap.getPixel(x, y) != 0) {
                // Convert index to its coordinates
                Particle newParticle;

                if (particleIndex < particleCount) {
                    // Use a particle that's already on the screen
                    newParticle = particles.get(particleIndex);
                    newParticle.isKilled = false;
                    particleIndex += 1;
                } else {
                    // Create a new particle
                    newParticle = new Particle();
                    PVector randomPos = generateRandomPos(width/2, height/2, (width+height)/2);
                    newParticle.pos.x = randomPos.x;
                    newParticle.pos.y = randomPos.y;

                    newParticle.maxSpeed = random(2.0f, 5.0f);
                    newParticle.maxForce = newParticle.maxSpeed*0.025f;
                    newParticle.particleSize = random(3, 6);
                    newParticle.colorBlendRate = random(0.0025f, 0.03f);

                    particles.add(newParticle);
                }

                // Blend it from its current color
                newParticle.startColor = lerpColor(newParticle.startColor, newParticle.targetColor, newParticle.colorWeight);
                newParticle.targetColor = newColor;
                newParticle.colorWeight = 0;

                // Assign the particle's new target to seek
                newParticle.target.x = x;
                newParticle.target.y = y;
            }
        }

        // Kill off any left over particles
        if (particleIndex < particleCount) {
            for (int i = particleIndex; i < particleCount; i++) {
                Particle particle = particles.get(i);
                particle.kill();
            }
        }

        tempBtimpap.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvasBitmap == null || canvasBitmap.isRecycled()) {
            canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(canvasBitmap);
            BitmapShader mBitmapShader = new BitmapShader(canvasBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint = new Paint();
            mBitmapPaint.setShader(mBitmapShader);
            //mBitmapPaint.setDither(false);
        }
        bitmapCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        bitmapCanvas.save();
        bitmapCanvas.scale(scaleX, scaleY);

        int total = particles.size();
        int reached = 0;
        for (int x = particles.size ()-1; x > -1; x--) {
            // Simulate and draw pixels
            Particle particle = particles.get(x);
            particle.move();
            particle.draw(bitmapCanvas);
            reached += particle.reached()? 1: 0;
            // Remove any dead pixels out of bounds
            if (particle.isKilled) {
                if (particle.pos.x < 0 || particle.pos.x > width || particle.pos.y < 0 || particle.pos.y > height) {
                    particles.remove(particle);
                }
            }
        }
        bitmapCanvas.restore();
        canvas.drawPaint(mBitmapPaint);

        if (!isDetached && total > reached) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX()/scaleX;
                float y = event.getY()/scaleY;
                for (Particle particle : particles) {
                    if (dist(particle.pos.x, particle.pos.y, x, y) < 85) {
                        particle.kill();
                    }
                }
                ViewCompat.postInvalidateOnAnimation(this);
                break;
        }
        return true;
    }

    boolean isDetached = false;
    @Override
    protected void onDetachedFromWindow() {
        isDetached = true;
        super.onDetachedFromWindow();
    }
}
