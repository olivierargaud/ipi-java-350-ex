package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;


    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        // Simuler qu'aucun employé n'est present
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        // la on connait le matricule car c'est le premier employe et que c'est un technicien donc T00001
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);

        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Employe employe = employeCaptor.getValue();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");

    }

    @Test
    void testEmbaucheTempsPartiel() throws EmployeException {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 0.5;
        // Simuler qu'aucun employé n'est present
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());

        Assertions.assertThat(employeCaptor.getValue().getNom()).isEqualTo(nom);
        Assertions.assertThat(employeCaptor.getValue().getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employeCaptor.getValue().getSalaire()).isEqualTo(912.73);
        Assertions.assertThat(employeCaptor.getValue().getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employeCaptor.getValue().getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeCaptor.getValue().getMatricule()).isEqualTo("T00001");

    }

    @Test
    void testEmbaucheLimiteMatricule() {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        // Simuler que 99999 employe sont deja present
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une execption");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
            // pour verifier que l'on a jamais appelé la methode save
            Mockito.verify(employeRepository, Mockito.never()).save(Mockito.any(Employe.class));
        }

    }


    @Test
    void testEmbaucheMatriculeDejaPresent() {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);

        // Simuler qu'un employe avec ce matricule existe deja
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une execption");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }

    }

    @ParameterizedTest(name = "matricule {0}, caTraite {1}, objectifCa {2}, performanceMoyenne {3}, performancePrecedente {4}, performanceObtenue {5}")
    @CsvSource
            ({
                    // performance en dessous de la moyenne
                    "'C12345',799,1000,10,1,1", // caTraite < -20% objectif

                    "'C12345',800,1000,10,0,1", // -20% objectif <= caTraite < -5% objectif
                    "'C12345',800,1000,10,4,2", // -20% objectif <= caTraite < -5% objectif
                    "'C12345',801,1000,10,4,2", // -20% objectif <= caTraite < -5% objectif

                    "'C12345',950,1000,10,4,4", // -5% objectif <= caTraite <= +5% objectif
                    "'C12345',1049,1000,10,6,6", // -5% objectif <= caTraite <= +5% objectif
                    "'C12345',1050,1000,10,7,7", // -5% objectif <= caTraite <= +5% objectif

                    "'C12345',1051,1000,10,4,5", // +5% objectif < caTraite <= +20% objectif
                    "'C12345',1199,1000,10,4,5", // +5% objectif < caTraite <= +20% objectif
                    "'C12345',1200,1000,10,4,5", // +5% objectif < caTraite <= +20% objectif

                    "'C12345',1201,1000,10,1,5", // +20% objectif < caTraite

                    // performance égale a la moyenne
                    "'C12345',1201,1000,8,4,8",  // +20% objectif < caTraite

                    // performance au dessus de la moyenne
                    "'C12345',1201,1000,2,4,9",  // +20% objectif < caTraite

                    // test caTraite a zero
                    "'C12345',0,1000,10,4,1", //
                    // test objectifCa a zero
                    "'C12345',1000,0,10,2,6", //

            })
    void testCalculPerformanceCommercial(String matricule, Long caTraite, Long objectifCa, Double performanceMoyenne, Integer performancePrecedente, Integer performanceObtenue) throws EmployeException {
        //Given
        Employe employe = new Employe("Doe", "Jane", matricule, LocalDate.now(), 1500d, performancePrecedente, 1.0);

        Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(performanceMoyenne);
        Mockito.when(employeRepository.save(employe)).thenReturn(null);
        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performanceObtenue);

    }

    @ParameterizedTest(name = "matricule {0}, caTraite {1}, objectifCa {2}, message de l'exception {3}")
    @CsvSource
            ({

                    "'C12345',,1000,Le chiffre d'affaire traité ne peut être négatif ou null !",
                    "'C12345',-500,1000,Le chiffre d'affaire traité ne peut être négatif ou null !",
                    "'C12345',1000,,L'objectif de chiffre d'affaire ne peut être négatif ou null !",
                    "'C12345',1000,-500,L'objectif de chiffre d'affaire ne peut être négatif ou null !",
                    ",1000,1000,Le matricule ne peut être null et doit commencer par un C !",
                    "'T12345',1000,1000,Le matricule ne peut être null et doit commencer par un C !"

            })
    void testParametreCalculPerformanceValide(String matricule, Long caTraite, Long objectifCa, String exceptionMessage) {
        //Given
        String test = exceptionMessage;
        //When
        try {
            employeService.testParametreCalculPerformanceValide(matricule, caTraite, objectifCa);
            Assertions.fail("testParametreCalculPerformanceValide aurait du lancer une exception");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo(exceptionMessage);
        }
    }

    @Test
    void testParametreCalculPerformanceValideEmployeInexistant() {
        //Given
        String matricule = "C12345";
        Long caTraite = 1000L;
        Long objectifCa = 1000L;
        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(null);
        //When
        try {
            employeService.testParametreCalculPerformanceValide(matricule, caTraite, objectifCa);
            Assertions.fail("testParametreCalculPerformanceValide aurait du lancer une exception");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule " + matricule + " n'existe pas !");
        }

    }


}

