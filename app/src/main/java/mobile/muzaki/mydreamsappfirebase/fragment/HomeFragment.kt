package mobile.muzaki.mydreamsappfirebase.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import mobile.muzaki.mydreamsappfirebase.Helper
import mobile.muzaki.mydreamsappfirebase.IOnBackPressed
import mobile.muzaki.mydreamsappfirebase.LoginActivity
import mobile.muzaki.mydreamsappfirebase.R

class HomeFragment : Fragment(), IOnBackPressed {
    private val TAG:String="HomeFragment"
    private lateinit var helper: Helper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = Helper.newInstance(requireContext(),requireActivity())
        auth = Firebase.auth
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val user = auth.currentUser
        tv_nama_user.setText(user!!.displayName)
        tv_email_user.setText(user.email)
        Glide.with(context).load(user.photoUrl).into(image_profil);
        btnLogout.setOnClickListener {
            auth.signOut()
            helper.tampilToast("Logout Berhasil")
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }


    override fun onBackPressed(): Boolean {
        return true
    }

}