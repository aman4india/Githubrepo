package com.aman.githubrepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aman.githubrepo.ApiServices;
import com.aman.githubrepo.R;
import com.aman.githubrepo.ServiceGenerator;
import com.aman.githubrepo.adapter.RecyclerAdapter;
import com.aman.githubrepo.models.GitResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    public  RecyclerAdapter adapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private ProgressBar progressBar;
    GitResponse repositoriesList;
    CharSequence dateString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date d = new Date();
        dateString = DateFormat.format("yyyy-MM-dd", d.getTime());

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                currentPage+=1;
                                fetchNextPage();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }, 1000);

                    }
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        });
        fetchFirstPage();
    }


    private void prepareData(GitResponse gitResponse) {
        adapter = new RecyclerAdapter(gitResponse.getItems());
        recyclerView.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }

    private void fetchFirstPage() {
        Map<String, String> data = new HashMap<>();
        data.put("q", "created:>2022-06-22");
        data.put("sort", "stars");
        data.put("order", "desc");
        ApiServices apiService = ServiceGenerator.createService(ApiServices.class);
        Log.e(TAG, "fetchFirstPage: "+data );
        Call<GitResponse>  repositoryListCall= apiService.getRepositoryList(data);
        repositoryListCall.enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(Call<GitResponse> call, Response<GitResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, " Sucessful", Toast.LENGTH_SHORT).show();
                    repositoriesList = response.body();
                    prepareData(repositoriesList);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Network Error for loading the data. Try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error for loading the data. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchNextPage() {
        Log.e(TAG, "loadNextPage: " + currentPage);
        Map<String, String> data = new HashMap<>();
        data.put("q", "created:>2022-06-22");
        data.put("sort", "stars");
        data.put("order", "desc");
        data.put("page", String.valueOf(currentPage));
        ApiServices apiService = ServiceGenerator.createService(ApiServices.class);
        Call<GitResponse> repositoryListCall= apiService.getRepositoryList(data);
        repositoryListCall.enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(Call<GitResponse> call, Response<GitResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, " Loading more  ", Toast.LENGTH_SHORT).show();
                    GitResponse repositoriesList2 = response.body();
                    repositoriesList.getItems().addAll(repositoriesList2.getItems());
                    Log.e("new size ",repositoriesList.getItems().size()+"");
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(MainActivity.this, "Network Error for loading the data. Try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error for loading the data. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}