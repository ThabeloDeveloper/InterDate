package com.mecaroid.interdate.Kotlin

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mecaroid.interdate.Authentication.SignIn
import com.mecaroid.interdate.GetInformation
import com.mecaroid.interdate.MainActivity
import com.mecaroid.interdate.databinding.ActivitySplashScreenKotlinBinding
import java.util.Locale

class SplashScreenKotlin : AppCompatActivity() {


    private var user : FirebaseUser? = Firebase.auth.currentUser
    private lateinit var handler : Handler
    private lateinit var binding: ActivitySplashScreenKotlinBinding
    private var isGetInformationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(mainLooper)

        val intent = Intent(this,SignIn::class.java)
        val intentMain = Intent(this,MainActivity::class.java)
        val intentGetInfo = Intent(this,GetInformation::class.java)
        handler.postDelayed({
            if(user == null){
                startActivity(intent)
                finish()

            }else{
                val reference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            startActivity(intentMain)
                            finish()
                        }else{
                            if (!isGetInformationStarted){
                                isGetInformationStarted = true
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                startActivity(intentGetInfo)
                                finish()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        },5000)

    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("AppTheme", MODE_PRIVATE)
        val currentTheme = sharedPreferences.getString("currentTheme","null")

        when(currentTheme) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "null" ->AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        }
        switchLang()

    }
    private fun switchLang(){

        if (Locale.getDefault().language == "af") {
            changeLanguages("af")
        } else if (Locale.getDefault().language == "cs") {
            changeLanguages("cs")
        } else if (Locale.getDefault().language == "de") {
            changeLanguages("de")
        } else if (Locale.getDefault().language == "en") {
            changeLanguages("en")
        } else if (Locale.getDefault().language == "es") {
            changeLanguages("es")
        } else if (Locale.getDefault().language == "fr") {
            changeLanguages("fr")
        } else if (Locale.getDefault().language == "it") {
            changeLanguages("it")
        } else if (Locale.getDefault().language == "ja") {
            changeLanguages("ja")
        } else if (Locale.getDefault().language == "ko") {
            changeLanguages("ko")
        } else if (Locale.getDefault().language == "pl") {
            changeLanguages("pl")
        } else if (Locale.getDefault().language == "pt") {
            changeLanguages("pt")
        } else if (Locale.getDefault().language == "ru") {
            changeLanguages("ru")
        } else if (Locale.getDefault().language == "tr") {
            changeLanguages("tr")
        }

    }
    private fun changeLanguages(code: String) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}