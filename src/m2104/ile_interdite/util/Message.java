package m2104.ile_interdite.util;

import java.io.Serializable;

//************************************************************************************************************
//                          Classe générant les messages
//*************************************************************************************************************

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Utils.Commandes commande;
    private final String idAventurier, idAventurier2;
    private final String idCarte;
    private final String idTuile;
    private final Integer nb;

    private Message(Utils.Commandes commande, String idAventurier,String idAventurier2, String idCarte, String idTuile, Integer nb) {
        this.commande = commande;
        this.idAventurier = idAventurier;
        this.idAventurier2 = idAventurier2;
        this.idCarte = idCarte;
        this.idTuile = idTuile;
        this.nb = nb;
        
        
    }

    /**
     *
     * @param nbJoueurs
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message validerJoueurs(int niveau) {
        return new Message(Utils.Commandes.VALIDER_JOUEURS, null, null, null, null, niveau);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message bouger(String idAventurier, String nomTuile) {
        return new Message(Utils.Commandes.BOUGER, idAventurier,null, null, nomTuile, null);
    }

    /**
     *
     * @param idAventurier
     * @return un message bouger interpretable par le controleur
     */
    public static Message assecher(String idAventurier, String nomTuile) {
        return new Message(Utils.Commandes.ASSECHER, idAventurier,null, null, nomTuile,null);
    }

    /**
     *
     * @param idAventurier idAventurier2, Carte
     * @Permet de donner une carte à un autre joueur
     */
    public static Message donner(String idAventurier, String idAventurier2, String idCarte) {
        return new Message(Utils.Commandes.DONNER, idAventurier, idAventurier2,idCarte, null,null);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message recupererTresor(String idAventurier) {
        return new Message(Utils.Commandes.RECUPERER_TRESOR, idAventurier,null, null, null,null);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message terminer(String idAventurier) {
        return new Message(Utils.Commandes.TERMINER, idAventurier,null, null, null,null);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message recevoir(String idAventurier) {
        return new Message(Utils.Commandes.RECEVOIR, idAventurier,null, null, null,null);
    }
    
    /**
     *
     * @param 
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message MiseAJour(){
        return new Message(Utils.Commandes.MISE_A_JOUR,null,null,null,null,null);
    }
    
    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    
    public static Message finTour(String idAventurier) {
        return new Message(Utils.Commandes.FIN_TOUR, idAventurier, null, null, null,null);
    }
    
    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message passerTour(String idAventurier) {
        return new Message(Utils.Commandes.PASSER_TOUR, idAventurier, null, null, null,null);
    }

    /**
     *
     * @param nivEau
     * @return un nouveau {@link #Message} interpretable par le controleur
     */

    public static Message augmentationEau(int nivEau){
        return new Message(Utils.Commandes.AUGMENTATION_EAU, null, null, null, null, nivEau);
    }
    
    /**
     *
     * @param nb 
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message finPartie(int nb){
        return new Message(Utils.Commandes.FIN_PARTIE, null, null, null, null, nb);
    }
    
    /**
     *
     * @param idAventurier,carte,tuile
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message carteAction(String aventurier,String carte,String tuile){
        return new Message(Utils.Commandes.CARTE_ACTION, aventurier, null, carte, tuile, null);
    }
    
    /**
     *
     * @param 
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    
    public static Message restart(){
        return new Message(Utils.Commandes.REINIT_JEU, null, null, null, null, null);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message deplacer(String idAventurier) {
        return new Message(Utils.Commandes.DEPLACER, idAventurier, null, null, null, null);
    }

    /**
     *
     * @param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message noyade(String idAventurier) {
        return new Message(Utils.Commandes.NOYADE, idAventurier,null, null, null, null);
    }

    /**
     *@param idAventurier
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message laMainPleinne(String idAventurier) {
        return new Message(Utils.Commandes.MAIN_PLEINNE, idAventurier,null, null, null, null);
    }
    
     /**
     *@param idAventurier, nom
     * @return un nouveau {@link #Message} interpretable par le controleur
     */
    public static Message defausser(String idAventurier, String nom ) {
        return new Message(Utils.Commandes.DEFAUSSER, idAventurier,null, nom , null, null);
    }


    /**
     * @return the commande
     */
    public Boolean hasCommande() {
        return commande != null;
    }
    public Utils.Commandes getCommande() {
        return commande;
    }

    public String getIdAventurier() {
        return idAventurier;
    }

    public String getIdCarte() {
        return idCarte;
    }
    public Integer getNb() {
        return nb;
    }

    public String getIdTuile() {
        return idTuile;
    }

    public String getIdAventurier2() {
        return idAventurier2;
    }
    
   
}
