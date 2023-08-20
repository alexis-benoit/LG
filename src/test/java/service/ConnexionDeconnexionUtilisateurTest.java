package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConnexionDeconnexionUtilisateurTest {
	
	@InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurDAO utilisateurDAO;
    
	@Test
    public void testConnexionUtilisateurValide() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("john_doe");
        utilisateur.setMotDePasse("password");

        when(utilisateurDAO.obtenirUtilisateurParNomUtilisateur("john_doe")).thenReturn(utilisateur);

        boolean isConnected = utilisateurService.estUtilisateurConnecte("john_doe", "password");

        assertTrue(isConnected);
    }

    @Test
    public void testConnexionUtilisateurInvalide() {
        when(utilisateurDAO.obtenirUtilisateurParNomUtilisateur("john_doe")).thenReturn(null);

        boolean isConnected = utilisateurService.estUtilisateurConnecte("john_doe", "password");

        assertFalse(isConnected);
    }

    @Test
    public void testDeconnexionUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("john_doe");
        utilisateur.setMotDePasse("password");

        when(utilisateurDAO.obtenirUtilisateurParNomUtilisateur("john_doe")).thenReturn(utilisateur);

        utilisateurService.deconnecterUtilisateur("john_doe");

        assertFalse(utilisateur.isConnecte());
    }
}
