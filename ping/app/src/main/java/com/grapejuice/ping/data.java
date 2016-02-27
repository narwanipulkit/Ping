package com.grapejuice.ping;

import java.util.List;

/**
 * Created by pulkitnarwani on 30/10/15.
 */
public class data {

    data()
    {

    }
    String nws,usr;
    data(String nws,String usr)
    {
        this.nws=nws;
        this.usr=usr;
    }

    static List<data> a;

    public void addobj(data b){
        a.add(b);
    }
    public List<data> getList()
    {
        return a;
    }

}
