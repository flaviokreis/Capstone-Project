package mobi.dende.simpletimesheet.ui.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.write.WriteException;
import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.controller.TimesheetManager;
import mobi.dende.simpletimesheet.conveter.ExportListener;
import mobi.dende.simpletimesheet.conveter.ProjectExportExcel;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.ProjectExport;
import mobi.dende.simpletimesheet.ui.dialog.DatePickerFragment;

/**
 * Created by flaviokreis on 21/02/16.
 */
public class ExportFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String STATE_PROJECTS = "state_projects";
    private static final String STATE_PROJECT_DETAIL = "state_project_detail";

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 201;

    private Spinner projectSpinner;
    private TextView exportFrom;
    private TextView exportTo;

    private TextView totalHours;
    private TextView hourValue;
    private TextView totalValue;

    private Button exportBtn;

    private ProjectExport mProjectExport;

    private ArrayList<Project> mProjects;

    private SharedPreferences mPrefs;
    private DateFormat mDateFormat;

    private ProgressDialog mProgressDialog;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_PROJECT_DETAIL, mProjectExport);
        outState.putParcelableArrayList(STATE_PROJECTS, mProjects);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mProjects = savedInstanceState.getParcelableArrayList(STATE_PROJECTS);
            mProjectExport = savedInstanceState.getParcelable(STATE_PROJECT_DETAIL);
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_export, container, false);

        projectSpinner  = (Spinner)  view.findViewById(R.id.export_project_spinner);
        exportFrom      = (TextView) view.findViewById(R.id.export_from);
        exportTo        = (TextView) view.findViewById(R.id.export_to);

        exportFrom.setOnClickListener(ExportFragment.this);
        exportTo.setOnClickListener(ExportFragment.this);

        totalHours = (TextView) view.findViewById(R.id.export_total_hours);
        hourValue  = (TextView) view.findViewById(R.id.export_value_hour);
        totalValue = (TextView) view.findViewById(R.id.export_value_total);

        exportBtn = (Button) view.findViewById(R.id.export_btn);
        exportBtn.setOnClickListener(ExportFragment.this);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mProjects == null){
            new ProjectsAsync().execute();
        }
        else{
            initLayout();
        }
    }

    private void initLayout(){
        if(mProjects == null){
            return ;
        }

        ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(adapter);

        Project project = (Project)projectSpinner.getSelectedItem();

        projectSpinner.setOnItemSelectedListener(ExportFragment.this);

        if((mProjectExport == null) && (project != null)){
            Calendar calendar = Calendar.getInstance();
            Calendar firstDayCalendar = Calendar.getInstance();
            firstDayCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 1);
            Calendar lastDayCalendar = Calendar.getInstance();
            lastDayCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);

            exportFrom.setText(mDateFormat.format(firstDayCalendar.getTime()));
            exportTo.setText(mDateFormat.format(lastDayCalendar.getTime()));

            mProjectExport = new ProjectExport();
            mProjectExport.setProject(project);
            mProjectExport.setStartDate(firstDayCalendar.getTime());
            mProjectExport.setEndDate(lastDayCalendar.getTime());

            new GetProjectDetail().execute(mProjectExport);
        }
        else{
            Project proj;
            for(int i = 0; i < mProjects.size(); i++){
                proj = mProjects.get(i);
                if(proj.getId() == mProjectExport.getProject().getId()){
                    projectSpinner.setSelection(i);
                    break;
                }
            }
            updateLayout();
        }
    }

    private void updateLayout(){

        String coin = mPrefs.getString("currency", "$");

        totalHours.setText(String.format(getString(R.string.total_hours), mProjectExport.getTotalHours()));
        hourValue.setText(String.format(getString(R.string.export_hour), coin, mProjectExport.getProject().getValueHour()));
        totalValue.setText(String.format("%s %.02f", coin, (mProjectExport.getTotalHours() * mProjectExport.getProject().getValueHour())));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.export_from){
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setListener(mProjectExport.getStartDate(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    exportFrom.setText(mDateFormat.format(calendar.getTime()));

                    mProjectExport.setProject((Project) projectSpinner.getSelectedItem());
                    mProjectExport.setStartDate(calendar.getTime());
                    mProjectExport.setTimers(null);
                    mProjectExport.setTotalHours(0);

                    new GetProjectDetail().execute(mProjectExport);
                }
            });

            datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        }
        else if(v.getId() == R.id.export_to){
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setListener(mProjectExport.getEndDate(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    exportTo.setText(mDateFormat.format(calendar.getTime()));

                    mProjectExport.setProject((Project)projectSpinner.getSelectedItem());
                    mProjectExport.setEndDate(calendar.getTime());
                    mProjectExport.setTimers(null);
                    mProjectExport.setTotalHours(0);

                    new GetProjectDetail().execute(mProjectExport);
                }
            });

            datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        }
        else if(v.getId() == R.id.export_btn){
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
            else{
                mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.export_dialog), true);
                new ExportAsync().execute(mProjectExport);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.export_dialog), true);
                    new ExportAsync().execute(mProjectExport);

                }
                return;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mProjectExport.setProject((Project)projectSpinner.getSelectedItem());
        mProjectExport.setTimers(null);
        mProjectExport.setTotalHours(0);

        new GetProjectDetail().execute(mProjectExport);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void onSaveDone( ){
        mProgressDialog.dismiss();
    }

    private class ProjectsAsync extends AsyncTask<Void, Void, List<Project>> {

        @Override
        protected List<Project> doInBackground(Void... params) {
            return TimesheetManager.getProjects(getActivity());
        }

        @Override
        protected void onPostExecute(List<Project> projects) {
            super.onPostExecute(projects);

            if((projects != null) && !projects.isEmpty()){
                mProjects = (ArrayList<Project>)projects;
                initLayout();
            }
        }
    }

    private class GetProjectDetail extends AsyncTask<ProjectExport, Void, ProjectExport> {

        @Override
        protected ProjectExport doInBackground(ProjectExport... params) {
            return TimesheetManager.getProjectExportDetail(getActivity(), params[0]);
        }

        @Override
        protected void onPostExecute(ProjectExport projectExport) {
            super.onPostExecute(projectExport);

            if(projectExport != null){
                mProjectExport = projectExport;
                updateLayout();
            }
        }
    }

    private class ExportAsync extends AsyncTask<ProjectExport, Integer, Void> {
        @Override
        protected Void doInBackground(ProjectExport... params) {
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_kk_mm_ss_", Locale.getDefault());
                ProjectExportExcel export = new ProjectExportExcel(getActivity(),
                        format.format(new Date()) + "timesheet_export.xls", params[0].getTimers() );

                export.addListener( new ExportListener() {
                    @Override
                    public void onProgress(int value, int total) {
                        onProgressUpdate(value);
                    }

                    @Override
                    public void onDone( String savedFile ) {
                        onSaveDone();
                    }

                    @Override
                    public void onError() {
                        onSaveDone();
                    }
                });

                export.write();
            }
            catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
