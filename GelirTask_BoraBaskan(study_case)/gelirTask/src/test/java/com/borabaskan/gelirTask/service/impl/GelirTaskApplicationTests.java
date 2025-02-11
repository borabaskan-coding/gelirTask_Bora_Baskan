package com.borabaskan.gelirTask.service.impl;

import static com.borabaskan.gelirTask.entities.enums.HesapTuruEnum.DOLAR;
import static com.borabaskan.gelirTask.entities.enums.HesapTuruEnum.STERLIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.borabaskan.gelirTask.dto.HesapHareketiDto;
import com.borabaskan.gelirTask.entities.enums.HareketTuruEnum;
import com.borabaskan.gelirTask.mapper.IModelMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.borabaskan.gelirTask.dto.HesapDto;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.repository.HesapRepository;
import com.borabaskan.gelirTask.service.IHesapHareketService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)//jUnit testlerinde spring destegi
@MockitoSettings(strictness = Strictness.LENIENT)//Mockito nun daha esnek olmasini saglar (gereksiz uyari ve hatalari onler)
@ActiveProfiles("test")

class HesapServiceImplTest {

	@InjectMocks//Test sinifinin bagimliliklarini otomatik enjekte eder( @Autowired gibi )
	private HesapServiceImpl hesapService;

	@Mock // Sahte bir repository nesnesi olusturulmak icin kullanildi
	private IHesapHareketService ihesapHareketService;

	@Mock
	private IModelMapper modelMapper;

	@Mock
	private HesapRepository hesapRepository;

	private Hesap hesap;
	private Hesap hesap2;
	private HesapDto hesapDto;
	private HesapDto hesapDto2;
	private UUID hesapId;
	private UUID hesapId2;

	@BeforeEach// Test metodlarindan once calistirilir bu sayede metodlarin ihtiyac duydugu nesneler bu ornek icin olusturlmus oldu
	void setUp() {
		hesapId = UUID.randomUUID();
		hesap = new Hesap();
		hesap.setId(hesapId);
		hesap.setHesapSahipAd("Test-name");
		hesap.setHesapSahipSoyad("Test-lastname");
		hesap.setBakiye(BigDecimal.ZERO);
		hesap.setHesapSahipKimlikNo("12345678901");
		hesap.setHesapTuru(DOLAR);

		hesapId2 = UUID.randomUUID();
		hesap2 = new Hesap();
		hesap2.setId(hesapId);
		hesap2.setHesapSahipAd("Test-name2");
		hesap2.setHesapSahipSoyad("Test-lastname2");
		hesap2.setBakiye(BigDecimal.ZERO);
		hesap2.setHesapSahipKimlikNo("98765432101");
		hesap2.setHesapTuru(STERLIN);

		hesapDto = new HesapDto();
		hesapDto.setHesapSahipKimlikNo("12345678901");
		hesapDto.setHesapTuru(DOLAR);
		hesapDto.setBakiye(BigDecimal.ZERO);
		hesapDto.setHesapSahipAd("Test-name");
		hesapDto.setHesapSahipSoyad("Test-lastname");

		hesapDto2 = new HesapDto();
		hesapDto2.setHesapSahipKimlikNo("98765432101");
		hesapDto2.setHesapTuru(STERLIN);
		hesapDto2.setBakiye(BigDecimal.ZERO);
		hesapDto2.setHesapSahipAd("Test-name2");
		hesapDto2.setHesapSahipSoyad("Test-lastname2");

	}

	@AfterEach// metodlarin calistirilmasi bittiginde calistirilir
	void tearDown() {
		System.out.println("Test basarili.");
	}

	@Test
	void testHesapOlustur() {

		//when()...thenReturn() kullanimi sahte mock davranislari tanimlar
		when(hesapRepository.findByHesapSahipKimlikNoAndHesapTuru(anyString(), any())).thenReturn(Optional.empty());//basarisiz kayit
		//davranisi simule edilisi
		when(modelMapper.mapToHesapEntity(any(HesapDto.class))).thenReturn(hesap);//Modelmapper davranisi simule edilisi (Dto dan entity e)
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);//hesap repository de basarili kayit simule edilisi
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);//Modelmapper davranisi simule edilisi (Entity den dto ya)

		HesapDto result = hesapService.hesapOlustur(hesapDto);// hesapDto hesapService in hesapOlustur metoduna gonderildi ve
		//donen dto result a kaydedildi

		System.out.println("Result: " + result.getHesapSahipAd()); // Test ciktisini konsola yazdirma

		//assertion junit icerisinde kullanilan dogrulama metodudur

		assertNotNull(result);//bos mu kontrolu
		assertEquals(hesap.getHesapSahipKimlikNo(), result.getHesapSahipKimlikNo());//esitlik kontrolu
		assertEquals(hesap.getHesapTuru(), result.getHesapTuru());//esitlik kontrolu
		assertEquals(hesap.getBakiye(), result.getBakiye());//esitlik kontrolu
		assertEquals(hesap.getHesapSahipAd(), result.getHesapSahipAd());//esitlik kontrolu
		assertEquals(hesap.getHesapSahipSoyad(), result.getHesapSahipSoyad());//esitlik kontrolu
	}

	@Test
	void testTekKayitOlabilir() {
		when(hesapRepository.findByHesapSahipKimlikNoAndHesapTuru(anyString(), any())).thenReturn(Optional.of(hesap));

		assertThrows(IllegalArgumentException.class, () -> {
			hesapService.hesapOlustur(hesapDto);// bu islemin bir hata firlatip firlatmadiginin kontrolu
		});

		verify(hesapRepository, never()).save(any(Hesap.class));// bu dogrulama (verify) hesapRepository nin save
		// metodunun cagirilip cagirilmadigini kontrol eder eger cagirilmissa test basarisiz olur
	}

	@Test
	void testGetAllHesap() {

		when(hesapRepository.findAll()).thenReturn(Arrays.asList(hesap, hesap2));//hesap ve hesap2 nesnelerinden bir ArrayList doner
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto, hesapDto2);//hesap entity leri dto lara donusturuldu

		List<HesapDto> result = hesapService.getallHesap();//simule edilen hesapService in getAll() metodu test edildi
		//ve donen degerler result listesinin icine atildi

		assertNotNull(result);//result null olmamali
		assertFalse(result.isEmpty());//result bos olmamali
		assertEquals(2, result.size());// result in size i 2 olmali 1 olursa hata verir
		System.out.println("Result: " + result.get(0).getHesapSahipAd());
		System.out.println("Result: " + result.get(1).getHesapSahipAd());

	}

	@Test
	void testHesapGuncelle() {
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));//belirtilen id ile bir hesabin buldugunu simule eder
		when(modelMapper.mapToHesapEntity(any(HesapDto.class))).thenReturn(hesap);
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		//Guncellenmis veriler
		hesapDto.setHesapSahipAd("Test-name-updated");
		hesapDto.setHesapSahipKimlikNo("98765432101");
		hesapDto.setHesapSahipSoyad("Test-lastname-updated");

		HesapDto result = hesapService.hesapGuncelle(hesapId, hesapDto);//hesapGuncelle metodunun testi

		assertNotNull(result);
		assertEquals("98765432101", result.getHesapSahipKimlikNo());// Degistirildi
		assertEquals("Test-name-updated", result.getHesapSahipAd());// Degistirildi
		assertEquals(DOLAR, result.getHesapTuru()); //Degistirilmedi
		assertEquals(BigDecimal.ZERO, result.getBakiye()); //Degistirilmedi
		assertEquals("Test-lastname-updated", result.getHesapSahipSoyad());// Degistirildi

		System.out.println(result.getHesapSahipAd());
		System.out.println(result.getHesapSahipSoyad());
		System.out.println(result.getHesapSahipKimlikNo());
		System.out.println(result.getHesapTuru());
		System.out.println(result.getBakiye());
	}

	@Test
	void testHesapSil() {
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));

		hesapService.hesapSil(hesapId);//hesap sil metodunun testi

		verify(hesapRepository, times(1)).delete(hesap);//hesapRepository nin 1 kez cagirildigini
		//(silme islemi icin) dogrular
	}

	@Test
	void testHesapSorgula() {
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		HesapDto result = hesapService.hesapSorgula(hesapId);//hesapId gondererek hesapsorgulama testi

		assertNotNull(result);//result null olamaz
		assertEquals(hesap.getHesapSahipKimlikNo(), result.getHesapSahipKimlikNo());//esitlik dogrulamasi
	}

	@Test
	void testParaCekHareketRepo() {//Para yatirma isleminin hesap hareketi testi
		BigDecimal miktar = new BigDecimal("50.00");
		hesap.setBakiye(new BigDecimal("100.00"));

		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		HesapHareketiDto hesapHareketiDto = new HesapHareketiDto();
		hesapHareketiDto.setHesapId(hesap.getId());
		hesapHareketiDto.setHareketTuru(HareketTuruEnum.CEKME);
		hesapHareketiDto.setMiktar(miktar);
		hesapHareketiDto.setIslemTarihi(LocalDateTime.now());

		doAnswer(invocation -> {//belirli bir metodun cagirildiginda ne yapmasi gerektigini belirtmek icin kullanilir
			HesapHareketiDto dto = invocation.getArgument(0);

			return null;
		}).when(ihesapHareketService).hesapHareketEkle(any(HesapHareketiDto.class));

		HesapDto result = hesapService.paraCek(hesapId, miktar);


		assertNotNull(result);
		assertEquals(new BigDecimal("50.00"), hesap.getBakiye());//Hesaptaki cekme isleminden sonra beklenen deger ile
		//guncel degerin karsilastirilmasi

		verify(hesapRepository, times(1)).save(hesap);//hesapRepository nin 1 defa cagirildigini dogrulama
		verify(ihesapHareketService, times(1)).hesapHareketEkle(any(HesapHareketiDto.class));//ihesapHareketService
		//nesnesinin bir kez cagirildigini dogrular


	}

	@Test
	void testParaYatirHareketRepo() {//para cekme isleminin hesap hareketi testi
		BigDecimal miktar = new BigDecimal("50.00");
		hesap.setBakiye(new BigDecimal("100.00"));
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		HesapHareketiDto hesapHareketiDto = new HesapHareketiDto();
		hesapHareketiDto.setHesapId(hesap.getId());
		hesapHareketiDto.setHareketTuru(HareketTuruEnum.YATIRMA);
		hesapHareketiDto.setMiktar(miktar);
		hesapHareketiDto.setIslemTarihi(LocalDateTime.now());

		doAnswer(invocation -> {
			HesapHareketiDto dto = invocation.getArgument(0);

			return null;
		}).when(ihesapHareketService).hesapHareketEkle(any(HesapHareketiDto.class));

		HesapDto result = hesapService.paraYatir(hesapId, miktar);

		assertNotNull(result);
		assertEquals(new BigDecimal("150.00"), hesap.getBakiye());

		verify(hesapRepository, times(1)).save(hesap);
		verify(ihesapHareketService, times(1)).hesapHareketEkle(any(HesapHareketiDto.class));

	}

	@Test
	void testBakiyeGuncellenmeliParaYatir() {//Para yatirildiktan sonra bakiyenin guncellenmesinin testi
		BigDecimal yatirilanMiktar = new BigDecimal("100.00");
		hesap.setBakiye(new BigDecimal("0.00"));

		System.out.println("Islem Oncesi Bakiye: " + hesap.getBakiye());

		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		HesapDto result = hesapService.paraYatir(hesapId, yatirilanMiktar);//paraYatir metodunun calismasinin simule edilmesi

		assertNotNull(result);//null kontrolu
		assertEquals(new BigDecimal("100.00"), hesap.getBakiye());//guncellenen bakiye kontrolu
		System.out.println("Islem Sonrasi Bakiye: " + hesap.getBakiye());
	}

	@Test
	void testBakiyeGuncellenmeliParaCek() {//Para cekildikten sonra bakiye guncellenmesinin testi
		BigDecimal cekilecekMiktar = new BigDecimal("50.00");
		hesap.setBakiye(new BigDecimal("100.00"));
		System.out.println("Islem Oncesi Bakiye: " + hesap.getBakiye());

		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));
		when(hesapRepository.save(any(Hesap.class))).thenReturn(hesap);
		when(modelMapper.mapToHesapDto(any(Hesap.class))).thenReturn(hesapDto);

		HesapDto result = hesapService.paraCek(hesapId, cekilecekMiktar);//hesapService deki paraCek metodunun calismasinin simulasyonu

		assertNotNull(result);//null kontrolu
		assertEquals(new BigDecimal("50.00"), hesap.getBakiye());//guncellenen bakiye kontrolu
		System.out.println("Islem Sonrasi Bakiye: " + hesap.getBakiye());
	}

	@Test
	void testBakiyeSifirAltinaDusemez() {//bakiyenin 0 in altina dusmemesinin testi
		BigDecimal miktar = new BigDecimal("150.00");//hesaptakinden fazla cekilmek istenen tutar
		System.out.println("Cekilmek istenen tutar: " + miktar+"\n");
		hesap.setBakiye(new BigDecimal("100.00"));//hesaptaki tutar
		System.out.println("Hesap deafult bakiye: " + hesap.getBakiye()+"\n");
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));

		assertThrows(RuntimeException.class, () -> {//hata kontrolu, eger hata firlatmaz ise test basarisiz olur
			hesapService.paraCek(hesapId, miktar);
		});

		System.out.println("Bakiye son: " + hesap.getBakiye()+"\n");

		verify(hesapRepository, never()).save(any(Hesap.class));//hesapRepository e asla bir kayit olmadiginin dogrulanmasi
	}

	@Test
	void testBakiyeLimitAsilamaz() {//bakiyenin 9.999.999,99 dan buyuk olamayacaginin testi
		BigDecimal miktar = new BigDecimal("10000000.00");
		System.out.println("Yatirilmak istenen tutar: " + miktar+"\n");
		hesap.setBakiye(new BigDecimal("0.00"));
		System.out.println("Hesap deafult bakiye: " + hesap.getBakiye()+"\n");
		when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(hesap));

		assertThrows(RuntimeException.class, () -> {//hata kontrolu, eger hata firlatmaz ise test basarisiz olur
			hesapService.paraYatir(hesapId, miktar);
		});

		System.out.println("Bakiye son: " + hesap.getBakiye()+"\n");
		verify(hesapRepository, never()).save(any(Hesap.class));//hesapRepository e asla bir kayit olmadiginin dogrulanmasi
	}
}
