package pt.atp.app_seai_g

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pt.atp.app_seai_g.models.User
import pt.atp.app_seai_g.models.Vehicle

// Activity to register the user


class ActivityRegister : AppCompatActivity() {

    private var nameTV: EditText? = null
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var confirmPasswordTV: EditText? = null
    private var regBtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        regBtn!!.setOnClickListener{
            registerNewUser()
        }
        database = Firebase.database.reference
    }

    public override fun onStart() {
        super.onStart()
        mAuth?.currentUser?.let{
            onAuthSuccess(it)
        }
    }

    private fun onAuthSuccess(user: FirebaseUser) {
        val username = usernameFromEmail(user.email!!)
        writeNewUserDatabase(user.uid, username, user.email!!)
        // Go to login activity
        Toast.makeText(applicationContext, getString(R.string.successRegister), Toast.LENGTH_LONG).show()
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            email
        }
    }

    private fun writeNewUserDatabase(userId: String, name: String, email: String) {
        val user = User(name,email)
        database.child("users").child(userId).setValue(user)
    }

    private fun registerNewUser() {
        val name: String = nameTV!!.text.toString()
        val email: String = emailTV!!.text.toString()
        val password: String = passwordTV!!.text.toString()
        val confirmPassword: String = confirmPasswordTV!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, getString(R.string.enterName), Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, getString(R.string.enterEmail), Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, getString(R.string.enterPassword), Toast.LENGTH_LONG).show()
            return
        }
        if(password != confirmPassword){
            Toast.makeText(applicationContext, getString(R.string.matchPassword), Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onAuthSuccess(task.result?.user!!)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.failRegister), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        nameTV = findViewById(R.id.name)
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        confirmPasswordTV = findViewById(R.id.confirm_password)
        regBtn = findViewById(R.id.button_register)
    }
}