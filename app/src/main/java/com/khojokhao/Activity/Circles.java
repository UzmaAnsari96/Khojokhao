package com.khojokhao.Activity;

public class Circles {
    private String name;
    private String code;
    private String cid;

    public Circles() {
    }

    public Circles(String code, String name, String cid) {
        this.code = code;
        this.name = name;
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return cid;
    }

    public String getCode() {
        return code;
    }
}

