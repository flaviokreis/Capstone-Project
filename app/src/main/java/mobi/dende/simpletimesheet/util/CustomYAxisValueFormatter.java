package mobi.dende.simpletimesheet.util;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by flaviokreis on 20/02/16.
 */
public class CustomYAxisValueFormatter implements YAxisValueFormatter {
    private DecimalFormat mFormat;

    public CustomYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value) + "h";
    }
}
