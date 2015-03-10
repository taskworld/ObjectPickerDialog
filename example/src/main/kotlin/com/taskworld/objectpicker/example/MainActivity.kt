package com.taskworld.objectpicker.example

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import kotlin.properties.Delegates
import android.widget.Button
import com.taskworld.os.picker.ObjectPickerDialog
import android.widget.Toast

/**
 * Created by VerachadW on 3/3/15.
 */
class MainActivity: ActionBarActivity() {

    val btShowMultiple by Delegates.lazy { findViewById(R.id.btShowMultiple) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = listOf("Villager", "Seer", "WereWolf", "Drunken", "Trouble Maker", "Bodyguard", "Cupid")

        btShowMultiple.setOnClickListener {

            val dialog = ObjectPickerDialog.Builder<String, RoleAdapter.ViewHolder>()
                    .setTitle("Pick Role(s)")
                    .setItemAdapter(RoleAdapter(data))
                    .setConfirmAction ("SELECT", { items ->
                        Toast.makeText(this@MainActivity, items.toString(), Toast.LENGTH_LONG).show()
                    }).build()
            dialog.show(getSupportFragmentManager(), "")
        }

    }


}