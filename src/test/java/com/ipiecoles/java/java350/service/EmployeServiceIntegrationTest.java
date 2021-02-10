package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeService employeService;
    @Autowired
    private EmployeRepository employeRepository;

//    @Test
//    public void test() throws EmployeException {
//        //Given
//        employeRepository.save(new Employe("Doe", "John", "T12345",
//                LocalDate.now().minusYears(5), 1500d, 1, 1.0));
//        String nom = "Doe2";
//        String prenom = "John";
//        Poste poste = Poste.COMMERCIAL;
//        NiveauEtude niveauEtude = NiveauEtude.CAP;
//        Double tempsPartiel = 1.0;
//
//        //When
//        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
//        Employe employe1 = employeRepository.findByMatricule("C12346");
//
//        //Then
//        Employe employe = new Employe("Doe2", "John", "C12346",
//                LocalDate.now(), 1521.22, 1, 1.0);
//
//        Assertions.assertThat(employe1).isNotNull();
//        Assertions.assertThat(employe1.getSalaire()).isEqualTo(1521.22);
//    }


    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

        //Then

//        List<Employe> employes = employeRepository.findAll();
//        Assertions.assertThat(employes).hasSize(1);
//        Employe employe = employeRepository.findAll().get(0);

        Employe employe = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employe).isNotNull();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");

    }


}

