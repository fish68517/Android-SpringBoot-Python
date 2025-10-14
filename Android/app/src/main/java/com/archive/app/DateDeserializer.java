package com.archive.app;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDeserializer implements JsonDeserializer<Date> {
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",
            "EEE MMM dd HH:mm:ss zzz yyyy"
    };

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        Date date = null;

        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                date = dateFormat.parse(dateString);
                System.out.println("Parsed date: " + date);
                return date;
            } catch (ParseException e) {
                // Ignore and try the next format
                System.out.println("Unable to parse date string using format: " + format);
            }
        }

        // If none of the formats work, return null or throw an exception
        System.out.println("Load news failed: Unable to parse date string: " + dateString);
        return null;
    }
}