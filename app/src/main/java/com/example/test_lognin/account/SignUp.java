package com.example.test_lognin.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText registerAccount,registerPassword,registerCheck,registerEmail;
    Button registerUserBtn;
    TextView siginBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID; //保存我們當前註冊的用戶的ID
    public static final String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseAuth firebaseAuth;

        registerAccount=findViewById(R.id.registerAccount);
        registerPassword=findViewById(R.id.registerPassword);
        registerCheck=findViewById(R.id.registerCheck);
        registerEmail=findViewById(R.id.registerEmail);
        registerUserBtn=findViewById(R.id.registerUserBtn);
        siginBtn=findViewById(R.id.signIn);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得註冊資料
                String username=registerAccount.getText().toString();
                String password=registerPassword.getText().toString();
                String checkpass=registerCheck.getText().toString();
                String email=registerEmail.getText().toString();

                if(username.isEmpty()){
                    registerAccount.setError("帳號欄位未填寫!");
                    return;
                }

                if(password.isEmpty()){
                    registerPassword.setError("密碼欄位未填寫!");
                    return;
                }

                if(password.length()<6){
                    registerPassword.setError("密碼至少6碼!");
                    return;
                }

                if(checkpass.isEmpty()){
                    registerCheck.setError("確認密碼欄位未填寫!");
                    return;
                }

                if(email.isEmpty()){
                    registerEmail.setError("信箱欄位未填寫!");
                    return;
                }

                if(!password.equals(checkpass)){
                    registerCheck.setError("與密碼不符!");
                    return;
                }
                //Toast.makeText(Register.this,"註冊成功!!!",Toast.LENGTH_SHORT).show();
                //利用firebase建立用戶
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override //添加建立成功的監聽
                    public void onSuccess(AuthResult authResult) {

                        //將 userID 和當前登錄的資料驗證做連結
                        userID=firebaseAuth.getCurrentUser().getUid();
                        //建立資料庫文件夾
                        DocumentReference documentReference=firestore.collection("users").document(userID);
                        //儲存 user資料在資料庫內
                        Map<String,Object> user=new HashMap<>();
                        //導入要插入的數據
                        user.put("UserName",username);
                        user.put("Email",email);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"成功: 使用者資料已建立"+userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                Log.d(TAG,"失敗: "+e.toString());
                            }
                        });

                        if (!firebaseAuth.getCurrentUser().isEmailVerified()){
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignUp.this,"已發送電子郵件驗證!",Toast.LENGTH_SHORT).show();
                                    //verifyEmail.setVisibility(View.GONE);
                                    //verifyMsg.setVisibility(View.GONE);
                                }
                            });
                        }
                        //帳戶創建成功自動登入
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override //添加建立失敗的監聽
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}