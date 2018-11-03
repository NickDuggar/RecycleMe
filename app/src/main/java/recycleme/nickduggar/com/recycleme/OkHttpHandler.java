package recycleme.nickduggar.com.recycleme;

import android.net.Uri;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandler implements Runnable {


    private String url ="https://southcentralus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/b09a8e35-0a9d-44db-8e24-a1d9215af07b/image?iterationId=cd9b0154-d2e4-4efb-ab2e-65269169fe83";
    private String projectId = "b09a8e35-0a9d-44db-8e24-a1d9215af07b";
    private String iterationId = "cd9b0154-d2e4-4efb-ab2e-65269169fe83";
    private String contentType = "application/octet-stream";
    private String predictionKey = "d8e80978ecb0463c8726626ab613ea77";


    // TODO: does microsoft want an octet stream or multipart/form-data? They aren't consistent in their docs!
    private static final String MEDIA_TYPE_OCTET_STREAM_STRING = "application/octet-stream"; // "multipart/form-data"?
    private static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse(MEDIA_TYPE_OCTET_STREAM_STRING);


    private Uri image; // stored as a jpg


    private final OkHttpClient client = new OkHttpClient();

    public OkHttpHandler(Uri imageToSend) {
        image = imageToSend;
    }

    public void run(){
        // code definitely copied from OkHttp3 Recipes. Not done editing yet :P
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "upload.jpg",
                        RequestBody.create(MEDIA_TYPE_OCTET_STREAM, new File(image.getPath()))) // should get the file from Uri
                .build();

        Request request = new Request.Builder()
                .header("Content-Type", MEDIA_TYPE_OCTET_STREAM_STRING)
                .header("Prediction-key", predictionKey)
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        } catch (Exception e) {
            // do almost nothing!
            e.printStackTrace();
        }
    }
}
