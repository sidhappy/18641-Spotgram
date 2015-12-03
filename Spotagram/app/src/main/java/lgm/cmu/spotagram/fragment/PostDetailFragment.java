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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.adapter.CommentListAdapter;
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.ReplyNoteRequest;

/**
 * Created by yulei on 2015/12/1.
 */
public class PostDetailFragment extends Fragment {
    private ImageView mProfileImage;
    private TextView mNameTextView;
    private TextView mInfoTextView;
    private TextView mDateTextView;
    private EditText mEditText;
    private Button mButton;
    private ListView mListView;

    private CommentListAdapter mAdapter;
    private List<Comment> mComments;
    private Note mNote;

    public PostDetailFragment() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private SimpleDateFormat mSimpleDateFormat;

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
        mDateTextView = (TextView) view.findViewById(R.id.textview_date);
        mListView = (ListView) view.findViewById(R.id.listview_comments);

        mEditText = (EditText) view.findViewById(R.id.edittext_comment);
        mButton = (Button) view.findViewById(R.id.button_comment);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReplyNote();
            }
        });

        return view;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        mAdapter = new CommentListAdapter(getActivity(), comments);
        mListView.setAdapter(mAdapter);
    }

    private void setNoteInfo(String name, String info, Date date) {
        mNameTextView.setText(name);
        mInfoTextView.setText(info);
        mDateTextView.setText(mSimpleDateFormat.format(date));
    }

    private void setUserProfile(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, mProfileImage);
    }

    public void setNoteInfo(Note note) {
        mNote = note;
        setNoteInfo(note.getUsername(), note.getContent(), note.getDate());
        setUserProfile(note.getUserURL());
    }

    public void requestReplyNote() {
        String content = mEditText.getText().toString();
        if (mNote != null && content != null && content.length() != 0) {
            ReplyNoteRequest request = new ReplyNoteRequest(mNote.getUserid(), mNote.getId(), mNote.getUsername(), content);

            // in this part, we should get the device owner's userId and name
            final Comment comment = new Comment(null, content, mNote.getUserid(), mNote.getUsername(), mNote.getId());

            request.setOnCommentReadyListener(new ReplyNoteRequest.OnNoteReplyListener() {
                @Override
                public void onNoteReplied(boolean isSuccess, int commentId) {
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "Post success", Toast.LENGTH_SHORT).show();
                        mComments.add(comment);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Internet err", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            request.execute();
        } else {
            Toast.makeText(getActivity(), "Note is null or no input", Toast.LENGTH_SHORT).show();
        }

    }
}
