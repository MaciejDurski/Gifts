package com.example.maciej.gifts;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    Button random, save;

    int ID;

    public static int ID_person_giving;

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    public static ArrayList<Users> osoby = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();
    public static Spinner spinner;
    public static ArrayAdapter<String> adapter;
    public static TextView check, result;
    public ProgressDialog progressDialog;
    String randomName;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermission();

        random = findViewById(R.id.randomBtn);
        save = findViewById(R.id.saveBtn);
        check = findViewById(R.id.selectNameTW);
        result = findViewById(R.id.resultTW);
        spinner = findViewById(R.id.spinner);
        save.setEnabled(false);

        dataUpdating();









        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setEnabled(true);


                randomName = getRandomNameAndId();
                Log.e("raz", String.valueOf(ID));
                Log.e("dwa",String.valueOf(ID_person_giving));

                if (randomName!=null) {

                    progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                    progressDialog.setMessage("Losuje...");
                    progressDialog.show();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();

                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Gratulacje!");
                                alertDialog.setMessage("Wylosowałeś " + randomName + "\n Zapisz lub losuj ponownie");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                                result.setText(randomName);
                            }
                        }
                    }, 500);


                } else {
                    result.setText("Wyszystkie osoby zostały wylosowane");
                    save.setEnabled(false);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatabase updateDatabase = new UpdateDatabase(MainActivity.this);
                updateDatabase.execute(ID, ID_person_giving);
                dataUpdating();
            }
        });


    }

    private void dataUpdating() {
        final getDataFromDatabase getDataFromDatabase = new getDataFromDatabase(MainActivity.this);

            getDataFromDatabase.execute();
            result.setText("");

    }



    public String getRandomNameAndId() {
        Random rand = new Random();
        String randomName = "";
        int index;
        int count =0;

        int status = 1;
        while (status == 1) {
            index = rand.nextInt(osoby.size());
            randomName = osoby.get(index).getName();

            ID = osoby.get(index).getID();

            if(ID==ID_person_giving){
                status=1;
                count=0;
            }else{
                status = osoby.get(index).getStatus();
            }




            count++;
            if (count>1+osoby.size()){
                randomName = null;


                break;
            }



        }



        return randomName;


    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);


            }
        }
    }

    public void syncMethod(View view) {
        osoby.clear();
        names.clear();


        dataUpdating();
    }


}
