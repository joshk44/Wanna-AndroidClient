package com.wanna.client.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;


public class MyAnimationView extends ImageView {
    private static final String TAG = "AnimationTest:AnimationView";
    private Context mContext = null;

    private static int delay = 58; //retraso entre cuadros en milisegundos

    private boolean mIsPlaying = true;
    private boolean mStartPlaying = true;
    private Long startTime;
    private boolean looping = false;

    private ArrayList<Bitmap> mBitmapList = new ArrayList<Bitmap>();

    private int play_frame = 0;
    private long last_tick = 0;

    public MyAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas c) {
        Log.d(TAG, "onDraw called");
        if (mStartPlaying) {
            Log.d(TAG, "starting animation...");
            play_frame = 0;
            mStartPlaying = false;
            mIsPlaying = true;
            postInvalidate();
        } else if (mIsPlaying) {

            if (play_frame >= mBitmapList.size() && !looping) {
                mIsPlaying = false;
                int draw_x = getWidth() / 2 - (mBitmapList.get(play_frame - 1).getWidth() / 2);
                int draw_y = getHeight() / 2 - (mBitmapList.get(play_frame - 1).getHeight() / 2);
                c.drawBitmap(mBitmapList.get(play_frame - 1), draw_x, draw_y, null);
            } else {
                long time = (System.currentTimeMillis() - last_tick);

                //la imagen mide mide 144x144 entonces dividimos en 2 para centrar y el y lo disminu para que quede centrado arriba
                //reste las dimensiones de las imagenes asi no teniamos problema en el cambio de pantalla resolucion

                int draw_x = getWidth() / 2 - (mBitmapList.get(play_frame).getWidth() / 2);
                int draw_y = getHeight() / 2 - (mBitmapList.get(play_frame).getHeight() / 2);
                if (time >= delay) //the delay time has passed. set next frame
                {
                    last_tick = System.currentTimeMillis();
                    c.drawBitmap(mBitmapList.get(play_frame), draw_x, draw_y, null);
                    play_frame++;
                    if (play_frame >= mBitmapList.size() && looping){
                        play_frame=0;
                    }
                    postInvalidate();
                } else //todava dentro de demora. redibujar trama actual
                {
                    c.drawBitmap(mBitmapList.get(play_frame), draw_x, draw_y, null);
                    postInvalidate();
                }
            }
        }
    }

    public void loadAnimation(String prefix, int nframes, boolean looping, int speed) {
        this.delay = speed;
        this.looping = looping;
        loadAnimation(prefix, nframes);
    }


    //esto ver de hacerlo un subproceso de fondo
    public void loadAnimation(String prefix, int nframes) {
        mBitmapList.clear();
        for (int x = 1; x <= nframes; x++) {
            String name = prefix + "_" + x;
            int res_id = mContext.getResources()
                .getIdentifier(name, "drawable", mContext.getPackageName());
            BitmapDrawable d = (BitmapDrawable) mContext.getResources().getDrawable(res_id);
            mBitmapList.add(d.getBitmap());
        }
        startTime = System.currentTimeMillis();
    }


    public Long startPlaying() {
        return startTime;
    }
}
