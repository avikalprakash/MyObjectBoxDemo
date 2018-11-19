package com.example.objectboxdemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.objectboxdemo.R;
import com.example.objectboxdemo.adapter.PlayerListAdapter;
import com.example.objectboxdemo.model.Player;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPlayerList;
    private List<Player> playerList = new ArrayList<>();
    private PlayerListAdapter playerListAdapter;
    private FloatingActionButton fabAddPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        playerListAdapter = new PlayerListAdapter(MainActivity.this, playerList);
        playerListAdapter.updatePlayers();
        rvPlayerList = (RecyclerView) findViewById(R.id.rvPlayerList);
        rvPlayerList.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(MainActivity.this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        rvPlayerList.setLayoutManager(llm2);
        rvPlayerList.setAdapter(playerListAdapter);
        fabAddPlayer = (FloatingActionButton) findViewById(R.id.fabAddPlayer);
        fabAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerListAdapter.showAddEditDialog(PlayerListAdapter.MODE_ADD, 0);
            }
        });
    }

}