package lgm.cmu.spotagram.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.model2.Note;

/**
 * Created by yulei on 2015/12/1.
 */
public class CommentListAdapter extends BaseAdapter {
    private List<Comment> mComments;
    private Context mContext;

    public CommentListAdapter(Context context, List<Comment> comments) {
        mComments = comments;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
            holder = new ViewHolder();

            holder.mUserNameTextView = (TextView) convertView.findViewById(R.id.textview_comment_user_name);
            holder.mCommentInfoTextView = (TextView) convertView.findViewById(R.id.textview_comment_info);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = mComments.get(position);
        holder.mUserNameTextView.setText(comment.getUsername());
        holder.mCommentInfoTextView.setText(comment.getContent());

        return convertView;
    }

    public class ViewHolder {
        public TextView mUserNameTextView;
        public TextView mCommentInfoTextView;
    }
}
