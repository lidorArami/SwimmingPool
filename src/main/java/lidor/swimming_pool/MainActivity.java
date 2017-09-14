package lidor.swimming_pool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    Button btnReg,btnLogin;//dialog buttons
    Button btnMainLogin,btnMainRegister;
    EditText etEmail,etPass,etAge,etID,etAccNum,etNumEnt;
    Dialog d;
    ProgressDialog progressDialog;
    Button btnAddPost,btnAllPost,btnProfile;
    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        btnMainLogin = (Button)findViewById(R.id.btnLogin);
        btnMainLogin.setOnClickListener(this);

        btnMainRegister = (Button)findViewById(R.id.btnRegister);
        btnMainRegister.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) btnMainLogin.setText("Log out");
        else btnMainLogin.setText("Log in");

        btnAddPost = (Button)findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                startActivity(intent);
            }
        });
        btnAllPost = (Button)findViewById(R.id.btnAllPost);
        btnAllPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,AllPostActivity.class);
                startActivity(intent);
            }
        });

        btnProfile = (Button)findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(this);

        btnLocation = (Button)findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(this);
    }

    public void createLoginDialog()
    {
        d = new Dialog(this);
        d.setContentView(R.layout.login_layout);
        d.setTitle("Log in");
        d.setCancelable(true);
        etEmail = (EditText)d.findViewById(R.id.etEmail);
        etPass = (EditText)d.findViewById(R.id.etPass);
        btnLogin = (Button)d.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        d.show();
    }

    public void createRegisterDialog()
    {
        d = new Dialog(this);
        d.setContentView(R.layout.register_layout);
        d.setTitle("Register");
        d.setCancelable(true);
        etEmail = (EditText)d.findViewById(R.id.etEmail);
        etPass = (EditText)d.findViewById(R.id.etPass);
        etAge = (EditText)d.findViewById(R.id.etAge);
        etID = (EditText)d.findViewById(R.id.etID);
        etAccNum = (EditText)d.findViewById(R.id.etAccNum);
        etNumEnt = (EditText)d.findViewById(R.id.etNumEnt);
        btnReg = (Button)d.findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(this);
        d.show();
    }

    public void register()
    {
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        String name = etEmail.getText().toString();
        // According to Google's definitions, the password must contain letters and numbers, start at letters and then numbers
        String password = etPass.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(name, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    btnMainLogin.setText("Log out");
                }
                else Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();

                d.dismiss();
                progressDialog.dismiss();
            }
        });
    }

    public void login()
    {
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "auth_success",Toast.LENGTH_SHORT).show();
                            btnMainLogin.setText("Log out");
                        }
                        else Toast.makeText(MainActivity.this, "auth_failed",Toast.LENGTH_SHORT).show();

                        d.dismiss();
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View v)
    {
        if(v == btnMainLogin)
        {
            if (btnMainLogin.getText().toString().equals("Log in")) createLoginDialog();
            else if (btnMainLogin.getText().toString().equals("Log out"))
            {
                firebaseAuth.signOut();
                btnMainLogin.setText("Log in");
            }
        }
        else if(v == btnMainRegister) createRegisterDialog();
        else if (btnReg == v) register();

        else if(v == btnLogin) login();

        else if(v == btnProfile)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("Name",etEmail.getText().toString());
            intent.putExtra("Age",etAge.getText().toString());
            intent.putExtra("ID",etID.getText().toString());
            intent.putExtra("AccNum",etAccNum.getText().toString());
            intent.putExtra("NumEnt",etNumEnt.getText().toString());
            startActivity(intent);
        }

        else if (v == btnLocation)
        {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        }
    }
}
