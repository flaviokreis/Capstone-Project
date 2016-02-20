package mobi.dende.simpletimesheet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.util.ChartUtil;
import mobi.dende.simpletimesheet.util.CustomYAxisValueFormatter;

public class ProjectDetailsFragment extends Fragment{

    public static final String EXTRA_PROJECT = "extra_project";

    private BarChart mChart;

    private Project mProject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProject = getArguments().getParcelable(EXTRA_PROJECT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_project, container, false);

        mChart = (BarChart) view.findViewById(R.id.chart);

        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(0);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxisValueFormatter custom = new CustomYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        setData(12, 176);

        return view;
    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xVals.add(ChartUtil.months[i % 12]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        BarEntry entry;
        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            entry = new BarEntry(val, i);
            yVals1.add(entry);
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(15f);
        set1.setColor(mProject.getColor());

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }
}
