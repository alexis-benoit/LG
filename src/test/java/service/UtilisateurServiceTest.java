package service;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurDAO utilisateurDAO;

    @Test
    public void testAjouterUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("john_doe");
        utilisateur.setMotDePasse("password");

        utilisateurService.ajouterUtilisateur(utilisateur);

        verify(utilisateurDAO).ajouterUtilisateur(utilisateur);
    }

    @Test
    public void testMettreAJourUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNomUtilisateur("updated_user");

        utilisateurService.mettreAJourUtilisateur(utilisateur);

        verify(utilisateurDAO).mettreAJourUtilisateur(utilisateur);
    }

    @Test
    public void testSupprimerUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        utilisateurService.supprimerUtilisateur(utilisateur);

        verify(utilisateurDAO).supprimerUtilisateur(utilisateur);
    }

    @Test
    public void testObtenirUtilisateurParId() {
        Long userId = 1L;
        when(utilisateurDAO.obtenirUtilisateurParId(userId)).thenReturn(new Utilisateur());

        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurParId(userId);

        assertNotNull(utilisateur);
    }

    @Test
    public void testObtenirTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = Arrays.asList(new Utilisateur(), new Utilisateur());
        when(utilisateurDAO.obtenirTousLesUtilisateurs()).thenReturn(utilisateurs);

        List<Utilisateur> result = utilisateurService.obtenirTousLesUtilisateurs();

        assertEquals(2, result.size());
    }
    
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
