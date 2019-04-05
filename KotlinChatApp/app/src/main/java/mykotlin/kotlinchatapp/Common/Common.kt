package mykotlin.kotlinchatapp.Common

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

object Common {
   public fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }


   public fun showToast(context: Context,text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }


   public fun showSnack(view: View,text: String,color: Int){

       val snackbar: Snackbar = Snackbar.make(view,text,Snackbar.LENGTH_SHORT)
       val view: View = snackbar.view
       view.setBackgroundColor(color)
       snackbar.show()



    }
}