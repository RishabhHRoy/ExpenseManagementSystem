package Database.ApiService

import Models.Budget
import retrofit2.Call
import retrofit2.http.*

interface BudgetService {
    @GET("api/budget")
    fun getItems(): Call<List<Budget>>

    @GET("api/budget/{id}")
    fun getItemById(@Path("id") id: String): Call<Budget>

    @POST("api/budget")
    fun createItem(@Body item: Budget): Call<Budget>

    @PUT("api/budget/{id}")
    fun updateItem(@Path("id") id: String, @Body item: Budget): Call<Budget>

    @DELETE("api/budget/{id}")
    fun deleteItem(@Path("id") id: String): Call<Budget>
}
