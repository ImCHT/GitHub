package com.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class Login{
	JFrame loginFrame = new JFrame("登录界面");
	JPasswordField passwordText = new JPasswordField(20);
	//初始化界面
		public void init() {
			
			String sql = null;
			QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
			//查询数据库test下有没有存储密码的account表格
			long count = -1;
			sql = "SELECT COUNT(*) FROM information_schema.TABLES t WHERE t.TABLE_SCHEMA ='test' AND t.TABLE_NAME ='account'";
			try {
				count = queryRunner.query(sql, new ScalarHandler<Long>());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//count为0，表示没有account，通过queryrunner新建一个account表格
			if (count==0) {
				sql = "CREATE TABLE account(id INT PRIMARY KEY AUTO_INCREMENT,pwd VARCHAR(50))";
				try {
//					statement.executeUpdate(sql);
					queryRunner.update(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//查询存储账号密码的item表格存不存在
			sql = "SELECT COUNT(*) FROM information_schema.TABLES t WHERE t.TABLE_SCHEMA ='test' AND t.TABLE_NAME ='item'";
			try {
				count = queryRunner.query(sql, new ScalarHandler<Long>());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//如果不存在，新建item表格
			if (count==0) {
				sql = "CREATE TABLE item(id INT PRIMARY KEY AUTO_INCREMENT,website VARCHAR(50),"
						+ "username VARCHAR(50),pwd VARCHAR(50),url VARCHAR(50))";
				try {
//					statement.executeUpdate(sql);
					queryRunner.update(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sql = "INSERT INTO account VALUES (1,1234)";
				try {
					queryRunner.update(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//界面初始化
			JPanel loginPanel1 = new JPanel();
			JPanel loginPanel2 = new JPanel();
			JButton loginButton = new JButton("登陆");
			JButton changePasswordButton = new JButton("修改密码");
			
			loginFrame.setLayout(new BorderLayout(10,10));
			loginPanel1.setLayout(new GridLayout(1, 2,0,10));
			loginPanel1.add(new JLabel("密码:"));
			loginPanel1.add(passwordText);
			loginPanel2.setLayout(new GridLayout(1, 2,50,50));
			loginPanel2.add(loginButton);
			loginPanel2.add(changePasswordButton);
			loginFrame.add(loginPanel1);
			//为了让四周留空，看起来不那么拥挤
			loginFrame.add(loginPanel2,BorderLayout.SOUTH);
			loginFrame.add(new JPanel(),BorderLayout.EAST);
			loginFrame.add(new JPanel(),BorderLayout.WEST);
			loginFrame.add(new JPanel(),BorderLayout.NORTH);
			
			loginFrame.pack();
			loginFrame.setLocationRelativeTo(null);
			loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			loginFrame.setVisible(true);
			//注册按钮事件
			loginButton.addActionListener(new loginDialog());
			changePasswordButton.addActionListener(new changePasswordButtonListener());
		}
		//更改密码按钮事件，内部类处理
		public class changePasswordButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				//需要新建新建一个界面，故单独写了一个类
				new changePasswordDialog().init();
			}
		}	
		
		//登录按钮事件响应，判断输入的密码和存储的密码一不一致，若一致显示主界面
		class loginDialog implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
				String sql = "select pwd from account where id = 1";
				String password = null;
				try {
					password = queryRunner.query(sql,new ScalarHandler<String>());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if ((String.valueOf(passwordText.getPassword()).equals(password))) {
					//若一直，关闭登陆界面，显示主界面
					loginFrame.dispose();
					new MainWindow().init();
				}else {
					JOptionPane.showMessageDialog(loginFrame, "密码错误，请重新输入！");
				}
			}
		}

}
//主要是更改密码界面初始化
class changePasswordDialog {
	JDialog dialog = new JDialog(new Login().loginFrame,"修改密码",true);
	JTextField oldPassWord = new JTextField(20);
	JTextField newPassWord = new JTextField(20);
	JButton confiemButton = new JButton("确定");
	void init() {
		JPanel jPanel = new JPanel(new GridLayout(5,1,10,10));
		dialog.setLayout(new BorderLayout(10, 10));
		jPanel.add(new JLabel("旧密码"));
		jPanel.add(oldPassWord);
		jPanel.add(new JLabel("新密码"));
		jPanel.add(newPassWord);
		jPanel.add(confiemButton);
		dialog.add(jPanel);
		dialog.add(new JPanel(),BorderLayout.EAST);
		dialog.add(new JPanel(),BorderLayout.WEST);
		dialog.add(new JPanel(),BorderLayout.NORTH);
		dialog.add(new JPanel(),BorderLayout.SOUTH);
		
//		dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		confiemButton.addActionListener(new confirmListener());
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	//修改密码对话框确定按钮事件，先判断原密码与数据库中一不一致，若一致可以更改
	class confirmListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
			String sql = "select pwd from account where id = 1";
			String password = null;
			try {
				password = queryRunner.query(sql,new ScalarHandler<String>());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if ((oldPassWord.getText().equals(password))) {
				sql = "update account set pwd = ? where id = 1";
				try {
					queryRunner.update(sql,newPassWord.getText());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(dialog, "修改密码成功！");
				dialog.dispose();
			}else {
				JOptionPane.showMessageDialog(dialog, "原密码错误，请重新输入！");
			}
		}
	}	
}






