package com.neu.prattle.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class represents a Translation object.
 */
class Translator {

    private Translate translate;

    public Translator() {
        File credentialsPath = new File("src/main/resources/google-apis-credentials.json");
        GoogleCredentials myCredentials;
        try {
            myCredentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath));
            translate = TranslateOptions.newBuilder().setCredentials(myCredentials).build().getService();
        } catch (IOException e) {
            // Empty
        }
    }

    /**
     * Given a string and a language, translate the given text into the
     * preferred language.
     *
     * @param text to be translated
     * @param lang to which the text will be translated
     * @return the translated text
     */
    public String translate(String text, String lang) {
        // detect original language
        String original = getOriginalLanguage(text);

        // translate text from original language to new language
        Translation translation = translate.translate(
                text,
                TranslateOption.sourceLanguage(original),
                TranslateOption.targetLanguage(lang));

        return translation.getTranslatedText();
    }

    /**
     * Helper method to detect the language of the incoming text.
     *
     * @param text whose language need to be detected
     * @return the string value of the language
     */
    private String getOriginalLanguage(String text) {
        Detection detect = translate.detect(text);

        return detect.getLanguage();
    }
}