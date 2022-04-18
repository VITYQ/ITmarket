package com.andrewmdr.itmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andrewmdr.itmarket.data.local.PreferencesProvider
import com.andrewmdr.itmarket.data.remote.LoginRepository
import com.andrewmdr.itmarket.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch





class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    val navHostFragment : NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment }
    val navController : NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val token = PreferencesProvider(this).getToken()
        if (!token.isNullOrEmpty()){
            Log.d("checklogin", "there's token $token")
            navController.navigate(R.id.routesFragment)
        }


        binding.nvMain.setupWithNavController(navController)
        setContentView(view)

        binding.mbLogout.setOnClickListener {
            navController.navigate(R.id.action_routesFragment_to_loginFragment)
            binding.dlMain.closeDrawer(GravityCompat.START)
            CoroutineScope(Dispatchers.IO).launch {
                //LoginRepository(this@MainActivity).logOut()
            }
        }


        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.add(R.id.routesFragment)


        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(binding.dlMain)
            .build()
//        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.dlMain)
//        binding.mtbMain.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.nvMain.setNavigationItemSelectedListener {
            val currentDestination = this.resources.getResourceEntryName(navController.currentDestination!!.id)
//            if(currentDestination != )
            when(it.itemId){
                R.id.routesFragment -> {
                    binding.dlMain.closeDrawer(GravityCompat.START)
                    if(currentDestination != "ordersFragment")
                        navController.navigate(R.id.routesFragment)
                    navController.clearBackStack(R.id.routesFragment)
                    false
                }
                R.id.ordersListFragment -> {
                    binding.dlMain.closeDrawer(GravityCompat.START)
                    if(currentDestination != "ordersListFragment")
                        navController.navigate(R.id.ordersListFragment)
                    navController.clearBackStack(R.id.ordersListFragment)
                    false
                }
                else -> false
            }
        }
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("CheckDestination", this.resources.getResourceEntryName(destination.id))
            val screenName = this.resources.getResourceEntryName(destination.id)
            Log.d("CheckDestination", screenName)
//            controller.clearBackStack()
            controller.backQueue.forEach {
                Log.d("CheckDestinations", it.id)
            }
            when(screenName) {
                "loginFragment" -> {
//                    binding.ablMain.visibility = View.GONE
                    supportActionBar?.hide()
                }
                "routesFragment" -> {
//                    binding.ablMain.visibility = View.VISIBLE
                    supportActionBar?.show()
                    supportActionBar?.subtitle = null
                    supportActionBar?.title = "Доступные маршруты"


                }
                "ordersListFragment" -> {
//                    binding.ablMain.visibility = View.VISIBLE
                    supportActionBar?.show()

                    supportActionBar?.title = "Список заказов"
                    supportActionBar?.subtitle = null

                }
                "orderFragment" -> {
//                    binding.ablMain.visibility = View.VISIBLE
                    supportActionBar?.show()
                    supportActionBar?.title = "Заказ"
                    supportActionBar?.subtitle = "№4739456734"
                }
                "createOrderFragment" -> {
//                    binding.ablMain.visibility = View.VISIBLE
                    supportActionBar?.show()
                    supportActionBar?.title = "Создать заказ"
                }
                "createRouteFragment" -> {
//                    binding.ablMain.visibility = View.VISIBLE
                    supportActionBar?.show()
                    supportActionBar?.title = "Создать маршрут"
                }
            }


//            when (screenName){
//
//            }
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {
        Log.d("checkDestinations", this.resources.getResourceEntryName(navController.currentDestination!!.id))
        if(this.resources.getResourceEntryName(navController.currentDestination!!.id) == "ordersListFragment"){
            navController.navigate(R.id.action_ordersListFragment_to_ordersFragment)
            Log.d("checkDestinations", "work")
        }
        else {
            findNavController(R.id.fcv_main).popBackStack()
        }
        Log.d("checkDestinations", this.resources.getResourceEntryName(navController.currentDestination!!.id))

//        if (!nav)
    }

}