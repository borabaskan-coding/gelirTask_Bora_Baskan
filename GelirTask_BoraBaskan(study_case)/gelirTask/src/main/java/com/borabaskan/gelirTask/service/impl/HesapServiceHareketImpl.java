package com.borabaskan.gelirTask.service.impl;

import com.borabaskan.gelirTask.dto.HesapHareketiDto;
import com.borabaskan.gelirTask.entities.HesapHareketi;
import com.borabaskan.gelirTask.mapper.IModelMapper;
import com.borabaskan.gelirTask.repository.HesapHareketiRepository;
import com.borabaskan.gelirTask.repository.HesapRepository;
import com.borabaskan.gelirTask.service.IHesapHareketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Autowired spring de otomatik bagimlilik enjeksiyonu (dependency injection) icin kullanilir

@Service // Bu sinifin bir service sinifi oldugunu belirtmek icin kullanildi
public class HesapServiceHareketImpl implements IHesapHareketService {

	@Autowired
	private HesapHareketiRepository hesapHareketiRepository;

	@Autowired
	private HesapRepository hesapRepository;

	@Autowired
	private IModelMapper modelMapper;

	@Override
	public HesapHareketiDto hesapHareketEkle(HesapHareketiDto hesapHareketiDto) {//Hesap hareketi tablosu

		// Dto dan entity e donusum
		HesapHareketi hesapHareketi = modelMapper.mapToHesapHareketiEntity(hesapHareketiDto);

		//hesapHareketi icin db den hesapRepository den ilgili hesabi bulur bulunan hesap hesapHareketi entity e set edilir
		//bulunamaz ise hata firlatir
		hesapHareketi.setHesap(hesapRepository.findById(hesapHareketiDto.getHesapId()).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi")));

		//Hesap hareketini kaydetme islemi
		hesapHareketiRepository.save(hesapHareketi);

		//hesapHareketi entity sini dto ya donusturerek dondurme islemi
		return modelMapper.mapToHesapHareketiDto(hesapHareketi);
	}
}
