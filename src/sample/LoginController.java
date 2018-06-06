package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.System.exit;


public class LoginController  {
    private Stage patientStage, doctorStage;
    @FXML private Button loginBtn, registBtn;
    @FXML private RadioButton patientRb, doctorRb;
    @FXML private TextField userNameText;
    @FXML private PasswordField passwordText;

    private ToggleGroup group;

    public void initView () {
        group = new ToggleGroup();

        patientRb.setToggleGroup(group);
        patientRb.setUserData(0);

        doctorRb.setToggleGroup(group);
        doctorRb.setUserData(1);

        group.selectToggle(doctorRb);
    }

    @FXML
    public void actionPerformed(ActionEvent e) throws Exception {
        Object obj = e.getSource();
        short pORd = 0;         // 0 is patient, 1 is doctor
        String userName = userNameText.getText().trim();
        String password = passwordText.getText();
        String sqlStr;
        String reFailed = "注册失败";
        String loFailed = "登陆失败";
        if (group.getSelectedToggle().getUserData().equals(1)) {
            pORd = 1;
        }

        if (obj == loginBtn) {
            if (pORd == 0) {  //patient
                sqlStr = "select BRBH, DLKL from T_BRXX where BRMC = \'" + userName + "\';";
                List<List<Object>> result = MySQLConnector.getData(sqlStr);
                if(!result.isEmpty()) {
                    if(password.equals(result.get(0).get(1))) {
                        System.out.println("病人登陆成功");

                        patientStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("Register.fxml"));
                        patientStage.setScene(new Scene(loader.load()));
                        patientStage.setTitle(userName);
                        GHController controller = loader.getController();
                        controller.init(result.get(0).get(0).toString());
                        controller.setAccount();
                        patientStage.show();
                        ((Stage)(loginBtn.getScene().getWindow())).close();

                    }
                    else{
                        display(loFailed, "密码错误");
                    }
                }
                else {
                    display(loFailed, "该用户不存在！\n请注册！");
                }
            }
            else {
                sqlStr = "select YSBH, DLKL from T_KSYS where YSMC = \'" + userName + "\';";
                List<List<Object>> result = MySQLConnector.getData(sqlStr);
                System.out.println(result);
                if(!result.isEmpty()) {
                    if(password.equals(result.get(0).get(1))) {
                        System.out.println("医生登陆成功");

                        doctorStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("Doctor.fxml"));
                        doctorStage.setScene(new Scene(loader.load()));
                        doctorStage.setTitle(userName);
                        DoctorController controller = loader.getController();
                        controller.init(result.get(0).get(0).toString());
                        doctorStage.show();
                        ((Stage)(loginBtn.getScene().getWindow())).close();

                    }
                    else{
                        display(loFailed, "密码错误");
                    }
                }
                else {
                    display(loFailed, "该用户不存在！\n请注册！");
                }
            }
        }

        if (obj == registBtn) {
            if(password.equals(" ") || userName.equals("")) {
                display(reFailed, "注册失败，\n请检查用户名和密码信息是否填写完整！");
            }
            else {
                if (pORd == 0) {  //patient
                    sqlStr = "select BRMC, DLKL from T_BRXX where BRMC = \'" + userName + "\';";
                    List<List<Object>> result = MySQLConnector.getData(sqlStr);
                    if (result.isEmpty()) {      //判断改用户名是否已经存在
                        // 得到应输入的下一个编号
                        sqlStr = "select max(BRBH) from T_BRXX";
                        List<List<Object>> nowpid = MySQLConnector.getData(sqlStr);
                        String nextpid = String.format("%06d", Integer.parseInt(nowpid.get(0).get(0).toString()) + 1);
                        // 得到登陆日期
                        Date day = new Date();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // 插入新的注册信息
                        sqlStr = String.format("insert into T_BRXX (BRBH, BRMC, DLKL, YCJE, DLRQ) values (\"%s\",\"%s\",\"%s\", %d, \"%s\");"
                                ,nextpid, userName, password, 100, df.format(day));
                        int res = MySQLConnector.insData(sqlStr, 8);
                        if (res == -1) {
                            display(reFailed, "注册失败，\n请检查用户名和密码信息是否填写完整！");
                        } else {
                            display("注册成功", "注册成功！");
                        }
                    } else {
                        display(reFailed, "该用户已存在，请更换用户名！");
                    }
                }
                else {
                    display(reFailed, "注册失败，医生不能注册！");
                }
            }
        }
    }

    /**
     * 对话框的显示
     * @param title
     * @param message
     */
    public void display(String title , String message){
        Stage window = new Stage();
        window.setTitle(title);
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Button button = new Button("确定");
        button.setOnAction(e->window.close());

        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    @FXML
    private void clickExitBtn(){
        exit(0);

    }
}



