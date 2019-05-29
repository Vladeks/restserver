package com.bighi.waterpark.restserver;


import com.bighi.waterpark.restserver.data.OwnershipType;
import com.bighi.waterpark.restserver.data.ParkingLocationData;
import com.bighi.waterpark.restserver.data.VehicleType;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController("/parking")
public class ParkingServices {


//	public static final String HOST = "localhost";
//	public static final int PORT = 27017;
	public static final String HOST = "mongodb://<dbuser>:<dbpassword>@ds263146.mlab.com:63146/heroku_71zrsf4t";
	public static final int PORT = 63146;

	@GetMapping("/bicycle")
	public List<ParkingLocationData> getBicycleParkingList()
			throws UnknownHostException, ParseException {

		List<ParkingLocationData> data = getPublicBicycleParkingListFromDB();
		
		List<ParkingLocationData> data2 = getPrivateParkingDataFromDB(VehicleType.BICYCLE);
		
		data.addAll(data2);
		
		return data;

	}

	public List<ParkingLocationData> getPublicBicycleParkingListFromDB()
			throws java.net.UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		DB db = mongoClient.getDB("parking_db");

		DBCollection coll = db.getCollection("bicycle_parking");

		DBCursor cursor = coll.find();
		DBObject dbObj;

		List<ParkingLocationData> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				dbObj = cursor.next();

				String id = dbObj.get("_id").toString();
				String latitude = dbObj.get("latitude").toString();
				String longitude = dbObj.get("longitude").toString();
				String description = dbObj.get("DESCR").toString();
				String address = dbObj.get("ADDRESS").toString();
				String capacity = dbObj.get("CAPACITY").toString();
				String owner = dbObj.get("OWNED_BY").toString();
				String type = dbObj.get("TYPE").toString();

				list.add(new ParkingLocationData(id, latitude, longitude,
						description, address, capacity, owner, type,
						OwnershipType.PUBLIC));
			}
		} finally {
			cursor.close();
		}

		return list;
	}


	@GetMapping("/carbike")
	public List<ParkingLocationData> getPublicCarbikeParkingList()
			throws UnknownHostException, ParseException {

		List<ParkingLocationData> data = getPublicCarbikeParkingListFromDB();

		List<ParkingLocationData> data2 = getPrivateParkingDataFromDB(VehicleType.CARBIKE);

		data.addAll(data2);

		return data;

	}

	public List<ParkingLocationData> getPublicCarbikeParkingListFromDB()
			throws java.net.UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		DB db = mongoClient.getDB("parking_db");

		DBCollection coll = db.getCollection("city_parking_lot");

		DBCursor cursor = coll.find();
		DBObject dbObj;

		List<ParkingLocationData> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				dbObj = cursor.next();

				String id = dbObj.get("_id").toString();
				String description = dbObj.get("DESCR").toString();
				String address = dbObj.get("ADDRESS").toString();

				String latitude = dbObj.get("latitude").toString();
				String longitude = dbObj.get("longitude").toString();

				// String latitude = null;
				// String longitude = null;

				// LatLong latlong = getLatLong("338 Regina St.N");
				// LatLong latlong = getLatLong(address);

				// if(latlong != null) {
				// latitude = latlong.getLatitude(); // find from address
				// longitude = latlong.getLongitude(); // find from address
				// }

				String capacity = dbObj.get("CAPACITY").toString();
				String owner = "Waterloo";
				String type = dbObj.get("SURFACE").toString();
				String motorcycleAllowed = dbObj.get("MOTORCYCLE").toString();
				String accessibleAvailable = dbObj.get("ACCESSIBLE").toString();

				// String _id = dbObj.get("_id").toString();

				list.add(new ParkingLocationData(id, latitude, longitude,
						description, address, capacity, owner, type,
						motorcycleAllowed, accessibleAvailable,
						OwnershipType.PUBLIC));
			}
		} finally {
			cursor.close();
		}

		return list;
	}


	@PostMapping("/saveparking")
	public ResponseEntity saveNewNote(ParkingLocationData data)
			throws IOException {

		System.out.println(data);

		boolean success = saveParkingLocationData(data);

		if (success) {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to insert data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean saveParkingLocationData(ParkingLocationData data)
			throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		DB db = mongoClient.getDB("parking_db");

		DBCollection coll = db.getCollection("private_parking_data");

		DBObject dbObject = (DBObject) JSON.parse(data.toString());

		WriteResult result = coll.insert(dbObject);
		System.out.println(result);

		return true;
	}
	
	private List<ParkingLocationData> getPrivateParkingDataFromDB(VehicleType vType) throws ParseException, UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		DB db = mongoClient.getDB("parking_db");

		DBCollection coll = db.getCollection("private_parking_data");

		DBCursor cursor = coll.find();
		DBObject dbObj;

		List<ParkingLocationData> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				dbObj = cursor.next();

				String id = dbObj.get("_id").toString();
				String latitude = dbObj.get("latitude").toString();
				String longitude = dbObj.get("longitude").toString();
				String description = dbObj.get("description").toString();
				String address = dbObj.get("address").toString();
				String capacity = dbObj.get("capacity").toString();
				String owner = dbObj.get("owner").toString();
				String type = dbObj.get("type").toString();

				String motorcycleAllowed = dbObj.get("motorcycleAllowed").toString();
				String accessibleAvailable = dbObj.get("accessibleAvailable").toString();
				//String ownershipType = dbObj.get("ownershipType").toString();
				String ownerEmail = dbObj.get("ownerEmail").toString();
				String ownerPhone = dbObj.get("ownerPhone").toString();
				String amountPerDay = dbObj.get("amountPerDay").toString();
				String startDate = dbObj.get("startDate").toString();
				String endDate = dbObj.get("endDate").toString();
				
				
				
				Object isBicycleParkingObj = dbObj.get("isBicycleParking");
				boolean isBicycleParking = false;
				//System.out.println("isBicycleParkingObj: "+isBicycleParkingObj);
				
				if(isBicycleParkingObj != null) {
					//System.out.println(isBicycleParkingObj.toString());
					isBicycleParking = Boolean.valueOf(isBicycleParkingObj.toString());
				}
				
				Object isCarBikeParkingObj = dbObj.get("isCarparking");
				boolean isCarBikeParking = false;
				//System.out.println("isCarBikeParkingObj: "+isCarBikeParkingObj);
				
				
				if(isCarBikeParkingObj != null) {
					//System.out.println(isCarBikeParkingObj.toString());
					isCarBikeParking = Boolean.valueOf(isCarBikeParkingObj.toString());
				}
				
				Object occupiedObj = dbObj.get("occupied");
				boolean occupied = false;
				if(occupiedObj != null) {
					//System.out.println(isCarBikeParkingObj.toString());
					occupied = Boolean.valueOf(occupiedObj.toString());
				}

				if(occupied) {
					continue;
				}
				
				SimpleDateFormat formatter = new SimpleDateFormat(ParkingLocationData.DATE_FORMAT);
				
				ParkingLocationData parkingLocData = 
				new ParkingLocationData(id, latitude, longitude, 
						description, address, capacity, owner, type,  
						motorcycleAllowed, accessibleAvailable, 
						OwnershipType.PRIVATE, ownerEmail, ownerPhone, amountPerDay, 
						formatter.parse(startDate), formatter.parse(endDate), isBicycleParking, isCarBikeParking);
				
				if(vType == VehicleType.BICYCLE && isBicycleParking){
					
					list.add(parkingLocData);
					
				} else if(vType == VehicleType.CARBIKE && isCarBikeParking){
					
					list.add(parkingLocData);
				}
				
			}
		} finally {
			cursor.close();
		}

		return list;
	}


	@PostMapping(value = "/book", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> bookParkingLocation(String data)
			throws IOException {

		System.out.println(data);

		boolean success = bookParkingLocationOnDB(data);

		if (success) {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to insert data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean bookParkingLocationOnDB(String data) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		DB db = mongoClient.getDB("parking_db");

		System.out.println("update4");
		
		DBCollection coll = db.getCollection("private_parking_data");

		//DBObject dbObject = (DBObject) JSON.parse(data.toString());

		String id = data; //dbObject.get("id").toString();
		System.out.println(id);
		
		BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(id));
	    
	    System.out.println("update3");
	    
	    DBObject update = new BasicDBObject();
        update.put("$set", new BasicDBObject("occupied","true"));
		
        System.out.println("update2");
        
        coll.update(query, update);
        
        System.out.println("update1");
        
        mongoClient.close();
	    //DBObject dbObj = coll.findOne(query);
	    
	    
		//WriteResult result = coll.insert(dbObject);
		//System.out.println(result);

		return true;
	}
	
	/*
	 * public LatLong getLatLong(String address) {
	 * 
	 * //System.out.println("00:");
	 * 
	 * if(address != null && "".equals(address.trim())) { return null; }
	 * 
	 * //System.out.println("0:");
	 * 
	 * String lat = null; String longi = null;
	 * 
	 * //System.out.println("1:" + address);
	 * 
	 * address = normalizeAddress(address);
	 * 
	 * //System.out.println("2:" + address);
	 * 
	 * Client restClient = ClientBuilder.newClient(); //
	 * https://maps.googleapis.com/maps/api/geocode/json? //
	 * address=35+ALBERT+ST
	 * ,+Waterloo,+ON&key=AIzaSyCUWIclv4q63rUqIuonj5EzKcnboTK9A8s WebTarget
	 * target = restClient.target("https://maps.googleapis.com"); WebTarget
	 * resourceTarget = target.path("maps").path("api")
	 * .path("geocode").path("json") .queryParam("address", address)
	 * .queryParam("key", "AIzaSyCUWIclv4q63rUqIuonj5EzKcnboTK9A8s"); String
	 * responseString =
	 * resourceTarget.request("application/json").get(String.class);
	 * //System.out.println("Here is the response from maps.googleapis.com: " +
	 * responseString);
	 * 
	 * GsonBuilder builder = new GsonBuilder(); Gson gson = builder.create();
	 * 
	 * JsonObject obj = gson.fromJson(responseString, JsonObject.class);
	 * JsonArray elem = obj.get("results").getAsJsonArray(); JsonElement loc =
	 * elem
	 * .get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location"
	 * );
	 * 
	 * JsonObject objLoc = loc.getAsJsonObject();
	 * //System.out.println(objLoc.get("lat"));
	 * //System.out.println(objLoc.get("lng"));
	 * 
	 * lat = objLoc.get("lat").getAsString(); longi =
	 * objLoc.get("lng").getAsString();
	 * 
	 * LatLong ll = new LatLong(lat, longi); //System.out.println("3:"); return
	 * ll; }
	 * 
	 * private String normalizeAddress(String address) { StringBuilder builder =
	 * new StringBuilder(address.replaceAll(" ", "+"));
	 * builder.append(",Waterloo,ON,Canada"); return builder.toString(); }
	 */
}
