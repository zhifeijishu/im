package com.tksflysun.im.common.compress;

public interface Icompress {
    byte[] compress(byte[] b);

    byte[] uncompress(byte[] b);
}
