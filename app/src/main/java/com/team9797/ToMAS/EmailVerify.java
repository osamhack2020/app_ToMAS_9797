package com.team9797.ToMAS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerify extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        TextView showemail=findViewById(R.id.showemail);
        firebaseAuth = FirebaseAuth.getInstance();
        showemail.setText(firebaseAuth.getCurrentUser().getEmail());
        Button sendemail=findViewById(R.id.sendemail);
        sendemail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "이메일 송신 완료", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        Button checkverify=findViewById(R.id.checkverify);
        checkverify.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getCurrentUser().reload();
                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                Intent intent=new Intent(EmailVerify.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                }
                else{

                    Toast.makeText(getApplicationContext(), "이메일 인증 완료후 클릭해주세요", Toast.LENGTH_LONG).show();

                }



            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!firebaseAuth.getCurrentUser().isEmailVerified())
        firebaseAuth.signOut();
    }
}