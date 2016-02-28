package mobi.dende.simpletimesheet.ui.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.List;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.controller.TimesheetManager;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.ProjectDetail;
import mobi.dende.simpletimesheet.util.CustomYAxisValueFormatter;
import mobi.dende.simpletimesheet.util.Utils;

public class ProjectDetailsFragment extends Fragment{

    public static final String EXTRA_PROJECT = "extra_project";
    private static final String STATE_PROJECT_DETAILS = "state_project_details";

    private BarChart mChart;

    private TextView hourToday;
    private TextView hourWeek;
    private TextView hourMonth;

    private TextView moneyHour;
    private TextView moneyMonth;
    private TextView moneyTotal;

    private TextView description;

    private Project mProject;
    private ProjectDetail mProjectDetail;

    private SharedPreferences mPrefs;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_PROJECT_DETAILS, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProject = getArguments().getParcelable(EXTRA_PROJECT);
        if(savedInstanceState != null){
            ProjectDetail projectDetail = savedInstanceState.getParcelable(STATE_PROJECT_DETAILS);
            if(projectDetail != null){
                mProjectDetail = projectDetail;
            }
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_project, container, false);

        mChart = (BarChart) view.findViewById(R.id.chart);

        hourToday = (TextView) view.findViewById(R.id.hour_today);
        hourWeek  = (TextView) view.findViewById(R.id.hour_week);
        hourMonth = (TextView) view.findViewById(R.id.hour_month);

        moneyHour  = (TextView) view.findViewById(R.id.money_hour);
        moneyMonth = (TextView) view.findViewById(R.id.money_month);
        moneyTotal = (TextView) view.findViewById(R.id.money_total);

        description = (TextView) view.findViewById(R.id.project_description);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if((mProjectDetail == null) && (mProject != null)){
            new ProjectDetailAsync().execute(mProject);
        }
        else if ( mProjectDetail != null ){
            setInfos(mProjectDetail);
        }
    }

    private void setChartData(List<Integer> months) {
        if( (months == null) || months.isEmpty() ){
            return ;
        }

        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < months.size(); i++) {
            xVals.add(Utils.months[i % 12]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        BarEntry entry;
        for (int i = 0; i < months.size(); i++) {
            entry = new BarEntry(months.get(i), i);
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

    private void setInfos(ProjectDetail projectDetail){
        if(projectDetail == null){
            return ;
        }

        mProjectDetail = projectDetail;

        hourToday.setText(Utils.getHourByMinute((int)mProjectDetail.getMinutesToday()));
        hourWeek.setText(Utils.getHourByMinute((int)mProjectDetail.getMinutesWeek()));
        hourMonth.setText(Utils.getHourByMinute((int) mProjectDetail.getMinutesMonth()));

        String coin = mPrefs.getString("currency", "$");

        moneyHour.setText(String.format("%s %.02f", coin, mProjectDetail.getEarnPerHour()));
        moneyMonth.setText(String.format("%s %.02f", coin, mProjectDetail.getEarnMonth()));
        moneyTotal.setText(String.format("%s %.02f", coin, mProjectDetail.getEarnTotal()));

        description.setText(mProjectDetail.getProject().getDescription());

        setChartData(mProjectDetail.getMonths());
    }

    private class ProjectDetailAsync extends AsyncTask<Project, Void, ProjectDetail> {
        @Override
        protected ProjectDetail doInBackground(Project... params) {
            if(params != null){
                ProjectDetail projectDetail = TimesheetManager.getProjectDetail(getActivity(), params[0]);
                return projectDetail;
            }

            return null;
        }

        @Override
        protected void onPostExecute(ProjectDetail result) {
            super.onPostExecute(result);
            setInfos(result);
        }
    }
}
