package com.demo.developer.deraesw.demomoviewes.ui.sorting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.extension.showShortToast

class SortingActivity : AppCompatActivity(), SortingFragment.SortingFragmentInterface {

    private val TAG = SortingActivity::class.java.simpleName

    companion object {
        const val EXTRA_SORT_CATEGORY = "com.demo.developer.deraesw.demomoviewes.EXTRA_SORT_CATEGORY"
        const val EXTRA_CODE_SELECTED = "com.demo.developer.deraesw.demomoviewes.EXTRA_CODE_SELECTED"
        const val EXTRA_NEW_CODE_SELECTED = "com.demo.developer.deraesw.demomoviewes.EXTRA_NEW_CODE_SELECTED"

        fun setup(context: Context, category : String, selectedCode : String) : Intent {
            val intent = Intent(context, SortingActivity::class.java)
            intent.putExtra(SortingActivity.EXTRA_SORT_CATEGORY, category)
            intent.putExtra(SortingActivity.EXTRA_CODE_SELECTED, selectedCode)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting_movie)

        val category = intent?.getStringExtra(EXTRA_SORT_CATEGORY) ?: ""
        val code = intent?.getStringExtra(EXTRA_CODE_SELECTED) ?: ""

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
        intent.putExtra(EXTRA_NEW_CODE_SELECTED, code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
