package com.sergeytalyzin.writer.views

import android.content.Intent
import android.graphics.Bitmap
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sergeytalyzin.writer.models.Draft


interface WriteView: MvpView {
    fun showPoster(poster: Bitmap) // Добаление постера в активити
    fun showError(errorStr: Int) // Показывает ошибку
    fun showLoadingAdd() // Показать загрузку
    fun showEndLoadingAddDraft() //
    fun showLoadingGetDraft() //
    fun showEndLoadingGetDraft() //
    fun showLoadingPhoto() //
    fun showEndLoadingPhoto() //
    fun startListDraftsFragment()
    fun startProfileFragment()
    fun setDataInFragment(draft: Draft)
    fun deletePoster()
    @StateStrategyType(value = SkipStrategy::class)
    fun startActivityForGerPoster(intent: Intent, title: String, requestCode: Int) // Запуск активити для получение постера
}