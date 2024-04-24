package com.example.bym

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bym.Login_Files.Login
import com.example.bym.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var result: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view) // Burada bottomNavigationView'ı tanımlıyoruz

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userId = user.uid
            ResultKontrol(userId)
        } else {
            // Kullanıcı null ise burada bir işlem gerçekleştir
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {
                        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_notifications -> {
                    if (result) {
                        // result true ise notifications fragmenta git
                        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_notifications)
                    } else {
                        // result false ise urunlerimfragmenta git
                        findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.urunlerim)
                    }
                    true
                }
                // Diğer durumlar buraya eklenir...
                else -> false
            }
        }

    }

    private fun ResultKontrol(userId: String) {

        db.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val resultKullanici = document.getBoolean("resultKullanici")
                    println("main $resultKullanici")

                    if (resultKullanici != null && resultKullanici) {
                        result = true
                        println("REsultkontrol başarılı : $result")
                    }
                } else {
                    // Belge null ise burada bir işlem gerçekleştir
                    Log.e("MainActivity", "Belge null döndü.")
                    println("REsultkontrol başarılı : $result")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Firestore sorgusu başarısız oldu: $exception")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Ayarlar tıklandığında hesaptan çıkış yap
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        auth.signOut()
        // Kullanıcıyı giriş ekranına yönlendir
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}
