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
        //??????????????????
        userID=firebaseAuth.getCurrentUser().getUid();
        documentReference=firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable  FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("UserName"));
                email.setText(documentSnapshot.getString("Email"));
            }
        });
        //????????????
        if (!firebaseAuth.getCurrentUser().isEmailVerified()){
            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(),"???????????????????????????!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        //??????
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //????????????
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_password,null);
                builder.setView(view)
                        .setTitle("?????????????")
                        .setMessage("?????????????????????!")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewPassword=view.findViewById(R.id.userNewPassword);
                                EditText userCheckPassword=view.findViewById(R.id.userCheckPassword);

                                if(userNewPassword.getText().toString().isEmpty()){
                                    userNewPassword.setError("?????????????????????!");
                                    return;
                                }
                                if(userNewPassword.getText().toString().length()<6){
                                    userNewPassword.setError("????????????6???!");
                                    return;
                                }
                                if(userCheckPassword.getText().toString().isEmpty()){
                                    userCheckPassword.setError("????????????!");
                                    return;
                                }
                                if(!userNewPassword.getText().toString().equals(userCheckPassword.getText().toString())){
                                    userCheckPassword.setError("??????????????????????????????!");
                                    return;
                                }
                                firebaseUser.updatePassword(userNewPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"??????????????????!",Toast.LENGTH_SHORT).show();
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

                                //???????????????????????????
                            }
                        });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //???????????? button
        resetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_email,null);
                builder.setView(view)
                        .setTitle("?????????????")
                        .setMessage("???????????????????????????!")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewEmail=view.findViewById(R.id.userNewEmail);
                                EditText userCheckEmail=view.findViewById(R.id.userCheckEmail);

                                if(userNewEmail.getText().toString().isEmpty()){
                                    userNewEmail.setError("?????????????????????!");
                                    return;
                                }
                                if(userCheckEmail.getText().toString().isEmpty()){
                                    userCheckEmail.setError("???????????????????????????!");
                                    return;
                                }
                                if(!userNewEmail.getText().toString().equals(userCheckEmail.getText().toString())){
                                    userCheckEmail.setError("??????????????????????????????!");
                                }
                                firebaseUser.updateEmail(userNewEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"??????????????????!",Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //???????????????????????????
                                documentReference.update("Email",userNewEmail.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"??????????????????!",Toast.LENGTH_SHORT).show();
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
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //???????????? ???
        changeMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                final View view=getLayoutInflater().inflate(R.layout.reset_email,null);
                builder.setView(view)
                        .setTitle("?????????????")
                        .setMessage("???????????????????????????!")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText userNewEmail=view.findViewById(R.id.userNewEmail);
                                EditText userCheckEmail=view.findViewById(R.id.userCheckEmail);

                                if(userNewEmail.getText().toString().isEmpty()){
                                    userNewEmail.setError("?????????????????????!");
                                    return;
                                }
                                if(userCheckEmail.getText().toString().isEmpty()){
                                    userCheckEmail.setError("???????????????????????????!");
                                    return;
                                }
                                if(!userNewEmail.getText().toString().equals(userCheckEmail.getText().toString())){
                                    userCheckEmail.setError("??????????????????????????????!");
                                }
                                firebaseUser.updateEmail(userNewEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"??????????????????!",Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //???????????????????????????
                                documentReference.update("Email",userNewEmail.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(),"??????????????????!",Toast.LENGTH_SHORT).show();
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
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //???????????? botton
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
               builder.setTitle("?????????????")
                        .setMessage("??????????????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //????????????
                                firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"???????????????!",Toast.LENGTH_SHORT).show();
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
                                //???????????????????????????
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"???????????????!",Toast.LENGTH_SHORT).show();
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
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //???????????? ???
        delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("?????????????")
                        .setMessage("??????????????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //????????????
                                firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"???????????????!",Toast.LENGTH_SHORT).show();
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
                                //???????????????????????????
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(),"???????????????!",Toast.LENGTH_SHORT).show();
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
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
