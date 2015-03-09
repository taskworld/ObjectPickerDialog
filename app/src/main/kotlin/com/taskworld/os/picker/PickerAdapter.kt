package com.taskworld.os.picker

import android.support.v7.widget.RecyclerView
import kotlin.properties.Delegates
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Filter
import android.view.View
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import java.util.ArrayList

/**
 * Created by VerachadW on 3/3/15.
 */
abstract class PickerAdapter<T, VH : PickerAdapter.ViewHolder>(items: List<T>) : RecyclerView.Adapter<VH>(), Filterable {

    private var mItems: List<T> by Delegates.observable(items, { (property, oldItems, newItems) ->
            mDisplayItems = newItems
            notifyDataSetChanged()
        })

    private var mDisplayItems = items

    // For multiple mode
    private val mPickedItems = hashMapOf<T, Boolean>()

    abstract fun onBindViewHolder(vh: VH, item: T, isPicked: Boolean)

    open fun validateItemWithConstraint(constraint: CharSequence, item: T): Boolean = true

    public fun getPickedItems(): List<T> {
        val items = arrayListOf<T>()
        for (entry in mPickedItems.entrySet()) {
            if (entry.getValue()) {
                items.add(entry.getKey())
            }
        }
        return items
    }

    override final fun onBindViewHolder(vh: VH, position: Int) {
        val item = mDisplayItems[position]
        onBindViewHolder(vh, item, mPickedItems.getOrElse(item, { false }))
    }

    override final fun getItemCount(): Int = mDisplayItems.size()

    abstract class ViewHolder(root: View): RecyclerView.ViewHolder(root), View.OnClickListener {
        {
            root.setOnClickListener(this)
        }

        abstract fun onItemPick(position: Int)

        override fun onClick(v: View) {
            onItemPick(getPosition())
        }
    }

    protected fun togglePickState(item: T) {
        val isChecked = mPickedItems.getOrElse(item, { false })
        if (isChecked) {
            // if checked or not exist, mark as not picked
            mPickedItems.put(item, false)
        } else {
            // if not, append the new one and mark as picked
            mPickedItems.put(item, true)
        }
    }

    [suppress("UNCHECKED_CAST")]
    override fun getFilter(): Filter {
        return object: Filter() {
            override fun  performFiltering(constraint: CharSequence): Filter.FilterResults {
                val result = Filter.FilterResults()
                val filteredItems = arrayListOf<T>()

                for (item in mDisplayItems) {
                    if (validateItemWithConstraint(constraint, item)) {
                        filteredItems.add(item)
                    }
                }

                result.values = filteredItems

                return result
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                if (constraint.length() > 0) {
                    mDisplayItems = results.values as ArrayList<T>
                } else {
                    mDisplayItems = mItems
                }
                this@PickerAdapter.notifyDataSetChanged()
            }
        }
    }
}