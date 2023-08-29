package validator;

import java.util.ArrayList;
import java.util.List;

import exception.ChampInvalideException;
import model.Utilisateur;

public class UtilisateurValidateur {
		 
	private UtilisateurValidateur() {
 
	}
 
	public static void verifierChamps(Utilisateur aVerifier, String resource) throws ChampInvalideException {
		
	    List<String> errors = new ArrayList<>();
	    
	    if (aVerifier.getNomUtilisateur() == null || aVerifier.getNomUtilisateur().isEmpty()) {
	    	errors.add("Nom d'utilisateur non renseigné");
        } else if (aVerifier.getNomUtilisateur().length() > 50) {
    		errors.add("Nom d'utilisateur ne peut exceder 50 caractères");
        }
        
        if (aVerifier.getMotDePasse() == null || aVerifier.getMotDePasse().isEmpty()) {
        	errors.add("Mot de passe non renseigné");
        } else if (aVerifier.getMotDePasse().length() > 50) {
    		errors.add("Mot de passe ne peut exceder 50 caractères");
        }
	    
	    if(!errors.isEmpty()) {
	    	throw new ChampInvalideException(errors, resource);
	    }
	}
}
