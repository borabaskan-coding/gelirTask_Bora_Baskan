package com.borabaskan.gelirTask.repository;


import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.enums.HesapTuruEnum;

public interface HesapRepository extends JpaRepository<Hesap, UUID> {// HesapRepository adli bir inteface tanimlandi ve
	//JpaRepository den extend edildi bu sayede jpaRepository metodlari ile Spring Data JPA kullanarak veritabani islemleri
	//gerceklestirildi bu interface bir Hesap ve UUID almaktadir

	Optional<Hesap> findByHesapSahipKimlikNoAndHesapTuru(String kimlikNo, HesapTuruEnum hesapTuru);// Belirtilen kimlikNo
	//ve Enum ile (DOLAR,STERLIN,TL) iliskili hesabi bulur ve bulunan hesabi doner eger hesap bulunmaz ise Optional.empty doner
	
}
