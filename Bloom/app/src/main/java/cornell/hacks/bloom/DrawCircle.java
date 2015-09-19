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
    //private int width = this.getWidth();
    //private int height = this.getHeight();
    //private int duration = 1000;
    private float radius;
    private Handler handler = new Handler();
    private int delay;
    private int velocity;
    private final float MIN_RADIUS = (float)75.0;
    private final float MAX_RADIUS = (float)500.0;
    private final float MIN_BLUR = (float)5.0;
    private final float MAX_BLUR = (float)90.0;
    private boolean growCircle;
    private int alpha;
    private final double SCALING_FACTOR = 3.75;
    private float blur;

    public DrawCircle(Context context,int intensity){
        //Empty Constructor
        super(context);
        this.intensity = intensity;
        paint.setAntiAlias(true);
        this.radius = MIN_RADIUS;
        this.growCircle = true;
        this.velocity = 5;
        this.delay = 20;
        alpha = 255;
        blur = (float) 0.0;
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
        //Intensity can't be 0 - divide by 0 error
        if(intValue <= 0){
            intValue = 1;
        }
        setIntensity(intValue);

        //Delay is less when the intensity is greater
        this.delay = (int)( (1/(double)intensity) * 2500.0);

        //Full opacity when radius is max, half opacity when radius is at min.
        alpha = (int)((127/(MAX_RADIUS - MIN_RADIUS))*radius + (255 - (MAX_RADIUS*127/(MAX_RADIUS-MIN_RADIUS))));

        //Blur increases as the radius of the circle increases.
        blur = radius*(MAX_BLUR-MIN_BLUR)/(MAX_RADIUS-MIN_RADIUS) +
                MAX_BLUR - MAX_RADIUS*(MAX_BLUR-MIN_BLUR)/(MAX_RADIUS-MIN_RADIUS);

        //Set radius of the circle
        float maxRadius = (float) (intensity * SCALING_FACTOR + MIN_RADIUS);
        float velocity = (float) (intensity/5.0);
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
        paint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL));  //Blur
        paint.setAlpha(alpha); //Transparency
        Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        canvas.drawCircle(disp.getWidth() / 2, (float) (disp.getHeight() / 1.75), this.radius, paint);

        handler.postDelayed(r, delay);

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
