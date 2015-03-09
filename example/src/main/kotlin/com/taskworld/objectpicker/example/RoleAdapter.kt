package com.taskworld.objectpicker.example

import com.taskworld.os.picker.PickerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.properties.Delegates
import com.taskworld.objectpicker.example
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.CompoundButton
import android.util.Log

/**
 * Created by VerachadW on 3/5/15.
 */
class RoleAdapter(items: List<String>): PickerAdapter<String, RoleAdapter.ViewHolder>(items) {

    override fun onBindViewHolder(vh: ViewHolder, item: String, isPicked: Boolean) {
        vh.textview.setText(item)
        vh.checkbox.setOnCheckedChangeListener(null)
        vh.checkbox.setChecked(isPicked)
        vh.checkbox.setOnCheckedChangeListener { (compoundButton, isChecked) ->
            super.togglePickState(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_multiple_item_role, parent, false)
        return ViewHolder(root)
    }

    override fun validateItemWithConstraint(constraint: CharSequence, item: String): Boolean {
        return item.toLowerCase().contains(constraint.toString().toLowerCase())
    }

    class ViewHolder(root: View) : PickerAdapter.ViewHolder(root) {
        override fun onItemPick(position: Int) {
            checkbox.toggle()
        }

        val textview by Delegates.lazy { root.findViewById(R.id.textview) as TextView }
        val checkbox by Delegates.lazy { root.findViewById(R.id.checkbox) as CheckBox }
    }
}
