package dao;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;

import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
	
	MongoCollection<Document> collection;
	ObjectId id;
	
	MongoClient mongoClient;
	MongoDatabase mongoDatabase;
	
	@Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		mongoClient = new MongoClient(mongoClientURI);

		mongoDatabase = mongoClient.getDatabase("shop");
	}

	@Override
	public void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		collection = mongoDatabase.getCollection("employee");

		Document document = collection.find(eq("employeeId", employeeId)).first();
		
		if (document != null && document.getString("password").equals(password)) {
			return new Employee(document.getInteger("employeeId"), document.getString("name"),
					document.getString("password"));
		}
		
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
	    ArrayList<Product> inventory = new ArrayList<>();
	    collection = mongoDatabase.getCollection("inventory");

	    Iterable<Document> documents = collection.find();

	    for (Document doc : documents) {
	        Document wp = doc.get("wholesalerPrice", Document.class);

	        double value = 0.0;
	        if (wp != null && wp.getDouble("value") != null) {
	            value = wp.getDouble("value");
	        }

	        Product p = new Product(
	            doc.getString("name"),
	            value,
	            doc.getBoolean("available"),
	            doc.getInteger("stock")
	        );

	        Integer dbId = doc.getInteger("id");
	        if (dbId != null) {
	            p.setId(dbId);
	        }

	        inventory.add(p);
	    }


	    return inventory;
	}


	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		boolean success = false;
		
		collection = mongoDatabase.getCollection("historical_inventory");
		
		for (Product product : inventory) {
			ObjectId objectId = new ObjectId();

			Document wholesalerPrice = new Document("value", product.getWholesalerPrice())
			        .append("currency", "€");

			Document document = new Document("_id", objectId)
			        .append("id", product.getId())
			        .append("created_at", new java.util.Date())
			        .append("available", product.isAvailable())
			        .append("name", product.getName())
			        .append("wholesalerPrice", wholesalerPrice)
			        .append("stock", product.getStock());

			
			collection.insertOne(document);
			success = true;
		}
		
		return success;
	}

	@Override
	public void addProduct(Product product) {
		collection = mongoDatabase.getCollection("inventory");
		
		Document wholesalerPrice = new Document("value", product.getWholesalerPrice())
		        .append("currency", "€");
		
		Document last = collection.find().sort(new Document("id", -1)).first();
		int nextId = (last != null && last.getInteger("id") != null) ? last.getInteger("id") + 1 : 1;
		
		Document document = new Document("_id", new ObjectId())
				.append("id", nextId)
		        .append("available", product.isAvailable())
		        .append("name", product.getName())
		        .append("wholesalerPrice", wholesalerPrice)
		        .append("stock", product.getStock());

		collection.insertOne(document);
	}

	@Override
	public void updateProduct(Product product) {
		collection = mongoDatabase.getCollection("inventory");
		collection.updateOne(eq("id", product.getId()),
				combine(set("stock", product.getStock())));
	}

	@Override
	public void deleteProduct(int productId) {
		collection = mongoDatabase.getCollection("inventory");
		collection.deleteMany(eq("id", productId));
	}

}
