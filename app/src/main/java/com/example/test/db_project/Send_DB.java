package com.example.test.db_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Send_DB extends AsyncTask<String, Void, String> {
    String text;
    Context context;
    JSONArray login;
    int category;
    String u_pw = "";
    String staffID="";

    public Send_DB(Context context, int category){
        this.category =category;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String uri = "http://192.168.0.18/"+params[0];
        /* 인풋 파라메터값 생성 */
        String param = params[1];
        Log.e("test1",param);
        u_pw = params[2];
        BufferedReader bufferedReader = null;
        try {
            /* 서버연결 */
            URL url = new URL(uri);
            Log.e("test1", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            Log.e("test1", "connect");
            /* 안드로이드 -> 서버 파라메터값 전달 */
            OutputStream outputStream = conn.getOutputStream();
            Log.e("test1", "connect1");
            outputStream.write(param.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();

            Log.e("test1", "connect2");
            /* 서버 -> 안드로이드 파라메터값 전달 */
            StringBuilder sb = new StringBuilder();

            Log.d("nomal", "test2");
            InputStream inputStream;
            int responseStatusCode = conn.getResponseCode();
            Log.d("nomal", "response code - " + responseStatusCode);
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
            }
            else{
                inputStream = conn.getErrorStream();
            }
            Log.d("nomal", "test3");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            Log.d("nomal", "test4");
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }

            String data = sb.toString().trim();

            /* 서버에서 응답 */
            Log.e("test1",data);

            return data;

        } catch (Exception e)
        {
            Log.e("test1", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        text = data;
        switch(category){
            case 1:
                Login();
                break;
        }



    }

    public String getText(){
        return text;
    }

    public void Login(){
        String db_pw="";

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        try {
            JSONObject jsonObj = new JSONObject(text);
            login = jsonObj.getJSONArray("result");

            for(int i = 0; i<login.length(); i++){
                JSONObject c = login.getJSONObject(i);
                db_pw = c.getString("pw");
                staffID = c.getString("staffID");
            }
        } catch (JSONException e) {
            Log.e("test1",e.toString());
        }

        Log.e("test1", u_pw +" "+db_pw);
        try{
            if(db_pw.equals(u_pw) && !u_pw.equals(""))
            {
                Log.e("test1","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 확인되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, Staff_Start.class);
                                intent.putExtra("staffID", staffID);
                                context.startActivity(intent);
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("아이디나 비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
        catch (Exception e){
            Log.e("test1", e.toString());
        }

    }
}
