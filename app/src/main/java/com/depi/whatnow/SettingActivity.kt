package com.depi.whatnow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.depi.whatnow.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)

        val prefCountry = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val countryCode = prefCountry.getString("country", "Egy")

        when (countryCode){
            "Egy" -> binding.countriesRg.check(R.id.eg_btn)
            "US" -> binding.countriesRg.check(R.id.us_rb)
            else -> binding.countriesRg.check(R.id.us_rb)
        }

        binding.saveBtn.setOnClickListener {

            val selectedCountry = binding.countriesRg.checkedRadioButtonId.toString()

            val editor = prefCountry.edit()
            editor.putString("country", selectedCountry)
            editor.apply()

            Toast.makeText(this, "Saved: $selectedCountry", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}