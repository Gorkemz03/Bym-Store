package com.example.bym.ui.navigation_buttons.notifications.Satici.fragments

import DeleteAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bym.databinding.FragmentDeleteUrunBinding
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.recyclerDelete.datadelete
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUrun : Fragment() {

    private lateinit var binding: FragmentDeleteUrunBinding
    private lateinit var deleteAdapter: DeleteAdapter
    private val dataList = mutableListOf<datadelete>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteUrunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        deleteAdapter = DeleteAdapter(dataList)
        binding.deleterecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.deleterecycler.adapter = deleteAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchProducts() {
        userId?.let { uid ->
            firestore.collection("Urunler")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val productId = document.id // Firestore belgesinin ID'sini alıyoruz
                        val productName = document.getString("İsim")
                        val product = datadelete(productName ?: "", productId)
                        dataList.add(product)
                    }
                    deleteAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda işlemler
                }
        }
    }

}
