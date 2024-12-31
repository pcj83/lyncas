package com.paulocezarjr.contasapagar.mapper;

import com.paulocezarjr.contasapagar.domain.Conta;
import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContaMapper {

    Conta toEntity(ContaCreateDTO dto);

    ContaResponseDTO toResponseDTO(Conta entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ContaUpdateDTO dto, @MappingTarget Conta entity);
}