package mobi.dende.simpletimesheet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.dende.simpletimesheet.R;

/**
 * Created by flaviokreis on 21/02/16.
 */
public class ExportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_export, container, false);

        return view;
    }
}
