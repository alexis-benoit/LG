package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import repository.UtilisateurRepository;

import static org.mockito.Mockito.*;

class UtilisateurServiceDeleteTest {

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
    void testSupprimerUtilisateur() {
        Long userId = 1L;

        doNothing().when(utilisateurRepository).deleteById(userId);

        utilisateurService.supprimerUtilisateur(userId);

        verify(utilisateurRepository, times(1)).deleteById(userId);
    }
}