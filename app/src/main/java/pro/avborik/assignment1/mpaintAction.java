package pro.avborik.assignment1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by avbor on 25/08/2017.
 */

public class mpaintAction extends View implements View.OnTouchListener {

    private final List<ColorChangedEventListener> colorChangedListeners = new ArrayList<>();
    private ArrayList<CircleShape> shapeList = new ArrayList<>();
    private final ArrayList<CircleShape> shapeList1  = new ArrayList<>();
    private final Paint paint = new Paint();
    private final Random random = new Random();
    private ColorThreadState colorThreadState = ColorThreadState.IDLE;
    private int radius = 70;

    private class CircleShape {

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
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                colorThreadState = ColorThreadState.STARTED;

                int color = random.nextInt();
                ChangeColor(color);

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

        int size = shapeList.size();

        int last = (size >= 10) ? (size - 10) : 0;
        for (int index = size - 1; index >= last; index--) {
            shapeList1.add(shapeList.get(index));
        }

        shapeList.clear();

        invalidate();
    }


    private void ChangeColor(int color) {
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
