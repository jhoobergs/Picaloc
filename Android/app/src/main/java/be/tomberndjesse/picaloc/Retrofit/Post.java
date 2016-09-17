package be.tomberndjesse.picaloc.Retrofit;

/**
 * Created by jesse on 17/09/2016.
 */
public class Post {
    private long id;
    private String title;
    private String image_url_id;
    private double latitude;
    private double longitude;
    private long likes;

    public Post(String title, String image_url_id, PostLocation location){
        this.title = title;
        this.image_url_id = image_url_id;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url_id() {
        return image_url_id;
    }

    public String getUrl(){
        return "https://firebasestorage.googleapis.com/v0/b/picaloc-2acb6.appspot.com"+getImage_url_id();
    }
}
