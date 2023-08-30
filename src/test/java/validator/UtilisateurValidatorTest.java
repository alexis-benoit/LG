package validator;

import model.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurValidatorTest {

    @Test
    void testUtilisateurValide() throws ChampInvalideException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("MotdepasseValide1!");

        assertDoesNotThrow(() -> asserUtilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur"));
    }

    @Test
    void testUtilisateurChampsManquant() {
        Utilisateur utilisateur = new Utilisateur();

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
	        asserUtilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
	      });
	    
        assertTrue(exception.getErrors().contains("Nom d'utilisateur doit être renseigné"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit faire au moins 3 caractères"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit commencer par une lettre"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur ne doit contenir que des lettres ou des chiffres"));
    	
    	
        assertTrue(exception.getErrors().contains("Mot de passe doit être renseigné"));
        assertFalse(exception.getErrors().contains("Mot de passe doit faire au moins 10 caractères"));
        assertFalse(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Minuscule"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Majuscule"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Chiffre"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Caractère spécial"));
    }
    
    @Test
    void testCreerUtilisateurChampsInvalides() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901!"); // + 50 caractères, Commence par chiffre, Contient caractère spécial
        utilisateur.setMotDePasse("abcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxysabcdefghijklmnopqrstucvwxys"); // + 50 caractères, ne contient pas de chiffre ni caractère spécial ni majuscule

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
            asserUtilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
        });
        
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit être renseigné"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit faire au moins 3 caractères"));
    	  assertTrue(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
    	  assertTrue(exception.getErrors().contains("Nom d'utilisateur doit commencer par une lettre"));
    	  assertTrue(exception.getErrors().contains("Nom d'utilisateur ne doit contenir que des lettres ou des chiffres"));
    	
    	
        assertFalse(exception.getErrors().contains("Mot de passe doit être renseigné"));
        assertFalse(exception.getErrors().contains("Mot de passe doit faire au moins 10 caractères"));
    	  assertTrue(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
    	  assertTrue(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Majuscule"));
    	  assertTrue(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Chiffre"));
    	  assertTrue(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Caractère spécial"));
    }
    
    @Test
    void testCreerUtilisateurChampsTropPetis() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("U");
        utilisateur.setMotDePasse("Aa1!");

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
            asserUtilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
        });
        
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit être renseigné"));
        assertTrue(exception.getErrors().contains("Nom d'utilisateur doit faire au moins 3 caractères"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur doit commencer par une lettre"));
        assertFalse(exception.getErrors().contains("Nom d'utilisateur ne doit contenir que des lettres ou des chiffres"));
    	
        assertFalse(exception.getErrors().contains("Mot de passe doit être renseigné"));
        assertTrue(exception.getErrors().contains("Mot de passe doit faire au moins 10 caractères"));
        assertFalse(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Majuscule"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Chiffre"));
        assertFalse(exception.getErrors().contains("Mot de passe doit contenir au moins 1 Caractère spécial"));
    }
}
