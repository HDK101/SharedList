package br.scl.ifsp.sharedlist.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity: AppCompatActivity() {
    private val activityForgotPasswordActivity: ActivityForgotPasswordBinding by lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityForgotPasswordActivity.root)

        auth = FirebaseAuth.getInstance()

        activityForgotPasswordActivity.buttonEmail.setOnClickListener {
            val email = activityForgotPasswordActivity.editTextEmail.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(this, "E-mail enviado", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Não foi possível enviar um e-mail de recuperação", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}