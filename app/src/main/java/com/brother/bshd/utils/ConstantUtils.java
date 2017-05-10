package com.brother.bshd.utils;

import com.evernote.client.android.EvernoteSession;

/**
 * Created by wule on 2017/04/05.
 */

public class ConstantUtils {
    //evernote token
    public static final String TOKEN = "S=s1:U=93861:E=16298ac0c09:C=15b40fadc90:P=1cd:A=en-devtoken:V=2:H=0abec87a9f932711eb19beed04151057";
    //your own account url
    public static final String SELF_URL = "https://sandbox.evernote.com/shard/s1/notestore";
    //evernote key
    public static final String CONSUMER_KEY = "levi";
    //evernote secret
    public static final String CONSUMER_SECRET = "39814263f344da67";
    //select run environment
    public static final EvernoteSession.EvernoteService ENVIRONMENT = EvernoteSession.EvernoteService.SANDBOX;
    //intent key
    public static final String KEY = "extra";
    public static final String KEY_NULL = "null";
    public static final String KEY_NAME = "name";

    //Evernote Markup Language
    public static final String DIV_SATRT = "<div>";
    public static final String DIV_END = "</div>";

}
