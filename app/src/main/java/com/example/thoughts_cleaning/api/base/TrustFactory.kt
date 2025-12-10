package com.example.thoughts_cleaning.api.base

import android.content.Context
import com.example.thoughts_cleaning.R
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.apply
import kotlin.io.use

/**
 * Created by humphrey on 2024/05/16.
* android 6.0 이하 버전 letsencrypt ssl 문제로 적용
 * * https://letsencrypt.org/docs/certificate-compatibility/
 *
 * https://stackoverflow.com/questions/78049111/android-devices-api-25-not-connecting-to-letsencrypt-ssl-servers-anymore
 * https://stackoverflow.com/questions/64844311/certpathvalidatorexception-connecting-to-a-lets-encrypt-host-on-android-m-or-ea
**/
class TrustFactory {
    companion object {
        fun getTrustFactoryManager(context: Context): Pair<SSLSocketFactory, X509TrustManager> {
            val cf = CertificateFactory.getInstance("X.509")

            val isrgRoot1Input = context.resources.openRawResource(R.raw.isrgrootx1)
            val isrgRoot1Certificate: Certificate = isrgRoot1Input.use {
                cf.generateCertificate(it)
            }

            val isrgRoot2Input = context.resources.openRawResource(R.raw.isrgrootx2)
            val isrgRoot2Certificate: Certificate = isrgRoot2Input.use {
                cf.generateCertificate(it)
            }

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType).apply {
                load(null, null)

                //ssl 인증서를 직접 추가하였음. 기한 1. 2030-06-04 까지, 2. 2035-09-04까지
                //https://letsencrypt.org/certificates/
                setCertificateEntry("isrgrootx1", isrgRoot1Certificate)
                setCertificateEntry("isrgrootx2", isrgRoot2Certificate)
            }

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
                init(keyStore)
            }

            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, tmf.trustManagers, null)
            }

            return Pair(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
        }
    }
}