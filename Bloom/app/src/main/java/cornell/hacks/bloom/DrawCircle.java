package cornell.hacks.bloom;

/**
 *
 *@author: Mark Zhao
 *9/19/2015
 *Big Red Hacks
 */

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.DisplayMetrics;
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
    private final float MIN_RADIUS = (float)60.0;
    private final float MAX_RADIUS = (float)500.0;
    private final float MIN_BLUR = (float)5.0;
    private final float MAX_BLUR = (float)90.0;
    private boolean growCircle;
    private int alpha;
    private final double SCALING_FACTOR = 3.75; //Scaling for max size
    private float blur;
    private final double DELAY_FACTOR = 400.0;
    private final float VELOCITY_FACTOR = (float)40.0;  //Increase for slower velocity
    private final float DIFFERENCE_FACTOR = (float)0.05; //
    private final float DPI_FACTOR = (float)0.6;
    private Context context;

    public DrawCircle(Context context){
        super(context);
        paint.setAntiAlias(true);
        this.radius = MIN_RADIUS;
        this.growCircle = true;
        this.velocity = 5;
        this.delay = 20;
        alpha = 255;
        blur = (float) 0.0;
        this.context = context;
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);  //Hardware acceleration off
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
        drawCircle(canvas);
    }


    public void drawCircle(Canvas canvas){
        //Intensity can't be 0 - divide by 0 error
        int intValue = CircleFragment.movingintensity;
        if(intValue <= 0){
            intValue = 1;
        }
        setIntensity(intValue);

        //Delay is less when the intensity is greater
        this.delay = (int)( (1/(double)intensity) * DELAY_FACTOR);

        //Full opacity when radius is max, half opacity when radius is at min.
        alpha = (int)((127/(MAX_RADIUS - MIN_RADIUS))*radius + (255 - (MAX_RADIUS*127/(MAX_RADIUS-MIN_RADIUS))));

        //Blur increases as the radius of the circle increases.
        //blur = radius*(MAX_BLUR-MIN_BLUR)/(MAX_RADIUS-MIN_RADIUS) +
        //        MAX_BLUR - MAX_RADIUS*(MAX_BLUR-MIN_BLUR)/(MAX_RADIUS-MIN_RADIUS);

        //Set radius of the circle
        float maxRadius = (float) (intensity * SCALING_FACTOR + MIN_RADIUS);


        if(radius <= MIN_RADIUS && growCircle == false){
            growCircle = true;
        }
        if(radius >= maxRadius && growCircle == true){
            growCircle = false;
        }
        float centerDifference = (float) (Math.abs(this.radius - (maxRadius + MIN_RADIUS)/2.0));
        float velocityAdd = (float)((maxRadius + MIN_RADIUS)/2.0 - MIN_RADIUS);
        float radiusPosition = Math.abs(velocityAdd - centerDifference);
        if(growCircle == true){
            //Velocity is proportional to the difference between max radius and radius
            float velocity = (intensity/VELOCITY_FACTOR) + radiusPosition * DIFFERENCE_FACTOR;
            this.radius = this.radius + velocity;

        }
        else{
            float minDifference = this.radius - MIN_RADIUS;
            float velocity = (intensity/VELOCITY_FACTOR) + radiusPosition * DIFFERENCE_FACTOR;
            this.radius = this.radius - velocity;
        }


        int blueInt = this.getResources().getColor(R.color.blue_500);
        int lightblue = this.getResources().getColor(R.color.light_blue);
        int superlightblue = this.getResources().getColor(R.color.super_light_blue);
        int grayblue = this.getResources().getColor(R.color.gray_blue);
        paint.setColor(blueInt);
        paint.setStyle(Paint.Style.FILL);
        //paint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.OUTER));  //Blur
        Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //create radial gradient
        RadialGradient gradient = new android.graphics.RadialGradient(
                (float)(disp.getWidth() / 2.0), (float) (disp.getHeight() / 1.75),
                radius, new int[]{blueInt, 0xffffff}, new float[]{0.5f, 1.0f},
                android.graphics.Shader.TileMode.CLAMP);

        paint.setAlpha(alpha); //Transparency
        paint.setShader(gradient);
        canvas.drawCircle((float)(disp.getWidth() / 2.0), (float) (disp.getHeight() / 1.75),
                convertDpToPixel(this.radius*DPI_FACTOR,this.context), paint);

        handler.postDelayed(r, delay);

        //animateCircle(canvas, intensity);
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
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
