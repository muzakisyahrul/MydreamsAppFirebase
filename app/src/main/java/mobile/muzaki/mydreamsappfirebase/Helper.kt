package mobile.muzaki.mydreamsappfirebase

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.NumberFormat
import java.util.*

class Helper (){
    private lateinit var context: Context

    constructor(context: Context,activity: Activity) : this() {
        this.context = context
    }
    companion object {
        fun newInstance(context: Context,activity: Activity): Helper {
            return Helper(context,activity)
        }
    }

    fun tampilToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    fun parse_rupiah(rupiah: String): String? {
        val hasil = rupiah.replace("Rp ", "").replace(",-", "").replace(",", "")
        Log.e("parse_rupiah", "hasil = $hasil")
        return hasil
    }

    fun rupiah(nominal: Double): String? {
        //return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format((long) nominal);
        var hasil =
            "Rp " + NumberFormat.getNumberInstance(Locale.US).format(remove_float_double(nominal))
        hasil = hasil.replace(",", ".")
        hasil += ""
        return hasil
    }

    private fun remove_float_double(value: Double): Double? {
        val s = String.format("%.0f", value)
        return java.lang.Double.valueOf(s)
    }

    fun rupiah_koma(nominal: Double): String? {
        //return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format((long) nominal);
        var hasil =
            NumberFormat.getNumberInstance(Locale.US).format(remove_float_double(nominal))
        hasil = hasil.replace(".", ",")
        hasil += ""
        return hasil
    }
}