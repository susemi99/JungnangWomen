package kr.susemi99.jungnangwomen.network

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.susemi99.jungnangwomen.BuildConfig
import kr.susemi99.jungnangwomen.R
import kr.susemi99.jungnangwomen.application.App
import kr.susemi99.jungnangwomen.model.WomenResourcesClassParentItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object WomenService {
  private val HOST = "http://openapi.seoul.go.kr:8088/${App.instance.getString(R.string.my_key)}/json/SeoulJungNangWomenResourcesClass/"

  private fun retrofit(className: Class<*>): Any {
    val builder = OkHttpClient.Builder()

    // 호출한 정보와 결과 로그를 보고 싶으면 아래 주석 해제해야함
    if (BuildConfig.DEBUG) {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.BODY
      builder.addInterceptor(logging)
    }

    return Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(HOST)
      .client(builder.build())
      .build()
      .create(className)
  }

  private fun api(): Api {
    return retrofit(Api::class.java) as Api
  }

  /**
   * 목록 불러오기
   */
  fun list(startIndex: Int, endIndex: Int): Single<WomenResourcesClassParentItem> {
    return api().list(startIndex, endIndex)
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
  }

  //////////////////////////////
  // api
  //////////////////////////////
  private interface Api {
    @GET("{start_index}/{end_index}")
    fun list(@Path("start_index") startIndex: Int, @Path("end_index") endIndex: Int): Single<WomenResourcesClassParentItem>
  }
}