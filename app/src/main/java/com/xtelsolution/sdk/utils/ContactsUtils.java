package com.xtelsolution.sdk.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ContactsCallback;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.ContactsEntity;

import java.util.ArrayList;
import java.util.List;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

/**
 * Created by vivu on 11/24/17.
 */

public class ContactsUtils {


    private static final String TAG = "ContactsUtils";


    // Method that return all contact details in array format
    public static void readContacts(ContactsCallback contactsCallback) {
        ArrayList<Contact_Model> contactList = new ArrayList<Contact_Model>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = MyApplication.getContext().getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return
        // all
        // contacts
        // name
        // containing
        // in
        // URI
        // in
        // ascending
        // order
        // Move cursor at starting
        if (contactsCursor != null && contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = MyApplication.getContext().getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID

                // Strings to get all details
                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                // in BLOB
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                String contactMobile = "";
                String contactHome = "";
                String contactWork = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";

                // Now start the cusrsor
                if (dataCursor != null && dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get
                    // the
                    // contact
                    // name
                    do {
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor
                                    .getColumnIndex("data1")); // Get Nick Name
                            contactOtherDetails += "NickName : " + nickName
                                    + "\n";// Add the nick name to string

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactHome = homePhone.trim();
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactWork = workPhone.trim();
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactMobile = mobilePhone.trim();
                                    break;

                            }
                        }
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactEmailAddresses += "Home Email : "
                                            + homeEmail + "\n";
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactEmailAddresses += "Work Email : "
                                            + workEmail + "\n";
                                    break;

                            }
                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                            companyName = dataCursor.getString(dataCursor
                                    .getColumnIndex("data1"));// get company
                            // name
                            contactOtherDetails += "Coompany Name : "
                                    + companyName + "\n";
                            title = dataCursor.getString(dataCursor
                                    .getColumnIndex("data4"));// get Company
                            // title
                            contactOtherDetails += "Title : " + title + "\n";

                        }


                    } while (dataCursor.moveToNext()); // Now move to next
                    // cursor

                    if (!android.text.TextUtils.isEmpty(contactMobile)) {
                        contactMobile = contactMobile.replace(" ", "").replace("+", "").replace("-", "");
                    }
                    if (!android.text.TextUtils.isEmpty(contactHome)) {
                        contactHome = contactHome.replace(" ", "").replace("+", "").replace("-", "");
                    }
                    if (!android.text.TextUtils.isEmpty(contactWork)) {
                        contactWork = contactWork.replace(" ", "").replace("+", "").replace("-", "");
                    }

                    contactList.add(new Contact_Model(Long.toString(contctId),
                            displayName, contactHome, contactMobile, contactWork, contactEmailAddresses, contactOtherDetails));// Finally add
                    // items to
                    // array list
                }

            } while (contactsCursor.moveToNext());
        }
        if (contactList.size() > 0) {
            contactsCallback.processSuccess(contactList);
        } else {
            contactsCallback.processError("List empty");
        }
//        return contactList;
    }

    public static List<ContactsEntity> getContactDisplayNameByNumber(Context context, String number) {
        List<ContactsEntity> contactsList = new ArrayList<>();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name;
        String phone;

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };


        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactsCursor = contentResolver.query(uri, projection, null, null, null);
        if (contactsCursor != null) {
            while (contactsCursor.moveToNext()) {
                name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                phone = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(new ContactsEntity(name, phone));
            }
            contactsCursor.close();
        }
        Log.e(TAG, "getContactDisplayNameByNumber: " + contactsList.toString());
        return contactsList;
    }
//
//    public static List<ContactsEntity> getContact(String regex) {
//        Intent search = new Intent("android.intent.action.SEARCH");
//        List<ContactsEntity> list = new ArrayList<>();
//
//        Cursor nameCursor = MyApplication.context.getContentResolver().query(search.getData(), null, null, null, null);
//        Cursor phoneCursor = MyApplication.context.getContentResolver().query(search.getData(), null, null, null, null);
//
//        if (nameCursor != null && phoneCursor != null) {
//            while (nameCursor.moveToNext()) {
//                nameCursor.moveToFirst();
//                int idDisplayPhone = nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER);
//                String phone = nameCursor.getString(idDisplayPhone);
//                nameCursor.close();
//
//
//                phoneCursor.moveToFirst();
//                int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                String name = phoneCursor.getString(idDisplayName);
//                phoneCursor.close();
//
//                list.add(new ContactsEntity(name, phone));
//            }
//        }
//    }


    /**
     * New Feature
     */
//    private String getDisplayPhoneForContact(Intent intent) {
//        Cursor phoneCursor = MyApplication.context.getContentResolver().query(intent.getData(), null, null, null, null);
//        phoneCursor.moveToFirst();
//        int idDisplayPhone = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER);
//        String phone = phoneCursor.getString(idDisplayPhone);
//        phoneCursor.close();
//        return phone;
//    }
//
//    private String getDisplayNameForContact(Intent intent) {
//
//        Cursor phoneCursor = MyApplication.context.getContentResolver().query(intent.getData(), null, null, null, null);
//        phoneCursor.moveToFirst();
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        String name = phoneCursor.getString(idDisplayName);
//        phoneCursor.close();
//        return name;
//    }
}
