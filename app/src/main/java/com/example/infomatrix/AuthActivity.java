package com.example.infomatrix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.serializers.Token;
import com.example.infomatrix.serializers.UserAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements Button.OnClickListener {

    private UserAuth userAuth;

    private EditText email;
    private EditText password;
    private Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    public void init() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        userAuth = new UserAuth();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.sign_in_button);

        email.setText(preferences.getString("email", ""));
        password.setText(preferences.getString("password", ""));

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        userAuth.setEmail(email.getText().toString().trim());
        userAuth.setPassword(password.getText().toString().trim());

        NetworkService.getInstance()
                .getTokenApi()
                .obtainToken(userAuth)
                .enqueue(new Callback<Token>() {

                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            NetworkService.getInstance().setToken(response.body().getToken());
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", userAuth.getEmail());
                            editor.putString("password", userAuth.getPassword());
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("token", response.body());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        t.printStackTrace();
                    }

                });

    }
}
