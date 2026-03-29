package com.example.awror_def;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ApiRest {

    //private static final String BASE_URL = "http://192.168.1.133:8080/apiawror/rest/";
    private static final String BASE_URL = "http://10.0.2.2:8080/apiawror/rest/";

    public static ArrayList<Post> postsEnMemoria = new ArrayList<>();
    public void subirUsuario(String user_name, String real_name, String real_surname,
                             String email, String password, Activity activity) {

        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "users");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("user_name", user_name);
                json.put("real_name", real_name);
                json.put("real_surname", real_surname);
                json.put("email", email.trim().toLowerCase());
                json.put("password", password);

                try (OutputStream os = con.getOutputStream()) {
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = con.getResponseCode();

                activity.runOnUiThread(() -> {

                    if (code == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(activity, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity, inicio_sesion.class));
                        activity.finish();
                    }
                    else if (code == HttpURLConnection.HTTP_CONFLICT) {
                        Toast.makeText(activity, "El correo ya está registrado", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(activity, "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
                    }

                });

            } catch (Exception e) {
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Error de conexión", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    private int loginCodigo = 0;

    public int getLoginCodigo() {
        return loginCodigo;
    }

    public static int userId;
    public static String userName;
    public static String realName;
    public static String realSurname;
    public static String emailUser;

    public void loginUser(String email, String password){
        new Thread(() -> {
            HttpURLConnection con = null;
            try {
                URL url = new URL(BASE_URL + "users/login");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);

                try (OutputStream os = con.getOutputStream()){
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = con.getResponseCode();
                loginCodigo = code;

                if(code == HttpURLConnection.HTTP_OK){

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream())
                    );

                    StringBuilder response = new StringBuilder();
                    String line;

                    while((line = br.readLine()) != null){
                        response.append(line);
                    }

                    JSONObject userJson = new JSONObject(response.toString());

                    userId = userJson.getInt("id");
                    userName = userJson.getString("user_name");
                    realName = userJson.getString("real_name");
                    realSurname = userJson.getString("real_surname");
                    emailUser = userJson.getString("email");

                    Log.i("LOGIN","Usuario logeado: " + userName);
                }

            } catch (Exception e){
                e.printStackTrace();
                loginCodigo = -1;
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }
        }).start();
    }

    public void crearPost(int userId, String contenido, Uri imagenUri, Activity activity) {

        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "posts");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);
                con.setDoInput(true);

                JSONObject json = new JSONObject();
                json.put("user_id", userId);
                json.put("contenido", contenido);

                if (imagenUri != null) {
                    String imagenBase64 = convertirImagenBase64(imagenUri, activity);
                    if (imagenBase64 != null) {
                        json.put("imagen", imagenBase64);
                    }
                }

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                int responseCode = con.getResponseCode();
                Log.i("UPLOAD", "Código respuesta: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Publicación creada", Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                Log.e("UPLOAD", "Error al crear post", e);
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Error al subir post", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public String convertirImagenBase64(Uri imageUri, Context context) {
        try {
            InputStream is = context.getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            is.close();

            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);

        } catch (Exception e) {
            Log.e("CONVERT", "Error al convertir imagen", e);
        }

        return null;
    }

    public void obtenerPosts(int userId, PostAdapter adapter, Activity activity) {

        new Thread(() -> {

            HttpURLConnection con = null;

            try {

                URL url = new URL(BASE_URL + "posts");

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                Log.e("POSTS", response.toString());

                JSONArray array = new JSONArray(response.toString());

                ArrayList<Post> lista = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject obj = array.getJSONObject(i);

                    Post p = new Post();

                    p.setId(obj.getInt("id"));
                    p.setUser_id(obj.getInt("user_id"));
                    p.setContenido(obj.getString("contenido"));

                    if (!obj.isNull("imagen")) {
                        p.setImagen(obj.getString("imagen"));
                    }

                    if (obj.has("username")) {
                        p.setUsername(obj.getString("username"));
                    }

                    lista.add(p);
                }

                postsEnMemoria.clear();
                postsEnMemoria.addAll(lista);

                activity.runOnUiThread(() -> {
                    adapter.setPosts(lista);
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }


    public void deleteUser(int id, Activity activity){
        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "users/" + id);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Content-Type","application/json");

                int code = con.getResponseCode();

                activity.runOnUiThread(() -> {

                    if(code == HttpURLConnection.HTTP_OK){
                        Toast.makeText(activity,"Usuario eliminado correctamente",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(activity,"Error al eliminar usuario: "+code,Toast.LENGTH_LONG).show();
                    }

                });

            } catch (Exception e){
                e.printStackTrace();

                activity.runOnUiThread(() ->
                        Toast.makeText(activity,"Error de conexión",Toast.LENGTH_LONG).show()
                );

            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public void updateUser(int id, String userName, String realName,
                           String realSurname, String email, String password){

        new Thread(() -> {

            HttpURLConnection con = null;

            try {

                URL url = new URL(BASE_URL + "users/" + id);

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type","application/json; charset=UTF-8");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();

                json.put("user_name", userName);
                json.put("real_name", realName);
                json.put("real_surname", realSurname);
                json.put("email", email);
                json.put("password", password);

                try(OutputStream os = con.getOutputStream()){
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = con.getResponseCode();

                Log.i("APIREST","Código actualizar usuario: " + code);

                if(code == HttpURLConnection.HTTP_OK){
                    ApiRest.userName = userName;
                    ApiRest.realName = realName;
                    ApiRest.realSurname = realSurname;
                    ApiRest.emailUser = email;
                }

            } catch (Exception e){
                Log.e("APIREST","Error al actualizar usuario", e);
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public void crearMascota(int userId, String nombre, String tipo, Uri imagenUri, Activity activity) {

        new Thread(() -> {

            HttpURLConnection con = null;

            try {

                URL url = new URL(BASE_URL + "pets");

                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                con.setDoOutput(true);
                con.setDoInput(true);

                JSONObject json = new JSONObject();

                json.put("user_id", userId);
                json.put("name", nombre);
                json.put("type", tipo);

                if (imagenUri != null) {
                    String imagenBase64 = convertirImagenBase64(imagenUri, activity);
                    if (imagenBase64 != null) {
                        json.put("image", imagenBase64);
                    }
                }

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                int responseCode = con.getResponseCode();
                Log.e("MASCOTA", "Código respuesta: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Mascota añadida", Toast.LENGTH_SHORT).show()
                    );

                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) error.append(line);

                    Log.e("MASCOTA", "Error servidor: " + error.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();

    }

    public void obtenerMascotas(int userId, PetAdapter adapter, Activity activity) {

        new Thread(() -> {

            HttpURLConnection con = null;

            try {

                URL url = new URL(BASE_URL + "pets/user/" + userId);

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                JSONArray array = new JSONArray(response.toString());

                ArrayList<Pet> lista = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject obj = array.getJSONObject(i);

                    Pet m = new Pet();

                    m.setId(obj.getInt("id"));
                    m.setUser_id(obj.getInt("user_id"));
                    m.setNombre(obj.getString("name"));
                    m.setTipo(obj.getString("type"));

                    if (!obj.isNull("image")) {
                        m.setImagen(obj.getString("image"));
                    }

                    m.setFecha(obj.getString("date"));

                    lista.add(m);

                }

                activity.runOnUiThread(() -> {
                    adapter.setPets(lista);
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public void eliminarMascota(int petId, Activity activity) {

        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "pets/" + petId);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");

                int code = con.getResponseCode();
                Log.e("DELETE_PET", "Código: " + code);

                if (code == HttpURLConnection.HTTP_OK) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Mascota eliminada", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) error.append(line);

                    Log.e("DELETE_PET", "Error servidor: " + error.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public void actualizarMascota(Pet mascota, Activity activity) {

        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "pets/" + mascota.getId());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("name", mascota.getNombre());
                json.put("type", mascota.getTipo());
                json.put("image", mascota.getImagen());

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                int code = con.getResponseCode();
                Log.e("UPDATE_PET", "Código: " + code);

                if (code == HttpURLConnection.HTTP_OK) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Mascota actualizada", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) error.append(line);

                    Log.e("UPDATE_PET", "Error servidor: " + error.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

    public void obtenerVacunas(int petId, VaccineAdapter adapter, Activity activity) {

        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "vaccines/pet/" + petId);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) json.append(line);

                JSONArray arr = new JSONArray(json.toString());
                ArrayList<Vaccine> lista = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    Vaccine v = new Vaccine();
                    v.setId(o.getInt("id"));
                    v.setPetId(o.getInt("petId"));
                    v.setName(o.getString("name"));
                    v.setDateGiven(o.getString("dateGiven"));
                    v.setNextDate(o.getString("nextDate"));
                    v.setNotes(o.getString("notes"));

                    lista.add(v);
                }

                activity.runOnUiThread(() -> adapter.setVaccines(lista));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void crearVacuna(Vaccine v, Activity activity) {

        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "vaccines");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("petId", v.getPetId());
                json.put("name", v.getName());
                json.put("dateGiven", v.getDateGiven());
                json.put("nextDate", v.getNextDate());
                json.put("notes", v.getNotes());

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                con.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void actualizarVacuna(Vaccine v, Activity activity) {

        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "vaccines/" + v.getId());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("name", v.getName());
                json.put("dateGiven", v.getDateGiven());
                json.put("nextDate", v.getNextDate());
                json.put("notes", v.getNotes());

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                con.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void eliminarVacuna(int id, Activity activity) {

        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "vaccines/" + id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");

                con.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void editarPost(int id, String contenido, Activity activity) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "posts/" + id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("contenido", contenido);
                json.put("user_id", ApiRest.userId);

                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes());
                os.close();

                con.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void eliminarPost(int postId, Activity activity) {

        new Thread(() -> {
            HttpURLConnection con = null;

            try {
                URL url = new URL(BASE_URL + "posts/" + postId);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");

                int code = con.getResponseCode();

                activity.runOnUiThread(() -> {
                    if (code == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(activity, "Publicación eliminada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Error al eliminar post: " + code, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Error de conexión", Toast.LENGTH_SHORT).show()
                );
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }

        }).start();
    }

}

