package com.team9797.ToMAS.ui.mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.ProgressDialog;
import com.team9797.ToMAS.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class FixProfile extends Fragment {


    MainActivity mainActivity;
    public FirebaseFirestore db;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");
    EditText fix_name;
    EditText fix_birth;
    EditText fix_anumber;
    EditText fix_belong;
    EditText fix_email;
    EditText fix_pw;
    EditText fix_ph;
    EditText fix_class;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseStorage storage;
    ProgressDialog customProgressDialog; //로딩창
    private static final int PICK_IMAGE = 1;
    ImageView profileimage;
    Bitmap img;
    private Uri mImageCaptureUri;
    private String absoultePath;//https://jeongchul.tistory.com/287 사진붙이기출처
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storage = FirebaseStorage.getInstance();
        fragmentManager = getFragmentManager();
        mainActivity = (MainActivity)getActivity();
        View root = inflater.inflate(R.layout.fragment_fixprofile, container, false);

        customProgressDialog = new ProgressDialog(mainActivity);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        profileimage=root.findViewById(R.id.profileimage);
        StorageReference storageRef =storage.getReference();
        StorageReference profileRef=storageRef.child("profiles/"+mainActivity.getUid());

        final long ONE_MEGABYTE = 1024 * 1024;
        profileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                img=bmp;
                profileimage.setImageBitmap(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                img = BitmapFactory.decodeResource(getResources(), R.drawable.profile_image);
                profileimage.setImageBitmap(img);

            }
        });
        fix_name= root.findViewById(R.id.fix_name);
        fix_birth=root.findViewById(R.id.fix_birth);
        fix_anumber=root.findViewById(R.id.fix_anumber);
        fix_belong=root.findViewById(R.id.fix_belong);
        fix_email=root.findViewById(R.id.fix_email);
        fix_pw=root.findViewById(R.id.fix_pw);
        fix_ph=root.findViewById(R.id.fix_ph);
        fix_class=root.findViewById(R.id.fix_class);
        Button button_fix=root.findViewById(R.id.button_fix);


        fix_name.setText(mainActivity.preferences.getString("이름",""));
        fix_birth.setText(mainActivity.preferences.getString("birth",""));
        fix_anumber.setText(mainActivity.preferences.getString("군번",""));
        fix_belong.setText(getPath(mainActivity.preferences.getString("소속","")));
        fix_email.setText(mainActivity.preferences.getString("email",""));
        fix_pw.setText(mainActivity.preferences.getString("password",""));
        fix_ph.setText(mainActivity.preferences.getString("phonenumber",""));
        fix_class.setText(mainActivity.preferences.getString("계급",""));




        button_fix.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=fix_name.getText().toString();
                String birth=fix_birth.getText().toString();
                String anumber=fix_anumber.getText().toString();
                String belong=fix_belong.getText().toString();
                String email=fix_email.getText().toString();
                String pw=fix_pw.getText().toString();
                String ph=fix_ph.getText().toString();
                String a_class=fix_class.getText().toString();
                if(checkEmpty(name)&&checkEmpty(birth)&&checkEmpty(anumber)&&checkEmpty(belong)&&checkEmpty(email)&&checkEmpty(pw)&&checkEmpty(ph)&&checkEmpty(a_class)){
                    if(isValidPasswd(pw)){
                        Fragment fixprofile = new FixProfile();
                        customProgressDialog.show();


                        mainActivity.sp_editor.putString("birth",birth);
                        mainActivity.sp_editor.putString("phonenumber",ph);
                        mainActivity.sp_editor.putString("계급",a_class);
                        mainActivity.sp_editor.putString("password",pw);
                        mainActivity.sp_editor.commit();
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("birth",birth);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("phonenumber",ph);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("계급",a_class);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("password",pw);
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(pw);
                        //이미지 업로드 파트
                        ByteArrayOutputStream baos =new ByteArrayOutputStream();
                        img.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte[] data =baos.toByteArray();

                        UploadTask uploadTask =profileRef.putBytes(data);
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(mainActivity, "수정실패", Toast.LENGTH_SHORT).show();

                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(FixProfile.this);
                                fragmentManager.popBackStack();
                                customProgressDialog.dismiss();
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle successful uploads on complete
                                // ...
                                Toast.makeText(mainActivity, "수정완료", Toast.LENGTH_SHORT).show();

                                customProgressDialog.dismiss();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(FixProfile.this);
                                fragmentManager.popBackStack();
                            }
                        });



                    }
                    else{
                        Toast.makeText(mainActivity, "비밀번호는 6자리이상 16자리이하입니다.", Toast.LENGTH_SHORT).show();

                    }
                }
                else{

                    Toast.makeText(mainActivity, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        profileimage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };


                new AlertDialog.Builder(mainActivity)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }

        });


        return root;
    }

    public String getPath(String str)
    {
        String stringed_path = "";
        String[] tmp = str.split("/");
        for (int i = 1; i<tmp.length; i++)
        {
            // document를 만들기 위해 tmp로 사용했던 path를 무시한다.
            if (i%2 == 0)
                stringed_path += tmp[i] + " ";
        }
        return stringed_path;
    }


    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);

    }
    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK)
            return;
        switch(requestCode)
        {
            case PICK_IMAGE:
            {
                try{
                    InputStream in = mainActivity.getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);        //https://lktprogrammer.tistory.com/188 이미지처리
                    in.close();
                    profileimage.setImageBitmap(img);
                }catch(Exception e)
                {

                }



            }

        }
    }




    private boolean isValidPasswd(String password) {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    public boolean checkEmpty(String s){    //뭐가 채워져있으면 true 반환
        if(!s.isEmpty())
            return true;
        else
            return false;

    }
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // firebase 에 저장하기

}

}