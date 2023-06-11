package br.scl.ifsp.sharedlist.view

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import br.scl.ifsp.sharedlist.databinding.ActivityLoginBinding

import br.scl.ifsp.sharedlist.R
import br.scl.ifsp.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextEmail = binding.editTextEmail
        val editTextPassword = binding.editTextPassword
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister

        buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonLogin.setOnClickListener {
            startActivity(Intent(this, TasksActivity::class.java))
            finish()

            return@setOnClickListener

            val authInstance = FirebaseAuth.getInstance();
            val emailText = editTextEmail.text.toString()
            val passwordText = editTextPassword.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) return@setOnClickListener;

            authInstance
                .signInWithEmailAndPassword(emailText, passwordText)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Usuário $emailText autenticado com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação do usuário!", Toast.LENGTH_LONG)
                        .show()
                }
        }

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "hello!", Toast.LENGTH_SHORT).show();
            Log.d("asd", "walter")
        }
    }
}
