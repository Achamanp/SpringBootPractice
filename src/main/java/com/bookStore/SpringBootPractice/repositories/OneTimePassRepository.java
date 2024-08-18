package com.bookStore.SpringBootPractice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookStore.SpringBootPractice.entities.OneTimePass;

public interface OneTimePassRepository extends JpaRepository<OneTimePass, Integer>{

	 OneTimePass findByOtp(Integer otp);

}
