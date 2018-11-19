package com.example.maciej.gifts;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateDatabase extends AsyncTask<Integer,Void,Void> {

    String add_info_url;
    Context context;

    public UpdateDatabase(Context context){
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        add_info_url="http://facereco.000webhostapp.com/add_info.php";

    }

    @Override
    protected void onPostExecute(Void aVoid) {

        Toast.makeText(context,"Zapisano",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Integer... args) {

        int ID;
        int ID_give;
        ID=args[0];
        ID_give=args[1];

        try {
            URL url = new URL(add_info_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String dataString = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(String.valueOf(ID),"UTF-8")+"&"+
                    URLEncoder.encode("idg","UTF-8")+"="+URLEncoder.encode(String.valueOf(ID_give));
            bufferedWriter.write(dataString);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream= httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
