package jahirfiquitiva.apps.textdrawable

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_RECT
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_RECT_BORDER
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_ROUND
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_ROUND_BORDER
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_ROUND_RECT
import jahirfiquitiva.apps.textdrawable.models.DataSource.Companion.SAMPLE_ROUND_RECT_BORDER
import jahirfiquitiva.libs.textdrawable.TextDrawable
import java.util.Arrays

class ListActivity : AppCompatActivity() {
    
    // list of data items
    private val mDataList = Arrays.asList(
        ListData("Iron Man"),
        ListData("Captain America"),
        ListData("James Bond"),
        ListData("Harry Potter"),
        ListData("Sherlock Holmes"),
        ListData("Black Widow"),
        ListData("Hawk Eye"),
        ListData("Iron Man"),
        ListData("Guava"),
        ListData("Tomato"),
        ListData("Pineapple"),
        ListData("Strawberry"),
        ListData("Watermelon"),
        ListData("Pears"),
        ListData("Kiwi"),
        ListData("Plums")
                                         )
    
    // declare the color generator and drawable builder
    private val mColorGenerator = ColorGenerator.MATERIAL
    private var mDrawableBuilder: TextDrawable.Builder? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        
        val intent = intent
        val type = intent.getIntExtra(MainActivity.TYPE, SAMPLE_RECT)
        
        // initialize the builder based on the "TYPE"
        when (type) {
            SAMPLE_RECT -> mDrawableBuilder = TextDrawable.rect()
            SAMPLE_ROUND_RECT -> mDrawableBuilder = TextDrawable.roundRect(10)
            SAMPLE_ROUND -> mDrawableBuilder = TextDrawable.round()
            SAMPLE_RECT_BORDER -> mDrawableBuilder = TextDrawable.rect { withBorder(4) }
            SAMPLE_ROUND_RECT_BORDER -> mDrawableBuilder =
                TextDrawable.roundRect(10) { withBorder(4) }
            SAMPLE_ROUND_BORDER -> mDrawableBuilder = TextDrawable.round { withBorder(4) }
        }
        
        // init the list view and its adapter
        val listView = findViewById<View>(R.id.listView) as ListView
        listView.adapter = SampleAdapter()
    }
    
    private inner class SampleAdapter : BaseAdapter() {
        
        override fun getCount(): Int {
            return mDataList.size
        }
        
        override fun getItem(position: Int): ListData {
            return mDataList[position]
        }
        
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var actualView = convertView
            val holder: ViewHolder
            if (actualView == null) {
                actualView = View.inflate(this@ListActivity, R.layout.list_item_layout, null)
                holder = ViewHolder(actualView)
                actualView.tag = holder
            } else {
                holder = actualView.tag as ViewHolder
            }
            
            val item = getItem(position)
            
            // provide support for selected state
            updateCheckedState(holder, item)
            holder.imageView?.setOnClickListener {
                // when the image is clicked, update the selected state
                val data = getItem(position)
                data.isChecked = !data.isChecked
                updateCheckedState(holder, data)
            }
            holder.textView?.text = item.data
            
            return actualView!!
        }
        
        private fun updateCheckedState(holder: ViewHolder, item: ListData) {
            if (item.isChecked) {
                holder.imageView?.setImageDrawable(mDrawableBuilder?.build(" ", -0x9e9e9f))
                holder.view.setBackgroundColor(HIGHLIGHT_COLOR)
                holder.checkIcon?.visibility = View.VISIBLE
            } else {
                val drawable = mDrawableBuilder?.build(
                    item.data[0].toString(), mColorGenerator.getColor(item.data))
                holder.imageView?.setImageDrawable(drawable)
                holder.view.setBackgroundColor(Color.TRANSPARENT)
                holder.checkIcon?.visibility = View.GONE
            }
        }
    }
    
    private class ViewHolder constructor(val view: View) {
        val imageView: ImageView? = view.findViewById<ImageView?>(R.id.imageView)
        val textView: TextView? = view.findViewById<TextView?>(R.id.textView)
        val checkIcon: ImageView? = view.findViewById<ImageView?>(R.id.check_icon)
    }
    
    class ListData(val data: String) {
        var isChecked: Boolean = false
    }
    
    companion object {
        private const val HIGHLIGHT_COLOR = -0x66641901
    }
}
