package mobi.dende.simpletimesheet.conveter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import mobi.dende.simpletimesheet.util.Utils;

/**
 * Created by flaviokreis on 28/02/16.
 */
public abstract class AbstractExport implements ExportListener {
    private static final String TAG = "AbstractExport";

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;

    private String inputFile;

    private String sheetName;

    private List<? extends Object> list;
    private List<ExportListener> listeners;

    private Map<Integer, Integer> collumnSizes;

    private Context mContext;
    private DateFormat mDateFormat;

    public AbstractExport( Context context, String sheetName, String inputFile, List<? extends Object> list )
            throws WriteException {

        mContext = context;

        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(mContext);

        listeners    = new ArrayList<>();
        collumnSizes = new HashMap<>();

        this.sheetName = sheetName;
        this.inputFile = inputFile;
        this.list      = list;

        createFormat();
    }

    public void addListener( ExportListener listener ){
        listeners.add(listener);
    }

    public void removeListener( ExportListener listener ){
        listeners.remove(listener);
    }

    List<? extends Object> getList(){
        return this.list;
    }

    void createFormat() throws WriteException{
        WritableFont times14pt = new WritableFont( WritableFont.TIMES, 14 );
        WritableFont times16ptBold = new WritableFont( WritableFont.TIMES, 16,
                WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE );

        times = new WritableCellFormat( times14pt );
        times.setWrap(true);

        timesBoldUnderline = new WritableCellFormat(times16ptBold);
        timesBoldUnderline.setWrap(true);
    }

    public final void write() throws IOException, WriteException {
        File externalDir = Environment.getExternalStorageDirectory();

        File dir = new File( externalDir.getAbsolutePath() + "/Timesheet/" );

        if( ! dir.exists() && ! dir.isDirectory() ){
            dir.mkdirs();
        }

        File file = new File(dir.getAbsolutePath(), inputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(Locale.getDefault());
        wbSettings.setEncoding( "UTF-8" );

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

        WritableSheet excelSheet = workbook.createSheet( sheetName, 0 );
        createLabel(excelSheet);
        createContent(excelSheet);
        updateSheetCollumnWidth(excelSheet);

        workbook.write();
        workbook.close();


        onDone( file.getAbsolutePath() );

        Log.d(TAG, "Finish create excel file");
    }

    abstract void createLabel(WritableSheet sheet) throws WriteException;

    abstract void createContent(WritableSheet sheet) throws WriteException;

    private void updateSheetCollumnWidth(WritableSheet sheet){
        for( Integer collumn : collumnSizes.keySet() ){
            sheet.setColumnView( collumn, (int)( collumnSizes.get( collumn ) * 1.5f ) );
        }
    }

    void addCaption(WritableSheet sheet, int column, int row, String s) throws WriteException {
        Label label;
        label = new Label( column, row, s, timesBoldUnderline );
        sheet.addCell( label );
        collumnSizes.put( column, s.length() );
    }

    void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
        int width = s.length();
        if( collumnSizes.containsKey( column ) ){
            int actualValue = collumnSizes.get( column );
            if( width > actualValue ){
                collumnSizes.put( column, width );
            }
        }
        else{
            collumnSizes.put( column, width );
        }
    }

    void addNumber(WritableSheet sheet, int column, int row, int s) throws WriteException {
        jxl.write.Number number = new jxl.write.Number( column, row, s, times );
        sheet.addCell( number );
    }

    void addDate(WritableSheet sheet, int column, int row, Date date) throws WriteException {
        String value;
        if( date == null ){
            value = "";
        }
        else{
            value = mDateFormat.format(date);
        }
        Label label;
        label = new Label(column, row, value, times);
        sheet.addCell(label);
        int width = value.length();
        if( collumnSizes.containsKey( column ) ){
            int actualValue = collumnSizes.get( column );
            if( width > actualValue ){
                collumnSizes.put( column, width );
            }
        }
        else{
            collumnSizes.put( column, width );
        }
    }

    void addTime(WritableSheet sheet, int column, int row, int minutes) throws WriteException {
        String value = Utils.getHourByMinute(minutes);
        Label label;
        label = new Label(column, row, value, times);
        sheet.addCell(label);
        int width = value.length();
        if( collumnSizes.containsKey( column ) ){
            int actualValue = collumnSizes.get( column );
            if( width > actualValue ){
                collumnSizes.put( column, width );
            }
        }
        else{
            collumnSizes.put( column, width );
        }
    }

    @Override
    public void onProgress( int value, int total ){
        if( listeners.isEmpty() == false ){
            for( ExportListener listener : listeners ){
                listener.onProgress( value, total );
            }
        }
    }

    @Override
    public void onDone( String localSaved ){
        if( listeners.isEmpty() == false ){
            for( ExportListener listener : listeners ){
                listener.onDone( localSaved );
            }
        }
    }

    @Override
    public void onError(){
        if( listeners.isEmpty() == false ){
            for( ExportListener listener : listeners ){
                listener.onError();
            }
        }
    }
}
