package net.malangbaram.DevLauncher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.malangbaram.DevLauncher.Util.AlterUtil;
import net.malangbaram.DevLauncher.Util.CompressionUtil;
import net.malangbaram.DevLauncher.Util.Util;
import net.malangbaram.DevLauncher.Util.VersionManagementUtil;

public class DevloperLauncher extends Application {

	static int version = 20151122;
	
	static File minecraftP = new File(System.getenv("APPDATA") + "\\.minecraft");
	static String myVersion;
	static String lastVersion;
	static boolean updatePlease;
	
	public static void main(String[] args) throws Exception {

		int i;
		if((version - (i = VersionManagementUtil.checkLauncherVersion("http://dl.malangbaram.net/launcherVersion.txt"))) != 0) {
			AlterUtil.showInformationAlter(Lang.TITLE, "새로운 런처버전이 존재합니다!");
			
		}else {

			myVersion = VersionManagementUtil.checkMyVersion(minecraftP, "mVersion.txt");
			lastVersion = VersionManagementUtil.checkLastVersion("http://dl.malangbaram.net/modVersion.txt");

			if (lastVersion.equals(myVersion)) {
				updatePlease = false;
			} else {
				updatePlease = true;
			}

			Application.launch(args);
	
		}
	}

	@Override
	public void start(Stage frame) throws Exception {
		frame.setTitle(Lang.TITLE);
		Group gp = new Group();
		Scene sc = new Scene(gp, 149, 120);

		Label loVer = new Label("현재버전:");
		final Label viewLoVer = new Label(myVersion);
		loVer.setLayoutX(5);
		loVer.setLayoutY(10);
		viewLoVer.setLayoutX(72);
		viewLoVer.setLayoutY(10);

		Label laVer = new Label("최신버전:");
		Label viewLaVer = new Label(lastVersion);
		laVer.setLayoutX(5);
		laVer.setLayoutY(25);
		viewLaVer.setLayoutX(72);
		viewLaVer.setLayoutY(25);

		final Button btnDownModpack = new Button("모드팩 다운로드");
		final Button btnLauch = new Button("런처실행");
		btnDownModpack.setLayoutX(5);
		btnDownModpack.setLayoutY(45);
		btnLauch.setLayoutX(5);
		btnLauch.setLayoutY(75);

		btnDownModpack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					
					
					btnDownModpack.setDisable(true);
					btnLauch.setDisable(true);
					
					Util.delDir(minecraftP);
					Util.webDown(minecraftP, "http://dl.malangbaram.net/modpack.zip", "modpack.zip");
					File zip = new File(minecraftP +"modpack.zip");

					CompressionUtil cu = new CompressionUtil();
					cu.unzip(zip, minecraftP);

					FileWriter mVersionFW = new FileWriter(minecraftP + "\\mVersion.txt");
					mVersionFW.write(lastVersion);
					BufferedWriter bw = new BufferedWriter(mVersionFW);
					bw.close();
					mVersionFW.close();
					viewLoVer.setText(lastVersion);

					zip.delete();

					AlterUtil.showInformationAlter(Lang.TITLE, "다운로드 및 설치 완료");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				} catch (Exception e) {
//					e.printStackTrace();
					AlterUtil.showErrorAlter(Lang.TITLE, "다운로드 및 설치 실패, 잠시후 다시 시도해주세요");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				}
			}
		});

		btnLauch.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				File launcher = new File(System.getenv("APPDATA") + "\\.minecraft\\launcher.jar");

				if (myVersion.equals("미설치")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "현재 로컬에 모드팩이 설치되어 있지 않습니다. 설치 후 다시 시도 해주세요.");
				} else if (myVersion.equals("확인실패")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "모드팩 버전확인이 불가능합니다. 서버접속이 불가능할 수도 있습니다");
					Util.PGAction(launcher);
				} else {
					Util.PGAction(launcher);
				}
			}
		});

		if (updatePlease) {
			AlterUtil.showInformationAlter(Lang.TITLE, "모드팩이 새로 업데이트되었습니다! 모드팩 다운로드 버튼을 눌러서 최신버전으로 설치해주세요");
		}

		gp.getChildren().addAll(loVer, viewLoVer, laVer, viewLaVer);
		gp.getChildren().addAll(btnDownModpack, btnLauch);
		frame.setScene(sc);
		frame.show();
	}
}