package ma.enset.ga.islandModel;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.ga.common.GAUtils;
import ma.enset.ga.common.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class masterAgent extends Agent {
    List<Individual> solutions=new ArrayList<>();
    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setName("master");
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this,dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        addBehaviour(new Behaviour() {
            int it=0;
            @Override
            public void action() {
                ACLMessage aclMessage=blockingReceive();
                String[] content=aclMessage.getContent().split("-");
                System.out.println("BEST SOLUTION FOUND BY AGENT : "+aclMessage.getSender().getName());
                System.out.println(content[0]+" fitness : "+content[1]);
                System.out.println("----------------------------------------------");
                solutions.add(new Individual(content[0].toCharArray(),Integer.valueOf(content[1]),aclMessage.getSender().getName()));
                it++;
            }

            @Override
            public boolean done() {
                if(it== GAUtils.ISLAND_NUMBER) {
                    Collections.sort(solutions,Collections.reverseOrder());
                    System.out.println();
                    System.out.println();
                    char[] genes = solutions.get(0).getGenes();
                    String genesString = new String(genes);
                    System.out.println("THE BEST OF THE BEST IS THE SOLUTION FOUND BY "+solutions.get(0).getAgentName());
                    System.out.println("solution : "+genesString +" | fitness: "+solutions.get(0).getFitness());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }
}
