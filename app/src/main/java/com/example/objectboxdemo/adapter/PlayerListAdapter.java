package com.example.objectboxdemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.objectboxdemo.ObjectBoxDemoApp;
import com.example.objectboxdemo.R;
import com.example.objectboxdemo.activity.MainActivity;
import com.example.objectboxdemo.model.Player;
import com.example.objectboxdemo.model.Player_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by AsifMoinul on 1/13/2018.
 */

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerListHolder> {

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_DELETE = 2;

    private Context context;
    private List<Player> playerList;

    private BoxStore boxStore;
    private Box<Player> playerBox;

    public PlayerListAdapter(Context context, List<Player> playerList) {
        this.context = context;
        this.playerList = playerList;
        //getting BoxStore object
        boxStore = ((ObjectBoxDemoApp) ((MainActivity)context).getApplication()).getBoxStore();
        //getting Player Box object
        playerBox = boxStore.boxFor(Player.class);

    }

    @Override
    public PlayerListAdapter.PlayerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.row_player_list, parent, false);
        return new PlayerListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerListAdapter.PlayerListHolder holder, int position) {
        final Player player = playerList.get(position);
        holder.tvPlayerWithJersey.setText(player.getName() +" ("+player.getJerseyNumber()+")");
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class PlayerListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tvPlayerWithJersey;
        protected ImageButton btnEdit, btnDelete;

        public PlayerListHolder(View itemView) {
            super(itemView);
            tvPlayerWithJersey = (TextView) itemView.findViewById(R.id.tvPlayerWithJersey);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnEdit:
                    showAddEditDialog(MODE_EDIT, playerList.get(getAdapterPosition()).getId());
                    break;
                case R.id.btnDelete:
                    showAddEditDialog(MODE_DELETE, playerList.get(getAdapterPosition()).getId());
                    break;
            }

        }
    }

    /**
     * Shows a simple dialog to add/edit/delete
     * @param mode mode of operation i.e, add/edit/delete
     * @param id id of the player, 0 for new player
     */
    public void showAddEditDialog(final int mode, final long id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((MainActivity)context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_player_add, null);
        dialogBuilder.setView(dialogView);

        final EditText etPlayerName = (EditText) dialogView.findViewById(R.id.etPlayerName);
        final EditText etJerseyNumber = (EditText) dialogView.findViewById(R.id.etJerseyNumber);

        String title = "";
        switch (mode) {
            case MODE_ADD:
                title = "Add Player";
                break;
            case MODE_EDIT:
                title = "Edit Player";
                Player playerToEdit = getPlayerById(id);
                etPlayerName.setText(playerToEdit.getName());
                etJerseyNumber.setText(String.valueOf(playerToEdit.getJerseyNumber()));
                break;
            case MODE_DELETE:
                title = "Delete Player";
                Player playerToDelete = getPlayerById(id);
                etPlayerName.setText(playerToDelete.getName());
                etJerseyNumber.setText(String.valueOf(playerToDelete.getJerseyNumber()));
                etPlayerName.setKeyListener(null);
                etJerseyNumber.setKeyListener(null);
                break;
            default:
                break;
        }
        dialogBuilder.setTitle(title);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                switch (mode) {
                    case MODE_ADD:
                        if(etPlayerName.getText().toString().trim().isEmpty() || etJerseyNumber.getText().toString().trim().isEmpty()) {
                            Toast.makeText(context, "Name and Jersey number can not be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        addOrUpdatePlayer(new Player(0, etPlayerName.getText().toString().trim(), Integer.parseInt(etJerseyNumber.getText().toString().trim())));
                        updatePlayers();
                        break;
                    case MODE_EDIT:
                        addOrUpdatePlayer(new Player(id, etPlayerName.getText().toString().trim(), Integer.parseInt(etJerseyNumber.getText().toString().trim())));
                        updatePlayers();
                        break;
                    case MODE_DELETE:
                        deletePlayer(id);
                        updatePlayers();
                        break;
                    default:
                        break;
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void setPlayers(List<Player> players) {
        playerList = players;
        notifyDataSetChanged();
    }
    //Database related operation here

    private Player getPlayerById(long id) {
        return playerBox.query().equal(Player_.id, id).build().findUnique();
    }

    private void addOrUpdatePlayer(Player player) {
        playerBox.put(player);
    }

    private void deletePlayer(long id) {
        Player player = getPlayerById(id);
        if(player != null) {
            playerBox.remove(id);
        }
    }

    public void updatePlayers() {
        List<Player> players = playerBox.query().build().find();
        this.setPlayers(players);
    }
}