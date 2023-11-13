package com.my.ad.service;

import  com.my.ad.exception.AdException;
import  com.my.ad.vo.AdUnitDistrictRequest;
import  com.my.ad.vo.AdUnitDistrictResponse;
import  com.my.ad.vo.AdUnitItRequest;
import  com.my.ad.vo.AdUnitItResponse;
import  com.my.ad.vo.AdUnitKeywordRequest;
import  com.my.ad.vo.AdUnitKeywordResponse;
import  com.my.ad.vo.AdUnitRequest;
import  com.my.ad.vo.AdUnitResponse;
import  com.my.ad.vo.CreativeUnitRequest;
import  com.my.ad.vo.CreativeUnitResponse;


public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request)
        throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request)
        throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)
        throws AdException;

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request)
        throws AdException;
}
