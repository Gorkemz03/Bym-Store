package com.example.bym.ui.navigation_buttons.dashboard.MyProfil

import Siparisreycylerview
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bym.databinding.FragmentGecmisSipraislerBinding
import com.example.bym.ui.navigation_buttons.dashboard.MyProfil.recyclerSiparis.Siparisdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GecmisSipraisler : Fragment() {
    private var _binding: FragmentGecmisSipraislerBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var veriArrayList: ArrayList<Siparisdata>
    private lateinit var siparisreycylerview: Siparisreycylerview
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGecmisSipraislerBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        veriArrayList = ArrayList()

        val userID = auth.currentUser?.uid

        if (userID != null) {
            fetchSiparisData(userID)
        }


        binding.RecycylerGecmis.layoutManager = LinearLayoutManager(requireContext())
        siparisreycylerview = Siparisreycylerview(veriArrayList)
        binding.RecycylerGecmis.adapter = siparisreycylerview

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSiparisData(userID: String) {
        // Firestore koleksiyon yollarını belirtin
        val gecmisSiparislerCollection = db.collection("GecmisSiparisler").document(userID).collection("Siparisler")

        // Firestore veri çekme işlemi
        gecmisSiparislerCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val ucret = document.getDouble("toplamUcret")
                    val urunFiyati = document.getDate("tarih")

                    // Firestore'dan gelen verileri ArrayList'e ekle
                    val siparisData = Siparisdata(urunFiyati,ucret )
                    veriArrayList.add(siparisData)
                }
                veriArrayList.sortWith(compareBy { it.date })
                veriArrayList.reverse()
                siparisreycylerview.notifyDataSetChanged()

                println(veriArrayList)
            }
            .addOnFailureListener {

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
