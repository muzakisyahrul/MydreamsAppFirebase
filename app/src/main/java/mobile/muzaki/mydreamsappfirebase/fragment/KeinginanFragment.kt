package mobile.muzaki.mydreamsappfirebase.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_keinginan.*
import kotlinx.android.synthetic.main.toolbar_primary.*
import mobile.muzaki.mydreamsappfirebase.*


class KeinginanFragment : Fragment(), IOnBackPressed,View.OnClickListener {
    private val TAG:String="KeinginanFragment"
    private lateinit var helper: Helper
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var keinginanAdapter: KeinginanAdapter
    private var listKeinginan = ArrayList<Keinginan>()

    companion object {
        fun newInstance(): KeinginanFragment {
            return KeinginanFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = Helper.newInstance(requireContext(),requireActivity())
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance(getString(R.string.server_firebase_db))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keinginan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        toolbar_title.setText("Daftar Keinginan")
        btnTambah!!.setOnClickListener(this)
        loadData();
        keinginanAdapter = KeinginanAdapter(
            arrayListOf(),
            requireContext(),
            requireActivity(),
            object : KeinginanAdapter.OnAdapterListener {
                override fun onClick(data: Keinginan) {

                }

                override fun onUpdate(data: Keinginan) {
                    (activity as MainActivity).setHalaman(KeinginanAddFragment.newInstance(
                        data.key,data.nama,data.deskripsi,data.harga.toString(),data.terpenuhi.toString()
                    ))
                }

                override fun onDelete(data: Keinginan) {
                    deleteAlert(data)
                }

            })

        rv_keinginan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keinginanAdapter
        }
    }

    private fun loadData() {
        //helper.tampilToast("Mohon Tunggu Sebentar...")
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child(getUserID).child("Keinginan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listKeinginan.clear();
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val keinginan =
                                snapshot.getValue(Keinginan::class.java)
                            keinginan?.key = snapshot.key
                            listKeinginan.add(keinginan!!)
                        }

                    }
                    keinginanAdapter.setData(listKeinginan)
                    keinginanAdapter.notifyDataSetChanged()
                    //helper.tampilToast("Data Berhasil Dimuat")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Kode ini akan dijalankan ketika ada error, simpan ke LogCat
                    Toast.makeText(context, "Data Gagal Dimuat",
                        Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " +
                            databaseError.message)
                }
            })
    }

    override fun onBackPressed(): Boolean {
        (activity as MainActivity).setBottomNavigationSelected(R.id.nav_home)
        return false
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnTambah -> {
                    (activity as MainActivity).setHalaman(KeinginanAddFragment.newInstance("","","","",""))
                }
            }
        }
    }

    private fun deleteAlert(data: Keinginan){
        val key = data.key
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply {
            setTitle("Konfirmasi Hapus")
            setMessage("Yakin hapus ${data.nama}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                val getReference = database.getReference()
                getReference
                    .child(auth.uid!!)
                    .child("Keinginan")
                    .child(key!!)
                    .removeValue()
                    .addOnSuccessListener {
                        helper.tampilToast("Data Berhasil Dihapus")
                    }
            }
        }

        dialog.show()
    }

}