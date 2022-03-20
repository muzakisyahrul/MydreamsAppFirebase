package mobile.muzaki.mydreamsappfirebase

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import mobile.muzaki.mydreamsappfirebase.fragment.HomeFragment
import mobile.muzaki.mydreamsappfirebase.fragment.KeinginanFragment

class MainActivity : AppCompatActivity() {
    private lateinit var helper: Helper
    val homeFragment = HomeFragment()
    val keinginanFragment = KeinginanFragment()
    var bottomNavSelected:Int=R.id.nav_home
    val manager: FragmentManager = supportFragmentManager
    var isHalaman:Boolean=false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helper = Helper.newInstance(this,this)
        replaceFragment(homeFragment)
        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->replaceFragment(homeFragment)
                R.id.nav_dream->replaceFragment(keinginanFragment)
            }
            true
        }
    }

    fun setBottomNavigationSelected(id:Int){
        bottomNavSelected = id
        bottom_navigation.selectedItemId = bottomNavSelected
    }

    fun backHalaman(fragment: Fragment?){
        if(fragment != null){
            val backStateName: String = fragment::class.java.name
            helper.tampilToast(backStateName)
            manager.popBackStack(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    override fun onBackPressed() {
        if(!isHalaman){
            val fragment =
                this.supportFragmentManager.findFragmentById(R.id.mainFrame)
            (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                if(!it){
                    super.onBackPressed()
                }else{
                    bottomNavSelected=R.id.nav_home
                }
            }
        }else{
            bottom_navigation.isVisible=true
            manager.popBackStackImmediate()
            isHalaman = false
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.replace(R.id.mainFrame, fragment)
        ft.commit()
    }

    fun setHalaman(fragment: Fragment){
        isHalaman=true
        bottom_navigation.isVisible=false
        val backStateName: String = fragment::class.java.name
        val fragmentPopped: Boolean = manager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.replace(R.id.mainFrame, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}