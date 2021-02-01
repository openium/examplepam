package fr.openium.examplepam.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.openium.examplepam.R
import fr.openium.examplepam.database.AppDatabase
import fr.openium.examplepam.rest.ApiHelper
import fr.openium.examplepam.ui.contactlist.ContactListFragment
import fr.openium.examplepam.ui.history.HistoryListFragment
import fr.openium.examplepam.ui.news.NewsFragment
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //We initialize database
        AppDatabase.getInstance(applicationContext)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.menu_contact_list
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (menuItem.itemId) {
            R.id.menu_history -> {
                fragment = HistoryListFragment()
            }
            R.id.menu_contact_list -> {
                fragment = ContactListFragment()
            }
            R.id.menu_news -> {
                fragment = NewsFragment()
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commitAllowingStateLoss()
        }
        return true
    }
}