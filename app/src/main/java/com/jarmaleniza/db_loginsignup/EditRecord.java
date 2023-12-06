package com.jarmaleniza.db_loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditRecord extends AppCompatActivity {

    TextView lblVerify;
    EditText txtFname, txtLname, txtAddress, txtEmail, txtContact, txtUsername, txtPassword;
    Button btnEdit, btnLogout;
    RadioButton selectedGender, isVerified;
    RadioGroup Option, Verify;
    String Fname, Lname, Gender, Address, EmailAd, ContactNo, Username, Password, Status;
    Integer selectedGenderId, verifyId;
    SQLiteDB Conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        Conn = new SQLiteDB(this);
        txtFname = (EditText) findViewById(R.id.FirstName);
        txtLname = (EditText) findViewById(R.id.LastName);
        Option = (RadioGroup) findViewById(R.id.genOption);
        txtAddress = (EditText) findViewById(R.id.Address);
        txtEmail = (EditText) findViewById(R.id.Email);
        txtContact = (EditText) findViewById(R.id.Contact);
        txtUsername = (EditText) findViewById(R.id.Username);
        txtPassword = (EditText) findViewById(R.id.Password);
        Verify = (RadioGroup) findViewById(R.id.Verify);
        lblVerify = (TextView) findViewById(R.id.lblVerify);
        btnEdit = (Button) findViewById(R.id.Edit);
        btnLogout = (Button) findViewById(R.id.Logout);


        int dataIndex = getIntent().getIntExtra("selectedData", 0);
        int accType = getIntent().getIntExtra("account", 0);

        String[] existingData = Conn.getUserRecordByIndex(dataIndex);
        if (existingData != null) {
            txtFname.setText(existingData[0]);
            txtLname.setText(existingData[1]);
            txtAddress.setText(existingData[3]);
            txtEmail.setText(existingData[4]);
            txtContact.setText(existingData[5]);
            String selGender = existingData[2];
            for(int i = 0; i < Option.getChildCount(); i++){
                View view = Option.getChildAt(i);
                if(view instanceof RadioButton){
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getText().toString().equals(selGender)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }

        existingData = Conn.getAccountRecordByIndex(dataIndex);
        if (existingData != null) {
            txtUsername.setText(existingData[0]);
            txtPassword.setText(existingData[1]);

            String radVerify = existingData[2];
            Log.i("Status", radVerify);
            if (radVerify.equals("Yes") || radVerify.equals("1")) {
                radVerify = "Yes";
            }
            else {
                radVerify = "No";
            }

            for (int i = 0; i < Verify.getChildCount(); i++) {
                View view = Verify.getChildAt(i);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getText().toString().equals(radVerify)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }


        if(Conn.checkAdminStatus(dataIndex) || accType == 1){
            txtFname.setEnabled(false);
            txtLname.setEnabled(false);
            txtAddress.setEnabled(false);
            txtEmail.setEnabled(false);
            txtContact.setEnabled(false);
            txtPassword.setEnabled(false);
            for (int i = 0; i < Option.getChildCount(); i++) {
                View view = Option.getChildAt(i);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    radioButton.setEnabled(false);
                }
            }
        }
        else{
            lblVerify.setVisibility(View.INVISIBLE);
            Verify.setVisibility(View.INVISIBLE);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
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

                verifyId = Verify.getCheckedRadioButtonId();
                if(verifyId == -1){
                    Status = "";
                }
                else {
                    isVerified = findViewById(verifyId);
                    Status = isVerified.getText().toString();
                    if(Status.equals("Yes")){
                        Status = "1";
                    }
                    else{
                        Status = "0";
                    }
                }

                if(Fname.isEmpty() || Lname.isEmpty() || Gender.isEmpty() || Address.isEmpty() || EmailAd.isEmpty()
                        || ContactNo.isEmpty() || Username.isEmpty() || Password.isEmpty() || Status.isEmpty()){
                    Toast.makeText(EditRecord.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }

                if(Conn.updateRecord(dataIndex, Fname, Lname, Gender, Address, EmailAd, ContactNo, Password, Status)){
                    Toast.makeText(EditRecord.this, "Record updated.", Toast.LENGTH_SHORT).show();
                    txtFname.setText("");
                    txtLname.setText("");
                    txtAddress.setText("");
                    txtEmail.setText("");
                    txtContact.setText("");
                    txtUsername.setText("");
                    txtPassword.setText("");
                    selectedGender.setChecked(false);
                    isVerified.setChecked(false);

                    Log.i("Status", Status);
                    finish();
                }
                else{
                    Toast.makeText(EditRecord.this, "Error on editing information. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}