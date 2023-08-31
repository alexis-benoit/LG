package service;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;
import validator.UtilisateurValidateur;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

class UtilisateurServiceUpdateTest {

	@Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages"); // No .properties or language suffix
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    private MessageSource messageSource = this.messageSource();

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	passwordEncoder = mock(BCryptPasswordEncoder.class);
    	UtilisateurValidateur utilisateurValidateur = new UtilisateurValidateur();
    	utilisateurValidateur.setMessageSource(messageSource);
    	
        utilisateurService = new UtilisateurService();
        utilisateurService.setUtilisateurRepository(utilisateurRepository);
        utilisateurService.setPasswordEncoder(passwordEncoder);
        utilisateurService.SetUtilisateurValidateur(utilisateurValidateur);
    }

    @Test
    void testMettreAJourUtilisateurChampsValides() throws ChampInvalideException {
        Long userId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(userId);
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("MotdepasseValide1!");

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
        
    	assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.vide", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.mincars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.maxcars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.debuteavecunelettre", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.lettrenumberuniquement", null, Locale.getDefault())));
    	
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.vide", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.mincars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.maxcars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientmajuscule", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientminuscule", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientchiffre", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientcarspecial", null, Locale.getDefault())));
    	
        assertEquals("mettreAJourUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
    
    @Test
    void testMettreAJourUtilisateurChampsInvalides() {
    	Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901!"); // 51 caractères
        utilisateur.setMotDePasse("abcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxys"); // 51 caractères
        
    	ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
    		utilisateurService.mettreAJourUtilisateur(1L, utilisateur);
        });
        
    	assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.vide", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.mincars", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.maxcars", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.debuteavecunelettre", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.lettrenumberuniquement", null, Locale.getDefault())));
    	
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.vide", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.mincars", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.maxcars", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientmajuscule", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientminuscule", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientchiffre", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientcarspecial", null, Locale.getDefault())));
    	
        assertEquals("mettreAJourUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
}