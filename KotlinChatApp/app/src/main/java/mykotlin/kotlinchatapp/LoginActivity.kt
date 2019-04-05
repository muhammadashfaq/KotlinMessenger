package mykotlin.kotlinchatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import mykotlin.kotlinchatapp.Common.Common

class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {

            val email = edt_txt_email.text.toString()
            val password = edt_txt_password.text.toString()


            if(Common.isNetworkAvailable(this)){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    task -> if(task.isSuccessful){
                    Common.showToast(this,"LOGIN SUCCESS")
                    val intent = Intent(this@LoginActivity,HomeActivty::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Common.showSnack(linear_layout_main,"Somthing bad happened.Try Again",R.color.colorRed)
                }
                }

            }else{
                Common.showSnack(linear_layout_main,"NO INTERNET CONNECITON",R.color.colorRed)
            }
        }


        txt_vu_signup.setOnClickListener {
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
        }



    }


    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(this@LoginActivity,HomeActivty::class.java)
            startActivity(intent)
            finish()
        }
    }
}
