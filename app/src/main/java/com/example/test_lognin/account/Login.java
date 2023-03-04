package com.example.test_lognin.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_lognin.MainActivity;
import com.example.test_lognin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView creatAccountBtn,forgetBtn;
    Button Loginbtn;
    EditText urseremail,userpassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        creatAccountBtn=findViewById(R.id.sign_up);
        creatAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
        //登入
        urseremail=findViewById(R.id.Login_email);
        userpassword=findViewById(R.id.Login_password);
        Loginbtn=findViewById(R.id.Loginbtn);
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=urseremail.getText().toString();
                String password=userpassword.getText().toString();

                //驗證資料
                if (email.isEmpty()){
                    urseremail.setError("電子郵件輸入錯誤!");
                    return;
                }
                if(password.isEmpty()){
                    userpassword.setError("密碼輸入錯誤!");
                    return;
                }
                //登入
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override //登入成功
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override //登入失敗
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        //忘記密碼
        forgetBtn=findViewById(R.id.forget);
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                final View view=getLayoutInflater().inflate(R.layout.forgot_password,null);
                builder.setView(view)
                        .setTitle("忘記密碼?")
                        .setMessage("輸入您的電子信箱來獲取密碼重置連結!")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //驗證電子信箱
                                EditText email=view.findViewById(R.id.rest_email_pop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("電子信箱輸入錯誤");
                                    return;
                                }
                                //發送連結
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Login.this,"已寄送重置密碼到電子信箱",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }

    //先前有登錄過的就直接進入到應用程式內
    protected void onStart(){
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}