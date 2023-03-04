package com.example.test_lognin.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.test_lognin.R;
import com.example.test_lognin.account.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FragmentAccountSet extends Fragment {
    Button logout,resetPassword,resetEmail,deleteAccount;
    TextView username,email,changeMail,changePassword,delect;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    String userID;
    DocumentReference documentReference;
    public static final String TAG="TAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_accountset,container,false);

        logout=view.findViewById(R.id.btn_Logout);
        resetPassword=view.findViewById(R.id.btn_resetPass);
        resetEmail=view.findViewById(R.id.btn_UpdateEmail);
        deleteAccount=view.findViewById(R.id.btn_Delete);
        username=view.findViewById(R.id.username);
        email=view.findViewById(R.id.usermail);

        changePassword=view.findViewById(R.id.txv_change_password);
        changeMail=view.findViewById(R.id.txv_update_mail);
        delect=view.findViewById(R.id.txv_delect_account);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        //抓取用戶資料
        userID=firebaseAuth.getCurrentUser().getUid();
        documentReference=firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable  FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("UserName"));
                email.setText(documentSnapshot.getString("Email"));
            }
        });
        //驗證信箱
        if (!firebaseAuth.getCurrentUser().isEmailVerified()){
            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(),"已發送電子郵件驗證!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        //登出
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //重置密碼
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_password,null);
                builder.setView(view)
                        .setTitle("更改密碼?")
                        .setMessage("請輸入新的密碼!")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewPassword=view.findViewById(R.id.userNewPassword);
                                EditText userCheckPassword=view.findViewById(R.id.userCheckPassword);

                                if(userNewPassword.getText().toString().isEmpty()){
                                    userNewPassword.setError("密碼欄位未填寫!");
                                    return;
                                }
                                if(userNewPassword.getText().toString().length()<6){
                                    userNewPassword.setError("密碼至少6碼!");
                                    return;
                                }
                                if(userCheckPassword.getText().toString().isEmpty()){
                                    userCheckPassword.setError("密碼錯誤!");
                                    return;
                                }
                                if(!userNewPassword.getText().toString().equals(userCheckPassword.getText().toString())){
                                    userCheckPassword.setError("確認密碼與密碼不相符!");
                                    return;
                                }
                                firebaseUser.updatePassword(userNewPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"密碼更新成功!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //資料庫內的密碼更改
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
        //重置信箱 button
        resetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_email,null);
                builder.setView(view)
                        .setTitle("更改信箱?")
                        .setMessage("請輸入新的電子信箱!")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewEmail=view.findViewById(R.id.userNewEmail);
                                EditText userCheckEmail=view.findViewById(R.id.userCheckEmail);

                                if(userNewEmail.getText().toString().isEmpty()){
                                    userNewEmail.setError("信箱欄位未填寫!");
                                    return;
                                }
                                if(userCheckEmail.getText().toString().isEmpty()){
                                    userCheckEmail.setError("確認信箱欄位未填寫!");
                                    return;
                                }
                                if(!userNewEmail.getText().toString().equals(userCheckEmail.getText().toString())){
                                    userCheckEmail.setError("確認信箱與信箱不相符!");
                                }
                                firebaseUser.updateEmail(userNewEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"信箱更新成功!",Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //資料庫內的信箱更改
                                documentReference.update("Email",userNewEmail.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"信箱更新成功!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        //重置信箱 框
        changeMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_email,null);
                builder.setView(view)
                        .setTitle("更改信箱?")
                        .setMessage("請輸入新的電子信箱!")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewEmail=view.findViewById(R.id.userNewEmail);
                                EditText userCheckEmail=view.findViewById(R.id.userCheckEmail);

                                if(userNewEmail.getText().toString().isEmpty()){
                                    userNewEmail.setError("信箱欄位未填寫!");
                                    return;
                                }
                                if(userCheckEmail.getText().toString().isEmpty()){
                                    userCheckEmail.setError("確認信箱欄位未填寫!");
                                    return;
                                }
                                if(!userNewEmail.getText().toString().equals(userCheckEmail.getText().toString())){
                                    userCheckEmail.setError("確認信箱與信箱不相符!");
                                }
                                firebaseUser.updateEmail(userNewEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"信箱更新成功!",Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //資料庫內的資料更改
                                documentReference.update("Email",userNewEmail.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(),"信箱更新成功!",Toast.LENGTH_SHORT).show();
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(getActivity(),Login.class));
                                                dialog.cancel();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        //刪除帳號 botton
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
               builder.setTitle("刪除帳號?")
                        .setMessage("此動作將永久刪除帳號，確定刪除?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //帳戶刪除
                                firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"帳號已刪除!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //資料庫內的資料刪除
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"帳號已刪除!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
        //刪除帳號 框
        delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("刪除帳號?")
                        .setMessage("此動作將永久刪除帳號，確定刪除?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //帳戶刪除
                                firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"帳號已刪除!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //資料庫內的資料刪除
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"帳號已刪除!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getActivity(),Login.class));
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
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

        return view;
    }

}
