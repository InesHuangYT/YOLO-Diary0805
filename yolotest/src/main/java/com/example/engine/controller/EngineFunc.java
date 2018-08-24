package com.example.engine.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.engine.entity.ModelMerge;
import com.example.engine.entity.ModelSwitch;
import com.example.engine.entity.RetrieveFace;
import com.example.engine.entity.TrainFace;
import com.example.engine.util.AttributeCheck;
import com.example.engine.util.CmdUtil;
import com.example.engine.util.TxtUtil;

/**
 * EngineControl Func
 * 
 * @author 雿�� Daniel
 * @date 2018撟�8���10� 銝��10:54:20
 * @version
 * @description:
 */

@Service
public class EngineFunc {

	static protected String ENGINEPATH = "C:\\eGroupAI_FaceRecognitionEngine_V3.0";

	// C:\\Users\\Administrator\\Desktop\\Engine0818 --> rrou's path
	public void trainEngine() {
		// TrainFace
		TrainFace trainFace = new TrainFace();
		trainFace.setModelExist(false);
		trainFace.setTrainListPath("list.txt");
		trainFace.setModelPath("eGroup\\trainTest0825.Model");
		trainFace(trainFace);
	}

	public void retrieveEngine() {
		// RetrieveFace
		RetrieveFace retrieveFace = new RetrieveFace();
		retrieveFace.setThreshold(0.7);
		retrieveFace.setHideMainWindow(false);
		retrieveFace.setResolution("720p");
		retrieveFace.setOutputFacePath("outputFace");
		retrieveFace.setOutputFramePath("outputFrame");
		retrieveFace.setCam("0");
		retrieveFace.setMinimumFaceSize(100);
		retrieveFace.setThreshold(0.7);
		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);

	}

	public void modelmergeEngine() {

		// ModelMerge
		ModelMerge modelMerge = new ModelMerge();
		modelMerge.setListPath("ModelList.egroup.List");
		modelMerge.setTrainedBinaryPath("eGroup\\eGroup_merged.binary");
		modelMerge.setTrainedFaceInfoPath("eGroup\\eGroup_merged.faceInfor");
		modelMerge(modelMerge);

	}

	public void modelswitchEngine() {
		// ModelSwitch
		ModelSwitch modelSwitch = new ModelSwitch();
		modelSwitch.setNewModelBinaryPath(ENGINEPATH + "/eGroup5/eGroup.Model.binary");
		modelSwitch.setNewModelFaceInfoPath(ENGINEPATH + "/eGroup5/eGroup.Model.faceInfor");
		modelSwitch.setSwitchFilePath(ENGINEPATH + "/Singal_For_Model_Switch.txt");
		modelSwitch(modelSwitch);

	}

	private static boolean trainFace(TrainFace trainFace) {
		boolean flag = false;
		// init func
		trainFace.generateCli();
		if (trainFace.getCommandList() != null) {
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(trainFace.getCommandList());
		}
		return flag;
	}

	private static boolean retrieveFace(RetrieveFace retrieveFace) {
		boolean flag = false;
		// init func
		retrieveFace.generateCli();
		if (retrieveFace.getCommandList() != null) {
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(retrieveFace.getCommandList());
		}
		return flag;
	}

	private static boolean modelMerge(ModelMerge modelMerge) {
		boolean flag = false;
		// init func
		modelMerge.generateCli();
		if (modelMerge.getCommandList() != null) {
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(modelMerge.getCommandList());
		}
		return flag;
	}

	private static void modelSwitch(ModelSwitch modelSwitch) {
		// init func
		final AttributeCheck attributeCheck = new AttributeCheck();

		// init variable
		final File newModelBinary = new File(modelSwitch.getNewModelBinaryPath());
		final File newModelFaceInfo = new File(modelSwitch.getNewModelFaceInfoPath());

		// Check Model Files
		if (newModelBinary.exists() && newModelFaceInfo.exists()
				&& attributeCheck.stringsNotNull(modelSwitch.getSwitchFilePath())) {
			// Model
			final List<String> dataList = new ArrayList<>();
			dataList.add(modelSwitch.getNewModelBinaryPath());
			dataList.add(modelSwitch.getNewModelFaceInfoPath());

			// init func
			final TxtUtil txtUtil = new TxtUtil();
			txtUtil.create(modelSwitch.getSwitchFilePath(), dataList);
		}
	}

}
