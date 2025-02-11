package com.borabaskan.gelirTask.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.borabaskan.gelirTask.dto.HesapDto;
import org.springframework.stereotype.Service;


public interface IHesapService {//HesapServiceImpl metodlarini tanimlamak icin interface

	// Hesap Olusturma
	public HesapDto hesapOlustur(HesapDto hesapDto);
	
	// Butun Hesaplari Gosterme
	public List<HesapDto> getallHesap();
	
	// Hesap Guncelleme
	public HesapDto hesapGuncelle(UUID hesapId, HesapDto hesapDto);
	
	// Hesap Silme
	public void hesapSil(UUID hesapId);
	
	// Hesap Sorgulama
	public HesapDto hesapSorgula(UUID hesapId);

	//Hesaba para yatirma
	public HesapDto paraYatir(UUID hesapId, BigDecimal miktar);
	
	// Hesaptan para cekme
	public HesapDto paraCek(UUID hesapId, BigDecimal miktar);

}
