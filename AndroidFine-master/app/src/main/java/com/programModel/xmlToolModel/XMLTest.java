package com.programModel.xmlToolModel;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class XMLTest {
    private static String string;
    private TextView tv_show_entity;

    public static void startXML(View view) {
//        SAXService saxService = new SAXService();
//        DOMService domService = new DOMService();
        PULLService pullService = new PULLService();
        try {

            InputStream inputStream = new BufferedInputStream(new FileInputStream("./Users.xml"));
           /* InputStream inputStream = getAssets().open("Users.xml");*/
//            List<Person> persons = saxService.getPerson(inputStream);
//            List<Person> persons = domService.getPersons(inputStream);
            List<Person> persons = pullService.getPersons(inputStream);
            for (Person person : persons) {
                Log.e("TAG", person.toString());
                string += person.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
