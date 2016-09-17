package be.tomberndjesse.picaloc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.tomberndjesse.picaloc.Retrofit.Post;

/**
 * Created by jesse on 17/09/2016.
 */
public class ImageListAdapter extends ArrayAdapter<Post> {
    private Context context;
    private LayoutInflater inflater;

    private List<Post> posts;

    public ImageListAdapter(Context context, List<Post> posts) {
        super(context, R.layout.listview_item_image, posts);

        this.context = context;
        this.posts = posts;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }

        Picasso
                .with(context)
                .load(posts.get(position).getUrl())
                .fit() // will explain later
                .centerInside()
                .into((ImageView) convertView);

        return convertView;
    }
}
