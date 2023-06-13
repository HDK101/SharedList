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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.scl.ifsp.sharedlist.databinding.ActivityLoginBinding

import br.scl.ifsp.sharedlist.R
import br.scl.ifsp.sharedlist.model.Task
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    private lateinit var googleActivityResultLauncher : ActivityResultLauncher<Intent>

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy{
        GoogleSignIn.getClient(this, googleSignInOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextEmail = binding.editTextEmail
        val editTextPassword = binding.editTextPassword
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister
        val buttonForgotPassword = binding.buttonForgotPassword
        val signInGoogleButton = binding.signInButtonGoogle

        googleActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                //Pegando retorno do login com conta google
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val googleSignInAccount: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(this, "Usuário ${googleSignInAccount.email} autenticado com sucesso!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, TasksActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação de usuário!", Toast.LENGTH_LONG).show()
                }
            }
        }

        buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        buttonLogin.setOnClickListener {

            val authInstance = FirebaseAuth.getInstance();
            val emailText = editTextEmail.text.toString()
            val passwordText = editTextPassword.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) return@setOnClickListener;

            authInstance
                .signInWithEmailAndPassword(emailText, passwordText)
                .addOnSuccessListener {
                    startActivity(Intent(this, TasksActivity::class.java))
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

        signInGoogleButton.setOnClickListener {
            googleActivityResultLauncher.launch(googleSignInClient.signInIntent)
        }

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, TasksActivity::class.java))
        }
    }
}
