/*
 * Les validators vont être là pour générer des Exceptions si les objets Java ne correspondent pas aux règles voulues
 */
package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import configuration.MessageSourceConfig;
import exception.ChampInvalideException;
import model.Utilisateur;

@Component
public class UtilisateurValidateur {
	
	@Autowired
	Locale locale = LocaleContextHolder.getLocale();
	
	private MessageSource messageSource;

	private List<String> verifierNomUtilisateur(String NomUtilisateur) {
		List<String> errors = new ArrayList<>(); 
		
		if (NomUtilisateur.length() > 50) {
			errors.add(messageSource.getMessage("erreur.utilisateur.nomutilisateur.maxcars", null, locale));
        }
		
		if (NomUtilisateur.length() < 3) {
			errors.add(messageSource.getMessage("erreur.utilisateur.nomutilisateur.mincars", null, locale));
        }
		
		Pattern beginWithUppercasePattern = Pattern.compile("^[A-Za-z]");
		Matcher beginWithUppercaseMatcher = beginWithUppercasePattern.matcher(NomUtilisateur);
		
		if (!beginWithUppercaseMatcher.find()) {
        	errors.add(messageSource.getMessage("erreur.utilisateur.nomutilisateur.debuteavecunelettre", null, locale));
        } 
		
		Pattern ContainOnlyLetterAndNumberPattern = Pattern.compile("^[A-Za-z0-9]*$");
		Matcher ContainOnlyLetterAndNumberPatternMatcher = ContainOnlyLetterAndNumberPattern.matcher(NomUtilisateur);
		
		if (!ContainOnlyLetterAndNumberPatternMatcher.find()) {
        	errors.add(messageSource.getMessage("erreur.utilisateur.nomutilisateur.lettrenumberuniquement", null, locale));
        } 
		
		return errors;
	}

	private List<String> verifierMotDePasse(String MotDePasse) {
		List<String> errors = new ArrayList<>();
		
		if (MotDePasse.length() > 50) {
			errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.maxcars", null, locale));
        } 
		
		if (MotDePasse.length() < 10) {
			errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.mincars", null, locale));
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
        	errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.contientmajuscule", null, locale));
        } 
        
        if (!lowercaseMatcher.find()) {
        	errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.contientminuscule", null, locale));
        } 
        
        if (!digitMatcher.find()) {
        	errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.contientchiffre", null, locale));
        } 
        
        if (!specialCharMatcher.find()) {
        	errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.contientcarspecial", null, locale));
        } 
        
        return errors;
	}
	
	/*
	 * La fonction vérifier va remplir un tableau de String contenant toutes les erreurs de validation soulevées
	 * Puis va lever l'exception ChampInvalideException avec ce tableau de message d'erreur en paramètre
	 */
	public void verifierChamps(Utilisateur aVerifier, String resource) throws ChampInvalideException {
	    List<String> errors = new ArrayList<>();

	    String nomUtilisateur = aVerifier.getNomUtilisateur();
	    if (nomUtilisateur == null || nomUtilisateur.isEmpty()) {
			errors.add(messageSource.getMessage("erreur.utilisateur.nomutilisateur.vide", null, locale));
        } else {
        	errors.addAll(verifierNomUtilisateur(nomUtilisateur));
        }

	    String motDePasse = aVerifier.getMotDePasse();
	    if (motDePasse == null || motDePasse.isEmpty()) {
			errors.add(messageSource.getMessage("erreur.utilisateur.motdepasse.vide", null, locale));
        } else {
        	errors.addAll(verifierMotDePasse(motDePasse));
        }

	    if(!errors.isEmpty()) {
	    	throw new ChampInvalideException(errors, resource);
	    }
	}
	
	/*
     * Définir l'Encodeur de mot de passe à utiliser
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
