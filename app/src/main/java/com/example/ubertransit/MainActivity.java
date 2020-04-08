package com.example.ubertransit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    enum State{
        SIGNUP, LOGIN
    }

    private State state;
    private EditText username, password, edtDriverOrPassenger;
    private RadioGroup radioGroup;
    private Button btnOTLogin, btnSignup;
    private RadioButton passenger, driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ParseInstallation.getCurrentInstallation().saveInBackground();
        if (ParseUser.getCurrentUser() != null){
            ParseUser.logOut();
        }

        username = findViewById(R.id.edtUsername);
        password = findViewById(R.id.edtPassword);
        edtDriverOrPassenger = findViewById(R.id.edtDoP);
        radioGroup = findViewById(R.id.radioGroup);
        btnOTLogin = findViewById(R.id.btnOTL);
        btnSignup = findViewById(R.id.btnSignup);
        passenger = findViewById(R.id.passenger);
        driver = findViewById(R.id.driver);

        state = State.SIGNUP;
        btnSignup.setOnClickListener(this);


        btnOTLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDriverOrPassenger.getText().toString().equals("Driver") || edtDriverOrPassenger.getText().toString().equals("Passenger")){
                    if (ParseUser.getCurrentUser() == null){
                        ParseAnonymousUtils.logIn(new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                Toast.makeText(MainActivity.this, "We have an anonymous user", Toast.LENGTH_LONG).show();

                                user.put("as", edtDriverOrPassenger.getText().toString());
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        transitionToPassengerActivity();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (state == State.SIGNUP){
            if (driver.isChecked() == false && passenger.isChecked() == false){
                Toast.makeText(MainActivity.this, "Are you a driver or a passenger", Toast.LENGTH_LONG).show();
                return;
            }
            ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            if (driver.isChecked()){
                user.put("as", "Driver");
            }else if (passenger.isChecked()){
                user.put("as", "Passenger");
            }
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(MainActivity.this, "Signed Up", Toast.LENGTH_LONG).show();
                        transitionToPassengerActivity();
                    }
                }
            });
        } else if (state == State.LOGIN){

            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && e == null){
                        Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_LONG);
                        transitionToPassengerActivity();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginItem:
                if (state == State.SIGNUP){
                    state = State.LOGIN;
                    item.setTitle("Sign Up");
                    btnSignup.setText("Log In");
                }else if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    item.setTitle("Log In");
                    btnSignup.setText("Sign Up");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity(){
        if (ParseUser.getCurrentUser() != null){
            if (ParseUser.getCurrentUser().get("as").equals("Passenger")){
                Intent intent = new Intent(MainActivity.this, PassengerActivity.class);
                startActivity(intent);
            }
        }
    }
}