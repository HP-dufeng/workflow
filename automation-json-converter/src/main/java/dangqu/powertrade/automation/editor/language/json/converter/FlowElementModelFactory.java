package dangqu.powertrade.automation.editor.language.json.converter;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;

import dangqu.powertrade.automation.editor.language.json.model.ApplicantModel;
import dangqu.powertrade.automation.editor.language.json.model.CarbonCopyModel;
import dangqu.powertrade.automation.editor.language.json.model.EndModel;
import dangqu.powertrade.automation.editor.language.json.model.FlowElementModel;
import dangqu.powertrade.automation.editor.language.json.model.ForkModel;
import dangqu.powertrade.automation.editor.language.json.model.JoinModel;
import dangqu.powertrade.automation.editor.language.json.model.SequenceFlowModel;
import dangqu.powertrade.automation.editor.language.json.model.StartModel;
import dangqu.powertrade.automation.editor.language.json.model.UserTaskModel;

public class FlowElementModelFactory {
    public static FlowElementModel build(FlowElementType elementType, JsonNode cellNode, JsonNode settingNode) {
        FlowElementModel.Builder<FlowElementModel> builder;
        switch (elementType) {
            case Start:
                builder = new StartModel().builder();
                break;
            case End:
                builder = new EndModel().builder();
                break;
            case Applicant:
                builder = new ApplicantModel().builder();
                break;
            case SequenceFlow:
                builder = new SequenceFlowModel().builder();
                break;
            case Fork:
                builder = new ForkModel().builder();
                break;
            case Join:
                builder = new JoinModel().builder();
                break;
            case UserTask:
                builder = new UserTaskModel().builder();
                break;
            case CC:
                builder = new CarbonCopyModel().builder();
                break;
            default:
                throw new FlowableIllegalArgumentException(
                        String.format("Flow Element type: %s not implemented.", elementType.getShpae()));

        }

        FlowElementModel model = builder.cellNode(cellNode).settingNode(settingNode).build();
        return model;
    }

}
