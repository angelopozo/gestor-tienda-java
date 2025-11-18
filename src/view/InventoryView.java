package view;
 
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
 
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
 
import main.Shop;
import model.Product;
 
public class InventoryView extends JDialog {
 
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableInventory;
    private DefaultTableModel tableModel;
 
    /**
     * Create the dialog.
     */
    public InventoryView(Shop shop) {
        setTitle("Inventario de la Tienda");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
 
        String[] columnNames = {"Producto", "Precio Entero", "Stock"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
 
        tableInventory = new JTable(tableModel);
        tableInventory.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tableInventory.setRowHeight(22);
        tableInventory.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 15));
 
        ArrayList<Product> inventory = shop.getInventory();
        if (!inventory.isEmpty()) {
            for (Product product : inventory) {
                Object[] rowData = {
                    product.getName(),
                    product.getPublicPrice().toString(),
                    product.getStock()
                };
                tableModel.addRow(rowData);
            }
        }
        JScrollPane scrollPane = new JScrollPane(tableInventory);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
}