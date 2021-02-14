package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeService employeService;
    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach // Junit 5
    @AfterEach
        //Junit 5
    void purgeBDD()
    {
        employeRepository.deleteAll();
    }

    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

        //Then

        Employe employe = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employe).isNotNull();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");

    }


    @Test
    void testCalculPerformanceCommercial() throws EmployeException {
        //Given
        Employe employe = employeRepository.save(new Employe("Doe", "John", "C12345",
                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe2", "John", "C23456",
                LocalDate.now().minusYears(5), 1500d, 2, 1.0));
        employeRepository.save(new Employe("Doe3", "John", "C34567",
                LocalDate.now().minusYears(5), 1500d, 3, 1.0));

        Long caTraite = 1100L;
        Long objectifCa = 1000L;

        //When
        employeService.calculPerformanceCommercial( employe.getMatricule(),  caTraite,  objectifCa);

        //Then
        Employe employe1 = employeRepository.findByMatricule(employe.getMatricule());
        Assertions.assertThat(employe1.getPerformance()).isEqualTo(2);

    }

}

