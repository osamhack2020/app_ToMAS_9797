package com.team9797.ToMAS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9797.ToMAS.ui.home.market.belong_tree.BelongTreeDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
public class SignUpActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editphone;
    EditText editname;
    EditText editbelong;
    EditText editclass;
    EditText editarmynumber;
    EditText editbirth;
    private String email = "";
    private String password = "";
    private String armynumber ="";
    private String belong ="";
    private String armyclass ="";
    private String birth ="";
    private String name ="";
    private String phone ="";
    BelongTreeDialog tree_dialog;
    String belong_path;

    ProgressDialog customProgressDialog;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_form);
        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        editarmynumber=findViewById(R.id.edit_armynumber);
        editbelong=findViewById(R.id.edit_belong);
        editclass=findViewById(R.id.edit_class);
        editbirth=findViewById(R.id.edit_birth);
        editname=findViewById(R.id.edit_name);
        editphone=findViewById(R.id.edit_phone);

        customProgressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        editbelong.setFocusable(false);
        editbelong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tree_dialog = new BelongTreeDialog(SignUpActivity.this);
                tree_dialog.show(getSupportFragmentManager(), "소속트리");
                tree_dialog.setDialogResult(new BelongTreeDialog.tree_dialog_result() {
                    @Override
                    public void get_result(String result) {
                        belong_path = result;
                        editbelong.setText(getPath(result));
                    }
                });
            }
        });

// Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

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

    public void signUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        name=editname.getText().toString();
        phone=editphone.getText().toString();
        birth=editbirth.getText().toString();
        armyclass=editclass.getText().toString();
        armynumber=editarmynumber.getText().toString();
        belong=editbelong.getText().toString();

        if(isEmpty(email)&&isEmpty(password)&&isEmpty(name)&&isEmpty(phone)&&isEmpty(birth)&&isEmpty(armyclass)&&isEmpty(armynumber)&&isEmpty(belong)) {
            if (isValidEmail()) {
                if(isValidPasswd())
                createUser(email, password);
                else
                    Toast.makeText(SignUpActivity.this, "비밀번호는 6자이상 16자리이하 입니다. ", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(SignUpActivity.this, "이메일 형식을 따라야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SignUpActivity.this,"모든 칸을 기입해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
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

    private boolean isEmpty(String s){
        if(s.isEmpty()){
            return false;
        }
        else
            return true;
    }
    private void createUser(String email, String password) {
        customProgressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            updateuser();
                            customProgressDialog.dismiss();
                            Intent intent=new Intent(SignUpActivity.this, EmailVerify.class);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();

                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            customProgressDialog.dismiss();
                        }
                    }
                });

    }

    private void updateuser(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText edit_phone = findViewById(R.id.edit_phone);
        EditText edit_name = findViewById(R.id.edit_name);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_password);
        EditText edit_belong = findViewById(R.id.edit_belong);
        EditText edit_class = findViewById(R.id.edit_class);
        EditText edit_armynumber = findViewById(R.id.edit_armynumber);
        EditText edit_birth = findViewById(R.id.edit_birth);
        String phone = edit_phone.getText().toString();
        String name = edit_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String armyclass = edit_class.getText().toString();
        String armynumber = edit_armynumber.getText().toString();
        String birth = edit_birth.getText().toString();

        Map<String, Object> upload = new HashMap<>();
       upload.put("이름",name);
        upload.put("소속",belong_path);
        upload.put("권한","사용자");
        upload.put("군번",armynumber);
        upload.put("계급",armyclass);
        upload.put("point",5000);
        upload.put("phonenumber",phone);
        upload.put("password",password);
        upload.put("email",email);
        upload.put("birth",birth);

        //사용자이름, 시간 등등 추가해야 함.

        String tmp_path_list[] = belong_path.split("/");
        String tmp_path = belong_path.substring(0, belong_path.length() - tmp_path_list[tmp_path_list.length - 1].length());
        tmp_path += "부대원";
        Map<String, Object> belong_upload = new HashMap<>();
        belong_upload.put("name", name);
        db.collection(tmp_path).document(firebaseAuth.getCurrentUser().getUid()).set(belong_upload);

        db.collection("user").document(firebaseAuth.getCurrentUser().getUid())
                .set(upload)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                    }
                });


    }
}
