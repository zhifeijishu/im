package com.tksflysun.im.common.compress.support;

import java.io.IOException;

import org.xerial.snappy.Snappy;

import com.tksflysun.im.common.compress.Icompress;

public class SnappyCompress implements Icompress {
    private volatile static SnappyCompress snappyCompress;

    private SnappyCompress() {

    }

    public static SnappyCompress getInstance() {
        if (snappyCompress == null) {
            synchronized (SnappyCompress.class) {
                if (snappyCompress == null) {
                    snappyCompress = new SnappyCompress();
                }
            }
        }
        return snappyCompress;
    }

    @Override
    public byte[] compress(byte[] b) {
        try {
            return Snappy.compress(b);
        } catch (IOException e) {
            return b;
        }
    }

    @Override
    public byte[] uncompress(byte[] b) {
        try {
            return Snappy.uncompress(b);
        } catch (IOException e) {
            return b;
        }
    }

}
