package com.borabaskan.gelirTask.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.borabaskan.gelirTask.entities.enums.HareketTuruEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Column Bu anotasyon, olusturulan alaninin (orn:hesapTuru) veritabanindaki db deki sutuna (orn:hesap_turu) eslenmesini saglar
//@GeneratedValue bu anotasyon JPA entity de anahtar alanlari icin kullanilir
//'nullable = false' sutunun bos olamayacagini belirtir "true" icin ise bos olabilecegini belirtir

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HesapHareketi {//Bir hesap hareketini temsil eden JPA varlik sinifi

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne // Bu anotasyon HesapHareketi sinifinin Hesap sinifi ile coktan-bire iliskisi oldugunu belirtir
    @JoinColumn(name = "hesap_id", referencedColumnName = "id")// Bu sinifin db deki iliskili sutunu ile iliskisi oldugu
	//sinifin db deki iliskili sutunun esler
    private Hesap hesap;
	
	@Column(name = "islem_tarihi", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")// tarih verisinin json formatina cevrilmesi
	private LocalDateTime islemTarihi;
	
	@Column(name = "hareket_turu", nullable = false)
	@Enumerated(EnumType.STRING)//Enum tipinin db de nasil saklanacagini belirtir
	private HareketTuruEnum hareketTuru; //Hareket Turu (YATIRMA,CEKME) onceden belirlendigi icin enum kullanildi
	
	@Column(name = "miktar", nullable = false)
	private BigDecimal miktar;
	
	
}
