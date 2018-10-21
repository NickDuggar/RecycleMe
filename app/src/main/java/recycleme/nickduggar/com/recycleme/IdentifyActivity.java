package recycleme.nickduggar.com.recycleme;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class IdentifyActivity extends AppCompatActivity {

    ImageView image;
    Button another, upload;
    int PICK_IMAGE_REQUEST = 111;
    String URL ="http://192.168.1.101/JavaRESTfullWS/DemoService/upload";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.image);
        another = findViewById(R.id.button_another);
        upload = findViewById(R.id.button_identify);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void anotherButtonClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void identifyButtonPressed(View view) {
        progressDialog = new ProgressDialog(IdentifyActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if(s.equals("true")){
                    Toast.makeText(IdentifyActivity.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(IdentifyActivity.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(IdentifyActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                return parameters;
            }
        };

        rQueue = Volley.newRequestQueue(IdentifyActivity.this);
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
