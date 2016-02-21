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
import mobi.dende.simpletimesheet.model.Task;

/**
 * Created by flaviokreis on 13/02/16.
 */
public class CreateTaskDialog extends DialogFragment {

    private int selectedColor = -1;

    private OnCreateTaskListener listener;

    public interface OnCreateTaskListener {
        void onCreateTask(Task task);
    }

    public void setListener(OnCreateTaskListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_task, null);

        final EditText tasktName          = (EditText)view.findViewById(R.id.task_name);
        final ColorPickerView colorPicker = (ColorPickerView)view.findViewById(R.id.task_color_picker);
        final EditText taskDescription    = (EditText)view.findViewById(R.id.task_description);

        colorPicker.setListener(new ColorPickerViewListener() {
            @Override
            public void onColorPickerClick(int color) {
                Integer value = (Integer)colorPicker.getAdapter().getItem(color);
                if(value != null){
                    selectedColor = value;
                }
            }
        } );

        builder.setTitle(getString(R.string.add_task))
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(TextUtils.isEmpty(tasktName.getText().toString().trim())){
                            Toast.makeText(getActivity(), R.string.need_insert_a_name, Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        if(selectedColor == -1){
                            selectedColor = getActivity().getResources().getColor(R.color.project_color_1);
                        }

                        if(listener != null){
                            Task task = new Task();
                            task.setId(0);
                            task.setName(tasktName.getText().toString().trim());
                            task.setColor(selectedColor);
                            task.setDescription(taskDescription.getText().toString().trim());
                            task.setInsertedDate(new Date());

                            listener.onCreateTask(task);
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
