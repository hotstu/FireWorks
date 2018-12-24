package github.hotstu.fireworks;

import java.util.Random;

/**
 * Created by hotstuNg on 2016/8/29.
 */

public class PContext {
    static final float PI = (float) Math.PI;
    static final float HALF_PI    = PI / 2.0f;
    static final float THIRD_PI   = PI / 3.0f;
    static final float QUARTER_PI = PI / 4.0f;
    static final float TWO_PI     = PI * 2.0f;
    static final float TAU        = PI * 2.0f;

    static final float DEG_TO_RAD = PI/180.0f;
    static final float RAD_TO_DEG = 180.0f/PI;

    static Random internalRandom;

    /**
     * Return a random number in the range [0, howbig).
     * <P>
     * The number returned will range from zero up to
     * (but not including) 'howbig'.
     */
    public static final float random(float howbig) {
        // for some reason (rounding error?) Math.random() * 3
        // can sometimes return '3' (once in ~30 million tries)
        // so a check was added to avoid the inclusion of 'howbig'

        // avoid an infinite loop
        if (howbig == 0) return 0;

        // internal random number object
        if (internalRandom == null) internalRandom = new Random();

        float value = 0;
        do {
            //value = (float)Math.random() * howbig;
            value = internalRandom.nextFloat() * howbig;
        } while (value == howbig);
        return value;
    }


    /**
     * Return a random number in the range [howsmall, howbig).
     * <P>
     * The number returned will range from 'howsmall' up to
     * (but not including 'howbig'.
     * <P>
     * If howsmall is >= howbig, howsmall will be returned,
     * meaning that random(5, 5) will return 5 (useful)
     * and random(7, 4) will return 7 (not useful.. better idea?)
     */
    public static final float random(float howsmall, float howbig) {
        if (howsmall >= howbig) return howsmall;
        float diff = howbig - howsmall;
        return random(diff) + howsmall;
    }


    public static final void randomSeed(long what) {
        // internal random number object
        if (internalRandom == null) internalRandom = new Random();
        internalRandom.setSeed(what);
    }


    //////////////////////////////////////////////////////////////

    //region MATH

    // lots of convenience methods for math with floats.
    // doubles are overkill for processing applets, and casting
    // things all the time is annoying, thus the functions below.


    static public final float abs(float n) {
        return (n < 0) ? -n : n;
    }

    static public final int abs(int n) {
        return (n < 0) ? -n : n;
    }

    static public final float sq(float a) {
        return a*a;
    }

    static public final float sqrt(float a) {
        return (float)Math.sqrt(a);
    }

    static public final float log(float a) {
        return (float)Math.log(a);
    }

    static public final float exp(float a) {
        return (float)Math.exp(a);
    }

    static public final float pow(float a, float b) {
        return (float)Math.pow(a, b);
    }


    static public final int max(int a, int b) {
        return (a > b) ? a : b;
    }

    static public final float max(float a, float b) {
        return (a > b) ? a : b;
    }


    static public final int max(int a, int b, int c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }

    static public final float max(float a, float b, float c) {
        return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
    }


    /**
     * Find the maximum value in an array.
     * Throws an ArrayIndexOutOfBoundsException if the array is length 0.
     * @param list the source array
     * @return The maximum value
     */
    static public final int max(int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int max = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] > max) max = list[i];
        }
        return max;
    }

    /**
     * Find the maximum value in an array.
     * Throws an ArrayIndexOutOfBoundsException if the array is length 0.
     * @param list the source array
     * @return The maximum value
     */
    static public final float max(float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        float max = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] > max) max = list[i];
        }
        return max;
    }


    static public final int min(int a, int b) {
        return (a < b) ? a : b;
    }

    static public final float min(float a, float b) {
        return (a < b) ? a : b;
    }


    static public final int min(int a, int b, int c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }

    static public final float min(float a, float b, float c) {
        return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
    }


    /**
     * Find the minimum value in an array.
     * Throws an ArrayIndexOutOfBoundsException if the array is length 0.
     * @param list the source array
     * @return The minimum value
     */
    static public final int min(int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int min = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] < min) min = list[i];
        }
        return min;
    }
    /**
     * Find the minimum value in an array.
     * Throws an ArrayIndexOutOfBoundsException if the array is length 0.
     * @param list the source array
     * @return The minimum value
     */
    static public final float min(float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        float min = list[0];
        for (int i = 1; i < list.length; i++) {
            if (list[i] < min) min = list[i];
        }
        return min;
    }


    static public final int constrain(int amt, int low, int high) {
        return (amt < low) ? low : ((amt > high) ? high : amt);
    }

    static public final float constrain(float amt, float low, float high) {
        return (amt < low) ? low : ((amt > high) ? high : amt);
    }


    static public final float sin(float angle) {
        return (float)Math.sin(angle);
    }

    static public final float cos(float angle) {
        return (float)Math.cos(angle);
    }

    static public final float tan(float angle) {
        return (float)Math.tan(angle);
    }


    static public final float asin(float value) {
        return (float)Math.asin(value);
    }

    static public final float acos(float value) {
        return (float)Math.acos(value);
    }

    static public final float atan(float value) {
        return (float)Math.atan(value);
    }

    static public final float atan2(float a, float b) {
        return (float)Math.atan2(a, b);
    }


    static public final float degrees(float radians) {
        return radians * RAD_TO_DEG;
    }

    static public final float radians(float degrees) {
        return degrees * DEG_TO_RAD;
    }


    static public final int ceil(float what) {
        return (int) Math.ceil(what);
    }

    static public final int floor(float what) {
        return (int) Math.floor(what);
    }

    static public final int round(float what) {
        return (int) Math.round(what);
    }


    static public final float mag(float a, float b) {
        return (float)Math.sqrt(a*a + b*b);
    }

    static public final float mag(float a, float b, float c) {
        return (float)Math.sqrt(a*a + b*b + c*c);
    }


    static public final float dist(float x1, float y1, float x2, float y2) {
        return sqrt(sq(x2-x1) + sq(y2-y1));
    }

    static public final float dist(float x1, float y1, float z1,
                                   float x2, float y2, float z2) {
        return sqrt(sq(x2-x1) + sq(y2-y1) + sq(z2-z1));
    }


    static public final float lerp(float start, float stop, float amt) {
        return start + (stop-start) * amt;
    }

    /**
     * Normalize a value to exist between 0 and 1 (inclusive).
     * Mathematically the opposite of lerp(), figures out what proportion
     * a particular value is relative to start and stop coordinates.
     */
    static public final float norm(float value, float start, float stop) {
        return (value - start) / (stop - start);
    }

    /**
     * Convenience function to map a variable from one coordinate space
     * to another. Equivalent to unlerp() followed by lerp().
     */
    static public final float map(float value,
                                  float istart, float istop,
                                  float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }


    //endregion
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    // COLOR DATATYPE INTERPOLATION

    // Against our better judgement.


    /**
     * Interpolate between two colors, using the current color mode.
     */
    public static int lerpColor(int c1, int c2, float amt) {
        float a1 = ((c1 >> 24) & 0xff);
        float r1 = (c1 >> 16) & 0xff;
        float g1 = (c1 >> 8) & 0xff;
        float b1 = c1 & 0xff;
        float a2 = (c2 >> 24) & 0xff;
        float r2 = (c2 >> 16) & 0xff;
        float g2 = (c2 >> 8) & 0xff;
        float b2 = c2 & 0xff;

        return (((int) (a1 + (a2-a1)*amt) << 24) |
                ((int) (r1 + (r2-r1)*amt) << 16) |
                ((int) (g1 + (g2-g1)*amt) << 8) |
                ((int) (b1 + (b2-b1)*amt)));
    }



}
