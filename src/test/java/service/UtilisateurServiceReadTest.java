package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import model.Utilisateur;
import repository.UtilisateurRepository;

public class UtilisateurServiceReadTest {

	@Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    List<Utilisateur> utilisateursFictifsList = new ArrayList<>();
    List<Utilisateur> utilisateursFictifsListVide = new ArrayList<>();
    
    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	passwordEncoder = mock(BCryptPasswordEncoder.class);
        utilisateurService = new UtilisateurService(); // Réinitialisez l'objet utilisateurService avec le passwordEncoder simulé.
        utilisateurService.setUtilisateurRepository(utilisateurRepository);
        utilisateurService.setPasswordEncoder(passwordEncoder);
        
    	Utilisateur utilisateur1 = new Utilisateur();
    	utilisateur1.setNomUtilisateur("utilisateur1");
    	utilisateur1.setMotDePasse("motdepasse");
    	utilisateur1.setId(1L);
    	
    	Utilisateur utilisateur2 = new Utilisateur();
    	utilisateur2.setNomUtilisateur("utilisateur2");
    	utilisateur2.setMotDePasse("motdepasse");
    	utilisateur2.setId(2L);
    	
    	utilisateursFictifsList.add(utilisateur1);
    	utilisateursFictifsList.add(utilisateur2);
    }
    
	@Test
    public void testObtenirTousLesUtilisateurs() {
        when(utilisateurRepository.findAll()).thenReturn(utilisateursFictifsListVide);

        List<Utilisateur> result = utilisateurService.obtenirTousLesUtilisateurs();

        assertEquals(0, result.size());
    }
    
	@Test
    public void testObtenirTousLesUtilisateursVide() {
        when(utilisateurRepository.findAll()).thenReturn(utilisateursFictifsList);

        List<Utilisateur> result = utilisateurService.obtenirTousLesUtilisateurs();

        assertEquals(2, result.size());
        assertEquals("utilisateur1", result.get(0).getNomUtilisateur());
        assertEquals("utilisateur2", result.get(1).getNomUtilisateur());
    }

    @Test
    public void testObtenirUtilisateurParId() {
        
        when(utilisateurRepository.findById(anyLong()))
        .thenAnswer(invocation -> {
            Long idDemande = invocation.getArgument(0);
            Utilisateur utilisateurCorrespondant = utilisateursFictifsList.stream()
                    .filter(utilisateur -> utilisateur.getId().equals(idDemande))
                    .findFirst()
                    .orElse(null);
            return utilisateurCorrespondant;
        });

		Utilisateur resultat1 = utilisateurService.obtenirUtilisateurParId(1L);
		Utilisateur resultat2 = utilisateurService.obtenirUtilisateurParId(2L);
		Utilisateur resultatInexistant = utilisateurService.obtenirUtilisateurParId(999L);
		
		assertNotNull(resultat1);
		assertEquals("utilisateur1", resultat1.getNomUtilisateur());
		
		assertNotNull(resultat2);
		assertEquals("utilisateur2", resultat2.getNomUtilisateur());
		
		assertNull(resultatInexistant);
    }
}
