package com.nr.viewnote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by vkirillov on 17.09.2015.
 */
public final class Const {
    public Const(){}

    public static final int THUMB_SIZE = 50;

    public static final String SMPL_DATE_FORMAT_STR = "yyyy-MMMM-dd HH:mm:ss";
    public static final DateFormat SMPL_DATE_FORMAT = new SimpleDateFormat(SMPL_DATE_FORMAT_STR);
    public static final String ENTITY_ID = "entityId";
}
