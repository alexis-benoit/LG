package service;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.ChampNonRenseigneException;
import exception.ChampTropGrandException;
import repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur obtenirUtilisateurParId(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        return utilisateurOptional.orElse(null);
    }

    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
    	if (utilisateur.getNomUtilisateur() == null || utilisateur.getNomUtilisateur().isEmpty()) {
            throw new ChampNonRenseigneException("Nom d'utilisateur");
        }
    	
    	if (utilisateur.getNomUtilisateur().length() > 50) {
            throw new ChampTropGrandException("Nom d'utilisateur", 50, utilisateur.getNomUtilisateur().length());
        }
        
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isEmpty()) {
            throw new ChampNonRenseigneException("Mot de passe");
        }

    	if (utilisateur.getMotDePasse().length() > 100) {
            throw new ChampTropGrandException("Mot de passe", 100, utilisateur.getMotDePasse().length());
        }
        
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur mettreAJourUtilisateur(Long id, Utilisateur utilisateur) {

        if (utilisateur.getNomUtilisateur() == null || utilisateur.getNomUtilisateur().isEmpty()) {
            throw new ChampNonRenseigneException("Nom d'utilisateur");
        }
        
        if (utilisateur.getNomUtilisateur().length() > 50) {
            throw new ChampTropGrandException("Nom d'utilisateur", 50, utilisateur.getNomUtilisateur().length());
        }
        
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isEmpty()) {
            throw new ChampNonRenseigneException("Mot de passe");
        }

    	if (utilisateur.getMotDePasse().length() > 100) {
            throw new ChampTropGrandException("Mot de passe", 100, utilisateur.getMotDePasse().length());
        }
        
        if (utilisateurRepository.existsById(id)) {
            utilisateur.setId(id);
            return utilisateurRepository.save(utilisateur);
        }
        return null;
    }

    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
