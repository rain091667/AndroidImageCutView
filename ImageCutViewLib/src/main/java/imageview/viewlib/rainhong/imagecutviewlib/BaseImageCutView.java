package imageview.viewlib.rainhong.imagecutviewlib;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BaseImageCutView extends ImageView {
    private Bitmap BaseImage;
    private int RectMaxWidth;
    private int RectMaxHeight;
    private int CurrentLocX;
    private int CurrentLocY;
    private Bitmap FlushLocationBitmap;
    private Canvas FlushCanvas;
    private Rect Bmp_DestinationRect;
    private Rect Bmp_SourceRect;

    public BaseImageCutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCutView();
    }

    public void initCutView() {
        RectMaxWidth = 0;
        RectMaxHeight = 0;
        BaseImage = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * init value and setBaseImage.
     * RectMaxWidth and RectMaxHeight will be set to Bitmap half width and half height.
     * init location will be X = 0, Y = 0.
     * @param bmp   Source Bitmap
     */
    public void setBaseImage(Bitmap bmp) {
        this.BaseImage = bmp;
        this.RectMaxWidth = BaseImage.getWidth()/2;
        this.RectMaxHeight = BaseImage.getHeight()/2;
        this.CurrentLocX = 0;
        this.CurrentLocY = 0;
        this.FlushLocationBitmap = Bitmap.createBitmap (
                RectMaxWidth,
                RectMaxHeight,
                Bitmap.Config.ARGB_8888
        );
        this.FlushCanvas = new Canvas(FlushLocationBitmap);
        this.Bmp_DestinationRect = new Rect(0, 0, RectMaxWidth, RectMaxHeight);
        this.Bmp_SourceRect = new Rect();
        this.FlushLocationBitmap(CurrentLocX, CurrentLocY);
        super.setImageBitmap(FlushLocationBitmap);
    }

    /**
     * init value and setBaseImage.
     * @param bmp   Source Bitmap
     * @param initX     Init X Location.
     * @param initY     Init Y Location.
     * @param MaxRectWidth     Init Rect Max Width.
     * @param MaxRectHeight     Init Rect Max Height.
     */
    public void setBaseImage(Bitmap bmp, int initX, int initY, int MaxRectWidth, int MaxRectHeight) {
        if(MaxRectWidth > bmp.getWidth() || MaxRectHeight > bmp.getHeight())
            throw new RuntimeException(
                    "\nBitmap Max Width: " + bmp.getWidth() + " MaxRectWidth: " + MaxRectWidth +
                            "\nBitmap Max Height: " + bmp.getHeight() + " MaxRectHeight: " + MaxRectHeight +
                            "\nMaxRectSize must be small than bitmap size."
            );
        this.BaseImage = bmp;
        this.RectMaxWidth = MaxRectWidth;
        this.RectMaxHeight = MaxRectHeight;
        this.CurrentLocX = initX;
        this.CurrentLocY = initY;
        this.FlushLocationBitmap = Bitmap.createBitmap (
                RectMaxWidth,
                RectMaxHeight,
                Bitmap.Config.ARGB_8888
        );
        this.FlushCanvas = new Canvas(FlushLocationBitmap);
        this.Bmp_DestinationRect = new Rect(0, 0, RectMaxWidth, RectMaxHeight);
        this.Bmp_SourceRect = new Rect();
        this.FlushLocationBitmap(CurrentLocX, CurrentLocY);
        super.setImageBitmap(FlushLocationBitmap);
    }

    /**
     * Reflush image by index.
     * @param x   new index of x.
     * @param y   new index of y.
     */
    public void FlushLocation(int x, int y) {
        this.CurrentLocX = x;
        this.CurrentLocY = y;
        FlushLocationBitmap(CurrentLocX, CurrentLocY);
        super.setImageBitmap(FlushLocationBitmap);
    }

    public int getFlushLocationX() {
        return this.CurrentLocX;
    }

    public int getFlushLocationY() {
        return this.CurrentLocY;
    }

    public int getRectMaxWidth() {
        return this.RectMaxWidth;
    }

    public int getRectMaxHeight() {
        return this.RectMaxHeight;
    }

    private void FlushLocationBitmap(int LocationX, int LocationY) {
        this.Bmp_SourceRect.set(
                LocationX,
                LocationY,
                LocationX + RectMaxWidth,
                LocationY + RectMaxHeight
        );
        this.FlushCanvas.drawBitmap(BaseImage, Bmp_SourceRect, Bmp_DestinationRect, null);
    }
}
