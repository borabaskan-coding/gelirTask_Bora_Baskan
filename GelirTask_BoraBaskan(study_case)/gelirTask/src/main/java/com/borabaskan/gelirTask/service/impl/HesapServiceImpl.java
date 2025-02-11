package com.borabaskan.gelirTask.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.borabaskan.gelirTask.mapper.IModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.dto.HesapHareketiDto;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.enums.HareketTuruEnum;
import com.borabaskan.gelirTask.repository.HesapHareketiRepository;
import com.borabaskan.gelirTask.repository.HesapRepository;
import com.borabaskan.gelirTask.service.IHesapHareketService;
import com.borabaskan.gelirTask.service.IHesapService;

//@Autowired spring de otomatik bagimlilik enjeksiyonu (dependency injection) icin kullanilir

@Service // Bu sinifin bir service sinifi oldugunu belirtmek icin kullanildi
public class HesapServiceImpl implements IHesapService {

    @Autowired
    private HesapRepository hesapRepository;

    @Autowired
    private IModelMapper modelMapper;

    @Autowired
    private HesapHareketiRepository hesapHareketiRepository;

    @Autowired
    private IHesapHareketService iHesapHareketService;


    @Override
    public HesapDto hesapOlustur(HesapDto hesapDto) {

        //Hesap kontrolu
        Optional<Hesap> mevcutHesap = hesapRepository.findByHesapSahipKimlikNoAndHesapTuru(hesapDto.getHesapSahipKimlikNo(), hesapDto.getHesapTuru());

        if (mevcutHesap.isPresent()) {//Eger olusturulmak istenen hesap zaten var ise
            throw new IllegalArgumentException("Bu kimlik numarası ve hesap türüne ait zaten bir hesap bulunmaktadır.");
            //yukarudaki hatayi firlatir
        }

        Hesap hesap = modelMapper.mapToHesapEntity(hesapDto);//gelen Hesapdto yu Hesap a donusturme islemi
        hesap.setBakiye(BigDecimal.ZERO); // Bakiye sıfırlama işlemi burada yapıldi

        Hesap kaydedilenHesap = hesapRepository.save(hesap);//Donusturulen hesabi repository uzerinden db ye kaydetme islemi
        HesapDto savedHesapDto = modelMapper.mapToHesapDto(kaydedilenHesap);// Kaydedilen hesabi tekrardan Dto ya donusturme

        return savedHesapDto;//Donusturulen dto yu dondurme islemi
    }

    @Override
    public List<HesapDto> getallHesap() {//Birden cok hesap olabilecegi icin List dondurecek
        List<HesapDto> dtoList = new ArrayList<>();// Yeni bir ArrayList tanimlandi
        List<Hesap> hesapList = hesapRepository.findAll();//hesapRepository nin findAll() metodu (JpaRepository) ile tum hesaplar
        //hesapList in icine atildi
        for (Hesap hesap : hesapList) {//for-each ile hesaplar tek tek dtoList e eklendi
            HesapDto dto = modelMapper.mapToHesapDto(hesap);
            dtoList.add(dto);
        }
        return dtoList;// dtoList donduruldu
    }

    @Override
    public HesapDto hesapGuncelle(UUID hesapId, HesapDto hesapDto) {//UUID ve HesapDto alir

        Hesap hesap = hesapRepository.findById(hesapId).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi"));// hesapId(UUID)
        //hesapRepository ile db de taranir bulamaz ise hata firlatir

        BeanUtils.copyProperties(hesapDto, hesap);//Bu kisimda hesapDto nun hesap ile ayni isimdeki ozellikeri hesap a kopyalandi

        // DTO'dan gelen verilerle hesap nesnesini güncelleme

        Hesap guncellenenHesap = hesapRepository.save(hesap);
        HesapDto updatedHesapDto = modelMapper.mapToHesapDto(guncellenenHesap);

        return updatedHesapDto;// guncellenen HesapDto donduruldu
    }

    @Override
    public void hesapSil(UUID hesapId) {
        Hesap hesap = hesapRepository.findById(hesapId).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi"));//Hesap var mi kontrolu

        hesapRepository.delete(hesap);// Hesap bulunur ise bulunan hesap silinir ve silindigi icin void
    }

    @Override
    public HesapDto hesapSorgula(UUID hesapId) {
        Hesap hesap = hesapRepository.findById(hesapId).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi"));//Hesap var mi
        HesapDto hesapDto = modelMapper.mapToHesapDto(hesap);//Bulunan hesabi mapper ile hesapDto ya kopyalar
        return hesapDto;//bulunan hesabin dto sunu dondurur
    }

    @Override
    public HesapDto paraYatir(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi"));//Hesap var mi

        if (hesap.getBakiye().add(miktar).compareTo(new BigDecimal("9999999.99")) > 0) {//bakiyeye eklenen miktari ekler ve
            //ust sinir degerinden (9999999.99) buyuk mu diye kontrol eder buyuk ise 1 doner ve asagidaki hatayi firlatir
            throw new RuntimeException("Bakiye limitini aşamazsınız");
        }

        //mevcut bakiyeye yeni miktari ekleme
        hesap.setBakiye(hesap.getBakiye().add(miktar));

        //HesapHareketiDto nesnesi olusturulur ve hesapId kisimina hesap in id si eklenir
        HesapHareketiDto hesapHareketiDto = new HesapHareketiDto();
        hesapHareketiDto.setHesapId(hesap.getId()); //Hamgi hesaba para girisi oldugunu gostermek icin hesap in id si eklendi
        hesapHareketiDto.setHareketTuru(HareketTuruEnum.YATIRMA);//hareket turu bu metod icin eklendi
        hesapHareketiDto.setMiktar(miktar);//Hesap a eklenen miktar Hesap hareketlerine de eklendi
        hesapHareketiDto.setIslemTarihi(LocalDateTime.now());

        hesapRepository.save(hesap);//hesap kaydedildi
        iHesapHareketService.hesapHareketEkle(hesapHareketiDto);//hesap hareketi kaydedildi
        Hesap sonHesap = hesapRepository.findById(hesap.getId()).get();//guncel hesap tekrar db den cagirildi
        HesapDto savedHesapDto = modelMapper.mapToHesapDto(sonHesap);// dto ya donusturuldu

        return savedHesapDto;//donusturulen dto donduruldu
    }

    @Override
    public HesapDto paraCek(UUID hesapId, BigDecimal miktar) {//Genel olarak para yatirma metodu ile ayni islemler

        Hesap hesap = hesapRepository.findById(hesapId).orElseThrow(() -> new RuntimeException("Hesap Bulunamadi"));//Hesap var mi

        if (hesap.getBakiye().compareTo(miktar) < 0) {//Burada para cekildiktan sonra bakiyenin 0 in altina dusup dusmemesi
            //kontrol edildi
            throw new RuntimeException("Yetersiz bakiye");
        }

        hesap.setBakiye(hesap.getBakiye().subtract(miktar));

        HesapHareketiDto hesapHareketiDto = new HesapHareketiDto();
        hesapHareketiDto.setHesapId(hesap.getId());
        hesapHareketiDto.setHareketTuru(HareketTuruEnum.CEKME);//hareket turu bu metod icin eklendi
        hesapHareketiDto.setMiktar(miktar);
        hesapHareketiDto.setIslemTarihi(LocalDateTime.now());
        hesapRepository.save(hesap);
        iHesapHareketService.hesapHareketEkle(hesapHareketiDto);
        Hesap sonHesap = hesapRepository.findById(hesap.getId()).get();
        HesapDto savedHesapDto = modelMapper.mapToHesapDto(sonHesap);

        return savedHesapDto;
    }
}

