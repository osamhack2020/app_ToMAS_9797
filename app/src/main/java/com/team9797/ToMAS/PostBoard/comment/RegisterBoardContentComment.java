package com.team9797.ToMAS.PostBoard.comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team9797.ToMAS.PostBoard.RegisterBoardContent;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.RenderPreview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import top.defaults.colorpicker.ColorPickerPopup;

// github.irshulx 에서 코드 인용.

public class RegisterBoardContentComment extends AppCompatActivity {
    Editor editor;
    FirebaseStorage storage;
    StorageReference storageRef;
    String path;
    String post_id;
    String title;
    Intent intent;
    EditText edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_board_content_comment);

        editor =  findViewById(R.id.editor);
        storage = FirebaseStorage.getInstance("gs://tomas-47250.appspot.com/");
        intent = getIntent();
        path = intent.getExtras().getString("path");
        post_id = intent.getExtras().getString("post_id");
        edit_title = findViewById(R.id.post_edit_title);
        setUpEditor();
    }

    private void setUpEditor() {
        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });


        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(RegisterBoardContentComment.this)
                        .initialColor(Color.RED) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                Toast.makeText(RegisterBoardContentComment.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                                editor.updateTextColor(colorHex(color));
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {

                            }
                        });


            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });
        //editor.dividerBackground=R.drawable.divider_background_dark;
        //editor.setFontFace(R.string.fontFamily__serif);
        Map<Integer, String> headingTypeface = getHeadingTypeface();
        Map<Integer, String> contentTypeface = getContentface();
        editor.setHeadingTypeface(headingTypeface);
        editor.setContentTypeface(contentTypeface);
        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);
        //editor.setNormalTextSize(10);
        // editor.setEditorTextColor("#FF3333");
        //editor.StartEditor();
        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, final String uuid) {
                Toast.makeText(RegisterBoardContentComment.this, uuid, Toast.LENGTH_LONG).show();
                /**
                 * TODO do your upload here from the bitmap received and all onImageUploadComplete(String url); to insert the result url to
                 * let the editor know the upload has completed
                 */
                // need to fix : 여기에서 사진을 바로 업로드 하면 안되지만, 어디다 파일 업로드를 넣어야 하는지 모르겠음. 여기서 바로 처리해주고 나중에 수정해야함.
                // 현재는 지금 여기서 firebase storage에 uuid를 이름으로가진 jpg파일에 저장함. 폴더는 "기본폴더/images/" 임
                //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                storageRef = storage.getReference().child("images/"+uuid+".jpg");

                final byte[] data = baos.toByteArray();

                UploadTask uploadTask = storageRef.putBytes(data);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            editor.onImageUploadComplete(downloadUri.toString(), uuid);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

                //editor.onImageUploadComplete("http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg", uuid);
            // editor.onImageUploadFailed(uuid);
        }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }

        });


        /**
         * rendering serialized content
         // */
        //  String serialized = "{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003etextline 1 a great time and I will branch office is closed on Sundays\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"H1\"],\"textSettings\":{\"textColor\":\"#c00000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003ethe only one that you have received the stream free and open minded person to discuss a business opportunity to discuss my background.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"it is a great weekend and we will have the same to me that the same a great time\"],\"contentStyles\":[\"BOLD\"],\"textSettings\":{\"textColor\":\"#FF0000\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eI have a place where I have a great time and I will branch manager state to boast a new job in a few weeks and we can host or domain to get to know.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"the stream of water in a few weeks and we can host in the stream free and no ippo\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#5E5E5E\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I can get it done today will online at location and I am not a big difference to me so that we are headed \\u003ca href\\u003d\\\"www.google.com\\\"\\u003ewww.google.com\\u003c/a\\u003e it was the only way I.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not a good day to get the latest version to blame it to the product the.\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"BOLDITALIC\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I can send me your email to you and I am not able a great time and consideration I have to do the needful.\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"INDENT\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eI will be a while ago to a great weekend a great time with the same.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}";
//        String serialized = "{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003e\\u003cspan style\\u003d\\\"color:#000000;\\\"\\u003e\\u003cspan style\\u003d\\\"color:#000000;\\\"\\u003eit is not available beyond that statue in a few days and then we could\\u003c/span\\u003e\\u003c/span\\u003e\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"H1\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"author-tag\"],\"macroSettings\":{\"data-author-name\":\"Alex Wong\",\"data-tag\":\"macro\",\"data-date\":\"12 July 2018\"},\"type\":\"macro\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is a free trial to get a great weekend a good day to you u can do that for.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I have to do the needful as early in life is not available beyond my imagination to be a good.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003e\\u003cb\\u003eit is not available in the next week or two and I have a place where I\\u003c/b\\u003e\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#006AFF\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not available in the next week to see you tomorrow morning to see you then.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not available in the next day delivery to you soon with it and.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}";
        // EditorContent des = editor.getContentDeserialized(serialized);
        // editor.render(des);

//        Intent intent = new Intent(getApplicationContext(), RenderTestActivity.class);
//        intent.putExtra("content", serialized);
//        startActivity(intent);


        /**
         * Rendering html
         */
        //render();
        //editor.render();  // this method must be called to start the editor
        //String text = "<h1 data-tag=\"input\" style=\"color:#c00000;\"><span style=\"color:#C00000;\">textline 1 a great time and I will branch office is closed on Sundays</span></h1><hr data-tag=\"hr\"/><p data-tag=\"input\" style=\"color:#000000;\">the only one that you have received the stream free and open minded person to discuss a business opportunity to discuss my background.</p><div data-tag=\"img\"><img src=\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\" /><p data-tag=\"img-sub\" style=\"color:#FF0000;\" class=\"editor-image-subtitle\"><b>it is a great weekend and we will have the same to me that the same a great time</b></p></div><p data-tag=\"input\" style=\"color:#000000;\">I have a place where I have a great time and I will branch manager state to boast a new job in a few weeks and we can host or domain to get to know.</p><div data-tag=\"img\"><img src=\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\" /><p data-tag=\"img-sub\" style=\"color:#5E5E5E;\" class=\"editor-image-subtitle\">the stream of water in a few weeks and we can host in the stream free and no ippo</p></div><p data-tag=\"input\" style=\"color:#000000;\">it is that I can get it done today will online at location and I am not a big difference to me so that we are headed <a href=\"www.google.com\">www.google.com</a> it was the only way I.</p><blockquote data-tag=\"input\" style=\"color:#000000;\">I have to do the negotiation and a half years old story and I am looking forward in a few days.</blockquote><p data-tag=\"input\" style=\"color:#000000;\">it is not a good day to get the latest version to blame it to the product the.</p><ol data-tag=\"ol\"><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">it is that I can send me your email to you and I am not able a great time and consideration I have to do the needful.</span></li><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">I have to do the needful and send to me and</span></li><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">I will be a while ago to a great weekend a great time with the same.</span></li></ol><p data-tag=\"input\" style=\"color:#000000;\">it was u can do to make an offer for a good day I u u have been working with a new job to the stream free and no.</p><p data-tag=\"input\" style=\"color:#000000;\">it was u disgraced our new home in time to get the chance I could not find a good idea for you have a great.</p><p data-tag=\"input\" style=\"color:#000000;\">I have to do a lot to do the same a great time and I have a great.</p><p data-tag=\"input\" style=\"color:#000000;\"></p>";
        //editor.render("<p>Hello man, whats up!</p>");
        //String text = "<p data-tag=\"input\" style=\"color:#000000;\">I have to do the needful and send to me and my husband is in a Apple has to offer a variety is not a.</p><p data-tag=\"input\" style=\"color:#000000;\">I have to go with you will be highly grateful if we can get the latest</p><blockquote data-tag=\"input\" style=\"color:#000000;\">I have to do the negotiation and a half years old story and I am looking forward in a few days.</blockquote><p data-tag=\"input\" style=\"color:#000000;\">I have to do the needful at your to the product and the other to a new job is going well and that the same old stuff and a half day city is the stream and a good idea to get onboard the stream.</p>";
        editor.render();
        findViewById(R.id.btnRender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //미리보기
                String text = editor.getContentAsSerialized();
                editor.getContentAsHTML();
                Intent intent = new Intent(RegisterBoardContentComment.this, RenderPreview.class);
                intent.putExtra("title", edit_title.getText().toString());
                intent.putExtra("SERIALIZED", text);
                startActivity(intent);
            }
        });

        findViewById(R.id.action_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RegisterBoardContentComment.this)
                        .setTitle("Exit Editor?")
                        .setMessage("Are you sure you want to exit the editor?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        /**
         * Since the endusers are typing the content, it's always considered good idea to backup the content every specific interval
         * to be safe.
         *
         private final long backupInterval = 10 * 1000;
         Timer timer = new Timer();
         timer.scheduleAtFixedRate(new TimerTask() {
        @Override public void run() {
        String text = editor.getContentAsSerialized();
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        preferences.putString(String.format("backup-{0}",  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date())), text);
        preferences.apply();
        }
        }, 0, backupInterval);

         */
    }
/*
    private View insertMacro() {
        View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
        Map<String, Object> map = new HashMap<>();
        map.put("author-name", "Alex Wong");
        map.put("date","12 July 2018");
        editor.insertMacro("author-tag",view, map);
        return view;
    }
*/
    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    public static void setGhost(Button button) {
        int radius = 4;
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setStroke(4, Color.WHITE);
        background.setCornerRadius(radius);
        button.setBackgroundDrawable(background);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                if (Build.VERSION.SDK_INT < 28) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    editor.insertImage(bitmap);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    editor.insertImage(bitmap);
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Editor?")
                .setMessage("Are you sure you want to exit the editor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setGhost((Button) findViewById(R.id.btnRender));
    }

    public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }

    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }

    public void comment_to_server(View view)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String string_html = editor.getContentAsHTML();
        title = edit_title.getText().toString();
            if(isVailid(title)){
                SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                Map<String, Object> post = new HashMap<>();
                post.put("html", string_html);
                post.put("title", title);
                post.put("timestamp", FieldValue.serverTimestamp());
                post.put("writer", preferences.getString("이름", ""));
                post.put("user_id", preferences.getString("user_id", ""));


                //test
                db.collection(path).document(post_id).update("num_comments", FieldValue.increment(1));
                db.collection( path + "/" + post_id + "/comments").document()
                        .set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                setResult(Activity.RESULT_OK);
                                finish();
                                Log.d("AAA", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AAA", "Error writing document", e);
                            }
                        });
                // need to fix finish되서 돌아갈 때 게시판 리스트 최신화하기.
            }
            else{
                Toast.makeText(RegisterBoardContentComment.this," 제목을 기입해주세요", Toast.LENGTH_SHORT).show();
            }


    }

    public boolean isVailid(String s){
        if(s.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
}