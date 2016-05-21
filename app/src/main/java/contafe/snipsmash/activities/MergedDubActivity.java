package contafe.snipsmash.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

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

        playButton = (Button) findViewById(R.id.playButton);
        saveButton = (Button) findViewById(R.id.saveButton);

    }

}