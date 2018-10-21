package recycleme.nickduggar.com.recycleme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class IdentifyActivity extends AppCompatActivity {

    ImageView image;
    Button another, upload;
    int PICK_IMAGE_REQUEST = 111;
    String URL ="https://southcentralus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/b09a8e35-0a9d-44db-8e24-a1d9215af07b/image?iterationId=cd9b0154-d2e4-4efb-ab2e-65269169fe83";
    Bitmap bitmap;
    RequestQueue rQueue;
    Uri imageLoc;


    static String response; // this will hold the response we get from the server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_identify);

        image = findViewById(R.id.image);
        upload = findViewById(R.id.button_identify);

        Intent intent = this.getIntent();
        imageLoc = intent.getParcelableExtra("uri");
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageLoc);
        } catch (Exception e) {
            e.printStackTrace();
        }


        rQueue = Volley.newRequestQueue(this);
    }

    public void identifyButtonPressed(View view) {

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageBytes = baos.toByteArray(); // this is the octet-stream for the request
        //final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                IdentifyActivity.response = response;
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public byte[] getBody() {
                return imageBytes;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                    IdentifyActivity.response = responseString;
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Prediction-key", "d8e80978ecb0463c8726626ab613ea77");
                params.put("Content-type", "application/octet-stream");

                return params;
            }
        };

        rQueue.add(request);
    }



//    final TextView mTextView = (TextView) findViewById(R.id.text);
//
//    // Instantiate the RequestQueue.
//    RequestQueue queue = Volley.newRequestQueue(this);
//    String url ="http://www.google.com";
//
//    // Request a string response from the provided URL.
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    // Display the first 500 characters of the response string.
//                    mTextView.setText("Response is: "+ response.substring(0,500));
//                }
//            }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            mTextView.setText("That didn't work!");
//        }
//    });
//
//    // Add the request to the RequestQueue.
//    queue.add(stringRequest);
}
