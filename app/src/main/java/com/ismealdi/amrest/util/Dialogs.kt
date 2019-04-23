package com.ismealdi.amrest.util

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.ismealdi.amrest.R
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * Created by Al
 * on 22/04/19 | 13:13
 */
class Dialogs {

    fun initProgressDialog(context: Context): KProgressHUD {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setDimAmount(0.1f)
            .setCornerRadius(4f)
            .setSize(45,45)
            .setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setAnimationSpeed(2)
    }
    
}