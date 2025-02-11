package com.borabaskan.gelirTask.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.service.IHesapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController// RESTful web service olarak tanimlandi (Controller)
@RequestMapping("/api/hesaplar") //base url tum metodlar bu yol ile erisilebilir
@RequiredArgsConstructor
public class HesapController {// HesapController sinif hesaplarla ilgili crud islemlerini gerceklestiren endpointleri tanimlar

	@Autowired
	private IHesapService hesapService; //Hesap islemlerini gerceklestiren metotlarin tanimli oldugu interface

	
	@PostMapping(path = "/save") // Yeni bir hesap olusturma
	public HesapDto hesapOlustur(@RequestBody @Valid HesapDto hesapDto) {
        HesapDto createdHesap = hesapService.hesapOlustur(hesapDto); //service deki ilgili metodu cagirarak hesap olusturur
		//ve olusturulan hesabi doner
        return createdHesap;
    }

	@GetMapping(path = "/get-all-data")//Tum hesaplari getirme
	public List<HesapDto> getallHesap() { //Liste dondurerek tum hesaplari liste halinde getirmek icin
		
		return hesapService.getallHesap();
	}

	@PutMapping(path = "/update/{id}")// Hesap guncelleme
	public HesapDto hesapGuncelle(@PathVariable (name = "id") UUID hesapId, @RequestBody @Valid HesapDto hesapDto) {
		//path uzerinden bir id almali ve secilen id deki hesap icin bir hesapDto alir bu sayede web tarafinda verilen
		//degerler bu metod uzerinden guncellenir

		return hesapService.hesapGuncelle(hesapId, hesapDto);
	}
	
	@DeleteMapping(path = "/delete/{id}") //Hesap silme
	public void hesapSil(@PathVariable (name = "id") @Valid UUID hesapId) {// Girilen id deki hesabi silmek icin yazildi
		//sadece path uzerinde yazilan id nin oldugu hesabi siler
		
		hesapService.hesapSil(hesapId);
	}
	
	@GetMapping(path = "/find/{id}")// Hesap bulma
	public HesapDto hesapSorgula(@PathVariable (name = "id") @Valid UUID hesapId) {// Girilen id deki hesabi bulmak icin yazildi
		//sadece path uzerinde yazilan id nin oldugu hesabi dondurur

		return hesapService.hesapSorgula(hesapId);
	}
	
	@PutMapping(path = "/addMoney/{id}")// Hesaba para ekleme
	public HesapDto paraYatir(@PathVariable(name = "id") UUID hesapId,@RequestBody BigDecimal miktar) {// path uzerinde
		//bir id ye ve body de ise eklenecek paraya gereksinim duyar (miktar) akabinde hesaba para ekler, degisiklik olan
		// hesabi doner

		return hesapService.paraYatir(hesapId, miktar);
	}
	
	@PutMapping(path = "/takeMoney/{id}")// Hesaptan para cekme
	public HesapDto paraCek(@PathVariable(name = "id") UUID hesapId, @RequestBody BigDecimal miktar) {// path uzerinde
		//bir id ye ve body de ise cekilecek paraya gereksinim duyar (miktar) akabinde hesaba para ceker ve degisiklik olan
		// hesabi doner

		return hesapService.paraCek(hesapId, miktar);
	}
	
}
