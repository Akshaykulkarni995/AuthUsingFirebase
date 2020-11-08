 package com.example.appusingfirebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

 class MainActivity : AppCompatActivity() {

    lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
//        auth.signOut()


        register.setOnClickListener {
            reg_user()
        }
        login.setOnClickListener {
            login_user()
        }

        update.setOnClickListener {
            updateProfile()
        }


    }

     override fun onStart() {
         super.onStart()
         checkLoggedInstate()
     }

     private fun updateProfile(){
          auth.currentUser.let { user ->
              val username = uname.text.toString()
              val photouri = Uri.parse("android.resource://$packageName/${R.drawable.ic_launcher_background}")
              val profileupdates = UserProfileChangeRequest.Builder()
                  .setDisplayName(username)
                  .setPhotoUri(photouri)
                  .build()

              CoroutineScope(Dispatchers.IO).launch {
                  try{
                      user!!.updateProfile(profileupdates).await()
                      withContext(Dispatchers.Main){
                          checkLoggedInstate()
                          Toast.makeText(this@MainActivity,"Updated",Toast.LENGTH_LONG).show()
                      }

                  }catch (e:Exception){
                      withContext(Dispatchers.Main){
                          Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                      }
                  }
              }
          }


     }



     private fun reg_user() {
         val email = regEmail.text.toString()
         val password = regPass.text.toString()
         if(email.isNotEmpty() && password.isNotEmpty()){
             CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.createUserWithEmailAndPassword(email,password).await()
                        withContext(Dispatchers.Main){
                            checkLoggedInstate()
                        }

                    }catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                        }
                    }

             }

         }
     }


     private fun login_user() {
         val email = loginEmail.text.toString()
         val pass= loginPass.text.toString()
         if(email.isNotEmpty() && pass.isNotEmpty()){
             CoroutineScope(Dispatchers.IO).launch {
                 try {
                     auth.signInWithEmailAndPassword(email,pass).await()
                     withContext(Dispatchers.Main){
                         checkLoggedInstate()
                     }

                 }catch (e:Exception){
                     withContext(Dispatchers.Main){
                         Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                     }
                 }

             }

         }
     }


     private fun checkLoggedInstate(){
         val user = auth.currentUser
         if (user == null){

             tvlogintxt.text = "Please login in"
         }else{
             tvlogintxt.text = "Logged in "
             uname.setText(user.displayName)
             profileImg.setImageURI(user.photoUrl)
         }
     }



}