package com.nr.viewnote.db;

import java.util.Date;

/**
 * Note entry for CRUD queries
 */
public class NoteEntity {
    private final int mId;
    private byte[] mImage;
    private byte[] mThumb;
    private String mText;
    private Date mDate;

    /**
     * Constructor for creating some entry existing in database
     * @param id
     */
    public NoteEntity(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        mImage = image;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public byte[] getThumb() {
        return mThumb;
    }

    public void setThumb(byte[] mThumb) {
        this.mThumb = mThumb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteEntity entity = (NoteEntity) o;

        return mId == entity.mId;

    }

    @Override
    public int hashCode() {
        return mId;
    }
}
