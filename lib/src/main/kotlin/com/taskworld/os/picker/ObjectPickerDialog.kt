package com.taskworld.os.picker

import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import kotlin.properties.Delegates
import com.taskworld.picker.R
import android.widget.TextView
import android.app.Activity
import android.support.v7.widget.SearchView
import android.widget.Button
import android.content.DialogInterface
import com.taskworld.os.picker.ObjectPickerDialog.Builder
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.app.Dialog
import android.app.AlertDialog

/**
 * Created by VerachadW on 3/2/15.
 */
class ObjectPickerDialog<T, VH : PickerAdapter.ViewHolder>(private val builder: Builder<T, VH>) : DialogFragment(), SearchView.OnQueryTextListener {

    val svItems by Delegates.lazy { vRoot.findViewById(R.id.svItems) as SearchView }
    val rvItems by Delegates.lazy { vRoot.findViewById(R.id.rvItems) as RecyclerView }

    var vRoot: View by Delegates.notNull()

    class object {

        class Builder<T, VH : PickerAdapter.ViewHolder>() {
            var title: String by Delegates.notNull()
            var confirmAction: ((items: List<T>) -> Unit) by Delegates.notNull()
            var adapter: PickerAdapter<T, VH> by Delegates.notNull()
            var confirmText: String by Delegates.notNull()

            public fun setTitle(title: String): Builder<T, VH> {
                this.title = title
                return this
            }

            public fun setItemAdapter(adapter: PickerAdapter<T, VH>): Builder<T, VH> {
                this.adapter = adapter
                return this
            }

            public fun setConfirmAction(confirmText: String, confirmBlock: ((items: List<T>) -> Unit)): Builder<T, VH> {
                this.confirmText = confirmText
                this.confirmAction = confirmBlock
                return this
            }

            public fun build(): ObjectPickerDialog<T, VH> {
                return ObjectPickerDialog(this)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val dialogBuilder = AlertDialog.Builder(getActivity())
        dialogBuilder.setTitle(builder.title)
                .setPositiveButton(builder.confirmText, { dialog, view ->
                    builder.confirmAction(builder.adapter.getPickedItems())
                })

        vRoot = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_picker, null, false)
        val layoutManager = LinearLayoutManager(getActivity())
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        rvItems.setLayoutManager(layoutManager)
        rvItems.setAdapter(builder.adapter)

        // Hack to show search action when click anywhere on SearchView
        svItems.setOnClickListener {
            svItems.onActionViewExpanded()
        }

        dialogBuilder.setView(vRoot)

        return dialogBuilder.create()
    }

    override fun onQueryTextChange(query: String?): Boolean {
        builder.adapter.getFilter().filter(query)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        builder.adapter.getFilter().filter(query)
        return true
    }
}
