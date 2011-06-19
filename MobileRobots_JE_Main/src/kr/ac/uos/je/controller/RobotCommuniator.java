package kr.ac.uos.je.controller;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.exceptions.RobotControllerException;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.EObjectType.SubObject;
import kr.ac.uos.semix2.robot.Command;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketHandler;
import kr.ac.uos.semix2.robot.DataPacketIterator;
import kr.ac.uos.semix2.robot.ParameterBuilder;
import kr.ac.uos.semix2.robot.RobotClient;
import kr.ac.uos.semix2.robot.RobotClientFactory;

public class RobotCommuniator implements Runnable{
	
	private RobotCommuniator() {
		// TODO Auto-generated constructor stub
	}
	
	private String ip;
	private static RobotCommuniator INSTANCE;
	private AndroidAdaptor androidAdaptor;
	public static RobotCommuniator initRobotClient(String ip, AndroidAdaptor androidAdaptor) throws RobotControllerException{
		
		if (INSTANCE == null) INSTANCE = new RobotCommuniator();
		INSTANCE.ip = ip;
		
		INSTANCE.setAndroidAdaptor(androidAdaptor);

		return getRobotClient();
	}
	public static RobotCommuniator getRobotClient() throws RobotControllerException {
		if(INSTANCE == null){
			throw new RobotControllerException("There is no available robot");
		}
		return INSTANCE;
	}
	
	
	
	private RobotClient client;
	/**
	 * Get Map
	 */
	@Override
	public void run() {
		client = RobotClientFactory.newRobotClient();
		client.setHost(ip);
		
		if (!client.connect()) {
			androidAdaptor.sendEmptyMessage(AndroidAdaptor.CONNECTION_FAIL);
			return;
		}
		
				
//				throw new RobotControllerException("connection failed.");
//			} catch (RobotControllerException e) {
//				e.printStackTrace();
//			}
		androidAdaptor.sendEmptyMessage(AndroidAdaptor.CONNECTION_ACCEPTED);
		getMapBinaryCommand();
		getPathCommand();
		updateCommand();
		getSensor();
//		getDrawingListCommand();
//		getSensorCurrent();
	}
		
	private void getMapBinaryCommand() {
		Command getMapBinaryCommand = client.getCommand("getMapBinary"); 
		
		//get map
		client.addDataPacketHandler(getMapBinaryCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				EMapManager.MapManager.updateMap(packet);
			}
		});
		client.request(getMapBinaryCommand);
	}

		
	
	public void disconnect(){
		client.disconnect();
	}
	public void setAndroidAdaptor(AndroidAdaptor androidAdaptor) {
		this.androidAdaptor = androidAdaptor;
	}
	
	

	private void getPathCommand() {
		//path
		Command getPathCommand = client.getCommand("getPath");
		client.addDataPacketHandler(getPathCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				int numOfPoints 	= iter.nextByte2();
				float[] path = new float[numOfPoints*3];
				for (int i = 0; i < numOfPoints; i++) {
					//X
					path[i*3] = iter.nextByte4();
					//Y
					path[i*3+1] = iter.nextByte4();
					//Z
					path[i*3+2] = 0.0f;
				}
				EObjectType.PATH.setVertices(path);
			}
		});
		client.request(getPathCommand, 200);
	
	}
		
	private void updateCommand() {
		//update
		Command updateCommand = client.getCommand("update");
		client.addDataPacketHandler(updateCommand, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				DataPacketIterator iter = packet.getDataPacketIterator();
				String status 		= iter.nextString();
				String mode 		= iter.nextString();
				double voltage 		= iter.nextByte2() / 10.0;
				int x 				= iter.nextByte4();
				int y 				= iter.nextByte4();
				int th 			= iter.nextByte2();
				int velocity 		= iter.nextByte2();
				int rotateVelocity 	= iter.nextByte2();
				
				EObjectType.ROBOT_POSITION.setVertices(new float[]{x,y, th, velocity, rotateVelocity});
			}
		});
		client.request(updateCommand, 200);
		
	}

//	
	public void gotoGoal(String goal){
		Command gotoCommand = client.getCommand("gotoGoal");
		ParameterBuilder paramBuilder =  new ParameterBuilder();
		paramBuilder.appendString(goal);
		client.request(gotoCommand, paramBuilder.toParameter());
	}
	public void getSensor(){
		getSensorList();
	}
	private void getSensorList() {
		Command getSensorList = client.getCommand("getSensorList");
		client.request(getSensorList);
		client.addDataPacketHandler(getSensorList, new DataPacketHandler() {
			public void handleDataPacket(DataPacket packet) {
				List<String> sensorList = new ArrayList<String>();
				DataPacketIterator iter = packet.getDataPacketIterator();
				int numSensors = iter.nextByte2();
				for (int i = 0; i < numSensors; i++) {
					 sensorList.add(iter.nextString());
				}
				androidAdaptor.getResourceManager().setSensorList(sensorList);
				getSensorCurrent(sensorList);
			}
		});
	}
	private void getSensorCurrent(List<String> sensorList) {
		Command getSensorCurrent = client.getCommand("getSensorCurrent");
		client.addDataPacketHandler(getSensorCurrent, new DataPacketHandler() {

			@Override
			public void handleDataPacket(DataPacket packet) {
				 DataPacketIterator iter = packet.getDataPacketIterator();
				 int numOfReading = iter.nextByte2();
//				 throw RobotControllerEception( "NumOfReading" + numOfReading);
				 if(numOfReading > 0){
					 String sensorName = iter.nextString();
					 float[] coordinates = new float[numOfReading*3]; 
					 for (int i = 0; i < numOfReading; i++) {
						 coordinates[i*3] = iter.nextByte4();
						 coordinates[i*3+1] = iter.nextByte4();
						 coordinates[i*3+2] = 0.0f;
					}
					 EObjectType.SENSORS.setSubObjectVertices(sensorName, coordinates);
				 }
			}
			});
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		for(String sensor : sensorList){
			parameterBuilder.appendString(sensor);
		}
		client.request(getSensorCurrent,parameterBuilder.toParameter() , 1000);
	}
	
	
//	private void getDrawingListCommand() {
//		Command getDrawingListCommand = client.getCommand("getDrawingList");
//		client.addDataPacketHandler(getDrawingListCommand, new DataPacketHandler() {
//			
//			@Override
//			public void handleDataPacket(DataPacket packet) {
////				throw RobotControllerEception( "Get Drawing List");
////				List<DrawingItem> drawingList = new ArrayList<DrawingItem>();
//				DataPacketIterator iter = packet.getDataPacketIterator();
//				while(iter.hasNext(1)){
//					String name = iter.nextString();
//					String shape = iter.nextString();
//					int bit_primaryColor = iter.nextByte4();
//					int[] primaryColor = new int[4];
//					primaryColor[0] = ((bit_primaryColor & 0xFF000000) >> 24);
//					primaryColor[1] = ((bit_primaryColor & 0x00FF0000) >> 16);
//					primaryColor[2] = ((bit_primaryColor & 0x0000FF00) >> 8);
//					primaryColor[3] = ((bit_primaryColor & 0x000000FF));
//					
//					int size = iter.nextByte4();
//					int layer = iter.nextByte4();
//					int defaultRefreshTime  = iter.nextByte4();
//					
//					int bit_secondaryColor = iter.nextByte4();
//					int[] secondaryColor = new int[4];
//					secondaryColor[0] = ((bit_secondaryColor& 0xFF000000) >> 24);
//					secondaryColor[1] = ((bit_secondaryColor& 0x00FF0000) >> 16);
//					secondaryColor[2] = ((bit_secondaryColor & 0x0000FF00) >> 8);
//					secondaryColor[3] = ((bit_secondaryColor & 0x000000FF));
//					String visibility = iter.nextString();
//					
////					drawingList.add(new DrawingItem(name, shape, primaryColor, size, layer, defaultRefreshTime, secondaryColor, visibility));
//				}
////				EMap.Map.setDrawingList(drawingList);
//			}
//		});
//		client.request(getDrawingListCommand);
//	}
//	public void updateSensorList() {
//		removeSensorCurrent();
//		getSensorCurrent();
//	}
//	
//	
//	private void removeSensorCurrent() {
//		Command getSensorList = client.getCommand("getSensorList");
//		client.stop(getSensorList);
//		
//	}
	public void forceStop() {
		Command forceStopCommand = client.getCommand("stop");
		client.request(forceStopCommand);
		
		Command removeRatioDriveComand = client.getCommand("ratioDrive");
		client.request(removeRatioDriveComand);
	}
	
	
	
//	public void ratioDrive(ManualDriveDirection direction) {
//		throw RobotControllerEception("Drive", "Request Drive : " + direction.name());
//		
//		Command ratioDriveCommand = client.getCommand("ratioDrive");
//		if(direction != ERobotStatus.ManualDriveDirection.Stop){
//			ParameterBuilder parameterBuilder = new ParameterBuilder();
//			parameterBuilder.appendDouble(direction.getTransRatio()); 		// translationValue
//			parameterBuilder.appendDouble(direction.getRotRatio());		// rotationValue
//			parameterBuilder.appendDouble(ERobotStatus.ManualDriveDirection.getThrottleRatio());	// scalingFactor
//			Parameter parameter = parameterBuilder.toParameter();
//			client.stop(ratioDriveCommand);
//			client.request(ratioDriveCommand, parameter, 100);
//		}else{
//			client.stop(ratioDriveCommand);
//			forceStop();
//		}
//	}
		
}
