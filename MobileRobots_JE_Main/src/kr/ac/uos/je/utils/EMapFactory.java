package kr.ac.uos.je.utils;

import java.util.List;

import kr.ac.uos.je.model.interfaces.MapManager;
import kr.ac.uos.je.model.interfaces.MapManager.MapStatus;
import kr.ac.uos.semix2.robot.DataPacket;
import kr.ac.uos.semix2.robot.DataPacketIterator;

public enum EMapFactory {
	MapFactory;
	
	public final String kMapBinaryPrefixMinPos	= "MinPos:";
	public final String kMapBinaryPrefixMaxPos	= "MaxPos:";
	public final String kMapBinaryPrefixResolution = "Resolution:";
	public final String kMapBinaryPrefixCairn	= "Cairn:";
	public final String kMapBinaryLines		= "LINES";
	public final String kMapBinaryData = "DATA";
	
	
	
	private boolean _isNextPacketLines = false;
	private boolean _isNextPacketData = false;
	public void updateMap(MapManager mapManager, DataPacket packet ) {
		DataPacketIterator iter = packet.getDataPacketIterator();
		if (_isNextPacketLines) {
			int count = iter.nextByte4();
				if (count > 0) {
					parseLine(mapManager, iter, count);
				} else {
					// LINES 정보 수신 완료 
					_isNextPacketLines = false;
				}
		} else if (_isNextPacketData) {
			int count = iter.nextByte4();
			if (count > 0) {
				parsePoint(mapManager, iter, count);
			} else {
				// DATA 정보 수신 완료 
				_isNextPacketData = false;
			}
		} else {
			if (iter.hasNext(1)) {
				String sentence = iter.nextString();
				if (sentence.equals(kMapBinaryLines)) {				// 다음 DataPacket은 LINES 정보를 담고 있다.
					_isNextPacketLines = true;
				} else if (sentence.equals(kMapBinaryData)){			// 다음 DataPacket은 DATA 정보를 담고 있다.
					_isNextPacketData = true;
				} else if (sentence.startsWith(kMapBinaryPrefixMinPos)) {		// Minimum Position 정보 해석
					parseMinPos(mapManager,sentence);
				} else if (sentence.startsWith(kMapBinaryPrefixMaxPos)) {		// Maximum Position 정보 해석
					parseMaxPos(mapManager,sentence);
				} else if (sentence.startsWith(kMapBinaryPrefixResolution)) {	// Resolution 정보 해석
					parseResolution(mapManager,sentence);
				} else if (sentence.startsWith(kMapBinaryPrefixCairn)) {		// MapObject 정보를 해석
					parseMapObject(mapManager,sentence);
				} else {
					System.out.println(sentence);
				}
			} else {
				// 모든 MapBinary 정보 수신 완료
				mapManager.setMapStatus(MapStatus.LoadingComplete);
			}
		}
	}

//	
//	public void checkAndInitMap() {
//		mMap.init();
//	}

	
	public void parseLine(MapManager mapManager, DataPacketIterator iter, int count) {
		for (int i=0; i<count; i++) {
			int x1 = iter.nextByte4();
			int y1 = iter.nextByte4();
			int x2 = iter.nextByte4();
			int y2 = iter.nextByte4();
			mapManager.addMapLine(x1, y1, 0, x2, y2, 0);
		}
	}
	
	public void parsePoint(MapManager mapManager, DataPacketIterator iter, int count) {
		for (int i=0; i<count; i++) {
			int x = iter.nextByte4();
			int y = iter.nextByte4();
			mapManager.addMapPoint(x, y, 0);
		}
	}

	
	public void parseMinPos(MapManager mapManager, String sentence) {
		String[] separetedSentence = sentence.split(" ");
		int x = Integer.valueOf(separetedSentence[1]);
		int y = Integer.valueOf(separetedSentence[2]);
		mapManager.setMinPos(x,y);
	}

	
	public void parseMaxPos(MapManager mapManager, String sentence) {
		String[] separetedSentence = sentence.split(" ");
		int x = Integer.valueOf(separetedSentence[1]);
		int y = Integer.valueOf(separetedSentence[2]);
		mapManager.setMinPos(x,y);
	}

	
	public void parseResolution(MapManager mapManager, String sentence) {
		String[] separetedSentence = sentence.split(" ");
		int resolution = Integer.parseInt(separetedSentence[1]);
		mapManager.setResolution(resolution);
	}

	
	public void parseMapObject(MapManager mapManager, String sentence) {
		MapObjectFactory.parseObjectWithSentece(mapManager, sentence);
	}
	
	private static class MapObjectFactory{
		public static final String kMapBinaryRobotHome			=	"RobotHome";
		public static final String kMapBinaryGoal				=	"Goal";
		public static final String kMapBinaryGoalWithHeading	=	"GoalWithHeading";
		public static final String kMapBinaryForbiddenLine		=	"ForbiddenLine";
		public static final String kMapBinaryForbiddenArea		=	"ForbiddenArea";
		public static void parseObjectWithSentece(MapManager mapManager, String sentence){
			String[] aSentence = sentence.split(" ");
			
			String type			= aSentence[1];
			int x 				= Integer.valueOf(aSentence[2]);
			int y				= Integer.valueOf(aSentence[3]);
			double theta		= Double.valueOf(aSentence[4]);

			
			// description은 따옴표로 감싸서 오는데, 위의 스페이스로 분리하는 규칙에 의해 더 작게 나뉠 가능성이 있다.
			// 따라서 끝따옴표를 검사해서 필요한 경우 string을 합쳐야 한다.
			// 처리 후에는 따옴표를 제거한다.
			
			int index = 5;
			String description = aSentence[index++];
			while (!description.endsWith("\"")) {
				description = String.format("%s%s", description, aSentence[index++]);
			}
			if(!description.equals("") && description.length() > 2) description = description.substring(1, description.length() - 1);
			
			String iconName = aSentence[index++];
			
			// name 역시 description과 마찬가지로 따옴표로 감싸서 온다.
			
			String name = aSentence[index++];
			while (!name.endsWith("\"")) {
				name = String.format("%s %s", name, aSentence[index++]);
			}
			if(!name.equals("") && name.length() > 2) name = name.substring(1, name.length() - 1);

			// MapObject를 생성한다.
			
			if (type.equals(kMapBinaryRobotHome)){
				mapManager.setRobotHome(x, y, description, iconName,  name); 
			} else if (type.equals(kMapBinaryGoal)) {
				mapManager.addGoal(x,y,  description, iconName,  name, false);  
			} else if (type.equals(kMapBinaryGoalWithHeading)) {
				mapManager.addGoal(x,y,  description, iconName,  name, true);
			} else if (type.equals(kMapBinaryForbiddenLine)) {
				Integer x1 = Integer.valueOf(aSentence[index++]);
				Integer y1 = Integer.valueOf(aSentence[index++]);
				Integer x2 = Integer.valueOf(aSentence[index++]);
				Integer y2 = Integer.valueOf(aSentence[index++]);
				mapManager.addForbiddenLine(x1,y1,x2,y2,description, iconName ,name);
			} else if (type.equals(kMapBinaryForbiddenArea)) {
				Integer x1 = Integer.valueOf(aSentence[index++]);
				Integer y1 = Integer.valueOf(aSentence[index++]);
				Integer x2 = Integer.valueOf(aSentence[index++]);
				Integer y2 = Integer.valueOf(aSentence[index++]);
				mapManager.addForbiddenArea(x1,y1,x2,y2,description, iconName ,name);
			} else {
				System.out.println("RCMapObjectFactory doesn't support type: " + type + "(name: " + name+ ")");
			}
			
		}
	}
	
	public static float[] integerListToFloatArray(List<Integer> list){
		float[] floatArray = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			floatArray[i] = list.get(i);
		}
		return floatArray; 
	}
}
