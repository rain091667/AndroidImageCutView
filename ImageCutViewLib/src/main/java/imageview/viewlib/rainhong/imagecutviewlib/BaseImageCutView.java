package imageview.viewlib.rainhong.imagecutviewlib;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     * @param ResourceId   drawable Source Image
     */
    public void setBaseImage(int ResourceId) {
        setBaseImage(BitmapFactory.decodeResource(getResources(), ResourceId));
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
        this.MoveLocationBitmap(CurrentLocX, CurrentLocY);
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
        this.MoveLocationBitmap(CurrentLocX, CurrentLocY);
        super.setImageBitmap(FlushLocationBitmap);
    }

    /**
     * Move image by index.
     * @param x   new index of x.
     * @param y   new index of y.
     */
    public void moveImageLocation(int x, int y) {
        this.CurrentLocX = x;
        this.CurrentLocY = y;
        MoveLocationBitmap(CurrentLocX, CurrentLocY);
        super.setImageBitmap(FlushLocationBitmap);
    }

    /**
     * Get current move index.
     * @return Current Move Index X.
     */
    public int getCurrentLocationX() {
        return this.CurrentLocX;
    }

    /**
     * Get current move index.
     * @return Current Move Index Y.
     */
    public int getCurrentLocationY() {
        return this.CurrentLocY;
    }

    /**
     * Get Rect Length.
     * @return Rect Width.
     */
    public int getRectMaxWidth() {
        return this.RectMaxWidth;
    }

    /**
     * Set Rect Length.
     * @param Width Set Max Rect Width.
     */
    public void setRectMaxWidth(int Width) {
        if(Width > BaseImage.getWidth())
            throw new RuntimeException(
                    "\nBitmap Max Width: " + BaseImage.getWidth() + " MaxRectWidth: " + Width +
                            "\nMaxRectSize must be small than bitmap size."
            );
        this.RectMaxWidth = Width;
    }

    /**
     * Set Rect Length.
     * @param Height Set Max Rect Height.
     */
    public void setRectMaxHeight(int Height) {
        if(Height > BaseImage.getHeight())
            throw new RuntimeException(
                    "\nBitmap Max Height: " + BaseImage.getHeight() + " MaxRectWidth: " + Height +
                            "\nMaxRectSize must be small than bitmap size."
            );
        this.RectMaxHeight = Height;
    }

    /**
     * Get Rect Length.
     * @return Rect Height.
     */
    public int getRectMaxHeight() {
        return this.RectMaxHeight;
    }

    /**
     * Get View BaseImage.
     * @return View BaseImage.
     */
    public Bitmap getBaseImage() {
        return this.BaseImage;
    }

    /**
     * Get BaseImage Length.
     * @return BaseImage Width.
     */
    public int getBaseImageWidth() {
        return this.BaseImage.getWidth();
    }

    /**
     * Get BaseImage Length.
     * @return BaseImage Height.
     */
    public int getBaseImageHeight() {
        return this.BaseImage.getHeight();
    }

    private void MoveLocationBitmap(int LocationX, int LocationY) {
        this.Bmp_SourceRect.set(
                LocationX,
                LocationY,
                LocationX + RectMaxWidth,
                LocationY + RectMaxHeight
        );
        this.FlushCanvas.drawBitmap(BaseImage, Bmp_SourceRect, Bmp_DestinationRect, null);
    }
}
