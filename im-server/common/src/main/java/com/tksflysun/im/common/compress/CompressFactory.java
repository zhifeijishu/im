package com.tksflysun.im.common.compress;

import com.tksflysun.im.common.compress.support.SnappyCompress;

public class CompressFactory {
    public static Icompress getCompress(String type) {
        return SnappyCompress.getInstance();
    }

    public static Icompress getDefaultCompress() {
        return SnappyCompress.getInstance();
    }
}
