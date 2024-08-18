package com.bookStore.SpringBootPractice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Otp;
@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer>{


	Otp findByOtp(Integer otp);

}
