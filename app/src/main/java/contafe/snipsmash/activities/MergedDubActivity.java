package contafe.snipsmash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;

import contafe.snipsmash.R;

public class MergedDubActivity extends AppCompatActivity {

    private RecyclerView snipView;
    private Button playButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_dub);
        snipView = (RecyclerView) findViewById(R.id.recyclerView);
        snipView.setHasFixedSize(true);
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("data");
        System.out.println("BLABLA + " + urls.size());
        playButton = (Button) findViewById(R.id.playButton);
        saveButton = (Button) findViewById(R.id.saveButton);

    }

}