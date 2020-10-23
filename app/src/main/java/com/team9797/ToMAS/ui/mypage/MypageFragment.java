package com.team9797.ToMAS.ui.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;

public class MypageFragment extends Fragment {

    MainActivity mainActivity;
    LinearLayout profile_container;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseStorage storage;
    ImageView profileimg_mypg;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("test","온크리에트실해중");
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);

        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();

        profile_container = root.findViewById(R.id.profile_container);
        Button btn_logout = root.findViewById(R.id.btn_logout);


        TextView name=root.findViewById(R.id.mypage_name);
        TextView belong=root.findViewById(R.id.mypage_regiment);
        name.setText("이름: " +mainActivity.preferences.getString("이름",""));
        belong.setText("소속: "+mainActivity.preferences.getString("소속",""));

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

        profile_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment change_fragment = new fixprofile();


                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
            }
        });



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.set_title();
    }

}