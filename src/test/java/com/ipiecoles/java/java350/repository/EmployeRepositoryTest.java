package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class EmployeRepositoryTest {


    @Autowired
    EmployeRepository employeRepository;


    @Test
    public void testFindLastMatricule()
    {
        //given
        Employe employe = employeRepository.save(new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        Employe employe2 = employeRepository.save(new Employe("Doe2", "John", "T23456",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        Employe employe3 = employeRepository.save(new Employe("Doe3", "John", "T34567",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        Employe employe4 = employeRepository.save(new Employe("Doe4", "John", "T12389",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        Employe employe5 = employeRepository.save(new Employe("Doe5", "John", "TBCDEF",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        Employe employe6 = employeRepository.save(new Employe("Doe5", "John", "TABCDE",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));

        //when
        String lastMatricule = employeRepository.findLastMatricule();


        //then
        Assertions.assertThat(lastMatricule).isEqualTo("BCDEF");
    }


}