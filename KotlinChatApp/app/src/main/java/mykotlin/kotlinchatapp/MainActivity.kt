package mykotlin.kotlinchatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import mykotlin.kotlinchatapp.Common.Common

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_signup.setOnClickListener {

            val username = edt_txt_username.text.toString().trim()
            val email = edt_txt_email.text.toString().trim()
            val password = edt_txt_password.text.toString().trim()
            val country = edt_txt_country.text.toString().trim()

            if(Common.isNetworkAvailable(this)){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                        task ->  if(task.isSuccessful){
                    saveDataToFireStore(username,email,password,country)
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Log.i(TAG,"SIGN IN FIALED");
                    Common.showSnack(linear_layout_main,"Sign in failed",R.color.colorRed)
                }
                }
            }else{
                Common.showSnack(linear_layout_main,"NO INTERNET CONNECITON",R.color.colorRed)
            }

        }


        txt_vu_login.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun saveDataToFireStore(
        username: String,
        email: String,
        password: String,
        country: String
    ) {
        val user = HashMap<String,Any>()
        user["Username"] = username
        user["Email"] = email
        user["Password"] = password
        user["Country"] = country

        // Add a new document with a generated ID
        db.collection("User")
            .add(user).addOnCompleteListener(this){
                task -> if(task.isSuccessful){
                Log.w(TAG, "SUCCESS")
            }else{
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Common.showSnack(linear_layout_main,"Somthing Bad Happend.Try Again",R.color.colorRed)
            }
            }
    }
}
