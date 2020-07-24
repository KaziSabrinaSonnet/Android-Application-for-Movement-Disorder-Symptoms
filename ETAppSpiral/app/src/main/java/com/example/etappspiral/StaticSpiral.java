package com.example.etappspiral;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.log;


public class StaticSpiral extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    private ArrayList<float []> list = new ArrayList<>();
    private ArrayList<float []> list1 = new ArrayList<>();
    public ArrayList<Double> features = new ArrayList<>();
    String w;
    public static String dataToWrite2;
    public static String dataToWrite3;
    private VelocityTracker mVelocityTracker = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        w = myIntent.getStringExtra("text");
        dv = new DrawingView(this);
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }


    public class DrawingView extends View{

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Canvas  mCanvasT;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;




        public DrawingView(Context c) {
            super(c);

            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.RED);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);


        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.d("YO", "onSizeChange");
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvasT = new Canvas(mBitmap);
            Paint p = new Paint();
            p.setTextSize(100);
            mCanvasT.drawText("Static Spiral Test",20,150,p);
            Log.d("YO",w+","+h);
            int W = mBitmap.getWidth();
            int H = mBitmap.getHeight();
            Log.d("YO",W+","+H);
            int X = 0, Y = 0;
            Double r = new Double((int)(W/30));
            Double theta = new Double(0);
            Double count = new Double(0);
            while (count <110) {
                X = (int)(W / 2 + r * Math.cos(Math.PI * theta / 180));
                Y = (int)(H / 2 - r * Math.sin(Math.PI * theta / 180));
                if(X>=W || Y>H)
                    continue;
                mCanvas.drawCircle(X,Y,15,p);
                r += 0.38;
                count += 0.09;
                theta += 0.9;

            }



        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        int width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        int height = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        private void touch_start(float x, float y, float pressure) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            float[] f = {x,y, getDrawingTime(), pressure };
            list.add(f);

        }

        private void touch_move(float x, float y, float pressure) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                float[] f = {x, y, getDrawingTime(), pressure};
                list.add(f);
            }


        }


        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
            //New Adding Starts

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to submit?")
                    .setTitle("Accept and submit for testing?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File folder = new File(getContext().getFilesDir().toString()+ "/TremorAssessment/StaticSpiral");
                    boolean success = false;
                    if (!folder.exists())
                    {
                        success = folder.mkdirs();
                    }
                    System.out.println(success+"folder");
                    File file = new File(getContext().getFilesDir().toString()+ "/TremorAssessment/StaticSpiral"+"/"+w.substring(9)+".png");
                    if ( !file.exists() )
                    {
                        try {
                            success = file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(success+"file");
                    FileOutputStream ostream = null;
                    try {
                        ostream = new FileOutputStream(file);

                        System.out.println(ostream);
                        DrawingView.this.setDrawingCacheEnabled(true);
                        DrawingView.this.buildDrawingCache();
                        Bitmap bmp = Bitmap.createBitmap(DrawingView.this.getDrawingCache());
                        DrawingView.this.setDrawingCacheEnabled(false);
                        Bitmap well = bmp;
                        Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        Canvas now = new Canvas(save);
                        now.drawRect(new Rect(0, 0, 320, 480), paint);
                        now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 320, 480), null);
                        if (save == null) {
                            System.out.println("NULL bitmap save\n");
                        }
                        save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    } catch (NullPointerException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
                    }

                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
                    }
                    double Appen = appEn(unravel_spiral(),2,0.2, unravel_spiral().size(), std(unravel_spiral()));
                    double Samen = SmEn(unravel_spiral(),2,0.2, unravel_spiral().size(), std(unravel_spiral()));
                    features.add(Appen);
                    features.add(Samen);
                    StaticSpiral.dataToWrite2 = save();
                    writeToFileInternal(getContext().getFilesDir().toString()+ "/TremorAssessment/StaticSpiral", w.substring(9)+"_basic.txt" , StaticSpiral.dataToWrite2);
                    writeToFileInternal(getContext().getFilesDir().toString()+ "/TremorAssessment/StaticSpiral", w.substring(9)+"_Features.txt", feature_save());
                    Intent i = new Intent(StaticSpiral.this,DynamicSpiral.class);
                    i.putExtra("text", w);
                    i.putExtra("StaticEntropy", features);
                    startActivity(i);
                    dialog.dismiss();
                    finish();
                }
            });

            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(StaticSpiral.this,StaticSpiral.class);
                    i.putExtra("text", w);
                    startActivity(i);
                    dialog.dismiss();
                    finish();
                }
            });

            builder.show();
        }


        @Override
        public boolean onTouchEvent( MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            float pressure = event.getPressure();
            int index = event.getActionIndex();
            int pointerId = event.getPointerId(index);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mVelocityTracker == null) {

                        // Retrieve a new VelocityTracker object to watch the velocity
                        // of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {

                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }

                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);
                    touch_start(x, y, pressure);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float velocityX = mVelocityTracker.getXVelocity(pointerId);
                    float velocityY = mVelocityTracker.getYVelocity(pointerId);
                    float[] f1 = {velocityX, velocityY};
                    list1.add(f1);
                    touch_move(x, y, pressure);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mVelocityTracker.recycle();
                    break;
            }
            return true;
        }

        private String save() {
            String data3 = "[";
            int n3 = list.size();
            for (int i = 0; i < n3; i++) {
                data3 += "[";
                data3 += "X:"+ (list.get(i)[0] / width) + "," + "Y:"+ (list.get(i)[1] / height) + "," + "T:" + list.get(i)[2]+","+ "P:"+ list.get(i)[3];
                if (i<list1.size())
                    data3 += ","+ "VX:"+ (list1.get(i)[0]) + "," + "VY:" + (list1.get(i)[1]);
                if (i == n3 - 1)
                    data3 += "]"+"\n";
                else
                    data3 += "]"+"\n";
            }
            data3 += "]";

            return data3;

        }
        private String feature_save() {
            String data3 = "[";
            data3 += "Approximate Entropy: "+ (features.get(0))  + ", " + "Sample Entropy: " + (features.get(1)); // + ","
            data3 += "]";
            return data3;

        }
        private ArrayList<Float> unravel_spiral(){
            int n = list.size();
            ArrayList<Float> x = new ArrayList<Float>();
            ArrayList<Float> y = new ArrayList<Float>();
            for (int i = 0; i < n; i++) {
                x.add(list.get(i)[0] / width);
                y.add(list.get(i)[1] / height);
            }
            ArrayList<Float> data = new ArrayList<Float>();
            int m = x.size();
            for (int i = 0; i < m; i++) {
                data.add((float)(Math.sqrt(Math.pow(x.get(i),2)+ Math.pow(y.get(i),2))));
            }

            return data;


        }
        private double std(ArrayList<Float> data){
            double sd = 0;
            int m= data.size();
            double sum = 0.0, standardDeviation = 0.0;
            int length = data.size();

            for(double num : data) {
                sum += num;
            }

            double mean = sum/length;

            for(double num: data) {
                standardDeviation += Math.pow(num - mean, 2);
            }

            return Math.sqrt(standardDeviation/length);
        }


        private double appEn(ArrayList<Float> list, int m, double r, int N, double std){
            int Cm = 0, Cm1 = 0;
            double err = 0.0, sum = 0.0;
            err = std * r;
            for (int i = 0; i < N - (m + 1) + 1; i++) {
                Cm = Cm1 = 0;
                for (int j = 0; j < N - (m + 1) + 1; j++) {
                    boolean eq = true;
                    for (int k = 0; k < m; k++){
                        if (abs(list.get(i+k) - list.get(j+k)) > err) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) Cm++;
                    int k = m;
                    if (eq && abs(list.get(i+k) - list.get(j+k)) <= err)
                        Cm1++;

                }
                if (Cm > 0 && Cm1 > 0)
                    sum += log((double)Cm / (double)Cm1);
            }

            return sum / (double)(N - m);
        }

        private double SmEn(ArrayList<Float> list, int m, double r, int N, double std)
        {
            int Cm = 0, Cm1 = 0;
            double err = 0.0, sum = 0.0;

            err = std * r;

            for (int i = 0; i < N - (m + 1) + 1; i++) {
                for (int j = i + 1; j < N - (m + 1) + 1; j++) {
                    boolean eq = true;
                    //m - length series
                    for (int k = 0; k < m; k++) {
                        if (abs(list.get(i+k) - list.get(j+k)) > err) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) Cm++;

                    //m+1 - length series
                    int k = m;
                    if (eq && abs(list.get(i+k) - list.get(j+k)) <= err)
                        Cm1++;
                }
            }

            if (Cm > 0 && Cm1 > 0)
                return log((double)Cm / (double)Cm1);
            else
                return 0.0;

        }

        public void writeToFileInternal(String directoryPath, String filename, String data) {
            File pathFolder = new File(directoryPath + File.separator + filename);
            // Here, thisActivity is the current activity
            boolean success = false;
            // Make sure the path directory exists.
            if (!pathFolder.exists()) {
                // Make it, if it doesn't exit
                success = pathFolder.mkdirs();
            }
            File pathFile = new File(pathFolder, w.substring(9)+".txt");
            if(!pathFile.exists()){
                try {
                    pathFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Save your stream, don't forget to flush() it before closing it.
            FileOutputStream fOut = null;
            try {

                fOut = new FileOutputStream(pathFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(data);

                myOutWriter.close();

                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }



}
