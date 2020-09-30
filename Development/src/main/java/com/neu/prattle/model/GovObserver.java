package com.neu.prattle.model;

import com.google.gson.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;

public class GovObserver implements PropertyChangeListener {

    private JsonArray allMessages = new JsonArray();

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        String unparsed = gson.toJson(evt.getNewValue().toString());
        JsonElement jsonElement = parser.parse(unparsed);

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyPrint = prettyGson.toJson(jsonElement).replaceAll("/\\r?\\n|\\r/g, ''", "");

        allMessages.add(prettyPrint);
        System.out.println("New message added: " + evt.getNewValue());
    }

    public JsonArray getAllObservedMessages() {
        writeJson();
        return allMessages;
    }

    private void writeJson() {
        try {
            try (FileWriter file = new FileWriter("./json_output.json")) {
                file.write(allMessages.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON created.");
    }
}
