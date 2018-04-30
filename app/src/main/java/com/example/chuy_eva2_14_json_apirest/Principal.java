package com.example.chuy_eva2_14_json_apirest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Principal extends AppCompatActivity {
    TextView txtData;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtData = findViewById(R.id.txt_data);
        btnDone = findViewById(R.id.btn_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONInsert().execute("Producto","10000");
            }
        });
    }

    private void sendData(View v) {
        new JSONConnect().execute();
    }

    class JSONConnect extends AsyncTask<Void, Void, String> {

        final String endpoint = "http://10.6.0.112:3000/tasks";

        @Override
        protected String doInBackground(Void... voids) {
            String resultado = "";
            try {
                URL url = new URL(endpoint);
                HttpURLConnection httpConCLima = (HttpURLConnection) url.openConnection();

                if (httpConCLima.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader buffer =
                            new BufferedReader(new InputStreamReader(httpConCLima.getInputStream()));
                    resultado = buffer.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtData.append(s);

        }
    }

    class JSONPost extends AsyncTask<String, Void, String> {
        final String endpoint = "http://10.6.0.112:3000/tasks";


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtData.append(s);

        }
    }

    class JSONInsert extends AsyncTask<String, Void, String> {
        final String sEnlace = "http://dirip:3000/Task/";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtData.append(s);

        }

        @Override
        protected String doInBackground(String... s) {
            BufferedWriter bwInsertJSON=null;
            String sResu="";
            try{
                JSONObject jsDatos= new JSONObject();
                jsDatos.put("Producto", s[0]);
                jsDatos.put("UnitPrice", s[1]);
                URL url=new URL(sEnlace);
                HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type","aplication/json");
                httpCon.connect();
                OutputStream usConnect =httpCon.getOutputStream();
                bwInsertJSON = new BufferedWriter(new OutputStreamWriter(usConnect));
                bwInsertJSON.write(jsDatos.toString());
                bwInsertJSON.flush();
                //leer respuesta
                InputStream inLeerRespuesta= httpCon.getInputStream();
                BufferedReader bfLeeRespuesta= new BufferedReader(new InputStreamReader(inLeerRespuesta));
                String sLinea;
                while ((sLinea = bfLeeRespuesta.readLine())!=null){
                    sResu=sResu+sLinea;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(bwInsertJSON!=null){
                    try {
                        bwInsertJSON.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return sResu;
        }
    }

}