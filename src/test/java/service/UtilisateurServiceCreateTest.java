/*
 * Les tests de Services correspondent aux Test unitaires
 * C'est ici que l'on va tester que nos fonctions font bien ce qu'on attends d'eux
 * Ici, j'ai divisé les tests par opération CRUD
 * Avec à chaque fois des cas OK et des cas qui doivent gérer des Exceptions
 */
package service;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceCreateTest {

	/*
	 * Ici, on veut tester que la classe Service fasse ce qu'on attend
	 * On ne veut pas tester le repository
	 * On définit donc le repository comme un Mock (un faux repository)
	 * Et à chaque fois qu'on fera appel au repository
	 * On définira l'action à faire
	 * Le role de notre service étant de lancer le repository
	 * On comptera le nombre d'appel au repository dans chaque test pour vérifier que l'appel se fait bien ou qu'il n'est pas appelé s'il ne le faut pas
	 */
	@Mock
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurService utilisateurService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /*
     * Avant chaque Test, cette fonction sera lancée
     * Cette fonction définit l'encodeur du service et son repository
     */
    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	passwordEncoder = mock(BCryptPasswordEncoder.class);
        utilisateurService = new UtilisateurService();
        utilisateurService.setUtilisateurRepository(utilisateurRepository);
        utilisateurService.setPasswordEncoder(passwordEncoder);
    }

    /*
     * Ce test va vérifier que lorsque les champs sont correctements renseignés
     * Aucune erreur n'est levée et l'utilisateur est bien créé et le mot de passe est bien crypté
     * Et que la fonction repository.save soit bien appelée 1 fois
     */
    @Test
    void testCreerUtilisateurValide() throws ChampInvalideException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("motdepasse");

        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);
        
        String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());

        Utilisateur nouveauUtilisateur = utilisateurService.creerUtilisateur(utilisateur);

        assertNotNull(nouveauUtilisateur);
        assertEquals(utilisateur.getNomUtilisateur(), nouveauUtilisateur.getNomUtilisateur());
        assertEquals(motDePasseCrypte, nouveauUtilisateur.getMotDePasse());

        verify(utilisateurRepository, times(1)).save(utilisateur);
    }
    
    /*
     * Ce test va vérifier que lorsque les champs ne sont pas renseignés
     * Les erreurs "Nom d'utilisateur non renseigné" et "Mot de passe non renseigné" soient levée
     * Mais pas les autres erreurs
     * Et que le utilisateur.repository ne soit pas appelé
     */
    @Test
    void testCreerUtilisateurChampManquant() {
        Utilisateur utilisateur = new Utilisateur();

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
	        utilisateurService.creerUtilisateur(utilisateur);
	    });
	    
        assertTrue(exception.getErrors().contains("Nom d'utilisateur non renseigné"));
    	assertTrue(exception.getErrors().contains("Mot de passe non renseigné"));
    	assertFalse(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
    	assertFalse(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
    	
        assertEquals("creerUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
    
    @Test
    void testCreerUtilisateurLimiteTaille() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901"); // 51 caractères
        utilisateur.setMotDePasse("123456789012345678901234567890123456789012345678901"); // 51 caractères

        ChampInvalideException exception = assertThrows(ChampInvalideException.class, () -> {
            utilisateurService.creerUtilisateur(utilisateur);
        });
        
        assertFalse(exception.getErrors().contains("Nom d'utilisateur non renseigné"));
        assertFalse(exception.getErrors().contains("Mot de passe non renseigné"));
    	assertTrue(exception.getErrors().contains("Nom d'utilisateur ne peut exceder 50 caractères"));
    	assertTrue(exception.getErrors().contains("Mot de passe ne peut exceder 50 caractères"));
    	
        assertEquals("creerUtilisateur", exception.getResource());
        
        verify(utilisateurRepository, times(0)).save(utilisateur);
    }
}