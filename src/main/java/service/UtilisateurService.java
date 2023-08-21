package service;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur mettreAJourUtilisateur(Long id, Utilisateur utilisateur) {
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
