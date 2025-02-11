package com.borabaskan.gelirTask.dto;

import java.math.BigDecimal;
import com.borabaskan.gelirTask.entities.enums.HesapTuruEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@NotNull(null olmamasini saglar) ve @NotBlank(string icin null,bos olamaz)
//@Column Bu anotasyon, olusturulan alaninin (orn:hesapTuru) veritabanindaki db deki sutuna (orn:hesap_turu) eslenmesini saglar
//@Size verinin boyutunu belirlemek icin kullanildi
//@DecimalMin ondalikli verinin minimum alabilecegi halini belirler
//@DecimalMax ondalikli verinin maximum alabilecegi halini belirler
//@Pattern string verinin belirli bir durum ile uyumlu olmasini saglar regex zorunludur
//'message' ilgili alanin sarti saglanmadiginda yazilan mesaji dondurur


@Getter //lombok tarafindan getter metodlarinin olusturulmasi icin
@Setter //lombok tarafindan setter metodlarinin olusturulmasi icin
@NoArgsConstructor// parametresiz yapici metod olusturur
@AllArgsConstructor //tum alanlari iceren yapici metod olusturur
public class HesapDto {//Hesap bilgilerini katmanlar arasinda tasimak icin yazilmistir. Dto kullanimi guvenligi arttirir
    // veri kisitlamasina imkan tanir

	@Column(name = "hesap_sahip_kimlik_no")
	@NotNull(message = "Kimlik numarasi bos olamaz")
    @Size(min = 11, max = 11, message = "Kimlik numarasi 11 haneli olmalidir")
    @Pattern(regexp = "^[0-9]+$", message = "Kimlik numarasi sadece rakamlardan olusmalidir")
    private String hesapSahipKimlikNo;

	@Column(name = "hesap_sahip_ad")
    @NotBlank(message = "Ad boş olamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    private String hesapSahipAd;

	@Column(name = "hesap_sahip_soyad")
    @NotBlank(message = "Soyad boş olamaz")
    @Size(max = 50, message = "Soyad en fazla 50 karakter olabilir")
    private String hesapSahipSoyad;

	@Column(name = "hesap_turu")
    @NotNull(message = "Hesap türü boş olamaz")
    private HesapTuruEnum hesapTuru;

	@Column(name = "bakiye")
    @NotNull(message = "Bakiye boş olamaz")
    @DecimalMin(value = "0.00", message = "Bakiye negatif olamaz") //minimum 0 olabilir ve mesaj olarak 0 in alti oldugu durumlarda
    // yazilan mesaj bastirilir
    @DecimalMax(value = "9999999.00", message = "Bakiye 9.999.999'u geçemez")//max 9.999.999 olabilir ve mesaj olarak 9.999.999 in
    // ustu oldugu durumlarda yazilan mesaj bastirilir
    private BigDecimal bakiye;

}
