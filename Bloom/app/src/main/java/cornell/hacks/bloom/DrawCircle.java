package cornell.hacks.bloom;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.WindowManager;

public class DrawCircle extends View {
    private int intensity;  // = int from 0 to 100 depending on the intensity of the signal.
    private Paint paint = new Paint();
    private int width = this.getWidth();
    private int height = this.getHeight();
    private int duration = 1000;
    private int radius;
    private Handler handler = new Handler();
    private final int DELAY = 10;
    private int velocity;
    private final int MIN_RADIUS = 50;
    private boolean growCircle;

    public DrawCircle(Context context,int intensity){
        //Empty Constructor
        super(context);
        this.intensity = intensity;
        paint.setAntiAlias(true);
        this.radius = MIN_RADIUS;
        this.growCircle = true;
        this.velocity = 5;
    }

    public void setIntensity(int intensity){
        this.intensity = intensity;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    @Override
    public void onDraw(Canvas canvas){
        drawCircle(canvas, intensity);
    }


    public void drawCircle(Canvas canvas,int intValue){
        setIntensity(intValue);

        //Set radius of the circle
        int maxRadius = intensity * 5 + MIN_RADIUS;
        int velocity = (int) (intensity/5.0);
        if(radius <= MIN_RADIUS && growCircle == false){
            growCircle = true;
        }
        if(radius >= maxRadius && growCircle == true){
            growCircle = false;
        }

        if(growCircle == true){
            this.radius = this.radius + velocity;
        }
        else{
            this.radius = this.radius - velocity;
        }


        int blueInt = this.getResources().getColor(R.color.blue_500);
        paint.setColor(blueInt);
        paint.setStyle(Paint.Style.FILL);
        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        canvas.drawCircle(disp.getWidth() / 2, (float) (disp.getHeight() / 1.75), (float) (this.radius), paint);

        handler.postDelayed(r, DELAY);

        //animateCircle(canvas, intensity);
    }

/*
    public void animateCircle(Canvas canvas, int intValue){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(SCALE_X, (float)3.0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(SCALE_Y, (float)(intensity/10.0));
        System.out.println((float)(intensity/10.0));
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY);
        animator.setDuration(duration * 1);
        animator.start();

    }
*/







}
