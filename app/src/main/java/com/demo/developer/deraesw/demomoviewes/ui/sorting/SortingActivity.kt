package com.demo.developer.deraesw.demomoviewes.ui.sorting

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.showShortToast

class SortingActivity : AppCompatActivity(), SortingFragment.SortingFragmentInterface {

    private val TAG = SortingActivity::class.java.simpleName

    companion object {
        const val KEY_SORT_CATEGORY = "KEY_SORT_CATEGORY"
        const val KEY_CODE_SELECTED = "KEY_CODE_SELECTED"
        const val KEY_NEW_CODE_SELECTED = "KEY_NEW_CODE_SELECTED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting_movie)

        val category = intent?.getStringExtra(KEY_SORT_CATEGORY) ?: ""
        val code = intent?.getStringExtra(KEY_CODE_SELECTED) ?: ""

        if(category.isEmpty()){
            this.showShortToast("Unknown Category")
            finish()
        } else {
            val mainContainer : FrameLayout = findViewById(R.id.main_container)
            mainContainer.setOnClickListener({
                finish()
            })

            if(savedInstanceState == null){
                val fragment = SortingFragment()
                fragment.arguments = SortingFragment.setup(category, code)
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.main_container_sorting_movie, fragment)
                        .commit()
            }
        }


    }

    override fun selectSortingOption(code: String) {
        intent = Intent()
        intent.putExtra(KEY_NEW_CODE_SELECTED, code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
