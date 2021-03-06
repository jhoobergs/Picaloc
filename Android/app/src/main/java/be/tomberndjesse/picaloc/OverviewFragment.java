package be.tomberndjesse.picaloc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.tomberndjesse.picaloc.Retrofit.Post;
import be.tomberndjesse.picaloc.Retrofit.PostClient;
import be.tomberndjesse.picaloc.Retrofit.PostLocation;
import be.tomberndjesse.picaloc.Retrofit.ServiceGenerator;
import be.tomberndjesse.picaloc.utils.SettingsUtil;
import be.tomberndjesse.picaloc.utils.SharedPreferencesKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jesse on 17/09/2016.
 */
public class OverviewFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        listView = (ListView) v.findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        getData();


        return v;
    }

    public void getData(){
        String data = new SettingsUtil(getActivity()).getString(SharedPreferencesKeys.PostsDataString);
        if(! "".equals(data)){
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
            listView.setAdapter(new ImageListAdapter(getActivity(), Arrays.asList(new Gson().fromJson(data, Post[].class))));
        }

        ServiceGenerator.createService(PostClient.class, getActivity()).getPosts(new PostLocation(((BaseActivity) getActivity()).getLocation())).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        listView.setAdapter(new ImageListAdapter(getActivity(), response.body()));
                        new SettingsUtil(getActivity()).setString(SharedPreferencesKeys.PostsDataString, new Gson().toJson(response.body()));
                    }
                    else{
                        listView.setAdapter(new ImageListAdapter(getActivity(), new ArrayList<Post>()));
                        new SettingsUtil(getActivity()).setString(SharedPreferencesKeys.PostsDataString, "[]");
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
