package com.example.hackathon_cambridge_v1.Presentation.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.example.hackathon_cambridge_v1.Domain.Models.Point.ChildsItem
import com.example.hackathon_cambridge_v1.Domain.Models.Point.FuelCategoryItem
import com.example.hackathon_cambridge_v1.Domain.Models.Point.PointItem
import com.example.hackathon_cambridge_v1.R
import com.yandex.mapkit.geometry.Point
import kotlin.collections.HashMap

class PointCategoriesAdapter(private val context: Context,private val categories: ArrayList<FuelCategoryItem?>,
                        private val productList: HashMap<String, ArrayList<ChildsItem>?>):
    BaseExpandableListAdapter()
{
    override fun getGroupCount(): Int
    {
        return categories.size
    }

    override fun getChildrenCount(groupPosition: Int): Int
    {
        return productList[categories[groupPosition].toString()]!!.size
    }

    override fun getGroup(groupPosition: Int): FuelCategoryItem?
    {
        return categories[groupPosition]
    }

    override fun getChild(groupPosition: Int,childPosition: Int): Any
    {
        return productList[categories[groupPosition].toString()]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long
    {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int,childPosition: Int): Long
    {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean
    {
        return true
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int,
                              isExpanded: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View?
    {
        val categoryTitle: String? = categories[groupPosition]?.name
        var convertV = convertView
        if(convertView == null)
        {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //TODO what's list_group?

            convertV = inflater.inflate(R.layout.category_item, null)
        }
        val categoryTitleTextView = convertV?.findViewById(R.id.category_item) as TextView
        categoryTitleTextView.text = categoryTitle

        return convertV
    }

    @SuppressLint("InflateParams")
    override fun getChildView(groupPosition: Int,
                              childPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View?
    {
        val productTitle: String = getChild(groupPosition, childPosition).toString()
        var convertV = convertView
        if(convertView == null)
        {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertV = inflater.inflate(R.layout.product_item, null)
        }
        val productTitleTextView = convertV?.findViewById(R.id.product_item) as TextView
        productTitleTextView.text = productTitle

        return convertV
    }

    override fun isChildSelectable(groupPosition: Int,childPosition: Int): Boolean
    {
        return true
    }

}