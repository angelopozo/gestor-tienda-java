package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {
	private Session session;
	private Transaction tx;

	@Override
	public void connect() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		org.hibernate.SessionFactory sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();
	}

	@Override
	public ArrayList<Product> getInventory() {
		try {
			tx = session.beginTransaction();
			ArrayList<Product> products = (ArrayList<Product>) session.createQuery("FROM Product", Product.class)
					.list();
			tx.commit();
			return products;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			return new ArrayList<Product>();
		}
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		try {
			tx = session.beginTransaction();
			
			for (Product product : products) {
				
				ProductHistory productHistory = new ProductHistory(product.getId(), product.getName(), product.getWholesalerPrice(),
						product.isAvailable(), product.getStock());
				
				session.save(productHistory);
			}
			
			tx.commit();
			return true;			
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			return false;
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	public void addProduct(Product product) {
		try {
			tx = session.beginTransaction();
			session.save(product);

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}

	}

	public void updateProduct(Product product) {
		try {
			tx = session.beginTransaction();
			session.update(product);

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	public void deleteProduct(int productId) {
		try {
			tx = session.beginTransaction();
			Product product = session.get(Product.class, productId);			
			session.delete(product);
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	public void disconnect() {
		session.close();
	}

}
