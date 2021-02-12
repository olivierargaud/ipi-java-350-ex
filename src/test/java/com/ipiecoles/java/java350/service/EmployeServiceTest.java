package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

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
        employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);

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
            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une execption");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
            // pour verifier que l'on a jamais appelé la methode save
            Mockito.verify(employeRepository,Mockito.never()).save(Mockito.any(Employe.class));
        }

    }


    @Test
    void testEmbaucheMatriculeDejaPresent() throws EmployeException {
        //Given
        String nom = "Doe2";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe","Jane","T00001",LocalDate.now(),1500d,1,1.0);

        // Simuler qu'un employe avec ce matricule existe deja
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        //When
        try{
            employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer une execption");
        }catch(Exception e)
        {
            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }


    }



}

