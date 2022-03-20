package mobile.muzaki.mydreamsappfirebase

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    val mContext: Context =this;
    val SPLASH_TIME_OUT = 2000
    private lateinit var helper: Helper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        helper = Helper.newInstance(this,this)
        auth = Firebase.auth

        /// tampilkan loading gif
        Glide.with(mContext).load(R.drawable.loading1).into(ivLoadingScreen);

        /// menampilkan versi android
        try {
            val pInfo: PackageInfo =
                mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0)
            val version = pInfo.versionName
            tvVersionApp.setText(version);
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            if(currentUser!=null){
                helper.tampilToast("Welcome "+currentUser!!.displayName.toString())
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}