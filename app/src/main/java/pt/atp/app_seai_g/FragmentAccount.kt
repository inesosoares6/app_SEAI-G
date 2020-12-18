package pt.atp.app_seai_g

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

// Fragment of account page
//    - sign out
//    - go to settings

class FragmentAccount : Fragment(R.layout.fragment_account) {

    private var fbAuth = FirebaseAuth.getInstance()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_account,container,false)
        val buttonLogout: Button = rootView.findViewById(R.id.logoutButton)
        val settingsButton: FloatingActionButton = rootView.findViewById(R.id.settingsButton)
        val switch1: Switch = rootView.findViewById(R.id.switch1)

        buttonLogout.setOnClickListener{
            fbAuth.signOut()
            Toast.makeText(context,getString(R.string.successSignOut),Toast.LENGTH_LONG).show()
            val intent = Intent(context,ActivityLogin::class.java)
            startActivity(intent)
        }

        fbAuth.addAuthStateListener {
            if(fbAuth.currentUser == null){
                activity?.finish()
            }
        }

        settingsButton.setOnClickListener{
            val intent = Intent(context,SettingsActivity::class.java)
            startActivity(intent)
        }

        switch1.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return rootView
    }
}