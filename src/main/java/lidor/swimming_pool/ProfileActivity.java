package lidor.swimming_pool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity
{
    TextView tvName, tvID, tvAge, tvAccNum, tvNumEnt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView)findViewById(R.id.tvName);
        tvID = (TextView)findViewById(R.id.tvID);
        tvAge = (TextView)findViewById(R.id.tvAge);
        tvAccNum = (TextView)findViewById(R.id.tvAccNum);
        tvNumEnt = (TextView)findViewById(R.id.tvNumEnt);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("Name");
        String Age = intent.getExtras().getString("Age");
        String ID = intent.getExtras().getString("ID");
        String AccNum = intent.getExtras().getString("AccNum");
        String NumEnt = intent.getExtras().getString("NumEnt");

        tvName.setText(name);
        tvID.setText(ID);
        tvAge.setText(Age);
        tvAccNum.setText(AccNum);
        tvNumEnt.setText(NumEnt);
    }
}
