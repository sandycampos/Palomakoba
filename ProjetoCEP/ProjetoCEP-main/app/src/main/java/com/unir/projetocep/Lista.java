package com.unir.projetocep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import com.unir.projetocep.dados.SQLite;

import java.util.ArrayList;
import java.util.List;

import AdapterRecycler.AdapterRecycler;

public class Lista extends AppCompatActivity {

    private RecyclerView recyclerView;
    SQLite bd;
    private List<ContentValues> listEndereco = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        bd = new SQLite(getApplicationContext());
        recyclerView = findViewById(R.id.recycler);
        listEndereco = bd.pesquiarPorTodos();

        Log.i("val","aqui:"+listEndereco.size());

        AdapterRecycler adapter = new AdapterRecycler(listEndereco,getSupportFragmentManager(),getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}