package com.finance.hytone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Output {
    int getout(Context ctx) throws IOException {
        ContactFetch cf = new ContactFetch();
        List c = cf.getContacts(ctx);
        String s = c.toString();
        Log.e("Conta", s);
        File myObj = new File("Contacts.txt");
        FileWriter o = new FileWriter("Contacts.txt");
        //o.write(cf.getContacts(ctx));
        return 1;
    }
}
