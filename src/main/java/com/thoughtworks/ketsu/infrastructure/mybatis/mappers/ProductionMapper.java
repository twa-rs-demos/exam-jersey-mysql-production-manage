package com.thoughtworks.ketsu.infrastructure.mybatis.mappers;


import com.thoughtworks.ketsu.domain.production.Production;

import java.util.List;

public interface ProductionMapper {

    List<Production> findAll();

    Production findById(Integer id);

    int insert(Production production);

    int deleteById(Integer id);

    int update(Production production);
}