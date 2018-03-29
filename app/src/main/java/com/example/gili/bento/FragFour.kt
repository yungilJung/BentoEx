package com.example.gili.bento

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_frag_one.*
import org.json.JSONArray
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragFour.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragFour.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragFour : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    internal lateinit var queue: RequestQueue
    internal lateinit var list: ArrayList<ItemVO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        list = ArrayList<ItemVO>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(context)
        val currentRequest = StringRequest(Request.Method.GET, "http://jdv.iptime.org:8181/api/productList/간식안주", Response.Listener { response -> parseJsonCurrent(this!!.getContext()!!, response, queue) }, Response.ErrorListener { })
        queue.add(currentRequest)

        return inflater.inflate(R.layout.fragment_frag_four, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
           // throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragFour.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragFour().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun parseJsonCurrent(context: Context, response: String, queue: RequestQueue){

        if(context == null) return;
        var midName : String = ""
        val jarray = JSONArray(response)
        for(x in 0..jarray.length()-1){
            val jObject = jarray.getJSONObject(x)

            if (!midName.equals(jObject.getString("MidCategory"))){
                midName = jObject.getString("MidCategory");
                var itemHeader : HeaderItem = HeaderItem()
                itemHeader.headerTitle = midName
                list.add(itemHeader)
            }


            var item : ListItem = ListItem();
            item.Id = jObject.getString("Id")
            item.Price = jObject.getString("Price")
            item.Name = jObject.getString("Name")
            item.ImageURL  = jObject.getString("ImageURL")
            item.Calorie = jObject.getString("Calorie")
            item.Information =jObject.getString("Information")
            list.add(item);
        }
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(ProductAdapter(context, list, queue))
        /*adapter = CustomAdaptor(context, R.layout.item_list,list)
        adapter.notifyDataSetChanged()
        adapter.queue = queue
        lst_product.setAdapter(adapter)*/
    }
}
