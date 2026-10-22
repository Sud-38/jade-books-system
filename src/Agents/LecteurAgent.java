package Agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class LecteurAgent extends Agent {

    protected void setup() {
        System.out.println("Déploiement de l'agent : " + getAID().getLocalName());
        Object[] para = getArguments();

        if (para != null && para.length > 0) {
            String livre = para[0].toString();
            System.out.println("Je vais essayer de lire : " + livre);

            // 1) Chercher le catalogue dans le DF
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("catalogue");
            dfd.addServices(sd);
            try {
                DFAgentDescription[] resultat = DFService.search(this, dfd);
                if (resultat.length > 0) {
                    AID catalogue = resultat[0].getName();
                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                    message.addReceiver(catalogue);
                    message.setContent(livre);
                    send(message);
                    System.out.println("Catalogue trouvé : " + catalogue.getLocalName());
                    System.out.println("Demande envoyée : " + livre);
                } else {
                    System.out.println("Aucun catalogue trouvé dans le DF");
                }
            } catch (FIPAException e) { e.printStackTrace(); }
        }

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage reponse = receive();
                if (reponse != null) {
                    String contenu = reponse.getContent();
                    System.out.println("Message reçu de : " + reponse.getSender().getLocalName()
                            + " → " + contenu);

                    if (contenu.startsWith("DISPONIBLE:")) {
                        String livre = contenu.substring("DISPONIBLE:".length()).trim();
                        System.out.println("✔ '" + livre + "' est au catalogue → je demande le prêt");
                        demanderPret(livre);

                    } else if (contenu.startsWith("INDISPONIBLE:")) {
                        String livre = contenu.substring("INDISPONIBLE:".length()).trim();
                        System.out.println("✘ '" + livre + "' n'est pas au catalogue → abandon");

                    } else if (contenu.startsWith("PRET_OK:")) {
                        String livre = contenu.substring("PRET_OK:".length()).trim();
                        System.out.println("✅ Prêt confirmé, je lis maintenant : " + livre);

                    } else if (contenu.startsWith("PRET_REFUSE:")) {
                        String livre = contenu.substring("PRET_REFUSE:".length()).trim();
                        System.out.println("❌ Prêt refusé, '" + livre + "' est déjà emprunté");
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void demanderPret(String livre) {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("pret");
        dfd.addServices(sd);
        try {
            DFAgentDescription[] resultat = DFService.search(this, dfd);
            if (resultat.length > 0) {
                AID pret = resultat[0].getName();
                ACLMessage demandePret = new ACLMessage(ACLMessage.REQUEST);
                demandePret.addReceiver(pret);
                demandePret.setContent(livre);
                send(demandePret);
                System.out.println("PretAgent trouvé : " + pret.getLocalName());
                System.out.println("Demande de prêt envoyée : " + livre);
            } else {
                System.out.println("Aucun agent de prêt trouvé dans le DF");
            }
        } catch (FIPAException e) { e.printStackTrace(); }
    }

    protected void beforeMove() { System.out.println("Avant migration : " + getAID().getLocalName()); }
    protected void afterMove()  { System.out.println("Après migration : " + getAID().getLocalName()); }
    protected void takeDown()   { System.out.println("Je meurs : " + getAID().getLocalName()); }
}