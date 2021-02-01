package com.example.screenbindger.db.remote.service.storage

import android.net.Uri
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import java.net.URI

interface StorageService {

    suspend fun downloadImage(userStateObservable: UserStateObservable)
    suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable)
    suspend fun deleteImage()
}