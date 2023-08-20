package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
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
}
