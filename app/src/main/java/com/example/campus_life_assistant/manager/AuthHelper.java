package com.example.campus_life_assistant.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthHelper {
    private static final String TAG = "AuthHelper";
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    public AuthHelper() {
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance()
                .getReference("users");
    }

    /** 注册新用户，并在 Realtime Database 中创建用户节点 */
    public void register(@NonNull String email,
                         @NonNull String password,
                         @NonNull OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            usersRef.child(user.getUid())
                                    .child("email")
                                    .setValue(email);
                        }
                    }
                    listener.onComplete(task);
                });
    }

    /** 用户登录 */
    public void login(@NonNull String email,
                      @NonNull String password,
                      @NonNull OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    /** 获取当前用户 */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /** 用户登出 */
    public void logout() {
        auth.signOut();
    }
}