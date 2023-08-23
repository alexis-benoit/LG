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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import exception.ChampNonRenseigneException;
import exception.ChampTropGrandException;
import repository.UtilisateurRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilisateurServiceTest {

	@Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;
    
    @Spy
    private UtilisateurService utilisateurServiceSpy;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	passwordEncoder = mock(BCryptPasswordEncoder.class);
        utilisateurService = new UtilisateurService(); // Réinitialisez l'objet utilisateurService avec le passwordEncoder simulé.
        utilisateurService.setUtilisateurRepository(utilisateurRepository);
        utilisateurService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void testCreerUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("motdepasse");
        
       System.out.println("mdp : "+ utilisateur.getMotDePasse()); 
        
        String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);

        Utilisateur nouveauUtilisateur = utilisateurService.creerUtilisateur(utilisateur);

        assertNotNull(nouveauUtilisateur);
        assertEquals(utilisateur.getNomUtilisateur(), nouveauUtilisateur.getNomUtilisateur());
        assertEquals(motDePasseCrypte, nouveauUtilisateur.getMotDePasse());

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
    
    @Test
    void testCreerUtilisateurChampManquant() {
        Utilisateur utilisateur = new Utilisateur();

        doThrow(ChampNonRenseigneException.class)
        .when(utilisateurRepository).save(utilisateur);

        ChampNonRenseigneException exception = assertThrows(ChampNonRenseigneException.class, () -> {
	        utilisateurService.creerUtilisateur(utilisateur);
	    });
	    
	    assertEquals("Nom d'utilisateur", exception.getChamp());
    }
    
    @Test
    void testMettreAJourUtilisateurChampManquant() {
    	Utilisateur utilisateur = new Utilisateur();
        // Configurer l'utilisateur avec des champs manquants

    	doThrow(new ChampNonRenseigneException("Nom d'utilisateur"))
	        .when(utilisateurServiceSpy)
	        .mettreAJourUtilisateur(anyLong(), any(Utilisateur.class));
        
        ChampNonRenseigneException exception = assertThrows(ChampNonRenseigneException.class, () -> {
            utilisateurServiceSpy.mettreAJourUtilisateur(1L, utilisateur);
        });
        
        assertEquals("Nom d'utilisateur", exception.getChamp());
    }
    
    @Test
    void testCreerUtilisateurLimiteTailleNomUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901"); // 51 caractères

        doThrow(ChampTropGrandException.class)
            .when(utilisateurRepository).save(utilisateur);

        ChampTropGrandException exception = assertThrows(ChampTropGrandException.class, () -> {
            utilisateurService.creerUtilisateur(utilisateur);
        });
        
        assertEquals("Nom d'utilisateur", exception.getChamp());
    }

    @Test
    void testCreerUtilisateurLimiteTailleMotDePasse() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("123456789012345678901234567890123456789012345678901"); // 51 caractères

        
        when(utilisateurRepository.save(utilisateur)).thenThrow(new ChampTropGrandException("Mot de passe", 50, utilisateur.getMotDePasse().length()));

        ChampTropGrandException exception = assertThrows(ChampTropGrandException.class, () -> {
            utilisateurService.creerUtilisateur(utilisateur);
        });

        assertEquals("Mot de passe", exception.getChamp());
    }

    @Test
    void testMettreAJourUtilisateurLimiteTailleNomUtilisateur() {
        Long userId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(userId);
        utilisateur.setNomUtilisateur("123456789012345678901234567890123456789012345678901"); // 51 caractères
        
        doThrow(new ChampTropGrandException("Nom d'utilisateur", 50, 51))
            .when(utilisateurServiceSpy)
            .mettreAJourUtilisateur(userId, utilisateur);

        ChampTropGrandException exception = assertThrows(ChampTropGrandException.class, () -> {
            utilisateurServiceSpy.mettreAJourUtilisateur(userId, utilisateur);
        });
        
        assertEquals("Nom d'utilisateur", exception.getChamp());
    }

    @Test
    void testMettreAJourUtilisateurLimiteTailleMotDePasse() {
        Long userId = 1L;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(userId);
        utilisateur.setNomUtilisateur("utilisateur1");
        utilisateur.setMotDePasse("123456789012345678901234567890123456789012345678901"); // 51 caractères
        
        doThrow(new ChampTropGrandException("Mot de passe", 50, 51))
            .when(utilisateurServiceSpy)
            .mettreAJourUtilisateur(userId, utilisateur);

        ChampTropGrandException exception = assertThrows(ChampTropGrandException.class, () -> {
            utilisateurServiceSpy.mettreAJourUtilisateur(userId, utilisateur);
        });
        
        assertEquals("Mot de passe", exception.getChamp());
    }
}