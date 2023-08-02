package Database.ApiService

import Models.Category
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryService {
    @GET("api/category")
    fun getItems(): Call<List<Category>>

    @GET("api/category/{id}")
    fun getItemById(@Path("id") id: String): Call<Category>

    @POST("api/category")
    fun createItem(@Body item: Category): Call<Category>

    @PATCH("api/category/{id}")
    fun updateItem(@Path("id") id: String, @Body item: Category): Call<Category>

    @DELETE("api/category/{id}")
    fun deleteItem(@Path("id") id: String): Call<Category>
}