package com.questionarium.assessment_service.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.questionarium.assessment_service.mapper.AlternativeSnapshotMapper;

@Configuration
public class MapStructConfig {

    // problema de bean AlternativeSnapshotMapper exista antes de ser injetado nos mappers
    @Bean
    public AlternativeSnapshotMapper alternativeSnapshotMapper() {
        return Mappers.getMapper(AlternativeSnapshotMapper.class);
    }
}