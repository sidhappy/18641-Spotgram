package lgm.cmu.spotagram.request;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import lgm.cmu.spotagram.request.util.RequestUtil;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by MiaojunLi on 15/12/4.
 */
public class UploadProfileRequest extends BasicRequest {
    public final static String TAG = "UploadProfileRequest";
    public final static String SUB_URL = "UploadProfileServlet";
    public static String imageName ;
    public static String imagePath ;

    private int mUserId;

    public UploadProfileRequest(int mUserId,String imageName,String imagePath) {
        this.mUserId = mUserId;
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
            if (file.exists()) {
                System.out.println("exist");
            }

            builder.addPart("profile", new FileBody(new File(imagePath)));
            builder.addPart(ConstantValue.KEY_USER_ID, new StringBody(mUserId + "", ContentType.TEXT_PLAIN));
            builder.addPart(ConstantValue.KEY_FILE_NAME, new StringBody(imageName, ContentType.TEXT_PLAIN));

            HttpEntity httpEntity = builder.build();

            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                System.out.println(response.getStatusLine());
                HttpEntity entity2 = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                responce=bufferedReader.readLine();
                bufferedReader.close();
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

        int commentId = RequestUtil.parseNoteReplyResult(s);
        if (commentId == -1) {
            mOnPhotoUpdateListener.onPhotoReplied(false, -1);
        } else {
            mOnPhotoUpdateListener.onPhotoReplied(mIsSuccess, commentId);
        }

    }

    public interface OnPhotouploadListener {
        void onPhotoReplied(boolean isSuccess, int commentId);
    }
}
