package lgm.cmu.spotagram.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.CommentListAdapter;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.model2.Note;

/**
 * Created by yulei on 2015/12/1.
 */
public class PostDetailFragment extends Fragment {
    private ImageView mProfileImage;
    private TextView mNameTextView;
    private TextView mInfoTextView;

    ListView mListView;
    private CommentListAdapter mAdapter;

    private View mContainer;
    private Boolean mInitialCreate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mProfileImage = (ImageView) view.findViewById(R.id.imageview_user_profile);
        mNameTextView = (TextView) view.findViewById(R.id.textview_user_name);
        mInfoTextView = (TextView) view.findViewById(R.id.textview_note_info);
        mListView = (ListView) view.findViewById(R.id.listview_comments);

        return view;
    }

    public void setComments(List<Comment> comments) {
        mAdapter = new CommentListAdapter(getActivity(), comments);
        mListView.setAdapter(mAdapter);
    }

    public void setNoteInfo(String name, String info) {
        mNameTextView.setText(name);
        mInfoTextView.setText(info);
    }

    public void setUserProfile(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, mProfileImage);
    }

    public void setNoteInfo(Note note) {
        setNoteInfo(note.getUsername(), note.getContent());
        setUserProfile(note.getUserURL());
    }
}
