package com.example.appfetin.repository

import android.content.Context
import android.net.Uri
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.appfetin.model.LoginRequest
import com.example.appfetin.model.Occurrence
import com.example.appfetin.network.OccurrenceApiService
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OccurrenceRepository(private val context: Context) {

    private val occurrenceApiService: OccurrenceApiService by lazy {
        createOccurrenceApiService()
    }

    private fun createOccurrenceApiService(): OccurrenceApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Configuração do CookieJar para persistir os cookies da sessão
        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar) // Adiciona o gerenciador de cookies
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rhk-io.online/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OccurrenceApiService::class.java)
    }

    // --- Autenticação (agora baseada em sessão de cookie) ---
    private var isSessionActive = false

    suspend fun login(): Result<Unit> = withContext(Dispatchers.IO) {
        // Se a sessão já estiver ativa (em memória), não faz o login de novo
        if (isSessionActive) {
            return@withContext Result.success(Unit)
        }
        try {
            // Insira aqui o e-mail e senha para login automático
            val loginRequest = LoginRequest("admin@admin.com", "admin123")
            val response = occurrenceApiService.login(loginRequest)

            if (response.isSuccessful) {
                isSessionActive = true
                Result.success(Unit)
            } else {
                isSessionActive = false
                Result.failure(Exception("Falha no login: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            isSessionActive = false
            Result.failure(e)
        }
    }

    // --- Upload para o S3 (permanece o mesmo) ---
    suspend fun uploadImageToS3(imageUri: Uri): Result<String> {
        // ... (código de upload para o S3 não muda)
        return suspendCoroutine { continuation ->
            try {
                // **IMPORTANTE**: Preencha com suas credenciais da AWS
                val awsAccessKey = ""
                val awsSecretKey = ""
                val bucketName = "mili-fetin"
                val region = Regions.US_EAST_1 // Altere para a região do seu bucket

                val credentials = BasicAWSCredentials(awsAccessKey, awsSecretKey)
                val s3Client = AmazonS3Client(credentials, Region.getRegion(region))

                TransferNetworkLossHandler.getInstance(context)

                val transferUtility = TransferUtility.builder()
                    .context(context)
                    .defaultBucket(bucketName)
                    .s3Client(s3Client)
                    .build()

                val key = "app/${UUID.randomUUID()}.jpg"
                val file = File(getRealPathFromURI(imageUri))

                val uploadObserver = transferUtility.upload(key, file)

                uploadObserver.setTransferListener(object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState) {
                        if (state == TransferState.COMPLETED) {
                            val imageUrl = s3Client.getResourceUrl(bucketName, key)
                            continuation.resume(Result.success(imageUrl))
                        } else if (state == TransferState.FAILED) {
                            continuation.resume(Result.failure(Exception("Falha no upload da imagem")))
                        }
                    }
                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
                    override fun onError(id: Int, ex: Exception) {
                        continuation.resume(Result.failure(ex))
                    }
                })
            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }
    }

    // --- Envio da Ocorrência ---
    suspend fun createOccurrence(occurrence: Occurrence): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Garante que a sessão está ativa antes de enviar a ocorrência
            val loginResult = login()
            if (loginResult.isFailure) {
                // Tenta o login novamente em caso de falha da sessão
                val retryLoginResult = login()
                if(retryLoginResult.isFailure){
                    return@withContext Result.failure(retryLoginResult.exceptionOrNull()!!)
                }
            }

            // O cookie será enviado automaticamente pelo OkHttp
            val response = occurrenceApiService.createOccurrence(occurrence)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                // Se a sessão expirou (ex: erro 401), invalida a sessão para forçar novo login na próxima vez
                if (response.code() == 401 || response.code() == 403) {
                    isSessionActive = false
                }
                Result.failure(Exception("Falha ao criar ocorrência: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Função auxiliar para obter o caminho real do arquivo a partir da Uri (permanece a mesma)
    private fun getRealPathFromURI(uri: Uri): String? {
        // ... (código não muda)
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex("_data")
                return cursor.getString(index)
            }
        }
        return uri.path
    }
}