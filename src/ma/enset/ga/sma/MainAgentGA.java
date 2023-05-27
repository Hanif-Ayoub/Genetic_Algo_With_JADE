package ma.enset.ga.sma;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.ga.common.GAUtils;

import java.util.*;

public class MainAgentGA extends Agent {
    List<AgentFitness> agentsFitness=new ArrayList<>();
    Random rnd=new Random();
    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] agentsDescriptions = DFService.search(this, dfAgentDescription);
            for (DFAgentDescription dfAD:agentsDescriptions) {
                agentsFitness.add(new AgentFitness(dfAD.getName(),0));
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        calculateFintness();

        SequentialBehaviour sequentialBehaviour=new SequentialBehaviour();


        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int cpt=0;
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();
                if (receivedMSG!=null){
                    cpt++;
                    int fintess=Integer.parseInt(receivedMSG.getContent());
                    AID sender=receivedMSG.getSender();
                    setAgentFintess(sender,fintess);
                    if(cpt==GAUtils.POPULATION_SIZE){
                        Collections.sort(agentsFitness,Collections.reverseOrder());
                        showPopulation();
                    }
                }else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return cpt==GAUtils.POPULATION_SIZE;
            }

        });

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int it=0;
            AgentFitness agent1;
            AgentFitness agent2;
            @Override
            public void action() {
                selection();
                crossover();
                Collections.sort(agentsFitness,Collections.reverseOrder());
                sendMessage(agentsFitness.get(0).getAid(),"chromosome",ACLMessage.REQUEST);
                ACLMessage aclMessage=blockingReceive();
                System.out.println(aclMessage.getContent()+" : "+agentsFitness.get(0).getFitness());
                it++;
            }
            public void selection(){
                agent1=agentsFitness.get(0);
                agent2=agentsFitness.get(1);
                sendMessage(agent1.getAid(),"chromosome",ACLMessage.REQUEST);
                sendMessage(agent2.getAid(),"chromosome",ACLMessage.REQUEST);
            }
            public void crossover(){
                ACLMessage aclMessage1=blockingReceive();
                ACLMessage aclMessage2=blockingReceive();

                int pointCroisment=rnd.nextInt(GAUtils.CHROMOSOME_SIZE-2);
                pointCroisment++;
                char[] chromosomeParent1=aclMessage1.getContent().toCharArray();
                char[] chromosomeParent2=aclMessage2.getContent().toCharArray();
                char[] chromosomeOffsring1=new char[GAUtils.CHROMOSOME_SIZE];
                char[] chromosomeOffsring2=new char[GAUtils.CHROMOSOME_SIZE];
                for (int i=0;i<chromosomeParent1.length;i++) {
                    chromosomeOffsring1[i]=chromosomeParent1[i];
                    chromosomeOffsring2[i]=chromosomeParent2[i];
                }
                for (int i=0;i<pointCroisment;i++) {
                    chromosomeOffsring1[i]=chromosomeParent2[i];
                    chromosomeOffsring2[i]=chromosomeParent1[i];
                }

                int fitnessChromo1=0;
                int fitnessChromo2=0;
                for(int i=0;i<GAUtils.CHROMOSOME_SIZE;i++){
                    if(chromosomeOffsring1[i]==GAUtils.SOLUTION.charAt(i)) fitnessChromo1++;
                    if(chromosomeOffsring2[i]==GAUtils.SOLUTION.charAt(i)) fitnessChromo2++;
                }
                agentsFitness.get(GAUtils.POPULATION_SIZE-2).setFitness(fitnessChromo1);
                agentsFitness.get(GAUtils.POPULATION_SIZE-1).setFitness(fitnessChromo2);


                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-2).getAid(),new String(chromosomeOffsring1),
                        ACLMessage.REQUEST);
                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-1).getAid(),new String(chromosomeOffsring2),
                        ACLMessage.REQUEST);

                ACLMessage receivedMsg1=blockingReceive();
                ACLMessage receivedMsg2=blockingReceive();

                setAgentFintess(receivedMsg1.getSender(),Integer.parseInt(receivedMsg1.getContent()));
                setAgentFintess(receivedMsg2.getSender(),Integer.parseInt(receivedMsg2.getContent()));
            }

            @Override
            public boolean done() {
                if(it==GAUtils.MAX_IT || agentsFitness.get(0).getFitness()==GAUtils.MAX_FITNESS){
                    System.out.println();
                    System.out.println("SOLUTION FOUND IN GENERATION "+it);
                    return true;
                }
                return false;
            }
        });

        addBehaviour(sequentialBehaviour);

    }
private void calculateFintness(){
    ACLMessage message=new ACLMessage(ACLMessage.REQUEST);

    for (AgentFitness agf:agentsFitness) {
        message.addReceiver(agf.getAid());
    }
    message.setContent("fitness");
    send(message);

}
private void setAgentFintess(AID aid,int fitness){
        for (int i=0;i<GAUtils.POPULATION_SIZE;i++){
            if(agentsFitness.get(i).getAid().equals(aid)){
                agentsFitness.get(i).setFitness(fitness);
                break;
            }
        }
}

private void sendMessage(AID aid,String content,int performative){
        ACLMessage aclMessage=new ACLMessage(performative);
        aclMessage.setContent(content);
        aclMessage.addReceiver(aid);
        send(aclMessage);
}

private void showPopulation(){
    for (AgentFitness agentFitness:agentsFitness) {
        System.out.println(agentFitness.getAid().getName()+" "+agentFitness.getFitness());
    }
}
}
