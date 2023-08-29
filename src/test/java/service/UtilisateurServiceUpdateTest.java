package service;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilisateurServiceUpdateTest {

	@Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	passwordEncoder = mock(BCryptPasswordEncoder.class);
        utilisateurService = new UtilisateurService();
        utilisateurService.setUtilisateurRepository(utilisateurRepository);
        utilisateurService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void testMettreAJourUtilisateurChampsValides() throws ChampInvalideException {
        Long userId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(userId);
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("motdepasse");

        when(utilisateurRepository.existsById(userId)).thenReturn(true);
        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);

        Utilisateur utilisateurMisAJour = utilisateurService.mettreAJourUtilisateur(userId, utilisateur);

        assertNotNull(utilisateurMisAJour);
        assertEquals(utilisateur.getId(), utilisateurMisAJour.getId());
        assertEquals(utilisateur.getNomUtilisateur(), utilisateurMisAJour.getNomUtilisateur());
        assertEquals(utilisateur.getMotDePasse(), utilisateurMisAJour.getMotDePasse());

        verify(utilisateurRepository, times(1)).save(utilisateur);
    }
    
    @Test
    void testMettreAJourUtilisateurChampsManquants() {
    	Utilisateur utilisateur = new Utilisateur();
        
    	ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
    		utilisateurService.mettreAJourUtilisateur(1L, utilisateur);
        });
        
    	assertTrue(exception.getErrors().contains("Nom d'utilisateur non renseigné"));
    	assertTrue(exception.getErrors().contains("Mot de passe non renseigné"));
    	assertFalse(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
    	assertFalse(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
    	
        assertEquals("mettreAJourUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
    
    @Test
    void testMettreAJourUtilisateurChampsInvalides() {
    	Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901"); // 51 caractères
        utilisateur.setMotDePasse("123456789012345678901234567890123456789012345678901"); // 51 caractères
        
    	ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
    		utilisateurService.mettreAJourUtilisateur(1L, utilisateur);
        });
        
    	assertFalse(exception.getErrors().contains("Nom d'utilisateur non renseigné"));
    	assertFalse(exception.getErrors().contains("Mot de passe non renseigné"));
    	assertTrue(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
    	assertTrue(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
    	
        assertEquals("mettreAJourUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
}