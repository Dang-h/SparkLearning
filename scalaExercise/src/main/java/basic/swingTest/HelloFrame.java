package basic.swingTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloFrame extends JFrame {
	public static void main(String[] args) {
		new HelloFrame();
	}

	public HelloFrame() throws HeadlessException {
		super("My first Swing App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);
		// 中央显示窗口
		setLocationRelativeTo(null);
		setVisible(true);

		JLabel nameLabel = new JLabel();
		nameLabel.setText("JLabel");
		nameLabel.setBounds(160, 20, 150, 50);
		add(nameLabel);

		JTextField nameTextField = new JTextField();
		nameTextField.setBounds(160,20,100,30);
		add(nameTextField);

		JButton acceptButton = new JButton("Accept");
		acceptButton.setBounds(100, 70, 100, 30);
		add(acceptButton);
		acceptButton.addActionListener(e -> System.out.println("The button is clicked"));

		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String helloText = "Hello";
				String name = nameTextField.getText();
				if (name != null && name.trim().length() >0) {
					helloText += String.format(", %s", name);
				}
				nameLabel.setText(helloText);
			}
		});

		acceptButton.addActionListener(e ->{
			String testText = "Test lambda";
			String name = nameTextField.getText();
			if (name != null && name.trim().length() > 0) {
				testText += String.format(", %s", name);
			}
			nameLabel.setText(testText);
		});

		JPanel greenPanel = new JPanel();
		greenPanel.setBounds(40, 150, 220,70);
		greenPanel.setLayout(new BorderLayout());
		greenPanel.setBackground(Color.PINK);
		add(greenPanel);

		JLabel testLabel = new JLabel("Test");
		testLabel.setBounds(50,20,100,30);
		testLabel.setHorizontalAlignment(SwingConstants.CENTER);
		testLabel.setVerticalAlignment(SwingConstants.CENTER);

		greenPanel.add(testLabel);
	}
}
