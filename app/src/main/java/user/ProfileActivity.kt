package user

import `interface`.StoreState
import actions.Actions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidappfinalproject.MainActivity
import com.example.androidappfinalproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.bottom_nav_bar_signedin.*
import models.Store
import state.AddStore
import state.StoreList
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity() {

    private val predefinedStores: MutableList<Store> = mutableListOf()

//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        showStoreList()
        bottomNavViewBarSignedIn.onNavigationItemSelectedListener =
            nOnNavigationItemSelectedListener
    }

//--------------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

//--------------------------------------------------------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOutProfileButton -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


//--------------------------------------------------------------------------------------------------
    var currentState by Delegates.observable<StoreState>(
        StoreList(
            emptyList()
        ), { _, old, new ->
            renderViewState(new, old)
        })

//--------------------------------------------------------------------------------------------------
    private fun renderViewState(newState: StoreState, oldState: StoreState) {
        when (newState) {
            is StoreList -> showStoreList()
            is AddStore -> showAddStoreView()
        }
        when (oldState) {
            is StoreList -> hideStoreList()
            is AddStore -> hideAddStoreView()
        }
    }

//--------------------------------------------------------------------------------------------------
    private fun showStoreList() {
        storeList.visibility = View.VISIBLE

        add_store_button.setOnClickListener {
            currentState = currentState.consumeAction(Actions.AddStoreClicked())
        }
    }

//--------------------------------------------------------------------------------------------------
    private fun showAddStoreView() {
        add_store_container.visibility = View.VISIBLE
        textViewUpdate.visibility = View.VISIBLE
        textViewWelcome.visibility = View.GONE
        textViewUpdateText.visibility = View.VISIBLE
        textViewWelcomeText.visibility = View.GONE
    }

//--------------------------------------------------------------------------------------------------
    private fun hideAddStoreView() {
        add_store_container.visibility = View.GONE
        textViewWelcome.visibility = View.VISIBLE
        textViewUpdate.visibility = View.GONE
        textViewUpdateText.visibility = View.GONE
        textViewWelcomeText.visibility = View.VISIBLE
    }

//--------------------------------------------------------------------------------------------------
    private fun hideStoreList() {
        storeList.visibility = View.GONE
        hide_store_button.setOnClickListener {
            val selectedStore = predefinedStores
            currentState =
                currentState.consumeAction(Actions.PredefinedStoreSelected(selectedStore))
        }
    }

//--------------------------------------------------------------------------------------------------
    private val nOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { i ->
            when (i.itemId) {
                R.id.navigation_user_search -> {
                    val intent2 = Intent(this, ProfileSearchActivity::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_user_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }

            }

            false
        }
}