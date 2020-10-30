package com.team9797.ToMAS.PostBoard.comment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> implements Html.ImageGetter {
    public ArrayList<CommentListItem> mDataset;
    String path;
    Context context;
    String user_id;
    String post_id;
    String original_writer_id;

    public CommentListAdapter(String tmp_path, String tmp_post_id, Context tmp_context, String uid, String tmp_original_writer_id)
    {
        mDataset = new ArrayList<>();
        path = tmp_path;
        post_id = tmp_post_id;
        context = tmp_context;
        user_id = uid;
        original_writer_id = tmp_original_writer_id;

    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = context.getDrawable(R.drawable.ic_arrow_forward_black_24dp);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new CommentListAdapter.LoadImage().execute(source, d);

        return d;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView item_title;
        TextView item_writer;
        TextView item_date;
        TextView item_html;
        Button btn_fix;
        Button btn_delete;

        public MyViewHolder(View view) {
            super(view);
            item_title = (TextView) view.findViewById(R.id.comment_list_item_title);
            item_writer = (TextView) view.findViewById(R.id.comment_list_item_writer);
            item_html = (TextView) view.findViewById(R.id.comment_list_item_html);
            item_date = (TextView) view.findViewById(R.id.comment_list_item_date);
            btn_fix = view.findViewById(R.id.btn_comment_update);
            btn_delete = view.findViewById(R.id.btn_comment_delete);
        }
    }

    @Override
    public CommentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.comment_list_item, parent, false) ;
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item_title.setText("제목 : " + mDataset.get(position).getTitle());
        holder.item_writer.setText("작성자 : " + mDataset.get(position).getWriter());
        holder.item_html.setText(Html.fromHtml(mDataset.get(position).getHtml(), CommentListAdapter.this, null));
        holder.item_date.setText("작성일 : " + mDataset.get(position).getDate());

        if (user_id.equals(mDataset.get(position).getWriterId()))
        {
            holder.btn_fix.setVisibility(View.VISIBLE);
            holder.btn_fix.setEnabled(true);
            holder.btn_delete.setVisibility(View.VISIBLE);
            holder.btn_delete.setEnabled(true);
            holder.btn_fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection(path + "/" + post_id + "/comments").document(mDataset.get(position).getId()).delete();
                    FirebaseFirestore.getInstance().collection(path).document(post_id).update("num_comments", FieldValue.increment(-1));
                    mDataset.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDataset.size());
                }
            });
        }
        else
        {
            if (user_id.equals(original_writer_id))
            {
                holder.btn_delete.setVisibility(View.VISIBLE);
                holder.btn_delete.setEnabled(true);
                holder.btn_delete.setText("채택");
                holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore.getInstance().collection("user").document(mDataset.get(position).getWriterId()).update("point", FieldValue.increment(500));
                        Map<String, Object> post = new HashMap<>();
                        post.put("title", "댓글 채택 : " + mDataset.get(position).getTitle());
                        post.put("timestamp", FieldValue.serverTimestamp());
                        post.put("change", "+500");


                        FirebaseFirestore.getInstance().collection("user").document(mDataset.get(position).getWriterId()).collection("point_record").document().set(post)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d("AAA", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w("AAA", "Error writing document", e);
                                    }
                                });
                    }
                });
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add_item(String title, String writer, String date, String html, String post_id, String writer_id)
    {
        CommentListItem item = new CommentListItem();

        item.setTitle(title);
        item.setWriter(writer);
        item.setDate(date);
        item.setHtml(html);
        item.setId(post_id);
        item.setWriterId(writer_id);

        mDataset.add(item);
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // need to fix : refresh하기 - 이미지 안나옴. 이거 구조를 바꾸던가 해야할 듯.
                notifyDataSetChanged();
            }
        }
    }

}
