package com.example.bym.Login_Files

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.bym.R
import com.example.bym.databinding.ActivityCreateUserBinding

class Create_User : AppCompatActivity() {

    private lateinit var binding: ActivityCreateUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "BYM STORE"
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.deneme) as NavHostFragment
        val navController = navHostFragment.navController

        val radioGroup: RadioGroup = binding.radioGroup

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.Kullanici -> navController.navigate(R.id.kullanici)
                R.id.Satici -> navController.navigate(R.id.satici)
            }
        }
    }
}
