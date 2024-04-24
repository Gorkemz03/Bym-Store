
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.databinding.RecyclergecmisrowBinding
import com.example.bym.ui.navigation_buttons.dashboard.MyProfil.recyclerSiparis.Siparisdata

class Siparisreycylerview(private val siparisList: List<Siparisdata>) : RecyclerView.Adapter<Siparisreycylerview.SiparisViewHolder>() {

    // ViewHolder sınıfı
    class SiparisViewHolder(val binding: RecyclergecmisrowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiparisViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclergecmisrowBinding.inflate(inflater, parent, false)
        return SiparisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SiparisViewHolder, position: Int) {
        val currentSiparis = siparisList[position]

        // ViewBinding ile öğeleri ayarla
        holder.binding.date.text = currentSiparis.date.toString()

        /*holder.itemView.setOnClickListener {
            // Context al
            val context = holder.itemView.context

            // GecmisOnClick fragmentını başlatmak için bir transaction oluştur
            val transaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()

            // GecmisOnClick fragmentını oluştur
            val gecmisOnClickFragment = GecmisOnClick()

            // Argümanları Bundle'a ekle
            val args = Bundle()
            args.putString("date", currentSiparis.date.toString())
            args.putDouble(
                "totalPrice",
                currentSiparis.TotalPrice ?: 0.0
            )

            // Bundle'ı fragmenta set et
            gecmisOnClickFragment.arguments = args

            // Fragmentı transactiona ekle
            transaction.replace(R.id.fragment_container, gecmisOnClickFragment)

            // İsterseniz transactionı geri alma yığınına ekleyebilirsiniz
            // transaction.addToBackStack(null)

            // Transaction'ı gerçekleştir
            transaction.commit()
        }*/

        // Diğer ViewBinding öğelerini de burada ayarlayabilirsiniz.
    }




    // Diğer ViewBinding öğelerini de burada ayarlayabilirsiniz.


    override fun getItemCount(): Int {
        return siparisList.size
    }
}

