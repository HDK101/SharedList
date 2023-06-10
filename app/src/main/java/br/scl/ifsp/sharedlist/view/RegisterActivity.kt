package br.scl.ifsp.sharedlist.view

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {
    private val activityRegisterBinding: ActivityRegisterBinding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityRegisterBinding.root)

        activityRegisterBinding.buttonRegister.setOnClickListener {
            val email = activityRegisterBinding.editTextEmail.text.toString()
            val password = activityRegisterBinding.editTextPassword.text.toString()

            //if (password.equals(password2)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Usuário $email criado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Erro na criação do usuário!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
//            } else {
//                //Senhas não batem
//                Toast.makeText(
//                    this@CreateAccountActivity,
//                    "Senhas não coincidem!",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
        }
    }
}