package ma.enset.ga.islandModel;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ma.enset.ga.common.GAUtils;


public class IslandContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        for (int i=0;i< GAUtils.ISLAND_NUMBER;i++){
            AgentController islandAgent = agentContainer.createNewAgent("island "+i, islandAgent.class.getName(), new Object[]{});
            islandAgent.start();
        }
    }
}
