package com.borabaskan.gelirTask.controller;

import com.borabaskan.gelirTask.GelirTaskApplication;
import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.enums.HesapTuruEnum;
import com.borabaskan.gelirTask.repository.HesapRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GelirTaskApplication.class)
@AutoConfigureMockMvc
@Transactional
public class HesapControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HesapRepository hesapRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        hesapRepository.deleteAll();
    }

    //Bu sinif kod tekranin onune gecmek icin yazildi (Hesap Olusturma)
    private HesapDto createHesapDto(String ad, String soyad, String kimlikNo, HesapTuruEnum turu, BigDecimal bakiye) {

        HesapDto hesapDto = new HesapDto();
        hesapDto.setHesapSahipAd(ad);
        hesapDto.setHesapSahipSoyad(soyad);
        hesapDto.setHesapSahipKimlikNo(kimlikNo);
        hesapDto.setHesapTuru(turu);
        hesapDto.setBakiye(bakiye);
        return hesapDto;
    }

    //Bu sinif kod tekranin onune gecmek icin yazildi (Kaydetme)
    private void saveHesap(HesapDto hesapDto) throws Exception {
        mockMvc.perform(post("/api/hesaplar/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hesapDto)))
                .andExpect(status().isOk());
    }

    //Bu sinif kod tekranin onune gecmek icin yazildi (Yazdirma)
    private void printHesap(Hesap hesap) {
        System.out.println("Hesap:");
        System.out.println("ID: " + hesap.getId());
        System.out.println("Bakiye: " + hesap.getBakiye());
        System.out.println("Hesap Sahip Ad: " + hesap.getHesapSahipAd());
        System.out.println("Hesap Sahip Soyad: " + hesap.getHesapSahipSoyad());
        System.out.println("Hesap Sahip Kimlik No: " + hesap.getHesapSahipKimlikNo());
        System.out.println("Hesap Turu: " + hesap.getHesapTuru());
    }

    //Bu sinif kod tekranin onune gecmek icin yazildi (Kontrol)
    private void assertHesap(Hesap hesap, String ad, String soyad, String kimlikNo, HesapTuruEnum turu, BigDecimal bakiye) {
        assertNotNull(hesap);
        assertEquals(ad, hesap.getHesapSahipAd());
        assertEquals(soyad, hesap.getHesapSahipSoyad());
        assertEquals(kimlikNo, hesap.getHesapSahipKimlikNo());
        assertEquals(turu, hesap.getHesapTuru());
    }

    @Test
    void testHesapOlustur() throws Exception {
        HesapDto hesapDto = createHesapDto("integration-test-name", "integration-test-lastName",
                "10293848576", HesapTuruEnum.TL, BigDecimal.ZERO);//hesapDto olusturuldu

        //HTTP Post istegi simule edildi

        mockMvc.perform(post("/api/hesaplar/save")//Post istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON)//Istek Json formatinda olmali
                        .content(objectMapper.writeValueAsString(hesapDto)))//hesapDto nesnesi Json formatina donusturuldu
                .andExpect(status().isOk())// yanitin 200 oldugunu dogrulama
                .andExpect(jsonPath("$.bakiye").value(BigDecimal.ZERO))//assert gibi, yanitin beklenen deger dogrulamalari
                .andExpect(jsonPath("$.hesapSahipAd").value("integration-test-name"))
                .andExpect(jsonPath("$.hesapSahipSoyad").value("integration-test-lastName"))
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value("10293848576"))
                .andExpect(jsonPath("$.hesapTuru").value("TL"));

        Hesap savedHesap = hesapRepository.findAll().get(0);
        printHesap(savedHesap);

        assertHesap(savedHesap, "integration-test-name", "integration-test-lastName", "10293848576",
                HesapTuruEnum.TL, BigDecimal.ZERO);//assert metoduna dogrulanacak verilerin gonderilmesi
    }

    @Test
    void testGetAllHesap() throws Exception {

        // 2 farkli hesapDto olusturuldu

        HesapDto hesapDto1 = createHesapDto("integration-test-name1", "integration-test-lastName1",
                "10293848576", HesapTuruEnum.DOLAR, BigDecimal.ZERO);
        HesapDto hesapDto2 = createHesapDto("integration-test-name2", "integration-test-lastName2",
                "10987654321", HesapTuruEnum.TL, BigDecimal.ZERO);

        saveHesap(hesapDto1);
        saveHesap(hesapDto2);// save metodu ile testHesapOlustur metodu calistirildi ve hesapDto lar kaydedildi

        mockMvc.perform(get("/api/hesaplar/get-all-data")//Get istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// yanitin 200 oldugunu dogrulama
                .andExpect(jsonPath("$[0].bakiye").value("0"))//hesaapDto1 icin yanitin beklenen deger dogrulamalari
                .andExpect(jsonPath("$[0].hesapSahipAd").value("integration-test-name1"))
                .andExpect(jsonPath("$[0].hesapSahipSoyad").value("integration-test-lastName1"))
                .andExpect(jsonPath("$[0].hesapSahipKimlikNo").value("10293848576"))
                .andExpect(jsonPath("$[0].hesapTuru").value("DOLAR"))

                .andExpect(jsonPath("$[1].bakiye").value("0"))//hesaapDto2 icin yanitin beklenen deger dogrulamalari
                .andExpect(jsonPath("$[1].hesapSahipAd").value("integration-test-name2"))
                .andExpect(jsonPath("$[1].hesapSahipSoyad").value("integration-test-lastName2"))
                .andExpect(jsonPath("$[1].hesapSahipKimlikNo").value("10987654321"))
                .andExpect(jsonPath("$[1].hesapTuru").value("TL"));

        List<Hesap> hesapList = hesapRepository.findAll();// Kaydedilen hesaplarin veritabanindan cekilmesi
        System.out.println();
        printHesap(hesapList.get(0));//hesapDto1 i yazdirdi
        System.out.println();
        printHesap(hesapList.get(1));//hesapDto2 i yazdirdi

        //HesapList in icindeki iki hesabin assertion ile dogrulanmasi

        assertHesap(hesapList.get(0), "integration-test-name1", "integration-test-lastName1",
                "10293848576", HesapTuruEnum.DOLAR, BigDecimal.ZERO);
        assertHesap(hesapList.get(1), "integration-test-name2", "integration-test-lastName2",
                "10987654321", HesapTuruEnum.TL, BigDecimal.ZERO);
    }

    @Test
    void testHesapGuncelle() throws Exception {
        HesapDto hesapDto = createHesapDto("integration-test-name", "integration-test-lastName",
                "10293848576", HesapTuruEnum.TL, BigDecimal.ZERO);
        saveHesap(hesapDto);

        Hesap savedHesap = hesapRepository.findAll().get(0);//Kaydedilen hesap db den bulundu
        UUID hesapId = savedHesap.getId();//bulunan hesaba bir id atandi

        HesapDto updatedHesapDto = createHesapDto("updated-name", "updated-lastName",
                "10293848576", HesapTuruEnum.TL, new BigDecimal("100.00"));//bulunan hesap burada manuel olarak guncellendi

        mockMvc.perform(put("/api/hesaplar/update/" + hesapId)//Put istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedHesapDto)))
                .andExpect(status().isOk())// yanitin 200 oldugunu dogrulama
                .andExpect(jsonPath("$.bakiye").value("100.0"))//hesapDto icin beklenen deger kontrolu
                .andExpect(jsonPath("$.hesapSahipAd").value("updated-name"))
                .andExpect(jsonPath("$.hesapSahipSoyad").value("updated-lastName"))
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value("10293848576"))
                .andExpect(jsonPath("$.hesapTuru").value("TL"));

        Hesap updatedHesap = hesapRepository.findById(hesapId).orElse(null);// hesapRepository de hesap bulunursa hesabi donecek
        //hesap bulunamaz ise null donecek
        assertHesap(updatedHesap, "updated-name", "updated-lastName", "10293848576",
                HesapTuruEnum.TL, new BigDecimal("100.00"));//beklenen deger kontrolu

        System.out.println("Guncellenmemis hesap bakiyesi: " + hesapDto.getBakiye());
        System.out.println("Guncellenmis hesap bakiyesi: " + updatedHesapDto.getBakiye());

    }

    @Test
    void testHesapSil() throws Exception {
        HesapDto hesapDto = createHesapDto("integration-test-name", "integration-test-lastName",
                "10293848576", HesapTuruEnum.TL, BigDecimal.ZERO);
        saveHesap(hesapDto);

        Hesap savedHesap = hesapRepository.findAll().get(0);
        UUID hesapId = savedHesap.getId();

        mockMvc.perform(delete("/api/hesaplar/delete/" + hesapId)//Delete istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());// yanitin 200 oldugunu dogrulama

        assertEquals(0, hesapRepository.count());//hesapRepository de beklenen hesap sayisi degerin 0 oldugunu dogrulama
    }

    @Test
    void testHesapSorgula() throws Exception {
        HesapDto hesapDto = createHesapDto("integration-test-name-sorgu", "integration-test-lastName-sorgu",
                "11111111223", HesapTuruEnum.STERLIN, BigDecimal.ZERO);//Hesap sorgulama testi icin bir dto olusturuldu
        saveHesap(hesapDto);//HesapDto kaydedildi

        Hesap savedHesap = hesapRepository.findAll().get(0);//hesapRepository de tek bir hesap kaydi bulundugu icin 0 inci hesap
        //savedHesap a atandi
        UUID hesapId = savedHesap.getId();

        mockMvc.perform(get("/api/hesaplar/find/" + hesapId)//Get istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// yanitin 200 oldugunu dogrulama
                .andExpect(jsonPath("$.bakiye").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.hesapSahipAd").value("integration-test-name-sorgu"))
                .andExpect(jsonPath("$.hesapSahipSoyad").value("integration-test-lastName-sorgu"))
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value("11111111223"))
                .andExpect(jsonPath("$.hesapTuru").value("STERLIN"));
    }

    @Test
    void testParaYatir() throws Exception {
        HesapDto hesapDto = createHesapDto("integration-test-name", "integration-test-lastName",
                "10293848576", HesapTuruEnum.TL, BigDecimal.ZERO);//baslangicta hesap miktar degeri 0 atandi
        saveHesap(hesapDto);

        Hesap savedHesap = hesapRepository.findAll().get(0);
        UUID hesapId = savedHesap.getId();
        BigDecimal miktar = new BigDecimal("100.00");

        mockMvc.perform(put("/api/hesaplar/addMoney/" + hesapId)//Put istegi gonderildi
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(miktar)))//atanacak miktar nesnesini Json formatinda string e cevirdi
                .andExpect(status().isOk())// yanitin 200 oldugunu dogrulama
                .andExpect(jsonPath("$.bakiye").value("100.0"))
                .andExpect(jsonPath("$.hesapSahipAd").value("integration-test-name"))
                .andExpect(jsonPath("$.hesapSahipSoyad").value("integration-test-lastName"))
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value("10293848576"))
                .andExpect(jsonPath("$.hesapTuru").value("TL"));

        Hesap updatedHesap = hesapRepository.findById(hesapId).orElse(null);//hesapRepository icinde bulunan hesabi
        //updatedHesap in icine atadi bulamaz ise null dondurecek
        assertHesap(updatedHesap, "integration-test-name", "integration-test-lastName", "10293848576",
                HesapTuruEnum.TL, new BigDecimal("100.00"));//updatedHesap in beklenen deger kontrolu

        System.out.println("\n"+"Hesap ilk bakiye: " + hesapDto.getBakiye() + "\n");
        System.out.println("Hesap son bakiye: " + updatedHesap.getBakiye() + "\n");
    }

    @Test
    void testParaCek() throws Exception {
        // Hesap nesnesi oluşturma ve ayarlama
        Hesap hesap = new Hesap();
        hesap.setHesapSahipAd("integration-test-name");
        hesap.setHesapSahipSoyad("integration-test-lastName");
        hesap.setHesapSahipKimlikNo("10293848576");
        hesap.setHesapTuru(HesapTuruEnum.TL);
        hesap.setBakiye(new BigDecimal("250.00"));

        System.out.println("\n" + "Hesap ilk bakiye: " + hesap.getBakiye() + "\n");
        // Hesap nesnesini veritabanına kaydetme
        hesapRepository.save(hesap);

        Hesap savedHesap = hesapRepository.findAll().get(0);
        UUID hesapId = savedHesap.getId();
        BigDecimal miktar = new BigDecimal("100.00");//cekilecek miktar tanimlandi

        // Para çekme işlemi
        mockMvc.perform(put("/api/hesaplar/takeMoney/" + hesapId)//Put istegi
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(miktar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bakiye").value("150.0"))//beklenen deger 250-100=150
                .andExpect(jsonPath("$.hesapSahipAd").value("integration-test-name"))
                .andExpect(jsonPath("$.hesapSahipSoyad").value("integration-test-lastName"))
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value("10293848576"))
                .andExpect(jsonPath("$.hesapTuru").value("TL"));

        Hesap updatedHesap = hesapRepository.findById(hesapId).orElse(null);
        assertHesap(updatedHesap, "integration-test-name", "integration-test-lastName", "10293848576",
                HesapTuruEnum.TL, new BigDecimal("150.00"));//assert ile verileri dogrulama


        System.out.println("Hesap son bakiye: " + updatedHesap.getBakiye() + "\n");
    }


}