package com.sergeytalyzin.writer.presenters

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.models.DataAboutUser
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.providers.WriteProvider
import com.sergeytalyzin.writer.views.WriteView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

@InjectViewState
class WritePresenter(private val context: Context): MvpPresenter<WriteView>() {

    private val writeProvider = WriteProvider(writePresenter = this@WritePresenter)
    private val posterRequest = 1
    private var poster: Bitmap? = null
    private var posterChange = false
    private var dataLoaded = false
    private var draftsOrPosts = ""
    private lateinit var mDraft: Draft
    var draftId = ""
    private var posterOldPath = ""

    fun createIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        viewState.startActivityForGerPoster(intent = intent, title = "Select Picture", requestCode = posterRequest)
    }

    fun workWithPoster(requestCode: Int, resultCode: Int, data: Intent?, contentResolver: ContentResolver) {

        if(requestCode == posterRequest && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            var img = contentResolver.openInputStream(data.data!!)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inPreferredConfig = Bitmap.Config.RGB_565
            BitmapFactory.decodeStream(img, null, options)

            options.inSampleSize = calculateInSampleSize(options)
            options.inJustDecodeBounds = false
            img = contentResolver.openInputStream(data.data!!)
            poster = BitmapFactory.decodeStream(img, null, options)
            posterChange = true
            viewState.showPoster(poster!!) // нужна проверка
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {

        var reqWidth = 400
        var reqHeight = 400

        if(options.outWidth > options.outHeight) {
            reqWidth = 500
            reqHeight = 300
        }
        else if(options.outWidth < options.outHeight) {
            reqWidth = 300
            reqHeight = 500
        }

        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while ( (halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun loadData() {

        if(!dataLoaded) {

            viewState.showLoadingGetDraft()
            writeProvider.getDraft(authorId = DataAboutUser.getId(), workId = draftId, data = { draft ->

                posterOldPath = draft.posterDownloadUrl!!
                viewState.setDataInFragment(draft = draft)
                viewState.showEndLoadingGetDraft()

                if(draft.posterDownloadUrl != "") {
                    viewState.showLoadingPhoto()
                    writeProvider.loadPoster(
                        context = context, pathPoster = draft.posterDownloadUrl!!, poster = { poster ->

                            this.poster = poster
                            viewState.showEndLoadingPhoto()
                            viewState.showPoster(poster = this.poster!!)
                            dataLoaded = true
                        })
                }
                else {
                    dataLoaded = true
                }
            }, error = {

            })
        }
    }

    fun deletePoster() {
        poster = null
        posterChange = true
        viewState.deletePoster()
    }

    private fun createDataForAddPoster() : ByteArray {

        val outputStream = ByteArrayOutputStream()
        poster!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return  outputStream.toByteArray()
    }

    private fun preparationWork(publish: Boolean, pathPoster: String) {

        viewState.showLoadingAdd()

        GlobalScope.launch(Dispatchers.Main) {

            if(poster != null && !publish) {

                mDraft.posterDownloadUrl = posterOldPath

                if(posterOldPath == "") {

                    write(pathPoster = pathPoster, addPoster = true)
                }
                else if(posterOldPath != "" && posterChange) {

                    write(pathPoster = pathPoster, addPoster = true)
                }
                else if(posterOldPath != "" && !posterChange)
                    write(pathPoster = pathPoster, addPoster = false)
            }
            else if(poster != null && publish) {
                write(pathPoster = pathPoster, addPoster = true)
            }
            else if(poster == null && !publish) {

                write(pathPoster = pathPoster, addPoster = false)
            }
            else if(poster == null && publish) {
                write(pathPoster = pathPoster, addPoster = false)
            }
        }
    }

    private fun write(pathPoster: String, addPoster: Boolean) {

        if(addPoster) {
            writeProvider.addPoster(poster = createDataForAddPoster(), path = pathPoster, getUrl = {

                mDraft.posterDownloadUrl = it

                writeProvider.writeWork(userId = DataAboutUser.getId(), draft = mDraft,
                    draftId = draftId, draftsOrPosts = draftsOrPosts)
            })
        }
        else {
            writeProvider.writeWork(userId = DataAboutUser.getId(), draft = mDraft,
                draftId = draftId, draftsOrPosts = draftsOrPosts)
        }
    }

    fun addToDraft(draft: Draft) {

        draftsOrPosts = "drafts"
        mDraft = draft

        if(mDraft.titleWork!!.isNotEmpty()) {

            preparationWork(publish = false, pathPoster = "drafts/images/" + "Id${DataAboutUser.getId()}" + "/" + UUID.randomUUID().toString())
        }
        else {
            viewState.showEndLoadingAddDraft()
            viewState.showError(R.string.error_notDataInTitleWork)
        }
    }

    fun publish(titleWork: String, descriptionWork: String, textWork: String) {

        draftsOrPosts = "posts"
        mDraft = Draft(titleWork = titleWork, descriptionWork = descriptionWork, textWork = textWork)

        preparationWork(publish = true, pathPoster = "posts/images/" + "Id${DataAboutUser.getId()}" + "/" + UUID.randomUUID().toString())
    }

    fun endAdd() {
        viewState.showEndLoadingAddDraft()
        if(draftsOrPosts == "drafts")
            viewState.startListDraftsFragment()
        else if(draftsOrPosts == "posts")
            viewState.startProfileFragment()
    }

    fun error(errorStr: Int) {
        viewState.showError(errorStr = errorStr)
    }
}