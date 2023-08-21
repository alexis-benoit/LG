package service;

import model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UtilisateurRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreerUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("motdepasse");

        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);

        Utilisateur nouveauUtilisateur = utilisateurService.creerUtilisateur(utilisateur);

        assertNotNull(nouveauUtilisateur);
        assertEquals(utilisateur.getNomUtilisateur(), nouveauUtilisateur.getNomUtilisateur());
        assertEquals(utilisateur.getMotDePasse(), nouveauUtilisateur.getMotDePasse());

        verify(utilisateurRepository, times(1)).save(utilisateur);
    }

    @Test
    void testMettreAJourUtilisateur() {
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
    void testSupprimerUtilisateur() {
        Long userId = 1L;

        doNothing().when(utilisateurRepository).deleteById(userId);

        utilisateurService.supprimerUtilisateur(userId);

        verify(utilisateurRepository, times(1)).deleteById(userId);
    }
}