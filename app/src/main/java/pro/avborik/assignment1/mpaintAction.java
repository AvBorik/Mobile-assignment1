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
        //Initiate second array list to store cycles, which will be used for clear, undo and redo methods
    private final ArrayList<CircleShape> shapeList1  = new ArrayList<>();
    // initiate brush for painting cycles
    private final Paint paint = new Paint();
    // initiate basic random method to change color of cycles
    private final Random random = new Random();
    // initiate thread for changing cycling colors(set idle on the default
    private ThreadState threadState = ThreadState.SLEEP;
    // default radius size of cycles
    private int radius = 70;
    // class for shapes and its parameters
    private class CircleShape {

        private float centerX;
        private float centerY;
        private int fillColor;
        private int radius;
         //shapes setter
        CircleShape(float centerX, float centerY, int radius, int fillColor) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.fillColor = fillColor;
        }
        // attributes get methods
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
   // Super classes
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

    // threads initialization
    private void init() {
        setOnTouchListener(this);
       // initiate handler
        final Handler hendler = new Handler();
        // initiate thread and set states
        Thread thread = new Thread(new Runnable() {
            public void run() {
                long timer = 0;
                while(true) {
                    synchronized(threadState) {
                        switch (threadState) {
                            case SLEEP: {
                                break;
                            }

                            case RUN: {
                                timer = SystemClock.currentThreadTimeMillis();
                                threadState = ThreadState.ACTIVE;
                                break;
                            }

                            case ACTIVE: {
                                if (SystemClock.currentThreadTimeMillis() - timer >= 300) {
                                    threadState = ThreadState.DEAD;
                                }
                                break;
                            }

                            case DEAD: {
                                hendler.post(updateColor);
                                threadState = ThreadState.RUN;
                                break;
                            }
                        }
                    }
                }
            }
        });
        // start the thread
        thread.start();
    }
   // shange color on long touch
    Runnable updateColor = new Runnable() {
        public void run() {
            CircleShape s = shapeList.get(shapeList.size() - 1);
            int color = random.nextInt();
            s.setFillColor(color);
            invalidate();
        }
    };
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
           //set motions for multitouch
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // set thread on start state
                threadState = ThreadState.RUN;
                // set random method to color (change color integer number)
                int color = random.nextInt();
                int pointers = motionEvent.getPointerCount();
                // add cycle from multitouch effect
                for (int index = 0; index < pointers; index++) {
                    shapeList.add(new CircleShape(motionEvent.getX(index), motionEvent.getY(index), radius, color));

                }

                break;
            // multitouch
            case MotionEvent.ACTION_UP:
                threadState = ThreadState.SLEEP;
                break;
        }

        shapeList1.clear();
        invalidate();
        return true;
    }
    // clear method
    public void Clear() {
        // clear undo shape list
        shapeList1.clear();

        for(CircleShape circle : shapeList) {
            shapeList1.add(circle);
        }

        shapeList.clear();

        invalidate();
    }
    // ando method
    public void Undo() {
        int size = shapeList.size();
        shapeList1.add(shapeList.get(size - 1));
        shapeList.remove(size - 1);
        invalidate();
    }
    // redo method
    public void Redo() {
        int size = shapeList1.size();

        if (size > 0) {
            shapeList.add(shapeList1.get(size - 1));
            shapeList1.remove(size - 1);
        }

        invalidate();
    }
    // change brush size method
    public void ChangeBrush(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // main method for add cycles on canvas
        super.onDraw(canvas);
        for (CircleShape s : shapeList) {
            paint.setColor(s.getFillColor());
            canvas.drawCircle(s.getCenterX(), s.getCenterY(), s.getRadius(), paint);
        }
    }
}
