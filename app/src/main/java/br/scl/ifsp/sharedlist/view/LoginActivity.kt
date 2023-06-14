package br.scl.ifsp.sharedlist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.scl.ifsp.sharedlist.databinding.ActivityLoginBinding

import br.scl.ifsp.sharedlist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    companion object {
        val LOGGED_WITH_EMAIL_PREFERENCE = "LOGGED_WITH_EMAIL_PREFERENCE"
        val EMAIL_EXTRA = "EMAIL_EXTRA"
    }

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

        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        val editTextEmail = binding.editTextEmail
        val editTextPassword = binding.editTextPassword
        val buttonLogin = binding.buttonLogin
        val buttonRegister = binding.buttonRegister
        val buttonForgotPassword = binding.buttonForgotPassword
        val signInGoogleButton = binding.signInButtonGoogle

        val logoutEmail = intent.getStringExtra(EMAIL_EXTRA) ?: ""

        Log.d("LOGGED", sharedPref.getBoolean(LOGGED_WITH_EMAIL_PREFERENCE, false).toString())

        if (sharedPref.getBoolean(LOGGED_WITH_EMAIL_PREFERENCE, false)) {
            editTextEmail.setText(logoutEmail)
            with (sharedPref.edit()) {
                putBoolean(LOGGED_WITH_EMAIL_PREFERENCE, false)
                apply()
            }
        }

        googleActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                //Pegando retorno do login com conta google
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val googleSignInAccount: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(this, "Usuário ${googleSignInAccount.email} autenticado com sucesso!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, TasksActivity::class.java))
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
                    val tasksIntent = Intent(this, TasksActivity::class.java)
                    with (sharedPref.edit()) {
                        putBoolean(LOGGED_WITH_EMAIL_PREFERENCE, true)
                        apply()
                    }
                    startActivity(tasksIntent)
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
