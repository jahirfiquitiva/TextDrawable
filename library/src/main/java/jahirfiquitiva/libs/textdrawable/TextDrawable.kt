package jahirfiquitiva.libs.textdrawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import androidx.annotation.ColorInt
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.min

@Suppress("unused")
open class TextDrawable private constructor(builder: Builder) : ShapeDrawable(builder.shape) {
    
    private val textPaint: Paint
    private val borderPaint: Paint
    private val text: String?
    @ColorInt private val color: Int
    private val shape: RectShape?
    private val height: Int
    private val width: Int
    private val fontSize: Int
    private val radius: Float
    private val borderThickness: Int
    private val borderColor: Int
    private val bitmap: Bitmap?
    
    init {
        // shape properties
        shape = builder.shape
        height = builder.height
        width = builder.width
        radius = builder.radius
        
        // text and color
        text =
            if (builder.toUpperCase) builder.text.orEmpty().toUpperCase(Locale.ROOT)
            else builder.text
        color = builder.color
        
        // text paint settings
        fontSize = builder.fontSize
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = builder.textColor
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = builder.isBold
        textPaint.style = Paint.Style.FILL
        textPaint.typeface = builder.font
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.strokeWidth = builder.borderThickness.toFloat()
        
        // border paint settings
        borderThickness = builder.borderThickness
        borderColor = builder.borderColor
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = if (borderColor == -1) getDarkerShade(color) else borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderThickness.toFloat()
        borderPaint.isAntiAlias = true
        
        // drawable paint color
        paint.color = color
        
        bitmap = (builder.drawable as? BitmapDrawable)?.bitmap
    }
    
    private fun getDarkerShade(@ColorInt color: Int): Int {
        return Color.rgb(
            (SHADE_FACTOR * Color.red(color)).toInt(),
            (SHADE_FACTOR * Color.green(color)).toInt(),
            (SHADE_FACTOR * Color.blue(color)).toInt())
    }
    
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val r = bounds
        
        // draw border
        if (borderThickness > 0) {
            drawBorder(canvas)
        }
        
        val count = canvas.save()
        if (bitmap == null)
            canvas.translate(r.left.toFloat(), r.top.toFloat())
        
        val width = if (this.width < 0) r.width() else this.width
        val height = if (this.height < 0) r.height() else this.height
        
        if (bitmap == null) {
            // draw text
            val fontSize = if (this.fontSize < 0) min(width, height) / 2 else this.fontSize
            textPaint.textSize = fontSize.toFloat()
            
            val textBounds = Rect()
            textPaint.getTextBounds(text, 0, text?.length ?: 0, textBounds)
            canvas.drawText(
                text.orEmpty(), (width / 2).toFloat(), height / 2 - textBounds.exactCenterY(),
                textPaint)
        } else {
            canvas.drawBitmap(
                bitmap, ((width - bitmap.width) / 2).toFloat(),
                ((height - bitmap.height) / 2).toFloat(), null)
        }
        
        canvas.restoreToCount(count)
    }
    
    private fun drawBorder(canvas: Canvas) {
        val rect = RectF(bounds)
        val inset = ceil((borderThickness / 2).toDouble())
        rect.inset(inset.toInt().toFloat(), inset.toInt().toFloat())
        when (shape) {
            is OvalShape -> canvas.drawOval(rect, borderPaint)
            is RoundRectShape -> canvas.drawRoundRect(rect, radius, radius, borderPaint)
            else -> canvas.drawRect(rect, borderPaint)
        }
    }
    
    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }
    
    override fun setColorFilter(cf: ColorFilter?) {
        textPaint.colorFilter = cf
    }
    
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
    
    override fun getIntrinsicWidth(): Int {
        return width
    }
    
    override fun getIntrinsicHeight(): Int {
        return height
    }
    
    class Builder {
        
        var text: String? = null
            private set
        @ColorInt var color: Int = 0
            private set
        var borderThickness: Int = 0
            private set
        var borderColor: Int = 0
            private set
        var width: Int = 0
            private set
        var height: Int = 0
            private set
        var font: Typeface? = null
            private set
        var shape: RectShape? = null
            private set
        var textColor: Int = 0
            private set
        var fontSize: Int = 0
            private set
        var isBold: Boolean = false
            private set
        var toUpperCase: Boolean = false
            private set
        var radius: Float = 0.toFloat()
            private set
        var drawable: Drawable? = null
            private set
        
        init {
            text = ""
            color = Color.GRAY
            textColor = Color.WHITE
            borderThickness = 0
            borderColor = -1
            width = -1
            height = -1
            shape = RectShape()
            font = DEFAULT_FONT
            fontSize = -1
            isBold = false
            toUpperCase = false
        }
        
        fun width(width: Int): Builder {
            this.width = width
            return this
        }
        
        fun height(height: Int): Builder {
            this.height = height
            return this
        }
        
        fun textColor(@ColorInt color: Int): Builder {
            this.textColor = color
            return this
        }
        
        fun withBorder(thickness: Int): Builder {
            return withBorder(thickness, this.borderColor)
        }
        
        fun withBorder(thickness: Int, @ColorInt color: Int): Builder {
            this.borderThickness = thickness
            this.borderColor = color
            return this
        }
        
        fun useFont(font: Typeface): Builder {
            this.font = font
            return this
        }
        
        fun fontSize(size: Int): Builder {
            this.fontSize = size
            return this
        }
        
        fun bold(): Builder {
            this.isBold = true
            return this
        }
        
        fun toUpperCase(): Builder {
            this.toUpperCase = true
            return this
        }
        
        internal fun beginConfig(): Builder {
            return this
        }
        
        internal fun endConfig(): Builder {
            return this
        }
        
        fun rect(): Builder {
            this.shape = RectShape()
            return this
        }
        
        fun round(): Builder {
            this.shape = OvalShape()
            return this
        }
        
        fun roundRect(radius: Int): Builder {
            this.radius = radius.toFloat()
            val radii = floatArrayOf(
                radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(),
                radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat())
            this.shape = RoundRectShape(radii, EMPTY_RECT, null)
            return this
        }
        
        fun buildRect(text: String, @ColorInt color: Int): TextDrawable {
            rect()
            return build(text, color)
        }
        
        fun buildRoundRect(text: String, @ColorInt color: Int, radius: Int): TextDrawable {
            roundRect(radius)
            return build(text, color)
        }
        
        fun buildRound(text: String, @ColorInt color: Int): TextDrawable {
            round()
            return build(text, color)
        }
        
        fun build(text: String, @ColorInt color: Int): TextDrawable {
            this.color = color
            this.text = text
            return TextDrawable(this)
        }
        
        fun buildRect(drawable: Drawable, @ColorInt color: Int): TextDrawable {
            rect()
            return build(drawable, color)
        }
        
        fun buildRoundRect(drawable: Drawable, @ColorInt color: Int, radius: Int): TextDrawable {
            roundRect(radius)
            return build(drawable, color)
        }
        
        fun buildRound(drawable: Drawable, @ColorInt color: Int): TextDrawable {
            round()
            return build(drawable, color)
        }
        
        fun build(drawable: Drawable, @ColorInt color: Int): TextDrawable {
            this.color = color
            this.drawable = drawable
            return TextDrawable(this)
        }
        
        companion object {
            private val EMPTY_RECT = RectF()
            
            @JvmStatic
            val DEFAULT_FONT: Typeface by lazy {
                Typeface.create("sans-serif-light", Typeface.NORMAL)
            }
        }
    }
    
    companion object {
        private const val SHADE_FACTOR = 0.9F
        
        @JvmStatic
        fun builder(what: Builder.() -> Unit = {}): Builder {
            val builder = Builder()
            builder.beginConfig()
            builder.what()
            builder.endConfig()
            return builder
        }
        
        @JvmStatic
        fun rect(what: Builder.() -> Unit = {}): Builder =
            builder(what).rect()
        
        @JvmStatic
        fun round(what: Builder.() -> Unit = {}): Builder =
            builder(what).round()
        
        @JvmStatic
        fun roundRect(radius: Int, what: Builder.() -> Unit = {}): Builder =
            builder(what).roundRect(radius)
        
        @JvmStatic
        fun buildRect(
            text: String,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                     ): TextDrawable =
            builder(what).buildRect(text, color)
        
        @JvmStatic
        fun buildRect(
            drawable: Drawable,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                     ): TextDrawable =
            builder(what).buildRect(drawable, color)
        
        @JvmStatic
        fun buildRoundRect(
            text: String,
            @ColorInt color: Int,
            radius: Int,
            what: Builder.() -> Unit = {}
                          ): TextDrawable =
            builder(what).buildRoundRect(text, color, radius)
        
        @JvmStatic
        fun buildRoundRect(
            drawable: Drawable,
            @ColorInt color: Int,
            radius: Int,
            what: Builder.() -> Unit = {}
                          ): TextDrawable =
            builder(what).buildRoundRect(drawable, color, radius)
        
        @JvmStatic
        fun buildRound(
            text: String,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                      ): TextDrawable =
            builder(what).buildRound(text, color)
        
        @JvmStatic
        fun buildRound(
            drawable: Drawable,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                      ): TextDrawable =
            builder(what).buildRound(drawable, color)
        
        @JvmStatic
        fun build(
            text: String,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                 ): TextDrawable =
            builder(what).build(text, color)
        
        @JvmStatic
        fun build(
            drawable: Drawable,
            @ColorInt color: Int,
            what: Builder.() -> Unit = {}
                 ): TextDrawable =
            builder(what).build(drawable, color)
    }
}