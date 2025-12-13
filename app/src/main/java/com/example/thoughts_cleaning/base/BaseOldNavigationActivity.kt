package com.example.thoughts_cleaning.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.thoughts_cleaning.base.BaseOldActivity
import com.example.thoughts_cleaning.util.base.BaseContract
import java.lang.NullPointerException


/**
 * Created by SeoKang on 2021-05-20.
 */
abstract class BaseOldNavigationActivity : BaseOldActivity(),  BaseContract.NavigationMethod  {

    fun getCurrentFragment(navHostId: Int) : Fragment? {
        var currentFragment: Fragment? = null
        return try {
            val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
            val fragments = navHostFragment.childFragmentManager.fragments

            if (fragments.isNotEmpty()){
                currentFragment = fragments[0]
            }

            currentFragment
        }catch (e: NullPointerException) {
            e.printStackTrace()

            currentFragment
        }

    }
}