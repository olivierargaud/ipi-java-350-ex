package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.time.LocalDate;

import java.util.ArrayList;

public class EmployeTest {

//      pas bien test unitaire egale un test une fonction
//    @Test
//    public void TestGetNombreAnneeAncienneteEmploye()
//    {
//        //Given
////        ArrayList<LocalDate> localDates = new ArrayList<>();
////        localDates.add( LocalDate.of(2004,9,30));
////        localDates.add( LocalDate.now().plusYears(1));
////        localDates.add( null);
////        localDates.add( LocalDate.of(1855,9,30));
//
//        LocalDate dateEmbauche1 = LocalDate.of(2004,9,30);
//        LocalDate dateEmbauche2 = LocalDate.now().plusYears(1);
//        LocalDate dateEmbauche3 = null;
//        LocalDate dateEmbauche4 = LocalDate.of(1855,9,30);
//
////        for (LocalDate localDate : localDates)
////        {
////            Employe employe1 = new Employe();
////            employe1.setDateEmbauche(localDate);
////
////        }
//
//        Employe employe1 = new Employe();
//        Employe employe2 = new Employe();
//        Employe employe3 = new Employe();
//        Employe employe4 = new Employe();
//
//
//        employe1.setDateEmbauche(dateEmbauche1);
//        employe2.setDateEmbauche(dateEmbauche2);
//        employe3.setDateEmbauche(dateEmbauche3);
//        employe4.setDateEmbauche(dateEmbauche4);
//
//        //When
//        Integer anciennete1 = employe1.getNombreAnneeAnciennete();
//        Integer anciennete2 = employe2.getNombreAnneeAnciennete();
//        Integer anciennete3 = employe3.getNombreAnneeAnciennete();
//        Integer anciennete4 = employe3.getNombreAnneeAnciennete();
//
//        //Then
//        Assertions.assertThat(anciennete1).isNotNegative();
//        Assertions.assertThat(anciennete2).isNotNegative();
//        Assertions.assertThat(anciennete3).isNotNegative();
//        Assertions.assertThat(anciennete4).isNotNegative();
//
//    }

    @Test
    public void TestGetNombreAnneeAncienneteDateEmbaucheNull() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                null, 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isNull();
    }

    @Test
    public void TestGetNombreAnneeAncienneteDateEmbaucheInfNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isEqualTo(5);
    }


    @Test
    public void TestGetNombreAnneeAncienneteDateEmbaucheSupNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now().plusYears(7), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isEqualTo(0);
    }

    @Test
    public void TestGetNombreAnneeAncienneteDateEmbaucheNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isEqualTo(0);
    }




    @ParameterizedTest(name = "matricule {0}, date d embauche {1}, performance {2}, temps partiel {3} ,valeur prime : {4}")
    @CsvSource
    ({
            "'M12345',0,1,1.0,1700.0",
            "'M12345',4,1,1.0,2100.0",
            "'M12345',0,1,0.5,850.0",
            "'T12345',0,1,1.0,1000.0",
            "'T12345',4,1,1.0,1400.0",
            "'T12345',4,1,0.5,700.0",
            "'T12345',0,2,1.0,2300.0",
            "'T12345',4,2,1.0,2700.0",
            "'T12345',0,2,0.5,1150.0",
            "'T12345',4,0,1.0,1400.0",
            "'T12345',0,1,0.0,0.0",
            "null,0,1,1.0,1000.0"
    })
    public void TestGetPrimeAnnuelle(String matricule,Integer anciennete,Integer performance,Double tempsPartiel,Double prime)
    {
        //Given
        LocalDate localDate = LocalDate.now().minusYears(anciennete);
        Employe employe = new Employe("Doe","John",matricule,localDate,1500d,performance,tempsPartiel);

        //When
        Double primeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(prime);

    }


    @Test
    public void TestGetPrimeAnnuelleAvecPerformanceNull() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, null, 1.0);

        //When
        Double primeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(1000.0);
    }

}
