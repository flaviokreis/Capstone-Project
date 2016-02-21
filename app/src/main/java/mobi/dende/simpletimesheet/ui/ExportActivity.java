package mobi.dende.simpletimesheet.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.ui.fragment.ExportFragment;
import mobi.dende.simpletimesheet.ui.fragment.ProjectDetailsFragment;

public class ExportActivity extends AppCompatActivity {

    ExportFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        if(savedInstanceState == null){
            mFragment = new ExportFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.export_main_content, mFragment)
                    .commit();
        }
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
