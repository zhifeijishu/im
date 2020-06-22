package com.tksflysun.im.common.serialize;

import com.tksflysun.im.common.serialize.support.ProtoStuffSerialize;

public class SerializeFactory {
    public static Iserialize getSerialize(String type) {
        if ("protostuff".equals(type)) {
            return ProtoStuffSerialize.getInstance();
        } else {
            return ProtoStuffSerialize.getInstance();
        }
    }

    public static Iserialize getDefaultSerialize() {
        return ProtoStuffSerialize.getInstance();

    }
}
