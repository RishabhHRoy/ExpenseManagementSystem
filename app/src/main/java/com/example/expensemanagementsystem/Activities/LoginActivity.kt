package com.example.expensemanagementsystem.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.expensemanagementsystem.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createLoginUI()
    }

    private fun createLoginUI(){
        val providers = arrayListOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(), // Email authentication
            AuthUI.IdpConfig.GoogleBuilder().build() // Google authentication
            // Add more providers as per your requirement
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // Continue with your app logic here, e.g., navigate to the main activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Sign in failed. If response is null, the user canceled the sign-in flow.
                // Handle the error gracefully (e.g., display a message to the user).
                if(response == null) finish()

                if(response?.error?.errorCode == ErrorCodes.NO_NETWORK){
                    return
                }

                if(response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, response?.error?.errorCode.toString(), Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }
}