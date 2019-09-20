package cn.hl.ox.huffmanzip;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Face extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CardLayout card;
	JPanel panlCard;
	JFileChooser jFileChooser1 = new JFileChooser("C:\\Documents and Settings\\Administrator\\桌面");
	JLabel jLabel1 = new JLabel();
	JTextField jTextField1 = new JTextField();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JTextField jTextField2 = new JTextField();
	JLabel jLabel4 = new JLabel();
	TitledBorder titledBorder1 = new TitledBorder("");
	JButton jButton1 = new JButton();
	JButton jButton7 = new JButton();
	JButton jButton3 = new JButton();
	JButton jButton4 = new JButton();

	JFileChooser jFileChooser2 = new JFileChooser("C:\\Documents and Settings\\Administrator\\桌面");
	JLabel jLabel5 = new JLabel();
	JButton jbutton1 = new JButton();
	JTextField jTextField3 = new JTextField();
	JLabel jLabel6 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JTextField jTextField4 = new JTextField();
	JLabel jLabel8 = new JLabel();
	TitledBorder titledBorder2 = new TitledBorder("");
	JButton jbutton7 = new JButton();
	JButton jbutton3 = new JButton();
	JButton jbutton4 = new JButton();

	public Face() {
		setTitle("压缩解压器");
		setBounds(150, 100, 415, 395);
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Font font1 = new Font("隶书", Font.PLAIN, 30);
		Font font2 = new Font("隶书", Font.PLAIN, 40);
		panlCard = new JPanel();
		card = new CardLayout();
		panlCard.setLayout(card);
		JPanel panlWel = new JPanel();
		panlWel.setBackground(Color.CYAN);
		panlWel.setLayout(null);
		JPanel panlCom = new JPanel();
		panlCom.setLayout(null);
		JPanel panlDec = new JPanel();
		panlDec.setLayout(null);
		panlCard.add("welcome", panlWel);
		panlCard.add("compress", panlCom);
		panlCard.add("decompress", panlDec);
		panlCard.setBounds(0, 30, 410, 395);
		JToolBar toolBar = new JToolBar();
		JButton b0 = new JButton("欢迎");
		b0.setToolTipText("欢迎使用");
		b0.setBackground(Color.GREEN);
		JButton b1 = new JButton("压缩");
		b1.setToolTipText("压缩文件");
		b1.setBackground(Color.GREEN);
		JButton b2 = new JButton("解压缩");
		b2.setToolTipText("解压缩文件");
		b2.setBackground(Color.GREEN);
		toolBar.add(b0);
		toolBar.add(b1);
		toolBar.add(b2);
		toolBar.setBounds(0, 0, 420, 30);
		JLabel label1 = new JLabel("欢迎使用");
		label1.setFont(new Font("隶书", Font.PLAIN, 35));
		label1.setBounds(120, 77, 150, 53);
		panlWel.add(label1);
		JLabel label2 = new JLabel("压缩解压缩器");
		label2.setFont(font2);
		label2.setBounds(68, 136, 300, 40);
		panlWel.add(label2);
		JLabel label3 = new JLabel("Ver 1.0");
		label3.setFont(font1);
		label3.setBounds(120, 220, 180, 30);
		panlWel.add(label3);
		// Icon logo1 = new ImageIcon("images\\1.jpg");
		JLabel lblLogo = new JLabel();
		lblLogo.setToolTipText("");
		lblLogo.setBounds(0, 0, 400, 365);
		panlWel.add(lblLogo);
		add(toolBar);
		add(panlCard);
		b0.addActionListener(this);
		b1.addActionListener(this);
		b2.addActionListener(this);

		jLabel1.setFont(new java.awt.Font("隶书", Font.BOLD, 25));
		jLabel1.setForeground(Color.BLACK);
		jLabel1.setText("文本压缩");
		jLabel1.setBounds(new Rectangle(151, 10, 150, 29));
		jButton1.setBounds(new Rectangle(277, 89, 84, 30));
		jButton1.setBorder(BorderFactory.createRaisedBevelBorder());
		jButton1.setText("浏览");
		jButton1.addActionListener(this);
		jTextField1.setBounds(new Rectangle(30, 90, 222, 30));
		jLabel2.setText("要压缩的文本");
		jLabel2.setBounds(new Rectangle(30, 63, 145, 29));
		jLabel3.setText("压缩到：");
		jLabel3.setBounds(new Rectangle(30, 130, 145, 24));
		jTextField2.setBounds(new Rectangle(30, 159, 220, 30));
		jLabel4.setBorder(titledBorder1);
		jLabel4.setBounds(new Rectangle(15, 45, 370, 195));
		jButton7.setBounds(new Rectangle(277, 159, 84, 30));
		jButton7.setText("浏览");
		jButton7.addActionListener(this);
		jButton3.setBounds(new Rectangle(30, 203, 84, 29));
		jButton3.setText("压缩");
		jButton3.addActionListener(this);
		jButton4.setBounds(new Rectangle(166, 203, 84, 29));
		jButton4.setText("返回");
		jButton4.addActionListener(this);
		panlCom.setBackground(Color.CYAN);
		panlCom.add(jButton7);
		panlCom.add(jLabel1);
		panlCom.add(jButton4);
		panlCom.add(jLabel4);
		panlCom.add(jButton3);
		panlCom.add(jLabel2);
		panlCom.add(jTextField1);
		panlCom.add(jLabel3);
		panlCom.add(jTextField2);
		panlCom.add(jButton1);
		panlCom.add(jFileChooser1);

		jLabel5.setFont(new java.awt.Font("隶书", Font.BOLD, 25));
		jLabel5.setForeground(Color.BLACK);
		jLabel5.setText("文件解压");
		jLabel5.setBounds(new Rectangle(150, 16, 150, 29));
		jbutton1.setBounds(new Rectangle(277, 89, 84, 30));
		jbutton1.setBorder(BorderFactory.createRaisedBevelBorder());
		jbutton1.setText("浏览");
		jbutton1.addActionListener(this);
		jTextField3.setBounds(new Rectangle(30, 90, 222, 29));
		jLabel6.setText("要解压的文本");
		jLabel6.setBounds(new Rectangle(30, 63, 145, 29));
		jLabel7.setText("解压到：");
		jLabel7.setBounds(new Rectangle(30, 130, 145, 24));
		jTextField4.setBounds(new Rectangle(30, 159, 220, 30));
		jLabel8.setBorder(titledBorder1);
		jLabel8.setBounds(new Rectangle(15, 45, 370, 195));
		jbutton7.setBounds(new Rectangle(277, 159, 84, 30));
		jbutton7.setText("浏览");
		jbutton7.addActionListener(this);
		jbutton3.setBounds(new Rectangle(30, 203, 84, 29));
		jbutton3.setText("解压");
		jbutton3.addActionListener(this);
		jbutton4.setBounds(new Rectangle(165, 203, 84, 29));
		jbutton4.setText("返回");
		jbutton4.addActionListener(this);
		panlDec.setBackground(Color.CYAN);
		panlDec.add(jbutton7);
		panlDec.add(jLabel5);
		panlDec.add(jbutton4);
		panlDec.add(jLabel8);
		panlDec.add(jbutton3);
		panlDec.add(jLabel6);
		panlDec.add(jTextField3);
		panlDec.add(jLabel7);
		panlDec.add(jTextField4);
		panlDec.add(jbutton1);
		panlDec.add(jFileChooser2);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("欢迎")) {
			card.show(panlCard, "welcome");

		}
		if (e.getActionCommand().equals("压缩")) {
			card.show(panlCard, "compress");

		}
		if (e.getActionCommand().equals("解压缩")) {
			card.show(panlCard, "decompress");

		}
		if (e.getSource() == jButton1) { // 压缩中的浏览键
			jFileChooser1.removeChoosableFileFilter(jFileChooser1.getAcceptAllFileFilter()); // 移去所有文件过滤器
			jFileChooser1.addChoosableFileFilter(new MyFileFilter("txt", "文本文件")); // 增加文件过滤器接收txt文档
																					// //
																					// 接爱txt文件
			int option = jFileChooser1.showOpenDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = jFileChooser1.getSelectedFile();
				jTextField1.setText(file.getPath());
				jTextField2.setText(file.getPath().substring(0, file.getPath().lastIndexOf(".") + 1) + "hjc");
			}
		}
		if (e.getSource() == jButton3) {// 压缩中的压缩键
			if (jTextField1.getText() == null) {
				JOptionPane.showMessageDialog(this, "请选择要压缩的文件");
			} else if (jTextField2.getText() == null) {
				JOptionPane.showMessageDialog(this, "请选择要保存的位置");
			} else {
				Compress file = new Compress(jTextField1.getText(), jTextField2.getText());
				// String name = jTextField1.getText();
				System.out.println("压缩文件路径为: " + jTextField1.getText());
				System.out.println("解压缩文件路径为: " + jTextField2.getText());

				File f = new File(jTextField1.getText());
				if (f.length() != 0) {
					jTextField1.setText("");
					jTextField2.setText("");
					Thread t = new Thread(file);
					t.start();
					// file.run();
					// file.compressRate();
					// this.setVisible(true);
				} else
					JOptionPane.showMessageDialog(null, "文件为空");
			}
		}
		if (e.getSource() == jButton4) {// 压缩中的返回键
			jTextField1.setText("");
			jTextField2.setText("");
			card.show(panlCard, "welcome");
		}
		if (e.getSource() == jButton7) {// 浏览解压缩文件目录
			JFileChooser jFileChooser2 = new JFileChooser("C:\\Documents and Settings\\Administrator\\桌面");
			jFileChooser2.removeChoosableFileFilter(jFileChooser1.getAcceptAllFileFilter()); // 移去所有文件过滤器
			jFileChooser2.addChoosableFileFilter(new MyFileFilter("hjc", "压缩文件")); // 增加文件过滤器
			int option = jFileChooser2.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {// 如果选择了保存路径则按路径保存，否则按默认
				String path1 = jFileChooser2.getSelectedFile().getPath();
				jTextField2.setText(path1);
			}
		}

		if (e.getSource() == jbutton1) {// 解压缩中的浏览键
			jFileChooser2.removeChoosableFileFilter(jFileChooser2.getAcceptAllFileFilter()); // 移去所有文件过滤器
			jFileChooser2.addChoosableFileFilter(new MyFileFilter("hjc", "压缩文件"));
			int option = jFileChooser2.showOpenDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {
				File file = jFileChooser2.getSelectedFile();
				if (file != null) {
					jTextField3.setText(file.getPath());
					jTextField4.setText(file.getPath().substring(0, file.getPath().lastIndexOf(".") + 1) + "txt");
				}

			}
		}
		if (e.getSource() == jbutton3) {// 解压缩中的解压键
			if (jTextField3.getText() == null) {
				JOptionPane.showMessageDialog(null, "请选择要解压的文件");
			} else {
				Decompress file = new Decompress(jTextField3.getText(), jTextField4.getText());
				File f = new File(jTextField3.getText());
				if (f.length() != 0) {
					jTextField3.setText("");
					jTextField4.setText("");
					file.run();
					JOptionPane.showMessageDialog(null, "解压完成");
				} else
					JOptionPane.showMessageDialog(null, "文件为空");
			}
		}
		if (e.getSource() == jbutton4)// 返回键
		{
			jTextField3.setText("");
			jTextField4.setText("");
			card.show(panlCard, "welcome");
		}
		if (e.getSource() == jbutton7) {// 浏览键保存解压后的文件
			String path1;
			JFileChooser jFileChooser3 = new JFileChooser("C:\\Documents and Settings\\Administrator\\桌面");
			jFileChooser3.removeChoosableFileFilter(jFileChooser2.getAcceptAllFileFilter()); // 移去所有文件过滤器
			jFileChooser3.addChoosableFileFilter(new MyFileFilter("txt", "文本文件")); // 增加文件过滤器
			int option = jFileChooser3.showSaveDialog(this); // 接爱txt文件
			if (option == JFileChooser.APPROVE_OPTION)// 如果选择了保存路径则按路径保存，否则按默认
			{
				path1 = jFileChooser3.getSelectedFile().getPath();
				jTextField4.setText(path1);
			}
		}
	}

	public static void main(String args[]) {
		Face w = new Face();
		w.setVisible(true);
	}
}
