package com.borabaskan.gelirTask.mapper;

import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.dto.HesapHareketiDto;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.HesapHareketi;

public interface IModelMapper {//ModelMapperImpl sinifinin implement ettigi interface ve metod imzalari

    HesapDto mapToHesapDto(Hesap entity);
    Hesap mapToHesapEntity(HesapDto dto);
    HesapHareketiDto mapToHesapHareketiDto(HesapHareketi entity);
    HesapHareketi mapToHesapHareketiEntity(HesapHareketiDto dto);
}
