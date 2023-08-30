package validator;

import model.Utilisateur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import configuration.MessageSourceConfig;
import exception.ChampInvalideException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

@SpringBootTest
@SpringJUnitConfig(MessageSourceConfig.class) // Cette annotation configure la classe de configuration
class UtilisateurValidatorTest {

    @Autowired
    private MessageSource messageSource;
    
    private UtilisateurValidateur utilisateurValidateur;

    @BeforeEach
    void setUp() {
        this.utilisateurValidateur = new UtilisateurValidateur();
        this.utilisateurValidateur.setMessageSource(messageSource);
    }
	
    @Test
    void testUtilisateurValide() throws ChampInvalideException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("MotdepasseValide1!");

        assertDoesNotThrow(() -> utilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur"));
    }

    @Test
    void testUtilisateurChampsManquant() {
        Utilisateur utilisateur = new Utilisateur();

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
        	utilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
	      });
	    
        System.out.println(exception.getErrors());
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
    }
    
    @Test
    void testCreerUtilisateurChampsInvalides() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901!"); // + 50 caractères, Commence par chiffre, Contient caractère spécial
        utilisateur.setMotDePasse("abcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxys"); // + 50 caractères, ne contient pas de chiffre ni caractère spécial ni majuscule

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
        	utilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
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
       
    }
    
    @Test
    void testCreerUtilisateurChampsTropPetis() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("U");
        utilisateur.setMotDePasse("Aa1!");

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
        	utilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
        });
        
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.vide", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.mincars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.maxcars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.debuteavecunelettre", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.nomutilisateur.lettrenumberuniquement", null, Locale.getDefault())));
    	
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.vide", null, Locale.getDefault())));
        assertTrue(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.mincars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.maxcars", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientmajuscule", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientminuscule", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientchiffre", null, Locale.getDefault())));
        assertFalse(exception.getErrors().contains(messageSource.getMessage("erreur.utilisateur.motdepasse.contientcarspecial", null, Locale.getDefault())));
    	
    }
}
