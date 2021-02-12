package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;


    @BeforeEach // Junit 5
    @AfterEach //Junit 5
    void purgeBDD()
    {
        employeRepository.deleteAll();
    }

    @Test
    void testFindLastMatricule0Employe()
    {
        //given

        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    void testFindLastMatricule1Employe()
    {
        //given
        employeRepository.save(new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));

        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    void testFindLastMatriculeNEmploye()
    {
        //given
        employeRepository.save(new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe2", "John", "T23456",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe3", "John", "T34567",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe4", "John", "T12389",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe5", "John", "TBCDEF",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe5", "John", "TABCDE",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe6", "John", "C40325",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe7", "John", "M06432",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));

        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isEqualTo("BCDEF");
    }



    @Test
    void testAvgPerformanceWhereMatriculeStartsWith()
    {





    }






}