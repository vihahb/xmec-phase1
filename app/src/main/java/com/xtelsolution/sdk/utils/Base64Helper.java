package com.xtelsolution.sdk.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/6/2017
 * Email: leconglongvu@gmail.com
 */
public class Base64Helper {

    public static String getEncode(String s) {
        return new String(Base64.encodeBase64(s.getBytes()));
    }

    public static String getDecode(String s) {
        return new String(Base64.decodeBase64(s.getBytes()));
    }
}