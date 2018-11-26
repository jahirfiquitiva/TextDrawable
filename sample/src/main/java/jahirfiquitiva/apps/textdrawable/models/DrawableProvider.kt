package jahirfiquitiva.apps.textdrawable.models

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.util.TypedValue
import jahirfiquitiva.apps.textdrawable.ColorGenerator
import jahirfiquitiva.libs.textdrawable.TextDrawable

class DrawableProvider(private val mContext: Context) {
    
    private val mGenerator: ColorGenerator = ColorGenerator.DEFAULT
    
    val rectWithMultiLetter: TextDrawable
        get() {
            val text = "AK"
            return TextDrawable.buildRect(text, mGenerator.getColor(text)) {
                fontSize(toPx(20))
                toUpperCase()
            }
        }
    
    val roundWithCustomFont: TextDrawable
        get() {
            val text = "Bold"
            return TextDrawable.buildRect(text, Color.DKGRAY) {
                useFont(Typeface.DEFAULT)
                fontSize(toPx(15))
                textColor(-0xa7aa7)
                bold()
            }
        }
    
    val rectWithCustomSize: Drawable
        get() {
            val leftText = "I"
            val rightText = "J"
            
            val builder = TextDrawable.rect {
                width(toPx(29))
                withBorder(toPx(2))
            }
            
            val left = builder.build(leftText, mGenerator.getColor(leftText))
            
            val right = builder.build(rightText, mGenerator.getColor(rightText))
            
            val layerList = arrayOf<Drawable>(
                InsetDrawable(left, 0, 0, toPx(31), 0), InsetDrawable(right, toPx(31), 0, 0, 0))
            return LayerDrawable(layerList)
        }
    
    val rectWithAnimation: Drawable
        get() {
            val builder = TextDrawable.rect()
            val animationDrawable = AnimationDrawable()
            for (i in 10 downTo 1) {
                val frame = builder.build(i.toString(), mGenerator.randomColor)
                animationDrawable.addFrame(frame, 1200)
            }
            animationDrawable.isOneShot = false
            animationDrawable.start()
            return animationDrawable
        }
    
    fun getRect(text: String): TextDrawable {
        return TextDrawable.buildRect(text, mGenerator.getColor(text))
    }
    
    fun getRound(text: String): TextDrawable {
        return TextDrawable.buildRound(text, mGenerator.getColor(text))
    }
    
    fun getRoundRect(text: String): TextDrawable {
        return TextDrawable.buildRoundRect(text, mGenerator.getColor(text), toPx(10))
    }
    
    fun getRectWithBorder(text: String): TextDrawable {
        return TextDrawable.buildRect(text, mGenerator.getColor(text)) {
            withBorder(toPx(2))
        }
    }
    
    fun getRoundWithBorder(text: String): TextDrawable {
        return TextDrawable.buildRound(text, mGenerator.getColor(text)) {
            withBorder(toPx(2))
        }
    }
    
    fun getRoundRectWithBorder(text: String): TextDrawable {
        return TextDrawable.buildRoundRect(text, mGenerator.getColor(text), toPx(10)) {
            withBorder(toPx(2))
        }
    }
    
    private fun toPx(dp: Int): Int {
        val resources = mContext.resources
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
            .toInt()
    }
}
