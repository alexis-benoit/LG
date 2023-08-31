package service;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import configuration.MessageSourceConfig;
import exception.ChampInvalideException;
import repository.UtilisateurRepository;
import validator.UtilisateurValidateur;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

class UtilisateurServiceCreateTest {

	@Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /*
     * Pour une raison inconnue j'avais des erreur messageSource is null
     * Quand j'essayais d'utiliser l'annotation @Autowired sur messageSource
     * 
     * Je ne peux pas appeler MessageSourceConfig.messageSource() a cause du "static"
     * 
     * J'ai également essayé d'ajouter les annotations @SpringBootTest et @SpringJUnitConfig(MessageSourceConfig.class)
     * Sur la classe, comme c'était fait pour le UtilisateurValidatorTes, mais cette fois j'avais une erreur liée au Bean sur mon passwordEncodeur
     * 
     * Avec cette méthode (copier la configuration, ça fonctionne), même si ce n'est pas conventionnel
     * 
     * Lorsque je comprendrais mieux les Bean, les Mock, Les configuration, je pourrais modifier ce code correctement je pense
     */
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
    void testCreerUtilisateurValide() throws ChampInvalideException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("MotdepasseValide1!");

        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);
        
        String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());

        Utilisateur nouveauUtilisateur = utilisateurService.creerUtilisateur(utilisateur);

        assertNotNull(nouveauUtilisateur);
        assertEquals(utilisateur.getNomUtilisateur(), nouveauUtilisateur.getNomUtilisateur());
        assertEquals(motDePasseCrypte, nouveauUtilisateur.getMotDePasse());

        verify(utilisateurRepository, times(1)).save(utilisateur);
    }
    
    @Test
    void testCreerUtilisateurChampManquant() {
        Utilisateur utilisateur = new Utilisateur();

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
	        utilisateurService.creerUtilisateur(utilisateur);
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
    	
        assertEquals("creerUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
    
    @Test
    void testCreerUtilisateurLimiteTaille() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901!"); // + 50 caractères, Commence par chiffre, Contient caractère spécial
        utilisateur.setMotDePasse("abcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxys"); // + 50 caractères, ne contient pas de chiffre ni caractère spécial ni majuscule

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
            utilisateurService.creerUtilisateur(utilisateur);
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
    	
        assertEquals("creerUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
}