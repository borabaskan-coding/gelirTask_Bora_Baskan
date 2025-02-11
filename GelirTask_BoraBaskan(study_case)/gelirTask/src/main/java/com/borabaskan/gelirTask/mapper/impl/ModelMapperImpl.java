package com.borabaskan.gelirTask.mapper.impl;

import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.dto.HesapHareketiDto;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.HesapHareketi;
import com.borabaskan.gelirTask.mapper.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component// Bu sinifin spring tarafindan otomatik algilanip yonetilmesini saglar
public class ModelMapperImpl implements IModelMapper {//Bu sinif modelMapper sinifini ozellestirmek icin yazilmistir
    //ozellestirmenin sebebi unitTest asamasinda modelMapper ile maplenen dto larin null deger dondurmesinin onune gecmektir
    private final ModelMapper modelMapper;

    @Autowired // spring de bagimlilik enjeksiyonu (dependency injection) icin kullanilir
    public ModelMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override // override bu alan icin IModelMapper interface inin metodlari icin kullanildi
    public HesapDto mapToHesapDto(Hesap entity) {
        return modelMapper.map(entity, HesapDto.class);
    }

    @Override
    public Hesap mapToHesapEntity(HesapDto dto) {
        return modelMapper.map(dto, Hesap.class);
    }

    @Override
    public HesapHareketiDto mapToHesapHareketiDto(HesapHareketi entity) {
        return modelMapper.map(entity, HesapHareketiDto.class);
    }

    @Override
    public HesapHareketi mapToHesapHareketiEntity(HesapHareketiDto dto) {
        return modelMapper.map(dto, HesapHareketi.class);
    }
}
