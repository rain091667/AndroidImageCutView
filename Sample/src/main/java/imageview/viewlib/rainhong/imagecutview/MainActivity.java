package imageview.viewlib.rainhong.imagecutview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

import imageview.viewlib.rainhong.imagecutviewlib.BaseImageCutView;

public class MainActivity extends AppCompatActivity {
    final AnimationHandle ActivityAnimationHandler = new AnimationHandle(this);
    BaseImageCutView MyImageCutView;
    Bitmap Test1Bitmap;
    Button buttonUp;
    Button buttonDown;
    Button buttonLeft;
    Button buttonRight;
    Button buttonAnimation;
    Thread AnimationThread;
    boolean Animation_Flag;
    int Location_x;
    int Location_y;
    int bmp_Width;
    int bmp_Height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyImageCutView = (BaseImageCutView) findViewById(R.id.MyImageCutView);
        Animation_Flag = false;
        Location_x=0;
        Location_y=0;

        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonAnimation = (Button) findViewById(R.id.buttonAnimation);

        buttonUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_y-=10;
                MyImageCutView.FlushLocation(Location_x, Location_y);
            }
        });
        buttonDown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_y+=10;
                MyImageCutView.FlushLocation(Location_x, Location_y);
            }
        });
        buttonRight.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_x+=10;
                MyImageCutView.FlushLocation(Location_x, Location_y);
            }
        });
        buttonLeft.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_x-=10;
                MyImageCutView.FlushLocation(Location_x, Location_y);
            }
        });
        buttonAnimation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Animation_Flag) {
                    Location_x=0;
                    Location_y=0;
                    MyImageCutView.FlushLocation(Location_x, Location_y);
                    Animation_Flag = true;
                    AnimationThread = new Thread(Animation);
                    AnimationThread.start();
                    buttonAnimation.setText("Stop");
                }
                else {
                    Animation_Flag = false;
                    buttonAnimation.setText("animation");
                }
            }
        });

        Test1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.testimg1);
        bmp_Width = Test1Bitmap.getWidth();
        bmp_Height = Test1Bitmap.getHeight();
        MyImageCutView.setBaseImage(Test1Bitmap, 0, 0, bmp_Width / 2, bmp_Height / 2);
    }

    private Runnable Animation = new Runnable() {
        int MaxRight;
        int MaxBottom;
        int IndexArea;
        @Override
        public void run() {
            MaxRight = bmp_Width/2;
            MaxBottom = bmp_Height/2;
            IndexArea = 1;
            while(Animation_Flag) {
                try {
                    switch (IndexArea) {
                        case 1:
                            Location_x++;
                            if(Location_x >= MaxRight)
                                IndexArea = 2;
                            break;
                        case 2:
                            Location_y++;
                            if(Location_y >= MaxBottom)
                                IndexArea = 3;
                            break;
                        case 3:
                            Location_x--;
                            if(Location_x <= 0)
                                IndexArea = 4;
                            break;
                        case 4:
                            Location_y--;
                            if(Location_y <= 0)
                                IndexArea = 1;
                            break;
                    }
                    ActivityAnimationHandler.sendEmptyMessage(0);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static class AnimationHandle extends Handler {
        private final WeakReference<MainActivity> mActivity;
        public AnimationHandle(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.MyImageCutView.FlushLocation(activity.Location_x, activity.Location_y);
            }
        }
    }
}
