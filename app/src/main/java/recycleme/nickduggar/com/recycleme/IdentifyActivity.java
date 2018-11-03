package recycleme.nickduggar.com.recycleme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import static recycleme.nickduggar.com.recycleme.MainActivity.bmp;

public class IdentifyActivity extends AppCompatActivity {

    ImageView image;
    Button another, upload;

    Bitmap bitmap;
    Uri imageLoc;


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
            //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageLoc);
            image.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void identifyButtonPressed(View view) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageBytes = baos.toByteArray(); // this is the octet-stream for the request
    }

    // // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)

//        public void identifyButtonPressed(View view) {
//            HttpClient httpclient = HttpClients.createDefault();
//
//            try {
//                URIBuilder builder = new URIBuilder("https://southcentralus.api.cognitive.microsoft.com/customvision/v1.1/Prediction/{projectId}/image");
//
//                builder.setParameter("iterationId", "4");
//                builder.setParameter("application", "{string}");
//
//                URI uri = builder.build();
//                HttpPost request = new HttpPost(uri);
//                request.setHeader("Content-Type", "application/octet-stream");
//                request.setHeader("Prediction-key", "d8e80978ecb0463c8726626ab613ea77");
//
//
//                // Request body
//                StringEntity reqEntity = new StringEntity("{body}");
//                request.setEntity(reqEntity);
//
//                org.apache.http.HttpResponse response = httpclient.execute(request);
//                HttpEntity entity = response.getEntity();
//
//                if (entity != null) {
//                    System.out.println(EntityUtils.toString(entity));
//                }
//            }
//            catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
}
