package mobi.dende.simpletimesheet.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.ui.fragment.ExportFragment;

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

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
