package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;


public class Maincontainer{
	public static void main(String[] args) throws ControllerException {
		Runtime rt = Runtime.instance();
		ProfileImpl profil = new ProfileImpl();
		profil.setParameter(ProfileImpl.GUI,"true");
		AgentContainer mainAgentContainer = rt.createMainContainer(profil);
		mainAgentContainer.start();
        System.out.println("Main conteuneur est démarée par succées !");
    }
}