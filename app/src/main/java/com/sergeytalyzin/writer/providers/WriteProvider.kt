package com.sergeytalyzin.writer.providers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.presenters.WritePresenter

class WriteProvider(private val writePresenter: WritePresenter) {

    fun getDraft(workId: String, data: (work: Draft) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("drafts").child(workId)

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                data(dataSnapshot.getValue(Draft::class.java)!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }

    fun loadPoster(context: Context, pathPoster: String, poster: (poster: Bitmap) -> Unit) {

        Glide.with(context)
            .asBitmap()
            .load(pathPoster)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    poster(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    fun addPoster(poster: ByteArray, path: String, getUrl: (url: String) -> Unit) {

        val mStorageRef = FirebaseStorage.getInstance().reference.child(path)

        mStorageRef.putBytes(poster)
            .addOnSuccessListener{

                getUrlOfPoster(mStorageRef, getUrl)
            }
            .addOnFailureListener{
                writePresenter.error(com.sergeytalyzin.writer.R.string.error_saveFailInFB)
            }
    }

    private fun getUrlOfPoster(mStorageRef: StorageReference, getUrl: (url: String) -> Unit) {

        mStorageRef.downloadUrl.addOnSuccessListener {

            getUrl(it.toString())

        }.addOnFailureListener {
            writePresenter.error(com.sergeytalyzin.writer.R.string.error_saveFailInFB)
        }
    }

    private fun getCountPosts(count: (count: Long) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts")

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                count(dataSnapshot.childrenCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun writeWork(draft: Draft, draftId: String = "", draftsOrPosts: String) {

        val database = FirebaseDatabase.getInstance().reference

        if(draftId == "" || draftsOrPosts == "posts") {

            getCountPosts {

                val name = "${it + 1}"
                database.child(draftsOrPosts).child(name).setValue(draft)
                database.child(draftsOrPosts).child(name).child("date").setValue(ServerValue.TIMESTAMP)
                writePresenter.endAdd()
            }
        }
        else if(draftId != "") {
            database.child(draftsOrPosts).child(draftId).setValue(draft)
            database.child(draftsOrPosts).child(draftId).child("date").setValue(ServerValue.TIMESTAMP)
            writePresenter.endAdd()
        }
    }
}