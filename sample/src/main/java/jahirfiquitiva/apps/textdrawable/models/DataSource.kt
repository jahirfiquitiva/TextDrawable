package jahirfiquitiva.apps.textdrawable.models

import android.content.Context
import android.graphics.drawable.Drawable

import java.util.ArrayList

class DataSource(context: Context) {
    
    private val mDataSource: ArrayList<DataItem> = ArrayList()
    private val mProvider: DrawableProvider = DrawableProvider(context)
    
    val count: Int
        get() = mDataSource.size
    
    init {
        mDataSource.add(itemFromType(SAMPLE_RECT))
        mDataSource.add(itemFromType(SAMPLE_ROUND_RECT))
        mDataSource.add(itemFromType(SAMPLE_ROUND))
        mDataSource.add(itemFromType(SAMPLE_RECT_BORDER))
        mDataSource.add(itemFromType(SAMPLE_ROUND_RECT_BORDER))
        mDataSource.add(itemFromType(SAMPLE_ROUND_BORDER))
        mDataSource.add(itemFromType(SAMPLE_MULTIPLE_LETTERS))
        mDataSource.add(itemFromType(SAMPLE_FONT))
        mDataSource.add(itemFromType(SAMPLE_SIZE))
        mDataSource.add(itemFromType(SAMPLE_ANIMATION))
        mDataSource.add(itemFromType(SAMPLE_MISC))
    }
    
    fun getItem(position: Int): DataItem {
        return mDataSource[position]
    }
    
    private fun itemFromType(type: Int): DataItem {
        var actualType = type
        var label: String? = null
        var drawable: Drawable? = null
        when (type) {
            SAMPLE_RECT -> {
                label = "Rectangle with Text"
                drawable = mProvider.getRect("A")
            }
            SAMPLE_ROUND_RECT -> {
                label = "Round Corner with Text"
                drawable = mProvider.getRoundRect("B")
            }
            SAMPLE_ROUND -> {
                label = "Round with Text"
                drawable = mProvider.getRound("C")
            }
            SAMPLE_RECT_BORDER -> {
                label = "Rectangle with Border"
                drawable = mProvider.getRectWithBorder("D")
            }
            SAMPLE_ROUND_RECT_BORDER -> {
                label = "Round Corner with Border"
                drawable = mProvider.getRoundRectWithBorder("E")
            }
            SAMPLE_ROUND_BORDER -> {
                label = "Round with Border"
                drawable = mProvider.getRoundWithBorder("F")
            }
            SAMPLE_MULTIPLE_LETTERS -> {
                label = "Support multiple letters"
                drawable = mProvider.rectWithMultiLetter
                actualType = NO_NAVIGATION
            }
            SAMPLE_FONT -> {
                label = "Support variable font styles"
                drawable = mProvider.roundWithCustomFont
                actualType = NO_NAVIGATION
            }
            SAMPLE_SIZE -> {
                label = "Support for custom size"
                drawable = mProvider.rectWithCustomSize
                actualType = NO_NAVIGATION
            }
            SAMPLE_ANIMATION -> {
                label = "Support for animations"
                drawable = mProvider.rectWithAnimation
                actualType = NO_NAVIGATION
            }
            SAMPLE_MISC -> {
                label = "Miscellaneous"
                drawable = mProvider.getRect("\u03c0")
                actualType = NO_NAVIGATION
            }
        }
        return DataItem(label.orEmpty(), drawable, actualType)
    }
    
    companion object {
        const val NO_NAVIGATION = -1
        const val SAMPLE_RECT = 1
        const val SAMPLE_ROUND_RECT = 2
        const val SAMPLE_ROUND = 3
        const val SAMPLE_RECT_BORDER = 4
        const val SAMPLE_ROUND_RECT_BORDER = 5
        const val SAMPLE_ROUND_BORDER = 6
        const val SAMPLE_MULTIPLE_LETTERS = 7
        const val SAMPLE_FONT = 8
        const val SAMPLE_SIZE = 9
        const val SAMPLE_ANIMATION = 10
        const val SAMPLE_MISC = 11
    }
}
