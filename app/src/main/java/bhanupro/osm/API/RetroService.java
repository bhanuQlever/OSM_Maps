package bhanupro.osm.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetroService {

    @POST
    Call<ResponseBody> getUserProfileImage(@Url String url);
}
