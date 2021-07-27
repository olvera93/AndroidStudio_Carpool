package com.example.carpool.progressbar

import android.app.Activity
import android.app.AlertDialog
import com.example.carpool.R

class LoadingDialog(val myActivity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {

        val infalter = myActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item, null)
        val builder = AlertDialog.Builder(myActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}