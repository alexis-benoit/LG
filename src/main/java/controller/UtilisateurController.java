package controller;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exception.ChampInvalideException;
import service.UtilisateurService;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Utilisateur>> obtenirTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.obtenirTousLesUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> obtenirUtilisateurParId(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.obtenirUtilisateurParId(id);
        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping
    public ResponseEntity<Utilisateur> creerUtilisateur(@RequestBody Utilisateur utilisateur) throws ChampInvalideException {
        Utilisateur nouveauUtilisateur = utilisateurService.creerUtilisateur(utilisateur);
        return ResponseEntity.ok(nouveauUtilisateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> mettreAJourUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) throws ChampInvalideException {
        Utilisateur utilisateurMisAJour = utilisateurService.mettreAJourUtilisateur(id, utilisateur);
        return ResponseEntity.ok(utilisateurMisAJour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}