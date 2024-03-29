package com.example.pm_gamecenter.menus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.pm_gamecenter.utilities.PopupActionListener;
import com.example.pm_gamecenter.R;

public class IdentificationPopup extends PopupWindow{
    private TextView title;
    private TextView description;
    private Button continueButton;
    private View popupView;
    private PopupActionListener popupActionListener;


    public IdentificationPopup(Context context, IdentificationFormScreen.IdentificationType idType, int width, int height) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_identification, null);
        this.setContentView(popupView);
        findViews(idType);
        setPopupInfo(idType);
        setWidth(width);
        setHeight(height);
    }

    public void findViews(IdentificationFormScreen.IdentificationType idType) {
        title = popupView.findViewById(R.id.popup_title);
        description = popupView.findViewById(R.id.popup_description);
        continueButton = popupView.findViewById(R.id.popup_button);

        // Set listener
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idType != IdentificationFormScreen.IdentificationType.REGISTER_SUCCESS) {
                    dismiss();
                }
                else {
                    if (popupActionListener != null) {
                        popupActionListener.onPopupDismissed();
                    }
                    dismiss();
                }
            }
        });
    }

    public void setPopupInfo(IdentificationFormScreen.IdentificationType idType) {
        switch (idType) {
            case LOGIN: {
                title.setText(R.string.popup_loginFailed_Title);
                description.setText(R.string.popup_loginFailed_Description);
                break;
            }
            case REGISTER_FAILED: {
                title.setText(R.string.popup_registerFailed_Title);
                description.setText(R.string.popup_registerFailed_Description);
                break;
            }
            case REGISTER_SUCCESS: {
                title.setText(R.string.popup_registerSuccess_Title);
                description.setText(R.string.popup_registerSuccess_Description);
                continueButton.setText(R.string.button_login);
                break;
            }
        }
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public void setContinueButton(Button continueButton) {
        this.continueButton = continueButton;
    }

    public void setPopupActionListener(PopupActionListener popupActionListener) {
        this.popupActionListener = popupActionListener;
    }

}