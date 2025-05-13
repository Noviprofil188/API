package Proba;

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Stavka_kosarice {

	private JFrame frame;
	private JComboBox<String> comboBox;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Stavka_kosarice window = new Stavka_kosarice();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Stavka_kosarice() {
		initialize();
		loadComboBoxData();  // Učitavanje podataka iz baze u JComboBox
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Stavka košarice");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		// Padajući izbornik
		comboBox = new JComboBox<>();
		frame.getContentPane().add(comboBox, BorderLayout.NORTH);

		// Tekstualno područje
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Reakcija na odabir iz padajućeg izbornika
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = (String) comboBox.getSelectedItem();
				textArea.setText("Odabrano: " + selected);
			}
		});
	}

	/**
	 * Povezivanje s MySQL bazom i punjenje JComboBox-a
	 */
	private void loadComboBoxData() {
		String url = "jdbc:mysql://ucka.veleri.hr/ppokos"; // zamijeni s imenom tvoje baze
		String username = "ppokos"; // npr. root
		String password = "11";        // npr. 1234

		try {
			// Učitaj MySQL driver (ako već nije)
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Spoji se na bazu
			Connection conn = DriverManager.getConnection(url, username, password);

			// Izvrši upit za tablicu 'biljka' (zamijeni stupac ako trebaš)
			String query = "SELECT nazivBiljke FROM Biljka";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			// Dodaj podatke u comboBox
			while (rs.next()) {
				comboBox.addItem(rs.getString("naziv"));
			}

			// Zatvori sve
			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			textArea.setText("Greška pri dohvaćanju podataka iz baze.");
		}
	}
}
