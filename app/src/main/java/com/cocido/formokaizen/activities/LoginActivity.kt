package com.cocido.formokaizen.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.cocido.formokaizen.databinding.ActivityLoginBinding
import com.cocido.formokaizen.db.AppDatabase
import com.cocido.formokaizen.utils.SessionManager
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var session: SessionManager
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "formokaizen_db").build()
        session = SessionManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val pass = binding.inputPassword.text.toString()
            lifecycleScope.launch {
                val user = db.userDao().getUserByEmail(email)
                if (user != null && user.password == pass) {
                    session.saveUserId(user.id)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("userRole", user.role)
                    startActivity(intent)
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.txtGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
