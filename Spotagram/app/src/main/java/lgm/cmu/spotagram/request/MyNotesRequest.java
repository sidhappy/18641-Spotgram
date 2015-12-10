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

import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.request.util.RequestUtil;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by MiaojunLi on 15/12/5.
 */
public class MyNotesRequest extends  BasicRequest{
    public final static String TAG = "GetMyNotesRequest";
    public final static String SUB_URL = "GetUserNotesServlet";

    private int UserID;

    private OnMyNotesReadyListener mOnMyNotesReadyListener;

    public MyNotesRequest(int userID) {
        this.UserID=userID;
    }



    @Override
    protected String doInBackground(String... params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responce = "";
        try {
            HttpPost httpPost = new HttpPost(BASE_URL+SUB_URL);
            Log.e(TAG, BASE_URL + SUB_URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_USER_ID, ""+UserID));

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

    public void setOnMyNotesReadyListener(OnMyNotesReadyListener lsn) {
        mOnMyNotesReadyListener = lsn;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null || s.length() == 0) {
            mOnMyNotesReadyListener.onNoteReady(false, null);
        }
        List<Note> notes = RequestUtil.parseNoteList(s);

        if (mOnMyNotesReadyListener != null) {
            mOnMyNotesReadyListener.onNoteReady(mIsSuccess, notes);
        }
    }

    public interface OnMyNotesReadyListener {
        void onNoteReady(boolean isSuccess, List<Note> notes);
    }

}
