package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Employee;
import model.Product;
import model.Amount;

import java.util.ArrayList;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, employeeId);
			ps.setString(2, password);
			// System.out.println(ps.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				}
			}
		} catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public boolean writeInventory(ArrayList<model.Product> inventory) {
		String query = "insert into historical_inventory (id_product, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			for (model.Product product : inventory) {
				ps.setInt(1, product.getId());
				ps.setString(2, product.getName());
				ps.setDouble(3, product.getWholesalerPrice().getValue());
				ps.setBoolean(4, product.isAvailable());
				ps.setInt(5, product.getStock());

				ps.executeUpdate();
			}

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public ArrayList<model.Product> getInventory() {
		ArrayList<model.Product> inventory = new ArrayList<>();
		String query = "select * from inventory";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Product product = new Product(rs.getInt("id"), rs.getString("name"),
						new Amount(rs.getInt("wholesalerPrice")), rs.getBoolean("available"), rs.getInt("stock"));
				inventory.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inventory;
	}

	@Override
	public void addProduct(Product product) {
		String query = "insert into inventory (id, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, product.getId());
			ps.setString(2, product.getName());
			ps.setDouble(3, product.getWholesalerPrice().getValue());
			ps.setBoolean(4, product.isAvailable());
			ps.setInt(5, product.getStock());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateProduct(Product product) {
		String query = "update inventory set stock = ? where id = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, product.getStock());
			ps.setInt(2, product.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProduct(int productId) {
		String query = "delete from inventory where id = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, productId);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
