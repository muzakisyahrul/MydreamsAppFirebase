package mobile.muzaki.mydreamsappfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_primary.*

class LoginActivity : AppCompatActivity(),View.OnClickListener{
    private lateinit var helper: Helper
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        helper = Helper.newInstance(this,this)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        initView();
    }

    private fun initView() {
        toolbar_title.setText("Login");
        btnLogin.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnLogin -> {
                    signIn();
                }
            }
        }
    }

    private fun signIn() {
        googleSignInClient.signOut();
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                helper.tampilToast("Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    helper.tampilToast("Login Berhasil")
                    checkAuth(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    helper.tampilToast("signInWithCredential:failure")
                }
            }
    }

    private fun checkAuth(user: FirebaseUser?) {
        if(user!=null){
            helper.tampilToast("Welcome "+user!!.displayName.toString())
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}