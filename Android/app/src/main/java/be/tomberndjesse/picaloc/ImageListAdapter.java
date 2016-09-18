package be.tomberndjesse.picaloc;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.tomberndjesse.picaloc.Retrofit.Empty;
import be.tomberndjesse.picaloc.Retrofit.LikePost;
import be.tomberndjesse.picaloc.Retrofit.Post;
import be.tomberndjesse.picaloc.Retrofit.PostClient;
import be.tomberndjesse.picaloc.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jesse on 17/09/2016.
 */
public class ImageListAdapter extends ArrayAdapter<Post> {
    private Context context;
    private LayoutInflater inflater;
    boolean likeSending = false;

    private List<Post> posts;

    public ImageListAdapter(Context context, List<Post> posts) {
        super(context, R.layout.listview_item_image, posts);

        this.context = context;
        this.posts = posts;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final Holder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_item_image, parent, false);

            holder = new Holder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.like = (ImageView) row.findViewById(R.id.like);
            holder.likes = (TextView) row.findViewById(R.id.amountLikes);


            row.setTag(holder);
        }else{
            holder = (Holder) row.getTag();
        }

        final Post current = posts.get(position);

        Picasso
                .with(context)
                .load(current.getUrl())
                .fit()
                .centerInside()
                .into(holder.image);
        holder.title.setText(current.getTitle());
        holder.likes.setText(String.valueOf(current.getLikes()));
        holder.like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!likeSending) {
                    likeSending = true;
                    ServiceGenerator.createService(PostClient.class, getContext()).like(new LikePost(current)).enqueue(new Callback<Empty>() {
                        @Override
                        public void onResponse(Call<Empty> call, Response<Empty> response) {
                            holder.like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_favorite_selected_24dp));
                            likeSending = false;
                            current.incrementLikes();
                            posts.set(position, current);
                            holder.likes.setText(String.valueOf(current.getLikes()));
                        }

                        @Override
                        public void onFailure(Call<Empty> call, Throwable t) {

                        }
                    });
                }
            }
        });


        return row;
    }

    static class Holder {
        ImageView image;
        TextView title;
        ImageView like;
        TextView likes;
    }
}
