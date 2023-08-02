package Database.ApiService

import Models.Category
import Models.Expense
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseService {
    @GET("api/expense")
    fun getItems(): Call<List<Expense>>

    @GET("api/expense/{id}")
    fun getItemById(@Path("id") id: String): Call<Expense>

    @POST("api/expense")
    fun createItem(@Body item: Expense): Call<Expense>

    @PATCH("api/expense/{id}")
    fun updateItem(@Path("id") id: String, @Body item: Expense): Call<Expense>

    @DELETE("api/expense/{id}")
    fun deleteItem(@Path("id") id: String): Call<Expense>
}