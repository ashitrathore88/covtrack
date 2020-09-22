package com.covid.covtrack.utilities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.covid.covtrack.R


class AlertMessages {

    init {
        // Prevent form the reflection api to create new instance from constructor.
        if (dialogUtil != null) {
            throw RuntimeException("Use getInstance() method to get the single instance of this class.")
        }
    }


    private var progressDialog: ProgressDialog? = null
    public var dialog: ProgressDialog? = null


    fun showProgressDialog(mcontext: Context) {
        progressDialog = ProgressDialog(mcontext)
        progressDialog!!.setMessage("loading")
        progressDialog!!.show()
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    fun alertMsgBox(message: String, mcontext: Context) {
        AlertDialog.Builder(mcontext,R.style.CustomAlertDialog)
                .setTitle(message)
                .setMessage(null)
                .setPositiveButton(android.R.string.yes) { dialog, which -> }
                .show()
    }

    companion object {

        var dialogUtil: AlertMessages? = null

        @Synchronized
        fun getInstance(): AlertMessages {
            if (dialogUtil == null) {
                dialogUtil = AlertMessages()
            }
            return dialogUtil as AlertMessages
        }


        fun showCustomToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showCustomDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }

    fun displayProgress(context: Context, show: Boolean) {
        if (show) {
            dialog = ProgressDialog(context) // this = YourActivity
            dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog!!.setIndeterminate(true)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (!dialog!!.isShowing() && !(context as AppCompatActivity).isFinishing && !context.isDestroyed) {
                dialog!!.show()
            }
        } else {
            if (dialog != null && dialog!!.isShowing()) {
                dialog!!.dismiss()
            }
            dialog = null
        }
        if (dialog != null) {
            dialog!!.setContentView(R.layout.progress_bar)
        }

    }

}
