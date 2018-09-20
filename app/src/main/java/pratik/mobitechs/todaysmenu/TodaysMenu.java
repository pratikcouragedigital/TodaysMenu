package pratik.mobitechs.todaysmenu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import pratik.mobitechs.todaysmenu.InternetConnectivity.NetworkChangeReceiver;
import pratik.mobitechs.todaysmenu.SessionManager.SessionManager;

public class TodaysMenu extends BaseActivity implements View.OnClickListener {

    private StringBuilder date;
    String selectedDateFor = "";
    String Userid;
    boolean doubleBackToExitPressedOnce = false;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Button submit;
    EditText txtIngredients;
    EditText txtRemark;
    EditText txtDate;
    Spinner timmingSpinner;

    String ingredients;
    String remark;
    String timming;
    String method ="CreateMenu";
    private ProgressDialog progressDialog = null;
    String saveMenuResponseResult;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todays_menu);

        SessionManager sessionManagerNgo = new SessionManager(TodaysMenu.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        Userid = typeOfUser.get(SessionManager.KEY_USERID);

        txtIngredients = (EditText) findViewById(R.id.txtIngredients);
        timmingSpinner = (Spinner) findViewById(R.id.timmingSpinner);
        txtRemark = (EditText) findViewById(R.id.txtRemark);
        txtDate = (EditText) findViewById(R.id.txtDate);
        submit = (Button) findViewById(R.id.btnSubmit);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        txtDate.setOnClickListener(this);
        submit.setOnClickListener(this);

        String CurrentDate = String.valueOf(day) +"/" + String.valueOf(month + 1) +"/" + String.valueOf(year);
        txtDate.setText(CurrentDate);

        txtDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(view);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            remark = txtRemark.getText().toString();
            ingredients = txtIngredients.getText().toString();
            timming = timmingSpinner.getSelectedItem().toString();

            if (selectedDateFor == "") {
                Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
            }
            else{
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.");
                progressDialog.show();

                SaveMenuAsyncCallWS task = new SaveMenuAsyncCallWS();
                task.execute();
            }
        }

    }

    public class SaveMenuAsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            saveMenuResponseResult = WebService.AddMenu(remark,ingredients,timming,selectedDateFor,Userid,method);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            if(saveMenuResponseResult.equals("New Menu Detail Add Succesfully.")) {
                progressDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(TodaysMenu.this);
                builder.setTitle("Result");
                builder.setMessage(saveMenuResponseResult);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface alert, int which) {
                        // TODO Auto-generated method stub
                        //Do something
                        alert.dismiss();
                        Intent gotohome = new Intent(TodaysMenu.this, MainActivity.class);
                        startActivity(gotohome);

                    }
                });
                AlertDialog alert1 = builder.create();
                alert1.show();
            }
            else if(saveMenuResponseResult.equals("No Network Found")) {
                progressDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(TodaysMenu.this);
                builder.setTitle("Result");
                builder.setMessage("Please Try Again Later.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface alert, int which) {
                        // TODO Auto-generated method stub
                        //Do something
                        alert.dismiss();
                    }
                });
                AlertDialog alert1 = builder.create();
                alert1.show();
            }
        }
    }

    @SuppressWarnings("deprecation")

    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        txtDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date = new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year);

        selectedDateFor = date.toString();
    }


    @Override
    public void onBackPressed() {
        TodaysMenu.this.finish();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = TodaysMenu.this.getPackageManager();
        ComponentName component = new ComponentName(TodaysMenu.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = TodaysMenu.this.getPackageManager();
        ComponentName component = new ComponentName(TodaysMenu.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
