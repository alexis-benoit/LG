/*
 * Les Services sont les points d'entrées des utilisateurs
 * C'est pas les services que l'on va opérer les opérations CRUD
 * C'est donc ici que l'on va faire appel au Validateur qui va vérifier que notre objet correspond aux règles
 * Et que l'on va chiffrer le mot de passe avec BCrypt (bibliotheque fournie par Spring)
 */
package service;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import exception.ChampInvalideException;
import repository.UtilisateurRepository;
import validator.UtilisateurValidateur;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UtilisateurService {

	/*
	 * lorsque vous utilisez @Autowired sur un champ, un constructeur ou une méthode : 
	 * Le framework va automatiquement identifier le type de dépendance requis et fournir une instance de cette dépendance au moment opportun
	 * Généralement lors de la création de l'objet parent. 
	 * Cela évite de devoir créer manuellement et gérer les instances de dépendances
	 * Simplifiant ainsi le processus de gestion des composants d'une application
	 */
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UtilisateurValidateur utilisateurValidateur;

    /*
     * Renvoie l'ensemble de tous les utilisateurs sous forme de Liste d'objet Utilisateur
     */
    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    /*
     * Renvoie 1 Objet Utilisateur correspondant à l'ID fournit en paramètre
     * La fonction findById renvoie null si l'ID n'existe pas
     * On doit alors définir notre variable de retour comme Optionnal
     * Pour permettre de récupérer null
     */
    public Utilisateur obtenirUtilisateurParId(Long id) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(id);
        return utilisateurOptional.orElse(null);
    }

    /*
     * Permet de créer l'objet Utilisateur passé en paramètre dans la BDD
     * A travers le repository
     * On commence cependant d'abord par appeler la fonction verifierChamps du Validateur
     * Pour vérifier que l'Objet Utilisateur corresponde bien aux règles voulues
     * On crypte ensuite son mot de passe avant de l'insérer dans la base
     * 
     * On déclare la fonction avec throws ChampInvalideException car le validateur risque de lever une erreur ChampInvalideException
     * Qui hérite de Exception et qui donc est levée en Cascade dans toutes les fonctions appelantes
     * Jusqu'à ce qu'elle soit gérée dans un bloc try catch
     */
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) throws ChampInvalideException {
    	utilisateurValidateur.verifierChamps(utilisateur, "creerUtilisateur");
        
    	String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseCrypte);
        
        return utilisateurRepository.save(utilisateur);
    }

    /*
     * Permet de mettre à jour, en BDD, l'utilisateur définit par l'id passé en paramètres avec les valeurs fournies par l'objet Utilisateur
     * Si l'ID n'existe pas, on ne fait rien
     */
    public Utilisateur mettreAJourUtilisateur(Long id, Utilisateur utilisateur) throws ChampInvalideException {

    	utilisateurValidateur.verifierChamps(utilisateur, "mettreAJourUtilisateur");
        
    	String motDePasseCrypte = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseCrypte);
        
        if (utilisateurRepository.existsById(id)) {
            return utilisateurRepository.save(utilisateur);
        }
        return null;
    }

    /*
     * Supprime l'utilisateur défini par l'id passé en paramètre dans le BDD
     */
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    /*
     * Définir le UtilisateurRepository à utiliser
     */
    public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /*
     * Définir l'Encodeur de mot de passe à utiliser
     */
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * Définir l'Encodeur de mot de passe à utiliser
     */
    public void SetUtilisateurValidateur(UtilisateurValidateur utilisateurValidateur) {
        this.utilisateurValidateur = utilisateurValidateur;
    }
}
