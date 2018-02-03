package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.model.ModelResultGraph;
import nl.gijspeters.pubint.model.Network;
import nl.gijspeters.pubint.model.ResultGraphBuilder;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 10-04-17.
 */
public class CreateNetworkTask extends Task {

    private ModelConfig config;
    private Leg leg;

    public CreateNetworkTask(TaskFinishedCallback callback, Leg leg, ModelConfig config) {
        super(callback);
        this.leg = leg;
        this.config = config;
    }

    @Override
    protected void executeTask() throws Exception {
        ResultGraphBuilder builder = new ResultGraphBuilder(config, leg);
        Network network = builder.buildProbabilityNetwork();
        ModelResultGraph edgeProbs = network.getEdgeProbabilities();
        MorphiaHandler.getInstance().saveResultGraph(edgeProbs);
        network = builder.buildVisitTimeNetwork();
        ModelResultGraph edgeTimes = network.getEdgeProbabilities();
        MorphiaHandler.getInstance().saveResultGraph(edgeTimes);
    }

    @Override
    public String toString() {
        return "CreateNetworkTask <" + leg.toString() + ">";
    }

}
