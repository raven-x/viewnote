package com.nr.androidutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

import nr.com.androidutils.R;

/**
 * Created by Limi on 12.09.2015.
 */
public class UtilsTest extends AndroidTestCase {

    private Bitmap mBitmap;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.testimg);
    }

    public void testConvertBitmapToByteArrayCompressed(){
        byte[] result = BitmapUtils.convertBitmapToByteArray(mBitmap);

        assertNotNull(result);
        assertTrue(result.length > 0);

        mBitmap = BitmapUtils.convertCompressedByteArrayToBitmap(result);

        assertNotNull(mBitmap);
        assertTrue(mBitmap.getByteCount() > 0);
    }

    public void testConvertBitmapToByteArrayUncompressed(){
        byte[] result = BitmapUtils.convertBitmapToByteArrayUncompressed(mBitmap);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
