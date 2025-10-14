package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.depi.whatnow.databinding.ActivitySettingBinding
import kotlin.math.log

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)

        val prefCountry = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val countryCode = prefCountry.getString("country", "us")

        when (countryCode) {
            "eg" -> binding.countriesRg.check(R.id.egy_rb)
            "us" -> binding.countriesRg.check(R.id.us_rb)
            "gb" -> binding.countriesRg.check(R.id.uk_rb)
            else -> binding.countriesRg.check(R.id.us_rb)
        }

        binding.saveBtn.setOnClickListener {

            val selectedCountry = when (binding.countriesRg.checkedRadioButtonId) {
                R.id.us_rb -> "us"
                R.id.egy_rb -> "eg"
                R.id.uk_rb -> "gb"
                else -> "us"
            }

            val editor = prefCountry.edit()
            editor.putString("country", selectedCountry)
            editor.apply()

            finish()
        }
    }
}
