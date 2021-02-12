package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;


public class EmployeTest {



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
            "'T12345',0,1,0.0,0.0"
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
    public void TestGetPrimeAnnuelleAvecMatriculeNull() {
        //Given
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now(), 1500d, 1, 1.0);

        //When
        Double primeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(1000.0);
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


    @ParameterizedTest(name = "annee {0}, nombre de RTT {1}")
    @CsvSource
            ({
                    // non bissextile
                    "2019,8",//mardi

                    "2026,9",//jeudi
                    "2021,10",//vendredi
                    "2022,10",//samedi

                    // bissextile
                    "2036,9",//mardi

                    "2032,11",//jeudi
                    "2044,9",//vendredi
                    "2028,9",//samedi

            })
    public void TestGetRTT(Integer annee, Integer nbRTT) {
        //Given
        LocalDate localDate = LocalDate.of(annee, 1, 1);
        Employe employe = new Employe();

        //When
        Integer nombreDeRTT = employe.getNbRtt(localDate);

        //Then
        Assertions.assertThat(nombreDeRTT).isEqualTo(nbRTT);

    }



    @ParameterizedTest(name = "poucentage {0},salaire aprés augmentation")
    @CsvSource
            ({
                    "0.1,2200",
                    "-0.5,2000",
                    ",2000"
            })
    public void TestAugmenterSalaire(Double pourcentage,Double salaireAugmente) {
        //Given

        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 2000d, 1, 1.0);

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(salaireAugmente);

    }






}
