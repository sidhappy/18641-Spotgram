package lgm.cmu.spotagram.request;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by dawang on 12/7/15.
 */
public class UploadNoteImageRequest extends BasicRequest{
    public final static String TAG = "UploadNewPhotoServlet";
    public final static String SUB_URL = "UploadNewPhotoServlet";
    public static String imageName ;
    public static String imagePath ;

    private int mNoteId;

    public UploadNoteImageRequest(int noteId,String imageName,String imagePath) {
        this.mNoteId = noteId;
        this.imageName=imageName;
        this.imagePath=imagePath;

    }

    private OnPhotouploadListener mOnPhotoUpdateListener;

    @Override
    protected String doInBackground(String... params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responce = "";
        try {

            HttpPost httpPost = new HttpPost(BASE_URL + SUB_URL);
            Log.e(TAG, BASE_URL + SUB_URL);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            File file = new File(imagePath);


            builder.addPart(ConstantValue.KEY_NOTE_PHOTO, new FileBody(new File(imagePath)));
            builder.addPart(ConstantValue.KEY_NOTE_ID, new StringBody(mNoteId + "", ContentType.TEXT_PLAIN));
            builder.addPart(ConstantValue.KEY_FILE_NAME, new StringBody(imageName, ContentType.TEXT_PLAIN));

            HttpEntity httpEntity = builder.build();

            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
//                Log.v(response.getStatusLine().toString(),null);
                HttpEntity entity2 = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                responce=bufferedReader.readLine();
                bufferedReader.close();
                return responce;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return responce;
    }

    public void setOnPhotoUpdateListener(OnPhotouploadListener lsn) {
        mOnPhotoUpdateListener = lsn;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null || s.length() == 0) {
            mOnPhotoUpdateListener.onPhotoReplied(false, -1);
        }

        String upload_status=s;
        Log.e(s,"From upload profile");
        if (!upload_status.equals("{\"result\":0}")) {
            mOnPhotoUpdateListener.onPhotoReplied(false, -1);
        } else {
            mOnPhotoUpdateListener.onPhotoReplied(true, 0);
        }

    }

    public interface OnPhotouploadListener {
        void onPhotoReplied(boolean isSuccess, int statusId);
    }

}
