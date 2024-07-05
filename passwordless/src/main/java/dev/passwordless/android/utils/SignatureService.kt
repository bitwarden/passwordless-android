package dev.passwordless.android.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import dev.passwordless.android.rest.converters.Base64UrlConverter
import java.security.MessageDigest

class SignatureService {
    private val context: Context
    private var facetId: String? = null

    constructor(context: Context) {
        this.context = context;
    }

    fun getFacetId(): String {
        if (facetId != null) {
            return facetId!!
        }
        val packageManager = context.packageManager
        val packageName = context.packageName
        try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES)

            val signatures: Array<Signature> = packageInfo.signingInfo.apkContentsSigners

            val signature: ByteArray = signatures[0].toByteArray()
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            val hash: ByteArray = md.digest(signature)

            val shortFacetId = Base64UrlConverter.convert(hash)
            this.facetId = "android:apk-key-hash:$shortFacetId"
            return facetId!!
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}