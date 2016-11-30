package com.gecaj.arianit.myapplication.effect;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Effect extends AppCompatActivity {
    EditText name,pw;
    TextView viewResponse;
    String server_url = "http://192.168.178.64/php/json_db.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        name = (EditText)findViewById(R.id.editText2);
        pw = (EditText)findViewById(R.id.editText3);
        viewResponse = (TextView)findViewById(R.id.textView);
    }

    public void gibIhm(final View view){
        final String name,pw;
        name = this.name.getText().toString();
        pw = this.pw.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewResponse.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //key same as param name in php!
                params.put("color1",name);
                params.put("color2",pw);
                return params;
            }
        };

        MySingleton.getInstance(Effect.this).addToRequestQue(stringRequest);

/*WORKING!
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("obj1");
                            jsonObject = jsonArray.getJSONObject(0);
                            viewResponse.setText(jsonObject.getString("propertyA")+"\n"+jsonObject.getString("propertyB"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //viewResponse.setText(response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Effect.this,"Error...",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //key same as param name in php!
                params.put("name",name);
                params.put("email",pw);
                return params;
            }
        };

        MySingleton.getInstance(Effect.this).addToRequestQue(stringRequest);
*/


        /*
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("name",name);
        params.put("email",pw);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,server_url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            viewResponse.setText(response.getString("propertyA")+"\n"+response.getString("propertyB"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MySingleton.getInstance(Effect.this).addToRequestQue(jsonObjectRequest);
*/
/*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            viewResponse.setText(response.getString("propertyA")+"\n"+response.getString("propertyB"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Effect.this,"Error...",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        MySingleton.getInstance(Effect.this).addToRequestQue(jsonObjectRequest);
*/

    }
}
