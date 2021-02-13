package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;


class EmployeTest {



    @Test
    void TestGetNombreAnneeAncienneteDateEmbaucheNull() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                null, 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isNull();
    }

    @Test
    void TestGetNombreAnneeAncienneteDateEmbaucheInfNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isEqualTo(5);
    }


    @Test
    void TestGetNombreAnneeAncienneteDateEmbaucheSupNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now().plusYears(7), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isZero();
    }

    @Test
    void TestGetNombreAnneeAncienneteDateEmbaucheNow() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);

        //When
        Integer anciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anciennete).isZero();
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
    void TestGetPrimeAnnuelle(String matricule,Integer anciennete,Integer performance,Double tempsPartiel,Double prime)
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
    void TestGetPrimeAnnuelleAvecMatriculeNull() {
        //Given
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now(), 1500d, 1, 1.0);

        //When
        Double primeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(1000.0);
    }

    @Test
    void TestGetPrimeAnnuelleAvecPerformanceNull() {
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, null, 1.0);

        //When
        Double primeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeAnnuelle).isEqualTo(1000.0);
    }


    @ParameterizedTest(name = "annee {0}, temps partiel {1}, nombre de RTT {2}")
    @CsvSource
            ({
                    // non bissextile
                    "2019,1.0,8",//mardi
                    "2019,0.5,4",//mardi, temps partiel

                    "2026,1.0,9",//jeudi
                    "2021,1.0,10",//vendredi
                    "2022,1.0,10",//samedi

                    // bissextile
                    "2036,1.0,9",//mardi

                    "2032,1.0,11",//jeudi
                    "2044,1.0,9",//vendredi
                    "2028,1.0,9",//samedi

            })
    void TestGetRTT(Integer annee,Double tempsPartiel, Integer nbRTT) {
        //Given
        LocalDate localDate = LocalDate.of(annee, 1, 1);
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 2000d, 1, tempsPartiel);

        //When
        Integer nombreDeRTT = employe.getNbRtt(localDate);

        //Then
        Assertions.assertThat(nombreDeRTT).isEqualTo(nbRTT);

    }


    @Test
    void TestAugmenterSalaireCasNominal() throws Exception {
        //Given
        Double pourcentage = 0.1;
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 2000d, 1, 1.0);

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2200);

    }

    @Test
    void TestAugmenterSalairePourcentageNegatif() {
        //Given
        Double pourcentage = -0.5;
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 2000d, 1, 1.0);

        //When
        try {
            employe.augmenterSalaire(pourcentage);
            Assertions.fail("augmenterSalaire aurait du lancer une exception");
        }catch (Exception e)
        {
            Assertions.assertThat(e).isInstanceOf(IllegalArgumentException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'augmentation de salaire ne peut avoir un pourcentage négatif");
            Assertions.assertThat(employe.getSalaire()).isEqualTo(2000);
        }

    }

    @Test
    void TestAugmenterSalairePourcentageNull() {
        //Given
        Double pourcentage = null;
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 2000d, 1, 1.0);

        //When
        try {
            employe.augmenterSalaire(pourcentage);
            Assertions.fail("augmenterSalaire aurait du lancer une exception");
        }catch (Exception e)
        {
            Assertions.assertThat(e).isInstanceOf(IllegalArgumentException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'augmentation de salaire ne peut avoir un pourcentage null");
            Assertions.assertThat(employe.getSalaire()).isEqualTo(2000);
        }

    }


}
