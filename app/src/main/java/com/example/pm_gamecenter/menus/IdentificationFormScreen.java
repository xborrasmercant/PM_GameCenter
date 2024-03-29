package com.example.pm_gamecenter.menus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm_gamecenter.utilities.PopupActionListener;
import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

public class IdentificationFormScreen extends AppCompatActivity implements PopupActionListener {

    private UserManager userManager = UserManager.getInstance();
    private EditText nameEditText, passwordEditText;
    private Button identificationButton;
    private FrameLayout formLayout;
    private IdentificationType idType;
    public enum IdentificationType {
        LOGIN, REGISTER, REGISTER_FAILED, REGISTER_SUCCESS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_identification_form);

        initComponents();
        onBackResume();
    }


    private void loginUser() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // If login success set active user and go to hub (where games are located) if not show try again popup
        if (userExists(name, password)) {
            userManager.setActiveUser(userManager.getUserByName(name));
            startActivity(new Intent(IdentificationFormScreen.this, HubScreen.class));
            finish();
        } else {
            showPopup(IdentificationType.LOGIN);
        }
    }

    private void registerUser() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // If register success show popup with login button if users exists show try again popup
        if (!userExists(name, password)) {
            User u = new User(name, password);
            userManager.addUser(this, u);
            userManager.setActiveUser(u);
            showPopup(IdentificationType.REGISTER_SUCCESS);
        } else {
            showPopup(IdentificationType.REGISTER_FAILED);
        }
    }

    public boolean userExists(String name, String password) {
        // Check users arraylist depending on if login or register
        for (User u : userManager.getUsers()) {
            Log.i("USERNAME", u.getUsername());
            Log.i("PASSWORD", u.getPassword());

            switch (idType) {
                case LOGIN: {
                    if (name.equals(u.getUsername()) && password.equals(u.getPassword())) {return true;}
                    break;
                }
                case REGISTER: {
                    if (name.equals(u.getUsername())) {return true;}
                    break;
                }
            }
        }

        return false;
    }

    public void setFormInfo() {
        switch (idType) {
            case LOGIN: {
                identificationButton.setText(R.string.button_login);
                break;
            }
            case REGISTER: {
                identificationButton.setText(R.string.button_register);
                break;
            }
        }
    }

    public void initComponents() {
        nameEditText = findViewById(R.id.inputText_name);
        passwordEditText = findViewById(R.id.inputText_password);
        identificationButton = findViewById(R.id.identification_button);
        formLayout = findViewById(R.id.formLayout);
        idType = (IdentificationType) getIntent().getSerializableExtra("IdentificationType");
        setFormInfo();

        identificationButton.setOnClickListener(view -> {

            switch (idType) {
                case LOGIN: {
                    loginUser();
                    break;
                }
                case REGISTER: {
                    registerUser();
                    break;
                }
            }
        });
    }

    public void showPopup(IdentificationType idType) {
        int popupWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        IdentificationPopup idPopup = new IdentificationPopup(this, idType, popupWidth, popupHeight);
        idPopup.setPopupActionListener(this);
        idPopup.showAtLocation(formLayout, Gravity.CENTER, 0, 0);
    }

    public void onBackResume() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(IdentificationFormScreen.this, IdentificationScreen.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onPopupDismissed() {
        startActivity(new Intent(IdentificationFormScreen.this, HubScreen.class));
        finish();
    }
}
