package com.lloyd.serviceworkerexample.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lloyd.serviceworkerexample.R
import com.lloyd.serviceworkerexample.interfaces.Task
import com.lloyd.serviceworkerexample.utils.first_image_url
import com.lloyd.serviceworkerexample.utils.second_image_url
import com.lloyd.serviceworkerexample.worker.ServiceWorker
import kotlinx.android.synthetic.main.activity_image_details.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class ImageDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private val firstServiceWorker: ServiceWorker = ServiceWorker()
    private val secondServiceWorker: ServiceWorker = ServiceWorker()
    private val okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        btn_get_first_image.setOnClickListener(this)
        btn_get_second_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_first_image -> {
                firstServiceWorker.addTask(object : Task<Bitmap> {
                    override fun onExecuteTask(): Bitmap {
                        val request: Request = Request.Builder().url(first_image_url).build()
                        val response: Response = okHttpClient.newCall(request).execute()
                        return BitmapFactory.decodeStream(response.body?.byteStream())
                    }

                    override fun onTaskCompleted(result: Bitmap) {
                        iv_first_image.setImageBitmap(result)
                    }
                })
            }

            R.id.btn_get_second_image -> {
                secondServiceWorker.addTask(object : Task<Bitmap> {
                    override fun onExecuteTask(): Bitmap {
                        val request: Request = Request.Builder().url(second_image_url).build()
                        val response: Response = okHttpClient.newCall(request).execute()
                        return BitmapFactory.decodeStream(response.body?.byteStream())
                    }

                    override fun onTaskCompleted(result: Bitmap) {
                        iv_second_image.setImageBitmap(result)
                    }
                })
            }


        }
    }
}
