package com.jarmaleniza.db_loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText txtFname, txtLname, txtAddress, txtEmail, txtContact, txtUsername, txtPassword;
    Button btnRegister, btnLogin;
    RadioButton selectedGender;
    RadioGroup Option;
    String Fname, Lname, Gender, Address, EmailAd, ContactNo, Username, Password;
    Integer selectedGenderId;
    SQLiteDB Conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Conn = new SQLiteDB(this);
        txtFname = (EditText) findViewById(R.id.FirstName);
        txtLname = (EditText) findViewById(R.id.LastName);
        Option = (RadioGroup) findViewById(R.id.genOption);
        txtAddress = (EditText) findViewById(R.id.Address);
        txtEmail = (EditText) findViewById(R.id.Email);
        txtContact = (EditText) findViewById(R.id.Contact);
        txtUsername = (EditText) findViewById(R.id.Username);
        txtPassword = (EditText) findViewById(R.id.Password);
        btnRegister = (Button) findViewById(R.id.Register);
        btnLogin = (Button) findViewById(R.id.Login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fname = txtFname.getText().toString();
                Lname = txtLname.getText().toString();
                Address = txtAddress.getText().toString();
                EmailAd = txtEmail.getText().toString();
                ContactNo = txtContact.getText().toString();
                Username = txtUsername.getText().toString();
                Password = txtPassword.getText().toString();

                selectedGenderId = Option.getCheckedRadioButtonId();
                selectedGender = (RadioButton) findViewById(selectedGenderId);
                if(selectedGenderId.equals(-1)){
                    Gender = "";
                }
                else {
                    Gender = selectedGender.getText().toString();
                }

                if(Fname.isEmpty() || Lname.isEmpty() || Gender.isEmpty() || Address.isEmpty()
                        || EmailAd.isEmpty() || ContactNo.isEmpty() || Username.isEmpty() || Password.isEmpty()){
                    Toast.makeText(SignUp.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }

                if(Conn.recordExists(Username)){
                    Toast.makeText(SignUp.this, "Username already exists. Please try another.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Conn.AddRecords(Fname, Lname, Gender, Address, EmailAd, ContactNo, Username, Password)){
                        Toast.makeText(SignUp.this, "Please wait for admin to verify your sign-up request.", Toast.LENGTH_SHORT).show();
                        txtFname.setText("");
                        txtLname.setText("");
                        txtAddress.setText("");
                        txtEmail.setText("");
                        txtContact.setText("");
                        txtUsername.setText("");
                        txtPassword.setText("");
                        selectedGender.setChecked(false);
                        finish();
                    }
                    else{
                        Toast.makeText(SignUp.this, "Error on saving information. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}