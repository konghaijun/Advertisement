package com.my.ad.service.impl;

import  com.my.ad.constant.Constants;
import  com.my.ad.dao.AdPlanRepository;
import  com.my.ad.dao.AdUnitRepository;
import  com.my.ad.dao.CreativeRepository;
import  com.my.ad.dao.unit_condition.AdUnitDistrictRepository;
import  com.my.ad.dao.unit_condition.AdUnitItRepository;
import  com.my.ad.dao.unit_condition.AdUnitKeywordRepository;
import  com.my.ad.dao.unit_condition.CreativeUnitRepository;
import  com.my.ad.entity.AdPlan;
import  com.my.ad.entity.AdUnit;
import  com.my.ad.entity.unit_condition.AdUnitDistrict;
import  com.my.ad.entity.unit_condition.AdUnitIt;
import  com.my.ad.entity.unit_condition.AdUnitKeyword;
import  com.my.ad.entity.unit_condition.CreativeUnit;
import  com.my.ad.exception.AdException;
import  com.my.ad.service.IAdUnitService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;

    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository,
                             AdUnitRepository unitRepository,
                             AdUnitKeywordRepository unitKeywordRepository,
                             AdUnitItRepository unitItRepository,
                             AdUnitDistrictRepository unitDistrictRepository, CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }


    @Override
    public AdUnitResponse createUnit(AdUnitRequest request)
            throws AdException {

        // 验证请求参数的有效性
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        // 查询对应的广告计划，如果不存在则抛出异常
        Optional<AdPlan> adPlan =
                planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        // 检查同名广告单元是否存在，如果存在则抛出异常
        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(), request.getUnitName()
        );
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

        // 创建新的广告单元并保存到数据库中
        AdUnit newAdUnit = unitRepository.save(
                new AdUnit(request.getPlanId(), request.getUnitName(),
                        request.getPositionType(), request.getBudget())
        );

        // 返回新创建的广告单元的ID和名称
        return new AdUnitResponse(newAdUnit.getId(),
                newAdUnit.getUnitName());
    }
    @Override
    public AdUnitKeywordResponse createUnitKeyword(
            AdUnitKeywordRequest request) throws AdException {

        // 提取请求中的广告单元ID列表
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());

        // 检查相关的广告单元是否存在，如果不存在则抛出异常
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        // 初始化ID列表
        List<Long> ids = Collections.emptyList();

        // 创建存储广告单元关键词的列表
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {
            // 遍历请求中的广告单元关键词，逐个添加到列表中
            request.getUnitKeywords().forEach(i -> unitKeywords.add(
                    new AdUnitKeyword(i.getUnitId(), i.getKeyword())
            ));
            // 保存所有的广告单元关键词并提取其ID列表
            ids = unitKeywordRepository.saveAll(unitKeywords).stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }

        // 返回包含新创建广告单元关键词ID列表的响应对象
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(
            AdUnitItRequest request) throws AdException {

        // 提取请求中的广告单元ID列表
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());

        // 检查相关的广告单元是否存在，如果不存在则抛出异常
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        // 创建存储广告单元兴趣标签的列表
        List<AdUnitIt> unitIts = new ArrayList<>();
        // 遍历请求中的广告单元兴趣标签，逐个添加到列表中
        request.getUnitIts().forEach(i -> unitIts.add(
                new AdUnitIt(i.getUnitId(), i.getItTag())
        ));
        // 保存所有的广告单元兴趣标签并提取其ID列表
        List<Long> ids = unitItRepository.saveAll(unitIts).stream()
                .map(AdUnitIt::getId)
                .collect(Collectors.toList());

        // 返回包含新创建广告单元兴趣标签ID列表的响应对象
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(
            AdUnitDistrictRequest request) throws AdException {

        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        request.getUnitDistricts().forEach(d -> unitDistricts.add(
                new AdUnitDistrict(d.getUnitId(), d.getProvince(),
                        d.getCity())
        ));
        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(
            CreativeUnitRequest request) throws AdException {

        List<Long> unitIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());

        if (!(isRelatedUnitExist(unitIds) && isRelatedUnitExist(creativeIds))) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i -> creativeUnits.add(
                new CreativeUnit(i.getCreativeId(), i.getUnitId())
        ));

        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits)
                .stream()
                .map(CreativeUnit::getId)
                .collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {

        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        return unitRepository.findAllById(unitIds).size() ==
                new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds) {

        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        return creativeRepository.findAllById(creativeIds).size() ==
                new HashSet<>(creativeIds).size();
    }
}
