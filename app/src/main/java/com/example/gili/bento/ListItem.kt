package com.example.gili.bento

/**
 * Created by gili on 2018-03-20.
 */
public class ListItem  : ItemVO() {
    var Id:String ?= null
    var Name: String? = null
    var Price: String? = null
    var ImageURL: String? = null
    var Information: String?= null
    var Calorie:String?=null
    var MidCategory:String?=null

    internal override fun getType(): Int {
        return ItemVO.TYPE_DATA
    }
}

