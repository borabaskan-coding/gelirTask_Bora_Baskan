package com.borabaskan.gelirTask.service;

import com.borabaskan.gelirTask.dto.HesapHareketiDto;

public interface IHesapHareketService {//HesapServiceHareketImpl metodlarini tanimlamak icin interface
	
	public HesapHareketiDto hesapHareketEkle(HesapHareketiDto hesapHareketiDto);//Bir hesap hareketi eklemek icin
	//HesapHareketiDto alir ve guncellenmis HesapHareketDto doner
}
