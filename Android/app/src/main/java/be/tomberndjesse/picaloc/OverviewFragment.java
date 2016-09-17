package be.tomberndjesse.picaloc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.tomberndjesse.picaloc.Retrofit.Post;
import be.tomberndjesse.picaloc.Retrofit.PostClient;
import be.tomberndjesse.picaloc.Retrofit.PostLocation;
import be.tomberndjesse.picaloc.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jesse on 17/09/2016.
 */
public class OverviewFragment extends Fragment {
    LinearLayout cardHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        //cardHolder = (LinearLayout) v.findViewById(R.id.card_holder);
        Log.d("test", "doing");
        final ListView listView = (ListView) v.findViewById(R.id.listview);

        ServiceGenerator.createService(PostClient.class, getActivity()).getPosts(new PostLocation(10,10)).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    listView.setAdapter(new ImageListAdapter(getActivity(), response.body()));
                    /*for(Post post : response.body()){
                        CardView card = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_post, cardHolder, false);;
                        ImageView image = (ImageView) card.findViewById(R.id.image);
                        TextView title = (TextView) card.findViewById(R.id.title);
                        title.setText(post.getTitle());
                        String url = "https://firebasestorage.googleapis.com/v0/b/picaloc-2acb6.appspot.com"+post.getImage_url_id();
                        Log.d("test", url);
                        Picasso.with(getActivity()).load(url).resize() .into(image);
                        cardHolder.addView(card);
                        Log.d("test", "Found");
                    }*/
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

        return v;
    }
}
