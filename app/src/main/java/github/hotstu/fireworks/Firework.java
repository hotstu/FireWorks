package github.hotstu.fireworks;// Daniel Shiffman


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import static github.hotstu.fireworks.PContext.random;


class Firework {
    private final static PVector gravity = new PVector(0f, 0.2f);
    ArrayList<Particle> particles;    // An arraylist for all the particles
    Particle firework;
    float hu;

    Firework(float x, float y) {
        hu = random(255);
        firework = new Particle(x, y, hu);
        particles = new ArrayList<>();   // Initialize the arraylist
    }

    boolean done() {
        return (firework == null && particles.isEmpty());
    }

    boolean firstExplode = false;

    void run(Canvas canvas, Paint paint) {
        if (firework != null) {//炸裂前
            firework.applyForce(gravity);
            firework.update();
            firework.display(canvas, paint);

            if (firework.explode()) {
                int fragments = (int) random(60, 100);//碎片个数
                for (int i = 0; i < fragments; i++) {
                    particles.add(new Particle(firework.location, hu));    // Add "num" amount of particles to the arraylist
                }
                firework = null;
                firstExplode = true;
            }
        } else {//炸裂后
            if (firstExplode) {//屏幕一闪
                firstExplode = false;
                canvas.drawColor(Color.HSVToColor(new float[]{hu, 0.6f, 0.6f}));
            }

            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.applyForce(gravity);
                p.update();
                p.display(canvas, paint);
                if (p.isDead()) {
                    particles.remove(i);
                }
            }
        }

    }

}