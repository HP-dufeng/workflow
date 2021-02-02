package dangqu.powertrade.automation.editor.language.json.converter;


public enum FlowElementType {
    Start("Start"),
    End("End"),
    Applicant("Applicant"),
    UserTask("UserTask"),
    CC("CC"),
    Fork("Fork"),
    Join("Join"),
    SequenceFlow("SequenceFlow");
    

    private String shpae;

    FlowElementType(String shape) {
        this.shpae = shape;
    }

    public String getShpae() {
        return shpae;
    }
}
