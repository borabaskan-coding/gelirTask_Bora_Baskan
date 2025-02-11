package com.borabaskan.gelirTask.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import com.borabaskan.gelirTask.entities.enums.HesapTuruEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Column Bu anotasyon, olusturulan alaninin (orn:hesapTuru) veritabanindaki db deki sutuna (orn:hesap_turu) eslenmesini saglar
//@GeneratedValue bu anotasyon JPA entity de anahtar alanlari icin kullanilir
//@Enumerated Enum tipinin db de nasil saklanacagini belirtir
//'nullable = false' sutunun bos olamayacagini belirtir "true" icin ise bos olabilecegini belirtir

@Entity //JPA kullanarak veritabani islemleri gerceklestiren siniflar icin kullanilir (JPA tarafindan yonetilen varlik)
@Table(name = "hesap")// Varsayilan olarak sinif adi-tablo adi olarak kullanilir ama @Table tablo adini ozellestirir
@Getter //lombok tarafindan getter metodlarinin olusturulmasi icin
@Setter //lombok tarafindan setter metodlarinin olusturulmasi icin
@NoArgsConstructor
@AllArgsConstructor
public class Hesap {//Bir hesabi temsil eden JPA varlik sinifi

	@Id //Bu alanin db de birincil anahtar oldugunu belirtir
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)//GenerationType.AUTO varsayilan stratejiyi kullanma
	private UUID id;
	
	
	@Column(name = "hesap_sahip_kimlik_no", nullable = false)
	private String hesapSahipKimlikNo;
	
	@Column(name = "hesap_sahip_ad", nullable = false)
	private String hesapSahipAd;
	
	@Column(name = "hesap_sahip_soyad", nullable = false)
	private String hesapSahipSoyad;
	
	@Column(name = "hesap_turu", nullable = false)
	@Enumerated(EnumType.STRING)
	private HesapTuruEnum hesapTuru; //Hesap turu (DOLAR,STERLIN,TL) onceden belirlendigi icin enum kullanildi
	
	@Column(name = "bakiye", nullable = false)
	@DecimalMin("0.0")
	@DecimalMax("9999999.99")
	private BigDecimal bakiye;
	
	@OneToMany(mappedBy = "hesap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)// bu sinifin HesapHareketi sinifi ile
	//one to many iliskisi kurmasini saglar, mappedBy iliskinin sahibi olan sinifi belirtir, cascade iliskide gerceklestirilecek
	//islemleri belirtir, fetchType.Lazy iliskinin nasil yuklenecegini belirtir Lazy oldugu icin Hesap yuklendiginde HesapHareketi
	//hemen yuklenmez, HesapHareketi nesnesine erisildiginde yuklenir

	private List<HesapHareketi> hareketler;// Ayni kisi uzerinden cekme-yatirma islemi birden cok olabilecegi icin list olarak
	//tanimlandi
}
