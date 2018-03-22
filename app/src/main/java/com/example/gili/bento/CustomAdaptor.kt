package com.example.gili.bento

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import java.util.ArrayList

/**
 * Created by gili on 2018-03-20.
 */
open class CustomAdaptor(context: Context, resource: Int, items: ArrayList<ListItem>) : ArrayAdapter<ListItem>(context, resource, items){

    var resId: Int
    var items:ArrayList<ListItem>
    public lateinit var queue: RequestQueue

    init {
        this.resId = resource
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(resId, null)
        val item = items[position]

        var txtProduct : TextView = convertView.findViewById(R.id.txtProduct)
        var txtPrice : TextView = convertView.findViewById(R.id.txtPrice)
        var imgProduct : ImageView = convertView.findViewById(R.id.imgProduct)

        txtProduct.setText(item.Name)
        txtPrice.setText(item.Price)

        val url  = "http://jdv.iptime.org:8181/filestorage/Prod/" + item.ImageURL
        val imageRequest = ImageRequest(url, Response.Listener { response ->
            //vo.image = response
            imgProduct.setImageBitmap(response)
            //this.notifyDataSetChanged()
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, Response.ErrorListener { })

        queue.add(imageRequest)
        /*val imageLoader = ImageLoader(queue, object : ImageLoader.ImageCache {
            override fun getBitmap(url: String): Bitmap? {
                return null
            }
            override fun putBitmap(url: String, bitmap: Bitmap) {
            }
        })
        imgProduct.setImageUrl(url, imageLoader)*/
        //this.notifyDataSetChanged()

        imgProduct.setOnClickListener{
            item.Id;
            var intent: Intent = Intent(context, ItemDetailActivity::class.java);
            intent.putExtra("Id", item.Id)
            context.startActivity(intent)
        }
        return convertView
    }

    override fun getCount(): Int {
        return items.size
    }

}
