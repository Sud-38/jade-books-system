package containers;

import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.AgentController;
import Agents.CatalogueAgent;


public class CatalogueContainer {
	public static void main(String[] args)throws ControllerException{
		Runtime rt = Runtime.instance();
		ProfileImpl profil = new ProfileImpl();
		profil.setParameter(ProfileImpl.MAIN_HOST,"localhost");
		
		AgentContainer catalogueContainer = rt.createAgentContainer(profil);
		catalogueContainer.start();
		
		System.out.println("CatalogueContainer démarré et connecté au Main Container !");
		
		AgentController catalogue = catalogueContainer.createNewAgent("CatalogueAgent",CatalogueAgent.class.getName(),new Object[] {"Le petit prince"});
	    catalogue.start();
	}
}
