package com.team9797.ToMAS.ui.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.ProgressDialog;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;

public class MypageFragment extends Fragment {

    MainActivity mainActivity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseStorage storage;
    ImageView profileimg_mypg;
    TextView point_textView;
    TextView buy_textView;
    ProgressDialog customProgressDialog; //로딩창


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_mypage, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

//로딩창 객체 생성
        customProgressDialog = new ProgressDialog(mainActivity);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show();

        // get views
        Button btn_logout = root.findViewById(R.id.btn_logout);
        point_textView = root.findViewById(R.id.mypage_option3);
        buy_textView = root.findViewById(R.id.mypage_option2);


        TextView name=root.findViewById(R.id.mypage_name);
        TextView belong=root.findViewById(R.id.mypage_regiment);
        name.setText("이름: " +mainActivity.preferences.getString("이름",""));
        belong.setText("소속: "+ getPath(mainActivity.preferences.getString("소속","")));



        profileimg_mypg=root.findViewById(R.id.profileimg_mypg);
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef =storage.getReference();
        StorageReference profileRef=storageRef.child("profiles/"+mainActivity.getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        profileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profileimg_mypg.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.profile_image);
                profileimg_mypg.setImageBitmap(bmp);
            }
        });

        mainActivity.db.collection("user").document(mainActivity.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {
                   DocumentSnapshot document = task.getResult();
                   if (document.exists()) {
                        point_textView.setText("현재 포인트 : " + Integer.toString(document.get("point", Integer.class)));
                   }
               }
           }
       });

        point_textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new point_record();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        buy_textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new buy_list();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });


        btn_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.sp_editor.clear();
                mainActivity.sp_editor.commit();

                FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent=new Intent(mainActivity,loginactivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Button profilefix=root.findViewById(R.id.button_profilefix);
        profilefix.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new fixprofile();


                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });

        customProgressDialog.dismiss();
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


    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }

}