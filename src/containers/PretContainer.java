package containers;

import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.AgentController;
import Agents.PretAgent;

public class PretContainer {
	public static void main(String[] args)throws ControllerException{
		Runtime rt = Runtime.instance();
		ProfileImpl profil = new ProfileImpl();
		profil.setParameter(ProfileImpl.MAIN_HOST,"localhost");
		AgentContainer pretContainer = rt.createAgentContainer(profil);
		pretContainer.start();
		
		System.out.println("PretContainer démarré et connecté au Main Container !");
		
		AgentController pret = pretContainer.createNewAgent("PrêtAgent",PretAgent.class.getName(), new Object[] {"Le petit prince"});
		pret.start();
	}

}
