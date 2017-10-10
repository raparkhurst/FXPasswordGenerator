package io.digitalsynapse;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.Random;

/**
 * Created by raparkhurst on 10/9/17.
 */
public class PasswordManager {

    /**
     * scramble()
     *
     * Method to scramble the generated password.  Found this from:
     * https://stackoverflow.com/questions/3316674/how-to-shuffle-characters-in-a-string
     *
     */
    public static String scramble(Random random, String inputString ) {
        // Convert your string into a simple char array:
        char a[] = inputString.toCharArray();

        // Scramble the letters using the standard Fisher-Yates shuffle,
        for( int i=0 ; i<a.length ; i++ )
        {
            int j = random.nextInt(a.length);
            // Swap letters
            char temp = a[i]; a[i] = a[j];  a[j] = temp;
        }

        return new String( a );
    }


    /**
     *
     * GeneratePassword()
     *
     * This contains the algorithm for generating the password.  It's pretty simple.  It takes all sliders
     * and populates an arrayList based on how many of each type are needed.  It then runs the arrayList through
     * the scramble() method to shuffle the array randomly to get the end, random, password.
     */
    public static String GeneratePassword(Slider fx_PasswordNumbersSlider,
                                        Slider fx_PasswordUpperCaseSlider,
                                        Slider fx_PasswordSpecialCaseSlider,
                                        Slider fx_PasswordLenSlider) {
        String specialCharacters = "~!@#$%^&*()-_=+[{]};:,<.>?";
        String alphaCharactersLower = "abcdefghijklmnopqrstuvwxyz";
        String alphaCharactersUpper = "abcdefghjkmnopqrstuvwxyz";

        String PasswordArray[] = new String[(int)fx_PasswordLenSlider.getValue()];

        String preShuffledPasswordString = "";
        String shuffledPasswordString;
        Random rand = new Random();
        int i = 0;

        /**
         * Each section below generates the number of characters necessary for its part.
         * If any slider has been disabled (due to password length constraints) then it is
         * skipped over.
         */
        // generate required numbers
        if(!fx_PasswordNumbersSlider.isDisable()) {
            for (int k = 0; k < fx_PasswordNumbersSlider.getValue(); k++) {
                preShuffledPasswordString += String.valueOf(rand.nextInt(9));
                i++;
            }
        }

        // generate required number of special case characters
        if(!fx_PasswordSpecialCaseSlider.isDisable()) {
            for (int k = 0; k < fx_PasswordSpecialCaseSlider.getValue(); k++) {
                preShuffledPasswordString += String.valueOf(specialCharacters.charAt(rand.nextInt(specialCharacters.length())));
                i++;
            }
        }

        // generate required number of upper case characters
        if(!fx_PasswordUpperCaseSlider.isDisable()) {
            for (int k = 0; k < fx_PasswordUpperCaseSlider.getValue(); k++) {
                preShuffledPasswordString += String.valueOf(alphaCharactersUpper.charAt(rand.nextInt(alphaCharactersUpper.length()))).toUpperCase();
                i++;
            }
        }

        // fill in the extra spaces with lower case characters
        for (; i < fx_PasswordLenSlider.getValue(); i++) {
            preShuffledPasswordString += String.valueOf(alphaCharactersLower.charAt(rand.nextInt(alphaCharactersLower.length())));
        }


        // leaving the following two lines here for now -- temp artifacts from my refactor
        //shuffledPasswordString = scramble(rand, preShuffledPasswordString);
        //fx_GeneratedPassword.setText(shuffledPasswordString);


        return scramble(rand, preShuffledPasswordString);
    }

}
