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


//    @BeforeAll // Junit 5
//    public void setUp(){//Nom setUp arbitraire
//        //Appelé une seule fois avant l'exécution des tests
//    }
//    @BeforeEach // Junit 5
//    public void before(){//Nom before arbitraire
//        //Appelé avant chaque test
//        employeRepository.deleteAll();
//    }
//    @AfterEach //Junit 5
//    public void after(){//Nom after arbitraire
//        //Appelé après chaque test
//        employeRepository.deleteAll();
//    }
//    @AfterAll // Junit 5
//    public void tearDown(){//Nom tearDown arbitraire
//        //Appelé une fois que tous les tests sont passés
//    }

    @BeforeEach // Junit 5
    @AfterEach //Junit 5
    public void purgeBDD()
    {
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatricule0Employe()
    {
        //given

        //when
        String lastMatricule = employeRepository.findLastMatricule();

        //then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    public void testFindLastMatricule1Employe()
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
    public void testFindLastMatriculeNEmploye()
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
    public void testAvgPerformanceWhereMatriculeStartsWith()
    {





    }






}