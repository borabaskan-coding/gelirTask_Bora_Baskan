package com.borabaskan.gelirTask.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.borabaskan.gelirTask.entities.HesapHareketi;
import com.borabaskan.gelirTask.entities.enums.HareketTuruEnum;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class HesapHareketiDto {

	private UUID id;// hesap hareketlerinin unique kimligi
    private UUID hesapId; //hesap hareketlerinin ait oldugu hesabin unique kimlik
    private LocalDateTime islemTarihi; //hesap hareketlerinin gerceklestigi tarih (islem oldugu an)
    private HareketTuruEnum hareketTuru; // hareket turunu temsil eder (Yatirma, cekme) (Enum)
    private BigDecimal miktar; //tek seferdeki hesap isleminin miktari

}
