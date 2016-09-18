package be.tomberndjesse.picaloc.Retrofit;

/**
 * Created by jesse on 17/09/2016.
 */
public class LikePost {
    private long post_id;
    public LikePost(Post post){
        this.post_id = post.getId();
    }
}
