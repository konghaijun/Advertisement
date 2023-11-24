package com.my.ad.mysql.listener;

import  com.my.ad.mysql.dto.BinlogRowData;


public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}
