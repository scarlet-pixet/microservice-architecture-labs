package org.example.mapper;

import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.model.FinancialOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OperationMapper {

    @Mapping(target = "user", source = "userId")
    FinancialOperation map(OperationCreateDTO dto);

    @Mapping(target = "userId", source = "user.id")
    OperationDTO map(FinancialOperation operation);

    @Mapping(target = "user", source = "userId")
    void update(OperationUpdateDTO dto, @MappingTarget FinancialOperation operation);
}
