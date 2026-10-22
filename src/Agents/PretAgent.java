package Agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

public class PretAgent extends Agent {

    // Livres actuellement prêtés (pour éviter qu'un même livre soit prêté 2 fois)
    private final List<String> livresPretes = new ArrayList<>();

    protected void setup() {
        System.out.println("Déploiement de l'agent : " + getAID().getLocalName());

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("pret");
        sd.setName("Service_prêt");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println("PretAgent enregistré dans le DF");
        } catch (FIPAException e) { e.printStackTrace(); }

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage demande = receive();
                if (demande != null) {
                    System.out.println("Demande reçue de : " + demande.getSender().getLocalName());
                    String livre = demande.getContent().trim();
                    System.out.println("Livre demandé : " + livre);

                    boolean dejaPrete = false;
                    for (String l : livresPretes) {
                        if (l.equalsIgnoreCase(livre)) { dejaPrete = true; break; }
                    }

                    ACLMessage reponse = demande.createReply();
                    reponse.setPerformative(ACLMessage.INFORM);
                    if (dejaPrete) {
                        reponse.setContent("PRET_REFUSE: " + livre);
                        System.out.println("→ Refus : " + livre + " déjà prêté");
                    } else {
                        livresPretes.add(livre);
                        reponse.setContent("PRET_OK: " + livre);
                        System.out.println("→ Prêt accordé : " + livre
                                + " (total prêtés : " + livresPretes.size() + ")");
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