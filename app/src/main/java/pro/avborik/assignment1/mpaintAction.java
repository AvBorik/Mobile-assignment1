package pro.avborik.assignment1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by avbor on 25/08/2017.
 */

public class mpaintAction extends View implements View.OnTouchListener {
   //Variables initiation
        //Initiate array list to store circles
    private ArrayList<CircleShape> shapeList = new ArrayList<>();
        //Initiate second array list to store cicles, which will be used for clear, undo and redo methods
    private final ArrayList<CircleShape> shapeList1  = new ArrayList<>();
    private final Paint paint = new Paint();
    private final Random random = new Random();
    private ColorThreadState colorThreadState = ColorThreadState.IDLE;
    private int radius = 70;

    private abstract class circle {
    }

    private class CircleShape extends circle {

        private float centerX;
        private float centerY;
        private int fillColor;
        private int radius;

        CircleShape(float centerX, float centerY, int radius, int fillColor) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.fillColor = fillColor;
        }
        public float getCenterX() {
            return centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public int getFillColor() {
            return fillColor;
        }

        public void setFillColor(int fillColor) {
            this.fillColor = fillColor;
        }

        public int getRadius() {
            return radius;
        }
    }

    public mpaintAction(Context context) {
        super(context);
        init();
    }

    public mpaintAction(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public mpaintAction(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public mpaintAction(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnTouchListener(this);

        final Handler h = new Handler();

        Thread t = new Thread(new Runnable() {
            public void run() {
                long time = 0;
                while(true) {
                    synchronized(colorThreadState) {
                        switch (colorThreadState) {
                            case IDLE: {
                                break;
                            }

                            case STARTED: {
                                time = SystemClock.currentThreadTimeMillis();
                                colorThreadState = ColorThreadState.ACTIVE;
                                break;
                            }

                            case ACTIVE: {
                                if (SystemClock.currentThreadTimeMillis() - time >= 500) {
                                    colorThreadState = ColorThreadState.COMPLETED;
                                }
                                break;
                            }

                            case COMPLETED: {
                                h.post(updateColor);
                                colorThreadState = ColorThreadState.STARTED;
                                break;
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }

    Runnable updateColor = new Runnable() {
        public void run() {
            circle s = shapeList.get(shapeList.size() - 1);
            if (s instanceof CircleShape) {
                int color = random.nextInt();
                ((CircleShape) s).setFillColor(color);
                invalidate();
            }
        }
    };
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                colorThreadState = ColorThreadState.STARTED;

                int color = random.nextInt();

                for (int index = 0; index < motionEvent.getPointerCount(); index++) {
                    shapeList.add(new CircleShape(motionEvent.getX(index), motionEvent.getY(index), radius, color));
                }

                break;

            case MotionEvent.ACTION_UP:
                colorThreadState = ColorThreadState.IDLE;
                break;
        }

        shapeList1.clear();
        invalidate();
        return true;
    }

    public void Clear() {

        shapeList1.clear();

        for(CircleShape circle : shapeList) {
            shapeList1.add(circle);
        }

        shapeList.clear();

        invalidate();
    }

    public void Undo() {
        int size = shapeList.size();
        shapeList1.add(shapeList.get(size - 1));
        shapeList.remove(size - 1);
        invalidate();
    }

    public void Redo() {
        int size = shapeList1.size();

        if (size > 0) {
            shapeList.add(shapeList1.get(size - 1));
            shapeList1.remove(size - 1);
        }

        invalidate();
    }

    public void ChangeBrush(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (CircleShape s : shapeList) {
            paint.setColor(s.getFillColor());
            canvas.drawCircle(s.getCenterX(), s.getCenterY(), s.getRadius(), paint);
        }
    }
}
