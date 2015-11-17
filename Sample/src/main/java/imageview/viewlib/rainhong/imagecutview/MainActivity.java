package imageview.viewlib.rainhong.imagecutview;

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
    Button buttonUp;
    Button buttonDown;
    Button buttonLeft;
    Button buttonRight;
    Button buttonAnimation;
    Button buttonImage1;
    Button buttonImage2;
    Button buttonImage3;
    Thread AnimationThread;
    boolean Animation_Flag;
    //int Animation_Type;
    int Location_x;
    int Location_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyImageCutView = (BaseImageCutView) findViewById(R.id.MyImageCutView);
        Animation_Flag = false;
        //Animation_Type = 0;
        Location_x=0;
        Location_y=0;

        buttonImage1 = (Button) findViewById(R.id.buttonImage1);
        buttonImage2 = (Button) findViewById(R.id.buttonImage2);
        buttonImage3 = (Button) findViewById(R.id.buttonImage3);
        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonAnimation = (Button) findViewById(R.id.buttonAnimation);

        buttonUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_y-=10;
                MyImageCutView.moveImageLocation(Location_x, Location_y);
            }
        });
        buttonDown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_y+=10;
                MyImageCutView.moveImageLocation(Location_x, Location_y);
            }
        });
        buttonRight.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_x+=10;
                MyImageCutView.moveImageLocation(Location_x, Location_y);
            }
        });
        buttonLeft.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location_x-=10;
                MyImageCutView.moveImageLocation(Location_x, Location_y);
            }
        });
        buttonAnimation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Animation_Flag) {
                    Location_x=0;
                    Location_y=0;
                    MyImageCutView.moveImageLocation(Location_x, Location_y);
                    Animation_Flag = true;
                    AnimationThread = new Thread(Animation);
                    AnimationThread.start();
                    buttonAnimation.setText("Stop");
                    buttonImage1.setEnabled(false);
                    buttonImage2.setEnabled(false);
                    buttonImage3.setEnabled(false);
                }
                else {
                    Animation_Flag = false;
                    buttonAnimation.setText("animation");
                    buttonImage1.setEnabled(true);
                    buttonImage2.setEnabled(true);
                    buttonImage3.setEnabled(true);
                }
            }
        });

        buttonImage1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyImageCutView.setBaseImage(R.drawable.testimg1);
            }
        });

        buttonImage2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyImageCutView.setBaseImage(R.drawable.testimg2);
                MyImageCutView.setRectMaxWidth(MyImageCutView.getRectMaxWidth() / 2);
                MyImageCutView.setRectMaxHeight(MyImageCutView.getRectMaxHeight() / 2);
                MyImageCutView.moveImageLocation(0, 0);
                Location_x=0;
                Location_y=0;
            }
        });

        buttonImage3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyImageCutView.setBaseImage(R.drawable.testimg2);
            }
        });

        MyImageCutView.setBaseImage(R.drawable.testimg1);
    }

    private Runnable Animation = new Runnable() {
        int MaxRight;
        int MaxBottom;
        int IndexArea;
        @Override
        public void run() {
            MaxRight = MyImageCutView.getRectMaxWidth();
            MaxBottom = MyImageCutView.getRectMaxHeight();
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
                activity.MyImageCutView.moveImageLocation(activity.Location_x, activity.Location_y);
            }
        }
    }

    /*public static Bitmap getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private Runnable LoadImageURL = new Runnable() {
        @Override
        public void run() {
            try {
                Test1Bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://192.168.1.236/web/CurrentImage.jpg").getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/
}
