package com.example.nabee.gymsocialmedia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logIn extends AppCompatActivity {

    //declare log string, user/pass fields, and login button
    public String TAG = "Login Activity";
    EditText usernamebox;
    EditText passwordbox;
    Button loginbtn;
    TextView createbtn;


    //declare instance of firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();

        //initializing FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get values from the edit texts
                String user = usernamebox.getText().toString();
                String pass = passwordbox.getText().toString();

                //if both fields have input we can begin authentication
                if(!user.equals("") && !pass.equals("")){
                    signIn(user, pass);
                }
                else{

                    Toasts("INCORRECT LOGIN");
                }
            }
        });

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String user = usernamebox.getText().toString();
                String pass = passwordbox.getText().toString();
                createAccount(user, pass);
            }
        });
    }//end of onCreate



    /*-----------------------create user---------------------------------------------------------------*/
    public void createAccount(String email, final String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toasts("NEW ACCOUNT SUCCESSFULLY CREATED");
                            usernamebox.setText("", TextView.BufferType.EDITABLE);
                            passwordbox.setText("", TextView.BufferType.EDITABLE);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            Toasts("ERROR MAKING ACCOUNT");
                        }

                        // ...
                    }
                });
    }

    /*-----------------------use this class to authenticate existing users and let them sign into their profile--------------------------*/
    public void signIn(String email, String Password){

        mAuth.signInWithEmailAndPassword(email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            Toasts("SUCCESSFUL LOGIN WITH: "+ user);
                            Intent intent = new Intent(logIn.this, profile.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toasts("INVALID EMAIL OR PASSWORD");
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    /*-----------------------simple method to show toasts--------------------------------------*/
    private void Toasts(String msg){

        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }

    /*-----------------------use this class to check current auth status-----------------------*/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    /*-------------use class to initialize edittexts and buttons-----------------*/
    public void init(){
        usernamebox = (EditText)findViewById(R.id.userName);
        passwordbox = (EditText)findViewById(R.id.passWord);
        loginbtn = (Button)findViewById(R.id.loginButton);
        createbtn = (TextView)findViewById(R.id.newUserButton);
    }

}//end of class


