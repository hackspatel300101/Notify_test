package com.example.notify_test;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {

    Button btn;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAV1CqY6k:APA91bFuBn-4l7ce7QI9uVdd70ZMPcCrKDsN_b620lGcir5YbMug_Fn-hEziLp1WRtuEBRbdZzVT4AQvtUYesNFNUPLdoaJxg7R3JcrA9W7oIAQ4D-Kl4mjAT19iXUFZJpbQnWXO9i5R";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    RequestQueue requestQueue;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.btn);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/Group"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "hello";
                NOTIFICATION_MESSAGE = "my msg";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("body", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("notification", notifcationBody);
                    Log.d("body", " "+notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                sendNotification(notification);
            }
        });

    }
    private void sendNotification(JSONObject notification) {
        Log.d("function", "sendNotification: "+notification);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                Log.d("params", "getHeaders: "+params);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }


}
