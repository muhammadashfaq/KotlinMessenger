package mykotlin.kotlinchatapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivty : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar as android.support.v7.widget.Toolbar?)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_items,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.menu_search -> {
                Toast.makeText(this,"SEARCH",Toast.LENGTH_LONG).show()
            }
            R.id.menu_faq -> {
                Toast.makeText(this,"FAQ",Toast.LENGTH_LONG).show()
            }
            R.id.menu_history -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@HomeActivty,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            else -> {
                super.onOptionsItemSelected(item)
            }


        }



        return true
    }
}
