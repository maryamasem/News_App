package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.depi.whatnow.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.toString

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.newUserTv.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passEt.text.toString()
            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Missing Field", Toast.LENGTH_SHORT).show()
            else {
                binding.loadingProgress.isVisible = true
                //login logic
                singIn(email, password)
            }
        }

        binding.forgotPassCb.setOnClickListener {
            binding.loadingProgress.isVisible = true
            val email = binding.emailEt.text.toString()
            if (email.isBlank()) {
                Toast.makeText(this, "Please enter your email first", Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false

                return@setOnClickListener
            }
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.loadingProgress.isVisible = false
                        Toast.makeText(this, "Email Sent! ", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
    private fun singIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.loadingProgress.isVisible = false
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        startActivity(Intent(this, CategoriesActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "check your email !!!!!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            startActivity(Intent(this, CategoriesActivity::class.java))
            finish()
        }
    }
}