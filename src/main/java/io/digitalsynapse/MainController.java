package io.digitalsynapse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;


import java.util.*;

import java.net.URL;


public class MainController implements Initializable{

    /**
     * Class and Instance specific variables
     */
    @FXML public Button fx_copyButton;
    @FXML public Button fx_GenerateNewPasswordButton;

    @FXML public TextField fx_PasswordLenText;
    @FXML public TextField fx_PasswordUpperCaseText;
    @FXML public TextField fx_PasswordSpecialCaseText;
    @FXML public TextField fx_PasswordNumbersText;
    @FXML public TextField fx_GeneratedPassword;

    @FXML public Slider fx_PasswordLenSlider;
    @FXML public Slider fx_PasswordUpperCaseSlider;
    @FXML public Slider fx_PasswordSpecialCaseSlider;
    @FXML public Slider fx_PasswordNumbersSlider;

    @FXML public ProgressIndicator fx_PasswordSecurityRankIndicator;

    boolean setCopied;
    boolean updatePassword;
    int minimumCharacters;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setInitialValues();
        getGeneratedPassword();


        /**
         * These are my override methods for each slider so that they can react when their values are
         * changed.
         */
        fx_PasswordLenSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.setTextFieldStrength(fx_PasswordLenText, newValue.intValue(), 32, 14, 10);
            if (newValue.intValue()-oldValue.intValue() != 0) updatePassword = true;
                this.setSliderRestrictions();
        } );
        fx_PasswordUpperCaseSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.setTextFieldStrength(fx_PasswordUpperCaseText, newValue.intValue());
            if (newValue.intValue()-oldValue.intValue() != 0) updatePassword = true;
                this.setSliderRestrictions();
        } );
        fx_PasswordSpecialCaseSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.setTextFieldStrength(fx_PasswordSpecialCaseText, newValue.intValue());
            if (newValue.intValue()-oldValue.intValue() != 0) updatePassword = true;
                this.setSliderRestrictions();
        } );
        fx_PasswordNumbersSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.setTextFieldStrength(fx_PasswordNumbersText, newValue.intValue());
            if (newValue.intValue()-oldValue.intValue() != 0) updatePassword = true;
                this.setSliderRestrictions();
        } );

    }


    @FXML
    public void CopyPassword(ActionEvent event) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();



        if(setCopied) {
            Transferable transferable = new StringSelection("no-no-no");
            clipboard.setContents(transferable, null);

            fx_copyButton.setText("Copy");
            setCopied = false;
        } else {
            Transferable transferable = new StringSelection(fx_GeneratedPassword.getText().toString());
            clipboard.setContents(transferable, null);

            fx_copyButton.setText("Copied!");
            setCopied = true;
        }
    }


    @FXML
    public void GenerateNewPassword(ActionEvent event) {
        getGeneratedPassword();

        fx_copyButton.setText("Copy");
        setCopied = false;
    }



    public void setInitialValues() {
        fx_PasswordLenText.setText(Integer.toString((int)fx_PasswordLenSlider.getValue()));
        fx_PasswordLenText.setStyle(this.setTextFieldColorFormatting("default"));

        fx_PasswordUpperCaseText.setText(Integer.toString((int)fx_PasswordUpperCaseSlider.getValue()));
        fx_PasswordUpperCaseText.setStyle(this.setTextFieldColorFormatting("good"));

        fx_PasswordSpecialCaseText.setText(Integer.toString((int)fx_PasswordSpecialCaseSlider.getValue()));
        fx_PasswordSpecialCaseText.setStyle(this.setTextFieldColorFormatting("medium"));

        fx_PasswordNumbersText.setText(Integer.toString((int)fx_PasswordNumbersSlider.getValue()));
        fx_PasswordNumbersText.setStyle(this.setTextFieldColorFormatting("good"));

        setSecurityIndicatorValue();
    }


    /**
     * setTextFieldColorFormatting(string value)
     *
     * Simple method to set consistent text coloring across application
     *
     * @param value
     * @return
     */
    public String setTextFieldColorFormatting(String value) {
        switch (value) {
            case "low":
                return "-fx-background-color: red; -fx-text-fill: white;";
            case "medium":
                return "-fx-background-color: yellow; -fx-text-fill: black;";
            case "good":
                return "-fx-background-color: forestgreen; -fx-text-fill: white;";
            case "high":
                return "-fx-background-color: lawngreen; -fx-text-fill: black;";

            // default = "good"
            default:
                return "-fx-background-color: forestgreen; -fx-text-fill: white;";

        }
    }


    /**
     * setTextFieldStrength
     *
     * Used for getting the strengths associated for each field type.  This is the
     * default version of the method; used for all fields EXCEPT the password length field
     *
     *
     * @param field
     * @param fieldValue
     */
    public void setTextFieldStrength(TextField field, int fieldValue) {
        setTextFieldStrength(field, fieldValue, 6, 4, 2);
    }

    /**
     * setTextFieldStrength
     *
     * Used for getting the strengths associated for each field type.  This is the
     * overloaded version of the method
     *
     *
     * @param field
     * @param fieldValue
     * @param high
     * @param good
     * @param medium
     */
    public void setTextFieldStrength(TextField field, int fieldValue, int high, int good, int medium) {
        if (fieldValue >= high) {
            field.setStyle(this.setTextFieldColorFormatting("high"));

        } else if (fieldValue >= good) {
            field.setStyle(this.setTextFieldColorFormatting("good"));

        } else if (fieldValue >= medium) {
            field.setStyle(this.setTextFieldColorFormatting("medium"));

        } else {
            field.setStyle(this.setTextFieldColorFormatting("low"));
        }

        field.setText(Integer.toString(fieldValue));
    }


    /**
     * setSliderRestrictions
     *
     * Method to set slider restrictions based on what the user is trying to select
     */
    public void setSliderRestrictions() {
        minimumCharacters = (int)fx_PasswordUpperCaseSlider.getValue() +
                (int)fx_PasswordSpecialCaseSlider.getValue() +
                (int)fx_PasswordNumbersSlider.getValue();

        if ((int)fx_PasswordLenSlider.getValue() <= minimumCharacters) {
            fx_PasswordUpperCaseSlider.setDisable(true);
            fx_PasswordUpperCaseText.setDisable(true);

            fx_PasswordSpecialCaseSlider.setDisable(true);
            fx_PasswordSpecialCaseText.setDisable(true);

            fx_PasswordNumbersSlider.setDisable(true);
            fx_PasswordNumbersText.setDisable(true);

        } else {
            fx_PasswordUpperCaseSlider.setDisable(false);
            fx_PasswordUpperCaseText.setDisable(false);

            fx_PasswordSpecialCaseSlider.setDisable(false);
            fx_PasswordSpecialCaseText.setDisable(false);

            fx_PasswordNumbersSlider.setDisable(false);
            fx_PasswordNumbersText.setDisable(false);
        }


        if (updatePassword) {
            setSecurityIndicatorValue();
            getGeneratedPassword();
            updatePassword = false;
        }
    }

    /**
     * setSecurityIndicatorValue
     *
     * Method to work with a progress bar and give a general overview of password complexity.
     *
     * Weights are arbitrary but given work with high security, the defaults set for this program
     * should work for almost every "complex" password system currently available.
     *
     */
    public void setSecurityIndicatorValue() {
        double characterLenWeight = 0.0;
        double upperCaseWeight = 0.0;
        double specialCaseWeight = 0.0;
        double numberWeight = 0.0;

        if (fx_PasswordLenSlider.getValue() <= 10.0) {
            characterLenWeight = 0.05;
        } else {
            characterLenWeight = (fx_PasswordLenSlider.getValue() * 0.025);
        }

        if (fx_PasswordUpperCaseSlider.getValue() == 0.0) {
            upperCaseWeight = 0.0;
        } else if (fx_PasswordUpperCaseSlider.isDisable()) {
            upperCaseWeight = 0.0;
        } else {
            upperCaseWeight = (fx_PasswordUpperCaseSlider.getValue() * 0.060);
        }

        if (fx_PasswordSpecialCaseSlider.getValue() == 0.0) {
            specialCaseWeight = 0.0;
        } else if (fx_PasswordSpecialCaseSlider.isDisable()) {
            specialCaseWeight = 0.0;
        } else {
            specialCaseWeight = (fx_PasswordSpecialCaseSlider.getValue() * 0.060);
        }

        if (fx_PasswordNumbersSlider.getValue() == 0.0) {
            numberWeight = 0.0;
        } else if (fx_PasswordNumbersSlider.isDisable()) {
            numberWeight = 0.0;
        } else {
            numberWeight = (fx_PasswordNumbersSlider.getValue() * 0.060);
        }


        //System.out.println("weights");
        //System.out.println("char = " + characterLenWeight);
        //System.out.println("upper = " + upperCaseWeight);
        //System.out.println("special = " + specialCaseWeight);
        //System.out.println("numbers = " + numberWeight);

        double totalWeight = characterLenWeight + upperCaseWeight + specialCaseWeight + numberWeight;

        //System.out.println("total weight = " + totalWeight);


        fx_PasswordSecurityRankIndicator.setProgress(totalWeight);

        /**
        Text text = (Text) fx_PasswordSecurityRankIndicator.lookup(".percentage");
        if (text != null) {
            text.setText("");
        } */

        if (fx_PasswordSecurityRankIndicator.getProgress() >= 0.90) {
            fx_PasswordSecurityRankIndicator.setStyle("-fx-progress-color: forestgreen");
        } else if (fx_PasswordSecurityRankIndicator.getProgress() >= 0.70) {
            fx_PasswordSecurityRankIndicator.setStyle("-fx-progress-color: forestgreen");
        } else if (fx_PasswordSecurityRankIndicator.getProgress() >= 0.60) {
            fx_PasswordSecurityRankIndicator.setStyle("-fx-progress-color: yellow");
        } else {
            fx_PasswordSecurityRankIndicator.setStyle("-fx-progress-color: red");
        }



    }


    /**
     * getGeneratedPassword()
     *
     * Method to get a generated password
     */
    public void getGeneratedPassword() {
        fx_GeneratedPassword.setText(PasswordManager.GeneratePassword(fx_PasswordNumbersSlider,
                fx_PasswordUpperCaseSlider,
                fx_PasswordSpecialCaseSlider,
                fx_PasswordLenSlider));
    }


}
