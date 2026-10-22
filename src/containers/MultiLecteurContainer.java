package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.AgentController;
import Agents.LecteurAgent;

public class MultiLecteurContainer {
    public static void main(String[] args) throws ControllerException {
        Runtime rt = Runtime.instance();
        ProfileImpl profil = new ProfileImpl();
        profil.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer lecteurContainer = rt.createAgentContainer(profil);
        lecteurContainer.start();
        System.out.println("MultiLecteurContainer démarré !");

        if (args.length > 0) {
            // Mode manuel : java containers.MultiLecteurContainer "1984" "Lecteur_X"
            String livre = args[0];
            String nom   = (args.length > 1) ? args[1] : "Lecteur 1";
            lecteurContainer.createNewAgent(
                    nom, LecteurAgent.class.getName(), new Object[]{ livre }).start();
        } else {
            // Mode démo : lance 6 lecteurs pour tester tous les scénarios
            String[][] lecteurs = {
                { "Lecteur 1", "Le petit prince" },
                { "Lecteur 2", "Les misérables" },
                { "Lecteur 3", "1984" },
                { "Lecteur 4", "Harry Potter" },
                { "Lecteur 5", "Le petit prince" },           // PRET_REFUSE
                { "Lecteur 6", "Le livre qui n'existe pas" }  // INDISPONIBLE
            };
            for (String[] l : lecteurs) {
                AgentController ac = lecteurContainer.createNewAgent(
                        l[0], LecteurAgent.class.getName(), new Object[]{ l[1] });
                ac.start();
            }
        }
    }
}