package mobile.muzaki.mydreamsappfirebase.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_keinginan_add.*
import kotlinx.android.synthetic.main.toolbar_primary.*
import mobile.muzaki.mydreamsappfirebase.Helper
import mobile.muzaki.mydreamsappfirebase.Keinginan
import mobile.muzaki.mydreamsappfirebase.MainActivity
import mobile.muzaki.mydreamsappfirebase.R

class KeinginanAddFragment : Fragment(),View.OnClickListener {
    private lateinit var helper: Helper
    private val TAG:String="KeinginanAddFragment"
    private lateinit var auth: FirebaseAuth

    private val ID_EDIT = "ID_EDIT"
    private val NAMA = "NAMA"
    private val DESKRIPSI = "DESKRIPSI"
    private val HARGA = "HARGA"
    private val TERPENUHI = "TERPENUHI"

    private var id_keinginan = ""
    private var nama : String = ""
    private var deskripsi : String = ""
    private var harga : String = ""
    private var terpenuhi : String = ""



    companion object {
        @JvmStatic
        fun newInstance(param1: String?,param2: String?,param3: String?,param4: String?,param5: String?) =
            KeinginanAddFragment().apply {
                arguments = Bundle().apply {
                    if (param1 != null) {
                        putString(ID_EDIT, param1)
                        putString(NAMA, param2)
                        putString(DESKRIPSI, param3)
                        putString(HARGA, param4)
                        putString(TERPENUHI, param5)
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id_keinginan = it.getString(ID_EDIT).toString()
            nama = it.getString(NAMA).toString()
            deskripsi = it.getString(DESKRIPSI).toString()
            harga = it.getString(HARGA).toString()
            terpenuhi = it.getString(TERPENUHI).toString()
        }
        helper = Helper.newInstance(requireContext(),requireActivity())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keinginan_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if(id_keinginan.isEmpty()){
            toolbar_title.setText("Tambah Daftar Keinginan")
            lnTerpenuhi.isVisible = false
        }else{
            toolbar_title.setText("Edit Daftar Keinginan")
            lnTerpenuhi.isVisible = true
            etNama.setText(nama)
            etDeskripsi.setText(deskripsi)
            etHarga.setText(helper.rupiah_koma(harga.toDouble()))
            if(!terpenuhi.isEmpty()){
                etTerpenuhi.setText(helper.rupiah_koma(terpenuhi.toDouble()))
            }
        }
        (activity as MainActivity).setSupportActionBar(app_toolbar)
        (activity as MainActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        (activity as MainActivity).onSupportNavigateUp()
        btnSave!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnSave -> {
                    validasiInput()
                }
            }
        }
    }

    private fun validasiInput() {
        nama = etNama.text.toString()
        deskripsi = etDeskripsi.text.toString()
        harga = helper.parse_rupiah(etHarga.text.toString()).toString()
        terpenuhi = helper.parse_rupiah(etTerpenuhi.text.toString()).toString()
        when{
            nama.isEmpty() -> etNama.error = "Nama tidak boleh kosong"
            deskripsi.isEmpty() -> etDeskripsi.error = "Deskripsi tidak boleh kosong"
            harga.isEmpty() -> etHarga.error = "Harga tidak boleh kosong"
            else -> {
                val uuid = auth!!.currentUser!!.uid
                //Mendapatkan Instance dari Database
                val database = FirebaseDatabase.getInstance(getString(R.string.server_firebase_db))
                val getReference: DatabaseReference
                getReference = database.reference

                var harga_fix = 0.0
                var terpenuhi_fix =0.0
                if(!harga.isEmpty()){
                    harga_fix = harga.toDouble()
                }
                if(!terpenuhi.isEmpty()){
                    terpenuhi_fix = terpenuhi.toDouble()
                }
                if(id_keinginan.isEmpty()) {
                    getReference.child(uuid).child("Keinginan").push()
                        .setValue(Keinginan(nama, deskripsi, harga_fix, terpenuhi_fix))
                        .addOnCompleteListener(requireActivity()) { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            helper.tampilToast("Data Tersimpan")
                            (activity as MainActivity).onBackPressed()
                        }
                }else{
                    getReference.child(uuid).child("Keinginan").child(id_keinginan)
                        .setValue(Keinginan(nama, deskripsi, harga_fix, terpenuhi_fix))
                        .addOnSuccessListener { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            helper.tampilToast("Data Tersimpan")
                            (activity as MainActivity).onBackPressed()
                        }
                }

            }
        }

    }



}