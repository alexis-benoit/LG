/*
 * Exception pouvant être levée par nos Validateurs
 */
package exception;

import java.util.List;

/*
 * Cette Exception est levée dès que des champs ne correspondent pas aux règles définie dans un validateur
 * Elle prend en argument une liste de message d'erreur
 */
public class ChampInvalideException extends Exception {
	private List<String> errors;
    private String resource;

    public ChampInvalideException(List<String> errors, String resource) {
        super(errors.get(0));
        this.errors = errors;
        this.resource = resource;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getResource() {
        return resource;
    }
}