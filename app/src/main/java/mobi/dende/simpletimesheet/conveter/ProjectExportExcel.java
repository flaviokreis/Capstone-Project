package mobi.dende.simpletimesheet.conveter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import jxl.write.WritableSheet;
import jxl.write.WriteException;
import mobi.dende.simpletimesheet.model.Timer;

import java.util.List;

/**
 * Created by flaviokreis on 28/02/16.
 */
public class ProjectExportExcel extends AbstractExport {
    public static final String TAG = "ExportClientToExcel";

    public static final String SHEET_NAME = "Timesheet";

    String coin = "";

    public ProjectExportExcel(Context context, String inputFile, List<Timer> list) throws WriteException {
        super( context, SHEET_NAME, inputFile, list );

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        coin = mPrefs.getString("currency", "$");
    }

    @Override
    void createLabel(WritableSheet sheet) throws WriteException {
        Log.d(TAG, "Write label");
        addCaption(sheet, 0, 0, "Start");
        addCaption(sheet, 1, 0, "End");
        addCaption(sheet, 2, 0, "Time");
        addCaption(sheet, 3, 0, "Task");
        addCaption(sheet, 4, 0, "Earn");
    }

    @Override
    void createContent(WritableSheet sheet) throws WriteException {
        Log.d( TAG, "Write content" );
        Timer item;
        for (int i = 0; i < getList().size(); i++) {
            item = (Timer) getList().get(i);
            addDate(  sheet, 0, i + 1, item.getStartTime());
            addDate(  sheet, 1, i + 1, item.getEndTime());
            addTime(  sheet, 2, i + 1, (int) ((item.getEndTime().getTime() - item.getStartTime().getTime()) / 60000));
            addLabel( sheet, 3, i + 1, item.getTask().getName());
            addLabel( sheet, 4, i + 1, String.format("%s %.02f", coin,
                    (int) ((item.getEndTime().getTime() - item.getStartTime().getTime()) / 3600000) *
                            item.getTask().getProject().getValueHour()));

            onProgress( ( i + 1 ), getList().size() );
        }
    }
}
