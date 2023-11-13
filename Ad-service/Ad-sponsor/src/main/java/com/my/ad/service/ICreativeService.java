package com.my.ad.service;

import  com.my.ad.vo.CreativeRequest;
import  com.my.ad.vo.CreativeResponse;


public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request);
}
