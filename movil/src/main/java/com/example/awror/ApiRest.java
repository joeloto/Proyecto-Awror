package com.example.awror;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiRest {

    public void subirUsuario(String user_name, String real_name, String real_surname, String email, String password){
        new Thread(()->{
            HttpURLConnection con = null;
            try {
                URL url = new URL("http://192.130.0.7:8080/apiawror/rest/users");
                con = (HttpURLConnection) url.openConnection(); //abrir conexion
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                con.setDoOutput(true); //voy a escribir en el body

                JSONObject json = new JSONObject();
                json.put("user_name",user_name);
                json.put("real_name",real_name);
                json.put("real_surname",real_surname);
                json.put("email",email);
                json.put("password", password);
                System.out.println(json);

                try (OutputStream os = con.getOutputStream()){
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));//enviar el body
                } catch (IOException e) {
                    throw new IOException(e);
                }

                int code = con.getResponseCode();//forzar envio
                Log.i("CODIGO APIREST","EL CODIGO RESTANTE ES " + code);

            } catch (Exception e) {
               Log.e("APIREST","error");
            } finally {
                if (con != null) con.disconnect();


            }
        }).start();
    }
}
