package com.team9797.ToMAS.ui.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.loginactivity;

import java.util.regex.Pattern;


public class fixprofile extends Fragment {

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentManager = getFragmentManager();
        mainActivity = (MainActivity)getActivity();
        View root = inflater.inflate(R.layout.fragment_fixprofile, container, false);
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
        fix_belong.setText(mainActivity.preferences.getString("소속",""));
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
                        Fragment fixprofile = new fixprofile();



                        mainActivity.sp_editor.putString("birth",birth);
                        mainActivity.sp_editor.putString("소속",belong);
                        mainActivity.sp_editor.putString("계급",a_class);
                        mainActivity.sp_editor.putString("password",pw);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("birth",birth);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("소속",belong);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("계급",a_class);
                        mainActivity.db.collection("user").document(mainActivity.getUid()).update("password",pw);
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(pw);
                        Toast.makeText(mainActivity, "수정완료", Toast.LENGTH_SHORT).show();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fixprofile.this);
                        fragmentManager.popBackStack();
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


        return root;
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

}