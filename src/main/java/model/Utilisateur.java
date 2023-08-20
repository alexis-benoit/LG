package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "nom_utilisateur", length = 50)
	@NotBlank
    private String nomUtilisateur;

    @Column(name = "mot_de_passe", length = 100)
    @NotBlank
    private String motDePasse;
    
    @Column(name = "est_connecte")
    @NotBlank
    private boolean estConnecte;

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