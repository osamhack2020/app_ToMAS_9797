package com.team9797.ToMAS;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;
public class loginactivity extends AppCompatActivity{
    ImageView imageView;
    TextView textView;
    private FirebaseAuth firebaseAuth;
    EditText signEmail;
    EditText signPassword;
    private String email = "";
    private String password = "";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");
    ProgressDialog customProgressDialog;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_form);
        imageView = findViewById(R.id.profileimage);
        firebaseAuth = FirebaseAuth.getInstance();
                if(firebaseAuth.getUid()!=null){
                    Intent intent=new Intent(loginactivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


        signEmail = findViewById(R.id.et_signemail);
        signPassword = findViewById(R.id.et_signpassword);

        Button buttonsign = (Button) findViewById(R.id.signup) ;
        buttonsign.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(loginactivity.this,signupactivity.class);
                startActivity(intent);



            }
        }) ;


        customProgressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }



    public void signIn(View view) {
        email = signEmail.getText().toString();
        password = signPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }
    //password 재설정 메일 보내기
    public void repw(View view) {
        email = signEmail.getText().toString();
        if(isValidEmail()) {
            firebaseAuth.sendPasswordResetEmail(email);
            Toast.makeText(loginactivity.this, R.string.repw, Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(loginactivity.this,"이메일 칸을 기입해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(loginactivity.this,"이메일 형식을 따라야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(loginactivity.this,"비밀번호 칸을 기입해주세요 ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(loginactivity.this,"비밀번호는 6자이상 16자리이하 입니다. ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private void loginUser(String email, String password)
    {
        customProgressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                           //Toast.makeText(loginactivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();


                            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                customProgressDialog.dismiss();
                                Intent intent = new Intent(loginactivity.this, MainActivity.class);
                                // activiy 스택 삭제
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            else{
                                Intent intent = new Intent(loginactivity.this, email_verify.class);

                                startActivity(intent);


                            }
                        } else {
                            // 로그인 실패
                            Toast.makeText(loginactivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
