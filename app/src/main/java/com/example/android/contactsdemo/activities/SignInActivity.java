package com.example.android.contactsdemo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.contactsdemo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SignInActivity extends AppCompatActivity {

//    GoogleSignInClient for authentication
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
//    FirebaseAuth for authentication in firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        Building signinoption

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//        Creating instance for firebase authentication
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


//        This is the only button displayed in the sign in activity. This button calls method that does the job of authentication
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

//    This method is called when sign in button is tapped. It starts activity that does authentication with google account
    private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

    }

//    When activity that doest authentication finishes execution, this method is called.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
//            If the signing in is successful, we have to get signed in account from the intent, which is done by calling handleSignInResult
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }
// Method to get signed in user account
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

//            If mAuth.getCurrentUser is not empty, user has already logged in, so no need to get account
            if(mAuth.getCurrentUser() != null) {
                Toast.makeText(getBaseContext(), "You are already logged in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), HomePageActivity.class);
                intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                intent.putExtra("userName", mAuth.getCurrentUser().getDisplayName());
                intent.putExtra("imageUrl", mAuth.getCurrentUser().getPhotoUrl());
                startActivity(intent);

            } else {
//                Else, get signed in account and pass it to firebaseAuthWithGoogle to authenticate user in firebase server
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
//            Catch exception for failure (e.g. throws exception when internet is not available)
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInActivity", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
//            updateUI(null); don't update the next interface since
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


//    Method to authenticate user on firebase.
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("SignInActivity", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        Sign in user to firebase using following inbuilt method
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignInActivity", "signInWithCredential:success");
//                            if successful, get a user (to access its provided details)
                            FirebaseUser user = mAuth.getCurrentUser();

//                                if sign in is successful, get necessary user data for future data and launch homepage activity passing these values
                                Toast.makeText(getApplicationContext(), "Signed in successfully as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), HomePageActivity.class);
                                intent.putExtra("uid", user.getUid());
                                intent.putExtra("userName", user.getDisplayName());
                                startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), "not happening", Toast.LENGTH_SHORT).show();
                            Snackbar.make(findViewById(R.id.sign_in_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
}
