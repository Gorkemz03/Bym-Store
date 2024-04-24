package com.example.bym.recycler

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.Urun_Aciklama
import com.example.bym.databinding.RecyclerviewszBinding
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val veriList: ArrayList<Veri>) : RecyclerView.Adapter<FeedRecyclerAdapter.VeriHolder>() {
    class VeriHolder(val binding: RecyclerviewszBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeriHolder {
        val binding = RecyclerviewszBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VeriHolder(binding)
    }

    override fun getItemCount(): Int {
        return veriList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VeriHolder, position: Int) {
        val currentVeri = veriList[position]
        holder.binding.aciklama.text = "Ürün:${currentVeri.isim} Fiyat:${currentVeri.fiyat} TL"

        Picasso.get().load(veriList.get(position).imageurl).into(holder.binding.gRsel)

        holder.itemView.setOnClickListener {
            // Birden fazla veriyi içeren bir ArrayList oluştur
            val dataArrayList = ArrayList<Any>()
            dataArrayList.add(currentVeri.isim)
            dataArrayList.add(currentVeri.aciklama)
            dataArrayList.add(currentVeri.imageurl)
            dataArrayList.add(currentVeri.fiyat)

            // Intent'e ArrayList'i ekle
            val intent = Intent(holder.itemView.context, Urun_Aciklama::class.java)
            intent.putExtra("dataArrayList", dataArrayList)

            // Aktiviteyi başlat
            holder.itemView.context.startActivity(intent)
        }
    }


}