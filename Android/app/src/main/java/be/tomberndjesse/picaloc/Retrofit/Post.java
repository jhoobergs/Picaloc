package be.tomberndjesse.picaloc.Retrofit;

/**
 * Created by jesse on 17/09/2016.
 */
public class Post {
    private long id;
    private String title;
    private String image_url_id;
    private PostLocation location;
    private long likes;

    public Post(String title, String image_url_id, PostLocation location){
        this.title = title;
        this.image_url_id = image_url_id;
        this.location = location;
    }
}
