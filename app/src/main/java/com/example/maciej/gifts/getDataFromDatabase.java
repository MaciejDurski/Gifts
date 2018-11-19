package com.example.maciej.gifts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.maciej.gifts.MainActivity.osoby;

public class getDataFromDatabase extends AsyncTask<Void,Void,String> {
    String json_url;
    String JSON_STRING;


    private ProgressDialog dialog;
    Context context;



    public  getDataFromDatabase(Context context){
        this.context = context.getApplicationContext();

        dialog = new ProgressDialog(context.getApplicationContext());
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);

    }




    @Override
    protected void onPreExecute() {
        json_url="http://facereco.000webhostapp.com/json_get_data.php";
        dialog.setMessage("Aktualizuje dane...");
        dialog.show();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(dialog.isShowing()){
            dialog.dismiss();
        }

        MainActivity.adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getNamesFromArray(osoby));
        MainActivity.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainActivity.spinner.setAdapter(MainActivity.adapter);
        MainActivity.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                MainActivity.ID_person_giving = osoby.get(position).getID();
                Log.e("id", String.valueOf(MainActivity.ID_person_giving));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);


    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((JSON_STRING = bufferedReader.readLine())!=null){
                stringBuilder.append(JSON_STRING+"\n");

            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            JSONObject jsonObject = new JSONObject( stringBuilder.toString() );
            JSONArray jsonArray = jsonObject.getJSONArray( "server_response" );

            int count =0;

            int id , status;

            String name="";

            while (count<jsonArray.length()){
                JSONObject JO =    jsonArray.getJSONObject(count);
                id = JO.getInt("ID");
                name= JO.getString("name");
                status= JO.getInt("status");
                Users user = new Users(id,name,status);

                osoby.add(user);
                MainActivity.names.add(name);

                count++;
            }
            Log.e("działą",name  );
            Thread.sleep(1000);



            return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> getNamesFromArray(ArrayList<Users> ludzie) {
        ArrayList<String> imiona = new ArrayList<>();
        for (int i = 0; i < ludzie.size(); i++) {
            imiona.add(ludzie.get(i).getName());
        }
        return imiona;
    }
}
