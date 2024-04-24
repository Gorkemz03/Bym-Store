import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bym.databinding.FragmentUpdateUrunBinding
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.recyclerUpdate.UpdateAdapter
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.recyclerUpdate.dataUpdate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUrun : Fragment() {

    private var _binding: FragmentUpdateUrunBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    private lateinit var updateAdapter: UpdateAdapter  // Değişiklik burada
    private val dataList = ArrayList<dataUpdate>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUrunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UpdateAdapter sınıfından bir örnek oluşturun
        updateAdapter = UpdateAdapter(dataList)

        // LinearLayoutManager'ı RecyclerView'e atayın
        binding.recyclerUpdate.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView'e adapter'ı set edin
        binding.recyclerUpdate.adapter = updateAdapter

        // Ürünleri getirmek için bu fonksiyonu çağırın
        fetchProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchProducts() {
        userId?.let { uid ->
            firestore.collection("Urunler")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val productId = document.id
                        val productName = document.getString("İsim")

                        // dataUpdate yerine dataUpdate kullanın
                        val product = dataUpdate(productName ?: "", productId)
                        dataList.add(product)
                    }
                    updateAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda işlemler
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
