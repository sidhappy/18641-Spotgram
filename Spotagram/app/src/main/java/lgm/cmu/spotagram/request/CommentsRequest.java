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

import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.request.util.RequestUtil;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by yulei on 2015/12/1.
 */
public class CommentsRequest extends  BasicRequest {
    public final static String TAG = "CommentsRequest";
    public final static String SUB_URL = "GetCommentsServlet";

    private int mNoteId;

    public CommentsRequest(int noteId) {
        this.mNoteId = noteId;
    };

    private OnCommentsReadyListener mOnCommentsReadyListener;

    @Override
    protected String doInBackground(String... params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responce = "";
        try {
            HttpPost httpPost = new HttpPost(BASE_URL+SUB_URL);
            Log.e(TAG, BASE_URL + SUB_URL);
            List <NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_NOTE_ID, mNoteId+""));
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

    public void setOnCommentReadyListener(OnCommentsReadyListener lsn) {
        mOnCommentsReadyListener = lsn;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null || s.length() == 0) {
            mOnCommentsReadyListener.onCommentsReady(false, null);
        }
        List<Comment> comments = RequestUtil.parseCommentList(s);

        if (mOnCommentsReadyListener != null) {
            mOnCommentsReadyListener.onCommentsReady(mIsSuccess, comments);
        }
    }

    public interface OnCommentsReadyListener {
        void onCommentsReady(boolean isSuccess, List<Comment> comments);
    }
}
