package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatalogueAgent extends Agent {

    // Liste des livres disponibles au catalogue
    private final List<String> livres = new ArrayList<>(Arrays.asList(
            "Le petit prince",
            "Les misérables",
            "Notre-Dame de Paris",
            "L'étranger",
            "Madame Bovary",
            "1984",
            "Harry Potter",
            "Le seigneur des anneaux",
            "Le comte de Monte-Cristo",
            "Vingt mille lieues sous les mers"
    ));

    protected void setup() {
        System.out.println("Déploiement de l'agent : " + getAID().getLocalName());
        System.out.println("Catalogue initialisé avec " + livres.size() + " livres :");
        livres.forEach(l -> System.out.println("   • " + l));

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("catalogue");
        sd.setName("Service_catalogue");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println("CatalogueAgent enregistré dans le DF");
        } catch (FIPAException e) { e.printStackTrace(); }

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage message = receive();
                if (message != null) {
                    System.out.println("Message reçu de : " + message.getSender().getLocalName());
                    String livre = message.getContent().trim();
                    System.out.println("Livre demandé : " + livre);

                    // Recherche insensible à la casse
                    String livreTrouve = null;
                    for (String l : livres) {
                        if (l.equalsIgnoreCase(livre)) { livreTrouve = l; break; }
                    }

                    ACLMessage reponse = message.createReply();
                    reponse.setPerformative(ACLMessage.INFORM);
                    if (livreTrouve != null) {
                        reponse.setContent("DISPONIBLE: " + livreTrouve);
                        System.out.println("→ Réponse envoyée : " + livreTrouve + " est au catalogue");
                    } else {
                        reponse.setContent("INDISPONIBLE: " + livre);
                        System.out.println("→ Réponse envoyée : " + livre + " n'est pas au catalogue");
                    }
                    send(reponse);
                } else {
                    block();
                }
            }
        });
    }

    protected void beforeMove() { System.out.println("Avant migration : " + getAID().getLocalName()); }
    protected void afterMove()  { System.out.println("Après migration : " + getAID().getLocalName()); }
    protected void takeDown()   { System.out.println("Je meurs : " + getAID().getLocalName()); }
}