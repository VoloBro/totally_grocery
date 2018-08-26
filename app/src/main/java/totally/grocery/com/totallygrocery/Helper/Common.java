package totally.grocery.com.totallygrocery.Helper;

import totally.grocery.com.totallygrocery.Remote.IMenuRequest;
import totally.grocery.com.totallygrocery.Remote.RetrofitClient;

/**
 * Created by Volodymyr P. on 8/26/18.
 */
public class Common {
    public static IMenuRequest getMenuRequest()
    {
        return RetrofitClient.getClient("https://api.androidhive.info/").create(IMenuRequest.class);
    }
}
