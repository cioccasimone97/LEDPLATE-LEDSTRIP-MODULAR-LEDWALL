package FileChooser;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FileChoose {

	private JFrame frame;
	private JTextField textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileChoose window = new FileChoose();
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
	public FileChoose() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// BOTTONE di apertura file
		JButton btnApriFile = new JButton("APRI FILE");
		btnApriFile.setBounds(163, 139, 89, 23);
		frame.getContentPane().add(btnApriFile);
		btnApriFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.home") + "/Documents");

				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnValue = jfc.showOpenDialog(null); // per scegliere se SALVA o APRI
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					String line = f.getAbsolutePath();
					textArea.setText(line);
				}
			}
		});

		textArea = new JTextField();
		textArea.setEditable(false);
		textArea.setBounds(54, 55, 319, 40);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);

	}
}
