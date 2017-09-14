package lidor.swimming_pool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference postRef;
    EditText etTitle,etBody;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        firebaseDatabase = FirebaseDatabase.getInstance();

        etTitle = (EditText)findViewById(R.id.etTitle);
        etBody = (EditText)findViewById(R.id.etBody);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Post p = new Post(Email,etTitle.getText().toString(),etBody.getText().toString(),0,"");
        postRef = firebaseDatabase.getReference("Posts").push();
        p.key = postRef.getKey();
        postRef.setValue(p);

        Intent intent = new Intent(AddPostActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
