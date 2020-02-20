package com.example.currentcyconverter;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class currenciesAPI extends AsyncTask<Void,Void,MainActivity> {


    private JSONObject jsonData;
    private MainActivity activity;



    public currenciesAPI(MainActivity act) {
        this.activity = act;
    }


    @Override
    protected MainActivity doInBackground(Void... voids) {

        String address = "https://api.exchangeratesapi.io/latest?base=USD";
        JSONObject root = null;
        try{
            URL url = new URL(address);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            InputStream cont = (InputStream) request.getContent();

            String jsonString = j2s(cont);

            root = new JSONObject(jsonString);
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        jsonData = root;

        return activity;
    }

    @Override
    protected void onPostExecute(MainActivity activity) {

        activity.finished(jsonData);
        super.onPostExecute(activity);
    }

    private String j2s(InputStream is) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }
}

