package com.borabaskan.gelirTask.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.borabaskan.gelirTask.entities.Hesap;
import com.borabaskan.gelirTask.entities.HesapHareketi;



public interface HesapHareketiRepository extends JpaRepository<HesapHareketi, UUID> {// HesapHareketiRepository adli bir inteface
	// tanimlandi ve JpaRepository den extend edildi bu sayede jpaRepository metodlari ile Spring Data JPA kullanarak veritabani
	// islemleri gerceklestirildi bu interface bir HesapHareketi ve UUID almaktadir

}
