package com.example.bym.ui.navigation_buttons.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bym.databinding.FragmentHomeBinding
import com.example.bym.recycler.FeedRecyclerAdapter
import com.example.bym.recycler.Veri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var veriArrayList: ArrayList<Veri>
    private lateinit var feedAdapter: FeedRecyclerAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        veriArrayList = ArrayList()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = FeedRecyclerAdapter(veriArrayList)

        binding.recyclerView.adapter = feedAdapter

        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getData() {
        db.collection("Urunler").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (value != null && !value.isEmpty) {
                    val documents = value.documents
                    for (document in documents) {
                        // İsim
                        val isim = document.getString("İsim")
                        // Açıklama
                        val aciklama = document.getString("Açıklama")
                        // Renk
                        val renk = document.getString("Renk")
                        // Image URL
                        val imageurl = document.getString("imageurl")
                        // Fiyat
                        val fiyat = (document.getLong("Fiyat")?.toInt()) ?: 0
                        // Puan
                        val puan = (document.getLong("Puan")?.toInt()) ?: 0
                        // Stok
                        val stok = (document.getLong("Stok")?.toInt()) ?: 0

                        // Null kontrolü ekleyin

                            val veri = Veri(isim.toString(),
                                aciklama.toString(), renk.toString(),
                                imageurl.toString(), fiyat, puan, stok)
                            veriArrayList.add(veri)

                    }
                    feedAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}