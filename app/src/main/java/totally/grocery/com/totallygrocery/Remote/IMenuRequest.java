package totally.grocery.com.totallygrocery.Remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import totally.grocery.com.totallygrocery.Model.Item;

/**
 * Created by Volodymyr P. on 8/26/18.
 */
public interface IMenuRequest {
    @GET
    Call<List<Item>> getMennuList(@Url String url);

 }
