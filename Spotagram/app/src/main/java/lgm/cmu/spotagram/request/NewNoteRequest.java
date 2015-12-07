package lgm.cmu.spotagram.request;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lgm.cmu.spotagram.request.util.RequestUtil;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by dawang on 12/3/15.
 */
public class NewNoteRequest extends BasicRequest{
    public final static String TAG = "NewNoteRequest";
    public final static String SUB_URL = "PostNoteServlet";

    private int mUserId;
    private String mUserName;
    private String mContent;
    private double mloc_long;
    private double mloc_lat;
    private int mType;

    public NewNoteRequest(int mUserId, String mUserName, String mContent, double lat, double lon) {
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mContent = mContent;
        this.mloc_lat = lat;
        this.mloc_long = lon;
        this.mType = 1;
    }

    private OnNewNoteListener mOnNewNoteListener;

    @Override
    protected String doInBackground(String... params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responce = "";
        try {
            HttpPost httpPost = new HttpPost(BASE_URL+SUB_URL);
            Log.e(TAG, BASE_URL + SUB_URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_USER_ID, mUserId+""));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_LOC_LATITUDE, mloc_lat+""));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_LOC_LONGITUDE, mloc_long+""));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_CONTENT, mContent));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_USERNAME, mUserName));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_TYPE, mType+""));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                Log.e(TAG, "code: " + response.getStatusLine().getStatusCode()+"");
                if (response.getStatusLine().getStatusCode() == 200) {
                    mIsSuccess = true;
                }
                //System.out.println(response.getStatusLine());
                HttpEntity entity2 = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                responce = bufferedReader.readLine();
                bufferedReader.close();

                return responce;
            } finally {
                response.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }	finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return responce;
    }

    public void setOnNewNoteListener(OnNewNoteListener lsn) {
        mOnNewNoteListener = lsn;
    }

    @Override
    protected void onPostExecute(String s) {
        // s json file content
        if (s == null || s.length() == 0) {
            mOnNewNoteListener.onNewNote(false, -1);
        }

        int commentId = RequestUtil.parseNewNoteReplyResult(s);

        if (commentId == -1) {
            mOnNewNoteListener.onNewNote(false, -1);
        } else {
            mOnNewNoteListener.onNewNote(mIsSuccess, commentId);
        }

    }

    public interface OnNewNoteListener {
        void onNewNote(boolean isSuccess, int noteId);
    }
}
