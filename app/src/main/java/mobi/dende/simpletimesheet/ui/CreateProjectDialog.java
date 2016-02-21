package mobi.dende.simpletimesheet.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ribell.colorpickerview.ColorPickerView;
import com.ribell.colorpickerview.interfaces.ColorPickerViewListener;

import java.util.Date;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;

/**
 * Created by flaviokreis on 13/02/16.
 */
public class CreateProjectDialog extends DialogFragment {
    private int selectedColor = -1;

    private OnCreateProjectListener listener;

    public interface OnCreateProjectListener {
        void onCreateProject(Project project);
    }

    public void setListener(OnCreateProjectListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_project, null);

        final EditText projectName        = (EditText)view.findViewById(R.id.project_name);
        final ColorPickerView colorPicker = (ColorPickerView)view.findViewById(R.id.project_color_picker);
        final EditText projectValue       = (EditText)view.findViewById(R.id.project_value);
        final EditText projectDescription = (EditText)view.findViewById(R.id.project_description);

        colorPicker.setListener(new ColorPickerViewListener() {
            @Override
            public void onColorPickerClick(int color) {
                Integer value = (Integer)colorPicker.getAdapter().getItem(color);
                if(value != null){
                    selectedColor = value;
                }
            }
        } );

        builder.setTitle(getString(R.string.add_project))
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(TextUtils.isEmpty(projectName.getText().toString().trim())){
                            Toast.makeText(getActivity(), R.string.need_insert_a_name, Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        else if(TextUtils.isEmpty(projectValue.getText().toString().trim())){
                            Toast.makeText(getActivity(), R.string.need_insert_a_value, Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        if(selectedColor == -1){
                            selectedColor = getActivity().getResources().getColor(R.color.project_color_1);
                        }

                        if(listener != null){
                            Project project = new Project();
                            project.setId(0);
                            project.setName(projectName.getText().toString().trim());
                            project.setColor(selectedColor);
                            project.setValueHour(Float.valueOf(projectValue.getText().toString()));
                            project.setDescription(projectDescription.getText().toString().trim());
                            project.setInsertedDate(new Date());
                            listener.onCreateProject(project);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //No code
                    }
                });
        return builder.create();
    }
}
