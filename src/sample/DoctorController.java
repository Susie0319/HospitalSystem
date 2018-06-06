package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.lang.System.exit;

public class DoctorController {
    String YSBH;
    String sqlStr;
    private List<List<Object>> result;
//    @FXML Button searchBtn;
    @FXML private DatePicker startDayPicker, endDayPicker;
    @FXML private TextField startHourText, endHourText;
    @FXML private TableView<patientInfo> patientTable;
    @FXML private TableView<incomeInfo> incomeTable;

    @FXML private TableColumn<patientInfo,String> patiGHBH;
    @FXML private TableColumn<patientInfo,String> patiBRXM;
    @FXML private TableColumn<patientInfo,String> patiGHSJ;
    @FXML private TableColumn<patientInfo,String> patiGHLX;

    @FXML private TableColumn<incomeInfo,String> incomeKSMC;
    @FXML private TableColumn<incomeInfo,String> incomeYSBH;
    @FXML private TableColumn<incomeInfo,String> incomeYSXM;
    @FXML private TableColumn<incomeInfo,String> incomeHZLB;
    @FXML private TableColumn<incomeInfo,String> incomeGHRC;
    @FXML private TableColumn<incomeInfo,String> incomeSRHJ;


    public void init (String str) throws Exception{
        YSBH = str;
        clickRefreshPatient();
        clickSearchBtn();
    }

    @FXML
    private void clickRefreshPatient(){
        sqlStr = "select GHBH, BRMC, RQSJ, if (SFZJ = true,\"专家号\",\"普通号\") as HZLB from T_GHXX, T_BRXX, T_HZXX where T_GHXX.YSBH = \"" + YSBH +
                "\" and T_GHXX.BRBH = T_BRXX.BRBH and T_GHXX.HZBH = T_HZXX.HZBH;";
        result = MySQLConnector.getData(sqlStr);

        ObservableList<patientInfo> list = FXCollections.observableArrayList();
        for(int i=0; i<result.size(); i++) {
            patientInfo temp = new patientInfo(result.get(i).get(0).toString(), result.get(i).get(1).toString(),
                    result.get(i).get(2).toString(), result.get(i).get(3).toString());
            list.add(temp);
        }
        patiGHBH.setCellValueFactory(cellData->cellData.getValue().patiGHBHProperty());
        patiBRXM.setCellValueFactory(cellData->cellData.getValue().patiBRXMProperty());
        patiGHSJ.setCellValueFactory(cellData->cellData.getValue().patiGHSJProperty());
        patiGHLX.setCellValueFactory(cellData->cellData.getValue().patiGHLXProperty());
        patientTable.setItems(list);
    }


    @FXML
    private void clickSearchBtn() throws Exception{
        String startDay, endDay;
        int startHour, endHour;
        String startTime, endTime;

        System.out.println(startDayPicker.getValue());
        if(startDayPicker.getValue() == null) {
            startDayPicker.setValue(LocalDate.now());
        }
        if(endDayPicker.getValue() == null) {
            endDayPicker.setValue(LocalDate.now());
        }

        startDay = startDayPicker.getValue().toString();
        endDay = endDayPicker.getValue().toString();


        if(startHourText.getText().isEmpty()){
            startHour = 0;
        }
        else {
            startHour = Integer.parseInt(startHourText.getText());
        }

        if(endHourText.getText().isEmpty()){
            endHour = LocalTime.now().getHour();
        }
        else {
            endHour = Integer.parseInt(endHourText.getText());
        }

        startTime = startDay + " " + Integer.toString(startHour) + ":00:00";
        System.out.println("startTime = " + startTime);
        endTime = endDay + " " + Integer.toString(endHour) + ":00:00";
        System.out.println("endTime = " + endTime);

        sqlStr = "select T_KSXX.KSMC '科室名称',T_GHXX.YSBH '医生编号',T_KSYS.YSMC '医生名称',T_HZXX.HZMC '号种类别',count(T_GHXX.GHRC) '挂号人数',sum(T_HZXX.GHFY) '收入'" +
                "from T_GHXX,T_HZXX,T_KSYS,T_KSXX where T_GHXX.HZBH=T_HZXX.HZBH and T_GHXX.YSBH=T_KSYS.YSBH and T_KSYS.KSBH=T_KSXX.KSBH and " +
                "T_GHXX.RQSJ>= \"" + startTime + "\" and T_GHXX.RQSJ<= \"" + endTime + "\" " +
                "group by T_KSXX.KSMC,T_GHXX.YSBH,T_HZXX.HZMC;";
        result = MySQLConnector.getData(sqlStr);
        ObservableList<incomeInfo> list = FXCollections.observableArrayList();
        for(int i=0; i<result.size(); i++) {
            incomeInfo temp = new incomeInfo(result.get(i).get(0).toString(), result.get(i).get(1).toString(),
                    result.get(i).get(2).toString(), result.get(i).get(3).toString(),
                    result.get(i).get(4).toString(), result.get(i).get(5).toString());
            list.add(temp);
        }
        incomeKSMC.setCellValueFactory(cellData->cellData.getValue().incomeKSMCProperty());
        incomeYSBH.setCellValueFactory(cellData->cellData.getValue().incomeYSBHProperty());
        incomeYSXM.setCellValueFactory(cellData->cellData.getValue().incomeYSXMProperty());
        incomeHZLB.setCellValueFactory(cellData->cellData.getValue().incomeHZLBProperty());
        incomeGHRC.setCellValueFactory(cellData->cellData.getValue().incomeGHRCProperty());
        incomeSRHJ.setCellValueFactory(cellData->cellData.getValue().incomeSRHJProperty());
        incomeTable.setItems(list);

    }

    @FXML
    private void clickExitBtn(){
        patientTable.getItems().clear();
        incomeTable.getItems().clear();
        exit(0);

    }
}

/**
 * 用来给tableView绑定的类
 */
class patientInfo {
    private SimpleStringProperty patiGHBH;
    private SimpleStringProperty patiBRXM;
    private SimpleStringProperty patiGHSJ;
    private SimpleStringProperty patiGHLX;

    public patientInfo(String patiGHBH, String patiBRXM, String patiGHSJ, String patiGHLX) {
        super();
        this.patiGHBH = new SimpleStringProperty(patiGHBH);
        this.patiBRXM = new SimpleStringProperty(patiBRXM);
        this.patiGHSJ = new SimpleStringProperty(patiGHSJ);
        this.patiGHLX = new SimpleStringProperty(patiGHLX);
    }

    public String getPatiGHBH() { return patiGHBH.get(); }
    public String getPatiBRXM() { return patiBRXM.get(); }
    public String getPatiGHSJ() { return patiGHSJ.get(); }
    public String getPatiGHLX() { return patiGHLX.get(); }

    public void setPatiGHBH(String patiGHBH) { this.patiGHBH.set(patiGHBH); }
    public void setPatiBRXM(String patiBRXM) { this.patiBRXM.set(patiBRXM); }
    public void setPatiGHSJ(String patiGHSJ) { this.patiGHSJ.set(patiGHSJ); }
    public void setPatiGHLX(String patiGHLX) { this.patiGHLX.set(patiGHLX); }

    public StringProperty patiGHBHProperty() { return patiGHBH; }
    public StringProperty patiBRXMProperty() { return patiBRXM; }
    public StringProperty patiGHSJProperty() { return patiGHSJ; }
    public StringProperty patiGHLXProperty() { return patiGHLX; }
}


class incomeInfo {
    private SimpleStringProperty incomeKSMC;
    private SimpleStringProperty incomeYSBH;
    private SimpleStringProperty incomeYSXM;
    private SimpleStringProperty incomeHZLB;
    private SimpleStringProperty incomeGHRC;
    private SimpleStringProperty incomeSRHJ;

    public incomeInfo(String incomeKSMC, String incomeYSBH, String incomeYSXM, String incomeHZLB, String incomeGHRC, String incomeSRHJ) {
        super();
        this.incomeKSMC = new SimpleStringProperty(incomeKSMC);
        this.incomeYSBH = new SimpleStringProperty(incomeYSBH);
        this.incomeYSXM = new SimpleStringProperty(incomeYSXM);
        this.incomeHZLB = new SimpleStringProperty(incomeHZLB);
        this.incomeGHRC = new SimpleStringProperty(incomeGHRC);
        this.incomeSRHJ = new SimpleStringProperty(incomeSRHJ);
    }

    public String getIncomeKSMC() { return incomeKSMC.get(); }
    public String getIncomeYSBH() { return incomeYSBH.get(); }
    public String getIncomeYSXM() { return incomeYSXM.get(); }
    public String getIncomeHZLB() { return incomeHZLB.get(); }
    public String getIncomeGHRC() { return incomeGHRC.get(); }
    public String getIncomeSRHJ() { return incomeSRHJ.get(); }

    public void setIncomeKSMC(String incomeKSMC) { this.incomeKSMC.set(incomeKSMC); }
    public void setIncomeYSBH(String incomeYSBH) { this.incomeYSBH.set(incomeYSBH); }
    public void setIncomeYSXM(String incomeYSXM) { this.incomeYSXM.set(incomeYSXM); }
    public void setIncomeHZLB(String incomeHZLB) { this.incomeHZLB.set(incomeHZLB); }
    public void setIncomeGHRC(String incomeGHRC) { this.incomeGHRC.set(incomeGHRC); }
    public void setIncomeSRHJ(String incomeSRHJ) { this.incomeSRHJ.set(incomeSRHJ); }

    public StringProperty incomeKSMCProperty() { return incomeKSMC; }
    public StringProperty incomeYSBHProperty() { return incomeYSBH; }
    public StringProperty incomeYSXMProperty() { return incomeYSXM; }
    public StringProperty incomeHZLBProperty() { return incomeHZLB; }
    public StringProperty incomeGHRCProperty() { return incomeGHRC; }
    public StringProperty incomeSRHJProperty() { return incomeSRHJ; }


}