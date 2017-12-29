package jahirfiquitiva.libs.textdrawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * @author amulya
 * @datetime 14 Oct 2014, 3:53 PM
 */
public class TextDrawable extends ShapeDrawable {

    private final Paint textPaint;
    private final Paint borderPaint;
    private static final float SHADE_FACTOR = 0.9f;
    private final String text;
    private final int color;
    private final int bgColor;
    private final RectShape shape;
    private final int height;
    private final int width;
    private final int fontSize;
    private final float radius;
    private final int borderThickness;
    private final int borderColor;
    private final Bitmap bitmap;

    private TextDrawable(Builder builder) {
        super(builder.shape);

        // shape properties
        shape = builder.shape;
        height = builder.height;
        width = builder.width;
        radius = builder.radius;

        // text and color
        text = builder.toUpperCase ? builder.text.toUpperCase() : builder.text;
        color = builder.color;
        bgColor = builder.backgroundColor;

        // text paint settings
        fontSize = builder.fontSize;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(builder.borderThickness);

        // border paint settings
        borderThickness = builder.borderThickness;
        borderColor = builder.borderColor;
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(borderColor == -1 ? getDarkerShade(color) : borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);
        borderPaint.setAntiAlias(true);

        // drawable paint color
        Paint paint = getPaint();
        paint.setColor(bgColor);

        if (builder.drawable != null) {
            bitmap = ((BitmapDrawable) builder.drawable).getBitmap();
        } else {
            bitmap = null;
        }
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int) (SHADE_FACTOR * Color.red(color)),
                         (int) (SHADE_FACTOR * Color.green(color)),
                         (int) (SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();


        // draw border
        if (borderThickness > 0) {
            drawBorder(canvas);
        }

        int count = canvas.save();
        if (bitmap == null)
            canvas.translate(r.left, r.top);

        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;

        if (bitmap == null) {
            // draw text
            int fontSize = this.fontSize < 0 ? (Math.min(width, height) / 2) : this.fontSize;
            textPaint.setTextSize(fontSize);

            Rect textBounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, width / 2, height / 2 - textBounds.exactCenterY(), textPaint);
        } else {
            canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2,
                              (height - bitmap.getHeight()) / 2, null);
        }

        canvas.restoreToCount(count);
    }

    private void drawBorder(Canvas canvas) {
        RectF rect = new RectF(getBounds());
        double inset = Math.ceil(borderThickness / 2);
        rect.inset((int) inset, (int) inset);

        if (shape instanceof OvalShape) {
            canvas.drawOval(rect, borderPaint);
        } else if (shape instanceof RoundRectShape) {
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        } else {
            canvas.drawRect(rect, borderPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public static IShapeBuilder builder() {
        return new Builder();
    }

    public static class Builder implements IConfigBuilder, IShapeBuilder, IBuilder {

        private static final RectF EMPTY_RECT = new RectF();

        private String text;

        private int color;

        private int backgroundColor;

        private int borderThickness;

        private int borderColor;

        private int width;

        private int height;

        private Typeface font;

        private RectShape shape;

        public int textColor;

        private int fontSize;

        private boolean isBold;

        private boolean toUpperCase;

        public float radius;

        public Drawable drawable;

        private Builder() {
            text = "";
            color = Color.GRAY;
            backgroundColor = Color.TRANSPARENT;
            textColor = Color.WHITE;
            borderThickness = 0;
            borderColor = -1;
            width = -1;
            height = -1;
            shape = new RectShape();
            font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            fontSize = -1;
            isBold = false;
            toUpperCase = false;
        }

        public IConfigBuilder width(int width) {
            this.width = width;
            return this;
        }

        public IConfigBuilder height(int height) {
            this.height = height;
            return this;
        }

        public IConfigBuilder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public IConfigBuilder backgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public IConfigBuilder withBorder(int thickness) {
            return withBorder(thickness, this.borderColor);
        }

        public IConfigBuilder withBorder(int thickness, int color) {
            this.borderThickness = thickness;
            this.borderColor = color;
            return this;
        }

        public IConfigBuilder useFont(Typeface font) {
            this.font = font;
            return this;
        }

        public IConfigBuilder fontSize(int size) {
            this.fontSize = size;
            return this;
        }

        public IConfigBuilder bold() {
            this.isBold = true;
            return this;
        }

        public IConfigBuilder toUpperCase() {
            this.toUpperCase = true;
            return this;
        }

        @Override
        public IConfigBuilder beginConfig() {
            return this;
        }

        @Override
        public IShapeBuilder endConfig() {
            return this;
        }

        @Override
        public IBuilder rect() {
            this.shape = new RectShape();
            return this;
        }

        @Override
        public IBuilder round() {
            this.shape = new OvalShape();
            return this;
        }

        @Override
        public IBuilder roundRect(int radius) {
            this.radius = radius;
            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            this.shape = new RoundRectShape(radii, EMPTY_RECT, null);
            return this;
        }

        @Override
        public TextDrawable buildRect(String text, int color) {
            rect();
            return build(text, color);
        }

        @Override
        public TextDrawable buildRoundRect(String text, int color, int radius) {
            roundRect(radius);
            return build(text, color);
        }

        @Override
        public TextDrawable buildRound(String text, int color) {
            round();
            return build(text, color);
        }

        @Override
        public TextDrawable build(String text, int color) {
            this.color = color;
            this.text = text;
            return new TextDrawable(this);
        }

        @Override
        public TextDrawable buildRect(Drawable drawable, int color) {
            rect();
            return build(drawable, color);
        }

        @Override
        public TextDrawable buildRoundRect(Drawable drawable, int color, int radius) {
            roundRect(radius);
            return build(drawable, color);
        }

        @Override
        public TextDrawable buildRound(Drawable drawable, int color) {
            round();
            return build(drawable, color);
        }

        @Override
        public TextDrawable build(Drawable drawable, int color) {
            this.color = color;
            this.drawable = drawable;
            return new TextDrawable(this);
        }
    }

    public interface IConfigBuilder {
        public IConfigBuilder width(int width);

        public IConfigBuilder height(int height);

        public IConfigBuilder textColor(int color);

        public IConfigBuilder backgroundColor(int color);

        public IConfigBuilder withBorder(int thickness);

        public IConfigBuilder withBorder(int thickness, int color);

        public IConfigBuilder useFont(Typeface font);

        public IConfigBuilder fontSize(int size);

        public IConfigBuilder bold();

        public IConfigBuilder toUpperCase();

        public IShapeBuilder endConfig();
    }

    public static interface IBuilder {

        public TextDrawable build(String text, int color);

        public TextDrawable build(Drawable drawable, int color);
    }

    public static interface IShapeBuilder {

        public IConfigBuilder beginConfig();

        public IBuilder rect();

        public IBuilder round();

        public IBuilder roundRect(int radius);

        public TextDrawable buildRect(String text, int color);

        public TextDrawable buildRoundRect(String text, int color, int radius);

        public TextDrawable buildRound(String text, int color);

        public TextDrawable buildRect(Drawable drawable, int color);

        public TextDrawable buildRoundRect(Drawable drawable, int color, int radius);

        public TextDrawable buildRound(Drawable drawable, int color);
    }
}