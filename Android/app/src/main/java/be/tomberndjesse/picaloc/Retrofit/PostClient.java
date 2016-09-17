package be.tomberndjesse.picaloc.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jesse on 17/09/2016.
 */
public interface PostClient {
    @POST("/posts/get")
    Call<List<Post>> getPosts(@Body PostLocation location);

    @POST("/posts/add")
    Call<Empty> addPost(@Body Post post);

    @POST("/user/location")
    Call<Empty> setLocation(@Body PostLocation location);
}
