package com.cocido.formokaizen.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cocido.formokaizen.databinding.ActivityRegisterBinding
import com.cocido.formokaizen.db.AppDatabase
import com.cocido.formokaizen.models.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "formokaizen_db").build()

        binding.btnRegister.setOnClickListener {
            val name = binding.inputName.text.toString()
            val email = binding.inputEmail.text.toString()
            val pass = binding.inputPassword.text.toString()
            if (name.isBlank() || email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val count = db.userDao().userCount()
                val role = if (count == 0) "admin" else "user"
                val user = User(name = name, email = email, password = pass, role = role)
                db.userDao().insert(user)
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registrado correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        binding.txtGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
