package com.example.gili.bento

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class ItemDetailActivity : AppCompatActivity() {

    internal lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        val intent = this.intent
        val id : String = intent.getExtras().getString("Id")

        setSupportActionBar(toolbar_detail)
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 툴바의 제목을 표시를 한다.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 삼단 메뉴 아이콘 출력을 한다.
        supportActionBar!!.setHomeButtonEnabled(true)
        toolbar_detail.setOnClickListener {
            finish()
        }
        queue = Volley.newRequestQueue(this@ItemDetailActivity)
        val currentRequest = StringRequest(Request.Method.GET, "http://jdv.iptime.org:8181/api/product/"+id, Response.Listener { response -> parseJsonCurrent(response, queue) }, Response.ErrorListener { })
        queue.add(currentRequest)
    }

    fun parseJsonCurrent(response: String, queue: RequestQueue){
        val jarray = JSONArray(response)
        var item : ListItem = ListItem();
        for(x in 0..jarray.length()-1){
            val jObject = jarray.getJSONObject(x)
            item.Price = jObject.getString("Price")
            item.Name = jObject.getString("Name")
            item.ImageURL  = jObject.getString("ImageURL")
            item.Calorie = jObject.getString("Calorie")
            item.Information =jObject.getString("Information")

            txtPrice.setText(item.Price+"원")
            txtName.setText(item.Name)
            txtCalorie.setText(item.Calorie)
            txtInformation.setText(item.Information)
        }
        val url  = "http://jdv.iptime.org:8181/filestorage/Prod/" + item.ImageURL
        val imageRequest = ImageRequest(url, Response.Listener { response ->
            //vo.image = response
            imgProduct.setImageBitmap(response)
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, Response.ErrorListener { })
        queue.add(imageRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item)
    }
}
