package com.example.stud.musicapp.searchalbum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stud.musicapp.MainActivity;
import com.example.stud.musicapp.R;
import com.example.stud.musicapp.api.ApiService;
import com.example.stud.musicapp.api.SearchAlbum;
import com.example.stud.musicapp.api.SearchAlbums;
import com.example.stud.musicapp.favorites.FavoritesActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAlbumActivity extends AppCompatActivity {
EditText etQuery;
RecyclerView rvList;


SharedPreferences sharedPreferences;
    List<SearchAlbum> albums = new ArrayList<>(0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_album);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        String artist = sharedPreferences.getString("query", null);
        etQuery.setText(artist);

        etQuery = findViewById(R.id.etQuery);
        rvList = findViewById(R.id.rvList);
        SearchAlbumAdapter searchAlbumAdapter = new SearchAlbumAdapter(albums);
        rvList.setAdapter(searchAlbumAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(linearLayoutManager);





        Button bSearch = findViewById(R.id.bSearch);
        bSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            String query = etQuery.getText().toString();
            rememberQuery(query);
            SearchAlbums(query);
            }
        });




    }
    private void rememberQuery(String query) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("query",query);
        editor.apply();


    }



    private void SearchAlbums (String query) {
        getSupportActionBar().setSubtitle(query);
        if (query == null || query.isEmpty()) {
            Toast.makeText(this, "Pusta fraza", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<SearchAlbums> searchAlbumsCall = ApiService.getService().searchAlbums(query);
        searchAlbumsCall.enqueue(new Callback<SearchAlbums>() {
            @Override
            public void onResponse(@NonNull Call<SearchAlbums> call, @NonNull Response<SearchAlbums> response) {
                SearchAlbums searchAlbums = response.body();

                if (searchAlbums == null || searchAlbums.album == null || searchAlbums.album.isEmpty()) {
                    Toast.makeText(SearchAlbumActivity.this, "Brak wyników", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateList(searchAlbums);
            }

            @Override
            public void onFailure(@NonNull Call<SearchAlbums> call, Throwable t) {
                Toast.makeText(SearchAlbumActivity.this, "Błąd pobierania danych: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateList(SearchAlbums searchAlbums) {
        albums.clear();
        albums.addAll(searchAlbums.album);

        rvList.getAdapter().notifyDataSetChanged();
    }











    public boolean onSupportNabigateUp(){
        onBackPressed();
        return true;
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_albums_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSortName:
                sortByName();
                return true;
            case R.id.itemSortYear:
                sortByYear();
                return true;
            case R.id.itemSortSales:
                sortBySales();
                return true;
            case R.id.itemSortScore:
                sortByScore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortByName() {
        Collections.sort(albums, new Comparator<SearchAlbum>() {
            @Override
            public int compare(SearchAlbum album1, SearchAlbum album2) {
                if (album1.strAlbum.equals(album2.strAlbum)) {
                    return 0;
                }

                return album1.strAlbum.compareTo(album2.strAlbum);
            }
        });

        rvList.getAdapter().notifyDataSetChanged();
    }


    private void sortByYear() {
        Collections.sort(albums, new Comparator<SearchAlbum>() {
            @Override
            public int compare(SearchAlbum album1, SearchAlbum album2) {
                if (album1.intYearReleased == album2.intYearReleased) {
                    return 0;
                }

                return album1.intYearReleased < album2.intYearReleased ? -1 : 1;
            }
        });

        rvList.getAdapter().notifyDataSetChanged();
    }


    private void sortBySales() {
        Collections.sort(albums, new Comparator<SearchAlbum>() {
            @Override
            public int compare(SearchAlbum album1, SearchAlbum album2) {
                if (album1.intSales == album2.intSales) {
                    return 0;
                }

                return album1.intSales < album2.intSales ? -1 : 1;
            }
        });

        rvList.getAdapter().notifyDataSetChanged();
    }


    private void sortByScore() {
        Collections.sort(albums, new Comparator<SearchAlbum>() {
            @Override
            public int compare(SearchAlbum album1, SearchAlbum album2) {
                if (album1.intScore == album2.intScore) {
                    return 0;
                }

                return album1.intScore < album2.intScore ? -1 : 1;
            }
        });

        rvList.getAdapter().notifyDataSetChanged();
    }




}