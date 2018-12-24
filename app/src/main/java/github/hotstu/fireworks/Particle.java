package github.hotstu.fireworks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static github.hotstu.fireworks.PContext.random;


class Particle {
    PVector location;
    PVector velocity;
    PVector acceleration;
    float life;
    boolean seed = false;
    float hu;
    float radius;

    Particle(float x, float y, float hue) {
        this.hu = hue;
        acceleration = new PVector(0, 0);
        velocity = new PVector(0, random(-20, -10));
        location = new PVector(x, y);
        seed = true;
        life = 255.0f;
        this.radius = random(10, 20);
    }

    Particle(PVector loc, float hue) {
        this.hu = hue;
        acceleration = new PVector(0, 0);
        velocity = PVector.random2D();
        velocity.mult(random(4, 8));
        location = loc.get();
        life = 255.0f;
        this.radius = random(10, 20);

    }

    void applyForce(PVector force) {
        acceleration.add(force);
    }


    boolean explode() {
        if (seed && velocity.y > 0) {
            life = 0;
            return true;
        }
        return false;
    }

    // Method to update location
    void update() {
        velocity.add(acceleration);
        location.add(velocity);
        if (!seed) {//未爆炸前生命不会减少
            life -= 5.0;
            velocity.mult(0.95f);
        }
        acceleration.mult(0);
    }

    // Method to display
    void display(Canvas canvas, Paint paint) {
        paint.setColor(HSBTOColor(hu, 1, 1, (int) life));
        float r;
        if (seed) {
            r = radius;
            paint.setStrokeWidth(radius);
        } else {
             r = radius *.2f;
        }
        canvas.drawCircle(location.x, location.y, r, paint);
    }

    // Is the particle still useful?
    boolean isDead() {
        return life <= 0;
    }


    private int HSBTOColor(float h, float s, float v, int alpha) {
        return Color.HSVToColor(alpha, new float[]{h, s, v});
    }
}