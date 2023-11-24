package com.my.ad.search;

import   com.my.ad.search.vo.SearchRequest;
import   com.my.ad.search.vo.SearchResponse;


public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
