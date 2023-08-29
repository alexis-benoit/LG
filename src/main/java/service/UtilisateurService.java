package service;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;
import validator.UtilisateurValidateur;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur obtenirUtilisateurParId(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        return utilisateurOptional.orElse(null);
    }

    public Utilisateur creerUtilisateur(Utilisateur utilisateur) throws ChampInvalideException {
    	UtilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
        
    	String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseCrypte);
        
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur mettreAJourUtilisateur(Long id, Utilisateur utilisateur) throws ChampInvalideException {

    	UtilisateurValidateur.verifierChamps(utilisateur, "mettreAJourUtilisateur");
        
    	String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseCrypte);
        
        if (utilisateurRepository.existsById(id)) {
            return utilisateurRepository.save(utilisateur);
        }
        return null;
    }

    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
