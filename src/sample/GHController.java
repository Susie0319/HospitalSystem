package sample;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.System.exit;

public class GHController {
    private String BRBH;
    float accountNum;
    private String sqlStr;
    private List<List<Object>> result;
    @FXML ComboBox KSMCComboBox;
    @FXML ComboBox HZMCComboBox;
    @FXML ComboBox YSMCComboBox;
    @FXML ComboBox HZLBComboBox;
    @FXML private TextField accountNumText, costNumText, changeNumText, registerIdText;


    public void init(String str) throws Exception{
        BRBH = str;
        AutoCompleteComboBoxListener ksBox = new AutoCompleteComboBoxListener(KSMCComboBox);
        AutoCompleteComboBoxListener ysBox = new AutoCompleteComboBoxListener(YSMCComboBox);
        AutoCompleteComboBoxListener hlBox = new AutoCompleteComboBoxListener(HZLBComboBox);
        AutoCompleteComboBoxListener hmBox = new AutoCompleteComboBoxListener(HZMCComboBox);
    }

    /**
     * 计算设置账户余额
     * @throws Exception
     */
    public void setAccount() throws Exception{
        sqlStr="select YCJE from T_BRXX where BRBH = \'" + BRBH +"\';";
        result = MySQLConnector.getData(sqlStr);
        accountNum = Float.parseFloat(result.get(0).get(0).toString());
        System.out.println("accountNum = " + accountNum);
        if(accountNum > 0) {
            accountNumText.setEditable(false);
            accountNumText.setText(Float.toString(accountNum));
            changeNumText.setEditable(false);
        }
    }

    /**
     * 计算设置应付金额
     * @throws Exception
     */
    public void setCharge() throws Exception{
        if(HZMCComboBox.getValue() == null)
            return ;
        String HZBH = HZMCComboBox.getValue().toString().split(",")[0];
        int GHRC = hasNumOrNot(HZBH);
        if(GHRC != -1) {
            registerIdText.setText(String.valueOf(GHRC + 1));
        }
        sqlStr = "select GHFY from T_HZXX where HZBH = \"" + HZBH + "\";";
        result = MySQLConnector.getData(sqlStr);
        String costNum = result.get(0).get(0).toString();
        costNumText.setText(costNum);
        if(accountNum == 0){
            accountNumText.setEditable(true);
            accountNumText.clear();
        }
        setChange();
    }

    /**
     * 计算设置找零金额
     * @throws Exception
     */
    private void setChange() throws Exception{
        float charge = 0;
        if(!costNumText.getText().isEmpty()){
            charge = Float.parseFloat(costNumText.getText());
        }
        float change = accountNum - charge;
        if(change < 0) {
            changeNumText.setText("0.00");
        }
        else {
            changeNumText.setText(String.valueOf(change));
        }
    }

    /**
     * 以下handleSet控制各个ComboBox
     * @throws Exception
     */
    @FXML
    private void handleSetKSMC() throws Exception {
//        AutoCompleteComboBoxListener ksBox = new AutoCompleteComboBoxListener(KSMCComboBox);
        sqlStr = "select KSBH, KSMC, PYZS from T_KSXX;";
        result = MySQLConnector.getData(sqlStr);
        String content;
        KSMCComboBox.getItems().clear();

        for(int i=0; i<result.size(); i++){
            content = result.get(i).get(0) + "," + result.get(i).get(1) + "," + result.get(i).get(2);
            KSMCComboBox.getItems().addAll(content);
        }
        KSMCComboBox.show();
        YSMCComboBox.getItems().clear();
        HZLBComboBox.getItems().clear();
        HZMCComboBox.getItems().clear();

    }

    @FXML
    private void handleSetYSMC() throws Exception{
//        AutoCompleteComboBoxListener a = new AutoCompleteComboBoxListener(YSMCComboBox);
        sqlStr = "select YSBH, YSMC, PYZS from T_KSYS";
        if(KSMCComboBox.getValue() != null) {
            sqlStr += " where KSBH = \""+ KSMCComboBox.getValue().toString().split(",")[0]+"\"";
            if(HZLBComboBox.getValue() != null ) {
                if(HZLBComboBox.getValue().toString().equals("普通号")) {
                    sqlStr += " and SFZJ = 0";
                }
                else {
                    sqlStr += " and SFZJ = 1";
                }
            }
        }

        sqlStr += ";";
        result = MySQLConnector.getData(sqlStr);
        String content;
        YSMCComboBox.getItems().clear();

        for(int i=0; i<result.size(); i++){
            content = result.get(i).get(0) + "," + result.get(i).get(1) + "," + result.get(i).get(2);
            YSMCComboBox.getItems().addAll(content);
        }
        YSMCComboBox.show();

//        HZMCComboBox.getItems().clear();
        HZLBComboBox.getItems().clear();

    }

    @FXML
    private void handleSetHZLB() throws Exception{
        HZLBComboBox.getItems().clear();
//        HZLBComboBox.getItems().addAll("普通号");
        if(YSMCComboBox.getValue() != null){
            String YSBH = YSMCComboBox.getValue().toString().split(",")[0];
            sqlStr ="select SFZJ from T_KSYS where YSBH = \"" + YSBH +"\";";
            result = MySQLConnector.getData(sqlStr);
            if(result.get(0).get(0).toString().equals("true")) {
                HZLBComboBox.getItems().addAll("专家号");
            }
            else {
                HZLBComboBox.getItems().addAll("普通号");
            }
        }
        HZLBComboBox.show();
        HZMCComboBox.getItems().clear();
    }

    @FXML
    private void handleSetHZMC() throws Exception{
        sqlStr = "select HZBH, HZMC, PYZS from T_HZXX";
        if(KSMCComboBox.getValue() != null) {
            sqlStr += " where KSBH = \"" + KSMCComboBox.getValue().toString().split(",")[0] + "\"";

            if (HZLBComboBox.getValue() != null) {
                if (HZLBComboBox.getValue().toString().equals("普通号")) {
                    sqlStr += " and SFZJ = 0";
                } else {
                    sqlStr += " and SFZJ = 1";
                }
            } else if (YSMCComboBox.getValue() != null) {
                String YSBH = YSMCComboBox.getValue().toString().split(",")[0];
                String sqlStrTemp = "select SFZJ from T_KSYS where YSBH = \"" + YSBH + "\";";
                List<List<Object>> res = MySQLConnector.getData(sqlStrTemp);
                if (res.get(0).get(0).toString().equals("false")) {
                    sqlStr += " and SFZJ = 0";
                }
            }
        }
        sqlStr += ";";
        result = MySQLConnector.getData(sqlStr);
        String content;
        HZMCComboBox.getItems().clear();

        for(int i=0; i<result.size(); i++){
            content = result.get(i).get(0) + "," + result.get(i).get(1) + "," + result.get(i).get(2);
            HZMCComboBox.getItems().addAll(content);
        }

        HZMCComboBox.show();
    }

    @FXML
    private synchronized void clickConfirmBtn() throws Exception{
        if(KSMCComboBox.getItems().isEmpty() || KSMCComboBox.getEditor().getText().isEmpty()) {
            showDialog("挂号失败", "请将科室名称补充完整！");
            return ;
        }
        else if (YSMCComboBox.getItems().isEmpty() || YSMCComboBox.getEditor().getText().isEmpty()) {
            showDialog("挂号失败", "请将医生名称补充完整！");
            return ;
        }
        else if (HZLBComboBox.getItems().isEmpty() || HZLBComboBox.getEditor().getText().isEmpty()) {
            showDialog("挂号失败", "请将号种类别补充完整！");
            return ;
        }
        else if (HZMCComboBox.getItems().isEmpty() || HZMCComboBox.getEditor().getText().isEmpty()) {
            showDialog("挂号失败", "请将号种名称补充完整！");
//            System.out.println();
            return ;
        }
        else if (accountNumText.getText().isEmpty()) {
            showDialog("挂号失败", "余额不足，请缴费！");
            return ;
        }

        String KSBH = KSMCComboBox.getValue().toString().split(",")[0];
        String YSBH = YSMCComboBox.getValue().toString().split(",")[0];
        String HZBH = HZMCComboBox.getValue().toString().split(",")[0];
        float costNum = Float.parseFloat(costNumText.getText());
        if(HZBH == null){
            showDialog("挂号失败", "请您选择号种！");
            return ;
        }
        //确定还有号
        int GHRC = hasNumOrNot(HZBH);
        if(GHRC != -1){
            int THBZ = 0;
            String GHHM;
            //确定时间
            java.util.Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if(accountNumText.isEditable()) {
                accountNum = Float.parseFloat(accountNumText.getText());
            }

            if(accountNum >= costNum) {
                sqlStr = "update T_BRXX set YCJE = " + (accountNum-costNum) + " where BRBH = \'" + BRBH + "\';";
                int res = MySQLConnector.insData(sqlStr, 8);
                if (res == -1) {
                    showDialog("挂号失败！", "挂号失败，请确认信息填写完整！");
                    return ;
                }
                GHHM = String.format("%06d", (GHRC + 1));
                sqlStr = String.format("insert into T_GHXX (GHBH,HZBH,YSBH,BRBH,GHRC,THBZ,GHFY,RQSJ) values(\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%f,\"%s\");",
                        GHHM, HZBH, YSBH, BRBH, GHRC, THBZ, Float.parseFloat(costNumText.getText()), df.format(day));
                res = MySQLConnector.insData(sqlStr, 8);
                if (res == -1) {
                    showDialog("挂号失败！", "挂号失败，请确认信息填写完整！");
                    return ;
                }
                registerIdText.setText(String.valueOf(GHRC + 1));
                accountNum -= costNum;
                showDialog("挂号成功！","挂号成功！您的号码是" + (GHRC + 1));
                setAccount();
                setCharge();
            }
            else {
                showDialog("余额不足！","余额不足，请您缴费！");
                accountNumText.setEditable(true);
                return ;
            }
        }
        return ;
    }

    @FXML
    private void clickExitBtn() throws Exception{
        exit(0);
    }

    @FXML
    private void clickClearBtn() throws Exception{
        KSMCComboBox.getItems().clear();
        HZMCComboBox.getItems().clear();
        YSMCComboBox.getItems().clear();
        HZLBComboBox.getItems().clear();
        costNumText.clear();
        changeNumText.clear();
        registerIdText.clear();
    }

    /**
     * 检查是否还有可挂的号（当日已挂号是否已满）
     * @param HZBH -- 号种编号
     * @return GHRC -- 此号种挂号人次, -1 -- 无号
     */
    private int hasNumOrNot(String HZBH) throws Exception {
        sqlStr = "select count(*) from T_GHXX where HZBH = \"" + HZBH + "\";";
        result = MySQLConnector.getData(sqlStr);
        int GHRC = Integer.parseInt(result.get(0).get(0).toString());
        sqlStr = "select GHRS from T_HZXX where HZBH = \"" + HZBH + "\";";
        result = MySQLConnector.getData(sqlStr);
        int GHRS = Integer.parseInt(result.get(0).get(0).toString());
        System.out.println("GHRC = " + GHRC);
        if(GHRC == GHRS){
            Alert broke = new Alert(Alert.AlertType.WARNING);
            broke.setTitle("人数已满！");
            broke.setHeaderText("此号种本日已无号，请您重新选择号种！");
            broke.show();
            return -1;
        }
        else {
            sqlStr = "select count(*) from T_GHXX;";
            result = MySQLConnector.getData(sqlStr);
            return Integer.parseInt(result.get(0).get(0).toString());
        }
    }

    /**
     * 弹出提示框
     * @param title -- 提示框标题
     * @param text -- 提示框内容
     */
    public void showDialog(String title, String text) throws Exception{
        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.show();
    }
}
