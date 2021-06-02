package com.dungeonmaster.tablegames.available;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.dungeonmaster.R;
import com.dungeonmaster.instruments.counters.typical.MagicTheGathering;
import com.dungeonmaster.instruments.counters.typical.mtg.PlayField;
import com.google.gson.Gson;
import com.menu.MenuBar;
import com.serverconnection.Server;
import com.serverconnection.URLs;
import com.serverconnection.model.GameProgress;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AvailableGames extends MenuBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_games);

        Server.passRequest(HttpMethod.GET, URLs.SAVES_LIST, "");
        ResponseEntity<String> response = Server.getQueryResult(URLs.SAVES_LIST);
        if (response.getStatusCode() == HttpStatus.OK) {
            Gson gson = new Gson();
            GameProgress[] gps;
            String body = response.getBody();
            gps = gson.fromJson(body, GameProgress[].class);
            for(int i = 0; i < gps.length; i++){
                LinearLayout ll = findViewById(R.id.savesList);
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);
                innerLayout.setBackground(getDrawable(R.drawable.btn_app_form));

                Button btn = new Button(this);
                LinearLayout.LayoutParams innerLl =  new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                innerLl.setMargins(0, 10,0,0);
                innerLl.weight = 3;

                btn.setLayoutParams(innerLl);
                btn.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_borderless));
                btn.setText(gps[i].getShowName() + " " + gps[i].getDateLastChange());

                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setImageDrawable(getDrawable(R.drawable.remove_notes));

                LinearLayout.LayoutParams innerLll =  new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                innerLll.setMargins(2, 10,0,0);
                innerLll.weight = 1;

                deleteButton.setLayoutParams(innerLll);
                deleteButton.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_borderless));
//
                innerLayout.addView(btn);
                innerLayout.addView(deleteButton);

                ll.addView(innerLayout);

                String name = gps[i].getGameName();
                Long id = gps[i].getId();
                String payload = gps[i].getPayload();

                btn.setOnClickListener(v -> {
                   switch (name){
                       case PlayField.GAME_NAME:
                          Intent intent = new Intent(this, PlayField.class);
                          intent.putExtra("AllTheData",  payload);
                           intent.putExtra("id",  id);

                          startActivity(intent);
                          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                       break;
                   }

                });
                deleteButton.setOnClickListener(v -> {
                    Server.passRequest(HttpMethod.GET, URLs.DROP_PROGRESS+"/"+id, "");
                    ll.removeView(innerLayout);
                });
            }
        }
    }
}