package com.jarmaleniza.db_loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnLogIn, btnSignUp;
    EditText txtUsername, txtPassword;
    SQLiteDB Conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Conn = new SQLiteDB(this);
        txtUsername = (EditText) findViewById(R.id.Username);
        txtPassword = (EditText) findViewById(R.id.Password);
        btnLogIn = (Button) findViewById(R.id.Login);
        btnSignUp = (Button) findViewById(R.id.Signup);

        Conn.createAdmin();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username, Password;
                Username = txtUsername.getText().toString();
                Password = txtPassword.getText().toString();

                if(Username.isEmpty() || Password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer state = Conn.checkLogin(Username, Password);

                switch (state) {
                    case 1:
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        if (Conn.checkAdmin(Username, Password)) {
                            Intent intent = new Intent(MainActivity.this, AccList.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, EditRecord.class);
                            Integer recordId = Conn.getUserId(Username, Password);
                            intent.putExtra("selectedData", recordId);
                            startActivity(intent);
                        }
                        txtUsername.setText("");
                        txtPassword.setText("");
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Please wait for admin to accept your sign-up request. Try again later.", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Username and Password do not match. Try again.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Account does not exist!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                txtUsername.setText("");
                txtPassword.setText("");
            }
        });
    }
}