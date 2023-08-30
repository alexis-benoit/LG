/*
 * Les validators vont être là pour générer des Exceptions si les objets Java ne correspondent pas aux règles voulues
 */
package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.ChampInvalideException;
import model.Utilisateur;

public class UtilisateurValidateur {
		 
	private UtilisateurValidateur() {
 
	}

	private static List<String> verifierNomUtilisateur(String NomUtilisateur) {
		List<String> errors = new ArrayList<>(); 
		
		if (NomUtilisateur.length() > 50) {
			errors.add("Nom d'utilisateur ne peut exceder 50 caractères");
        }
		
		if (NomUtilisateur.length() < 3) {
			errors.add("Nom d'utilisateur doit faire au moins 3 caractères");
        }
		
		Pattern beginWithUppercasePattern = Pattern.compile("^[A-Za-z]");
		Matcher uppercaseMatcher = beginWithUppercasePattern.matcher(NomUtilisateur);
		
		if (!uppercaseMatcher.find()) {
        	errors.add("Nom d'utilisateur doit commencer par une lettre");
        } 
		
		Pattern ContainOnlyLetterAndNumberPattern = Pattern.compile("^[A-Za-z0-9]*$");
		Matcher ContainOnlyLetterAndNumberPatternMatcher = ContainOnlyLetterAndNumberPattern.matcher(NomUtilisateur);
		
		if (!ContainOnlyLetterAndNumberPatternMatcher.find()) {
        	errors.add("Nom d'utilisateur ne doit contenir que des lettres ou des chiffres");
        } 
		
		return errors;
	}

	private static List<String> verifierMotDePasse(String MotDePasse) {
		List<String> errors = new ArrayList<>();
		
		if (MotDePasse.length() > 50) {
			errors.add("Mot de passe ne peut exceder 50 caractères");
        } 
		
		if (MotDePasse.length() < 10) {
			errors.add("Mot de passe doit faire au moins 10 caractères");
        } 
		
    	Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Pattern digitPattern = Pattern.compile("\\d");
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

        Matcher uppercaseMatcher = uppercasePattern.matcher(MotDePasse);
        Matcher lowercaseMatcher = lowercasePattern.matcher(MotDePasse);
        Matcher digitMatcher = digitPattern.matcher(MotDePasse);
        Matcher specialCharMatcher = specialCharPattern.matcher(MotDePasse);

        if (!uppercaseMatcher.find()) {
        	errors.add("Mot de passe doit contenir au moins 1 Majuscule");
        } 
        
        if (!lowercaseMatcher.find()) {
        	errors.add("Mot de passe doit contenir au moins 1 Minuscule");
        } 
        
        if (!digitMatcher.find()) {
        	errors.add("Mot de passe doit contenir au moins 1 Chiffre");
        } 
        
        if (!specialCharMatcher.find()) {
        	errors.add("Mot de passe doit contenir au moins 1 Caractère spécial");
        } 
        
        return errors;
	}
	
	/*
	 * La fonction vérifier va remplir un tableau de String contenant toutes les erreurs de validation soulevées
	 * Puis va lever l'exception ChampInvalideException avec ce tableau de message d'erreur en paramètre
	 */
	public static void verifierChamps(Utilisateur aVerifier, String resource) throws ChampInvalideException {
	    List<String> errors = new ArrayList<>();

	    String nomUtilisateur = aVerifier.getNomUtilisateur();
	    if (nomUtilisateur == null || nomUtilisateur.isEmpty()) {
			errors.add("Nom d'utilisateur doit être renseigné");
        } else {
        	errors.addAll(verifierNomUtilisateur(nomUtilisateur));
        }

	    String motDePasse = aVerifier.getMotDePasse();
	    if (motDePasse == null || motDePasse.isEmpty()) {
			errors.add("Mot de passe doit être renseigné");
        } else {
        	errors.addAll(verifierMotDePasse(motDePasse));
        }

	    if(!errors.isEmpty()) {
	    	throw new ChampInvalideException(errors, resource);
	    }
	}
}
