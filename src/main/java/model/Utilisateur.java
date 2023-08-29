/*
 * Les modèles seront la représentation des différentes tables de la base de données
 * On y définit les différents champs, leur type, leur taille et leur contraintes de BDD
 */
package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/*
 * @Table permet de définir que cette classe représente une table dans la base de donnée
 * Le name correspond au nom de la table dans la base de données
 */
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
	/*
	 * @Id permet de définir que ce champ sera la PK dans la table de la base de données
	 * @GeneratedValue va permettre de définir ce champ comme renseigné automatiquement
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	/*
	 * @Column permet de définit que ce champ sera une colonne de la base de donnée
	 * Le name définit le nom de la colonne
	 * La length définit la longueur de la chaine de caractère en base de données
	 * @NotBlank définit le champ comme devant obligatoirement être renseigné
	 * 
	 * @Column est issu de jakarta.persistence
	 * Cela signifie que la contrainte sera testée lors de l'insert dans la base de données
	 * C'est à dire par la repository
	 * 
	 * @NotBlank est issue de jakarta.validation.constraints
	 * Ce qui signifie que cette contrainte sera testée lors de la validation
	 * de l'entité
	 */
	@Column(name = "nom_utilisateur", length = 50)
	@NotBlank
    private String nomUtilisateur;

    @Column(name = "mot_de_passe", length = 100)
    @NotBlank
    private String motDePasse;
    
    @Column(name = "est_connecte")
    private boolean estConnecte;

    /*
     * Chaque champ peut être défini ou récupéré par les getter et les setters ci-dessous
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}