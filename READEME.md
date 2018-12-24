\[自定义view\]在canvas中实现高性能的烟花/粒子特效
================================

新年到了，本文将展示通过自定义view绘制烟花效果的案例，同时介绍一种优化canvas绘制时的性能的方法.

#### 效果展示
每点击一下屏幕会产生一枚烟花，烟花飞到最上空会炸裂成60~100个碎片，
同屏可能有上千个粒子在不停更新它的位置.
#### 项目地址

#### 代码分析
1. 首先项目结构

```bash
└─fireworks
        ColorfullView.java -自定义的view
        Firework.java -对烟花建立模型
        MainActivity.java
        Particle.java -粒子
        PContext.java -工具类
        PVector.java  -向量类
```
2. 使用GestureDetector处理用户输入，每点击一次在当前位置创建一枚烟花\

```java
        mGesture = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                fireworks.add(new Firework(e.getX(), e.getY()));
                ViewCompat.postInvalidateOnAnimation(ColorfullView.this);
                return true;
            }
        });
```
3. FireWork类包含两个属性

```java
    ArrayList<Particle> particles;    // 炸开后的粒子
    Particle firework;                // 炸开前的粒子
```
4. Particle类具有随机的颜色、大小、速度以接近真实
5. 每绘制一帧会调用FireWork的run方法，该方法中会调用当前粒子的update和display方法

```java
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
```

#### 性能优化
这时候功能基本实现了，剩下的就是将每一个烟花绘制在canvas上，通常我们会这样写

```java
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.run(canvas, mParticlePaint);
            if (f.done()) {
                fireworks.remove(i);
            }
        }
        //canvas.drawBitmap(canvasBitmap,0, 0, mBitmapPaint);
        if (!fireworks.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
```
然而你会发现性能很糟糕,帧数随着粒子数的增加直线下降直到个位数，优化如下

```java
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvasBitmap == null || canvasBitmap.isRecycled()) {
            canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(canvasBitmap);
            mBitmapShader = new BitmapShader(canvasBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setShader(mBitmapShader);
            mBitmapPaint.setDither(false);
        }
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.run(bitmapCanvas, mParticlePaint);
            if (f.done()) {
                fireworks.remove(i);
            }
        }
        canvas.drawPaint(mBitmapPaint);
        if (!fireworks.isEmpty()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
```


#### Thanks
some codes were from [Daniel Shiffman](https://github.com/shiffman)
