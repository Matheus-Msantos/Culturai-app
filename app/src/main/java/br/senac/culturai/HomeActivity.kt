package br.senac.culturai


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment

import br.senac.culturai.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawerLayout
        var frag: Fragment

        setSupportActionBar(findViewById(R.id.myToolBar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        frag = EventListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, frag)
            .commit()

        binding.navgationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            when(it.itemId) {
                R.id.home -> {
                    frag = EventListFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }

                R.id.ingressos -> {
                    frag = OrderFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }

                R.id.minhaConta -> {
                    frag = AccountFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, frag)
                        .commit()
                }
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.carrinho -> startActivity(Intent(this, CartActivity::class.java))
        }
        return toggle.onOptionsItemSelected(item)
    }
}