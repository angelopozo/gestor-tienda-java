package dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	@Override
	public void connect() {
		File objectsDirectory = new File("objects");
		if (!objectsDirectory.exists()) {
			objectsDirectory.mkdirs();
		}

		entityManagerFactory = Persistence.createEntityManagerFactory("objectdb:objects/users.odb");
		entityManager = entityManagerFactory.createEntityManager();
		ensureDefaultEmployee();
	}

	@Override
	public void disconnect() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		TypedQuery<Employee> query = entityManager.createQuery(
				"SELECT e FROM Employee e WHERE e.employeeId = :employeeId AND e.password = :password",
				Employee.class);
		query.setParameter("employeeId", employeeId);
		query.setParameter("password", password);

		List<Employee> employees = query.getResultList();
		if (employees.isEmpty()) {
			return null;
		}

		return employees.get(0);
	}

	private void ensureDefaultEmployee() {
		TypedQuery<Employee> query = entityManager.createQuery(
				"SELECT e FROM Employee e WHERE e.employeeId = :employeeId",
				Employee.class);
		query.setParameter("employeeId", 123);

		if (!query.getResultList().isEmpty()) {
			return;
		}

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(new Employee(123, "test", "test"));
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
	}

	@Override
	public ArrayList<Product> getInventory() {
		return new ArrayList<Product>();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		return false;
	}

	@Override
	public void addProduct(Product product) {
	}

	@Override
	public void updateProduct(Product product) {
	}

	@Override
	public void deleteProduct(int productId) {
	}
}
