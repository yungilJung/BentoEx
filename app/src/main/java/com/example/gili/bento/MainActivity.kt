package com.example.gili.bento

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    internal var toggle: ActionBarDrawerToggle? = null
    internal var isDrawerOpend: Boolean = false
    internal lateinit var queue: RequestQueue
    internal lateinit var list: ArrayList<ListItem>
    internal lateinit var adapter:CustomAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_drawer)
        supportActionBar!!.setDisplayShowTitleEnabled(true) // 툴바의 제목을 표시를 한다.

        toggle = object : ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                isDrawerOpend = true
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                isDrawerOpend = false
            }
        }
        drawer.addDrawerListener(toggle as ActionBarDrawerToggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 삼단 메뉴 아이콘 출력을 한다.
        (toggle as ActionBarDrawerToggle).syncState()     // 삼단 메뉴 아이콘 출력을 한다. 두라인이 함께 실행되어야 됨.

        list = ArrayList<ListItem>()
        // 데이터를 조회해서 바인딩 한다.
        queue = Volley.newRequestQueue(this@MainActivity)
        val currentRequest = StringRequest(Request.Method.GET, "http://jdv.iptime.org:8181/api/productList", Response.Listener { response -> parseJsonCurrent(response, queue) }, Response.ErrorListener { })
        queue.add(currentRequest)
    }

    fun parseJsonCurrent(response: String, queue: RequestQueue){
        val jarray = JSONArray(response)
        for(x in 0..jarray.length()-1){
            val jObject = jarray.getJSONObject(x)
            var item : ListItem = ListItem();
            item.Id = jObject.getString("Id")
            item.Price = jObject.getString("Price")
            item.Name = jObject.getString("Name")
            item.ImageURL  = jObject.getString("ImageURL")
            item.Calorie = jObject.getString("Calorie")
            item.Information =jObject.getString("Information")
            list.add(item);
        }
        adapter = CustomAdaptor(this, R.layout.item_list,list)
        adapter.notifyDataSetChanged()
        adapter.queue = queue
        lst_product.setAdapter(adapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isDrawerOpend) {
            drawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }


}

