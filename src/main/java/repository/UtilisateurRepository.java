/*
 * Les repository sont les interfaces entre un Objet Java et un Objet base de données
 * Ici on se sert de la repository fournie par défaut par Spring JpaRepository
 * Qui fournit un certains nombre de fonction utiles
 */
package repository;

import model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

}
