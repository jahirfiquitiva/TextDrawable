package jahirfiquitiva.apps.textdrawable

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import jahirfiquitiva.apps.textdrawable.models.DataItem
import jahirfiquitiva.apps.textdrawable.models.DataSource

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var mDataSource: DataSource? = null
    private var mListView: ListView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        mListView = findViewById<View>(R.id.listView) as ListView
        mDataSource = DataSource(this)
        mListView?.adapter = SampleAdapter()
        mListView?.onItemClickListener = this
    }
    
    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = mListView?.getItemAtPosition(position) as DataItem
        
        // if navigation is supported, open the next activity
        if (item.navigationInfo != DataSource.NO_NAVIGATION) {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra(TYPE, item.navigationInfo)
            startActivity(intent)
        }
    }
    
    private inner class SampleAdapter : BaseAdapter() {
        
        override fun getCount(): Int {
            return mDataSource?.count ?: 0
        }
        
        override fun getItem(position: Int): DataItem? {
            return mDataSource?.getItem(position)
        }
        
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder
            if (convertView ==
                null) {
                convertView = View.inflate(this@MainActivity, R.layout.list_item_layout, null)
                holder = ViewHolder(convertView!!)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            
            val item = getItem(position)
            
            val drawable = item?.drawable
            holder.imageView?.setImageDrawable(drawable)
            holder.textView?.text = item?.label
            
            // if navigation is supported, show the ">" navigation icon
            if (item?.navigationInfo != DataSource.NO_NAVIGATION) {
                holder.textView?.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_action_next_item),
                    null)
            } else {
                holder.textView?.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            
            // fix for animation not playing for some below 4.4 devices
            if (drawable is AnimationDrawable) {
                holder.imageView?.post {
                    drawable.stop()
                    drawable.start()
                }
            }
            return convertView
        }
    }
    
    private class ViewHolder constructor(view: View) {
        val imageView: ImageView? = view.findViewById<ImageView?>(R.id.imageView)
        val textView: TextView? = view.findViewById<TextView?>(R.id.textView)
    }
    
    companion object {
        const val TYPE = "TYPE"
    }
}
