package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.depi.whatnow.databinding.ActivitySignUpBinding
import kotlin.toString

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.alreadyUserTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.signupBtn.setOnClickListener {
            val email =binding.emailEt.text.toString()
            val password=binding.passEt.text.toString()
            val conpass=binding.confirmPassEt.text.toString()
            if (email.isBlank()||password.isBlank()||conpass.isBlank())
                Toast.makeText(this, "Missing Field", Toast.LENGTH_SHORT).show()
            else if (password.length<6)
                Toast.makeText(this, "Short password", Toast.LENGTH_SHORT).show()
            else if(password!=conpass)
                Toast.makeText(this, "Passwords Don't Match", Toast.LENGTH_SHORT).show()
            else{
                binding.loadingProgress.isVisible=true
//                addUser()
            }
        }
    }
}