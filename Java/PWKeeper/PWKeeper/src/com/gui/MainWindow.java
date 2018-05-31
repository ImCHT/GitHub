package com.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

//主界面类
public class MainWindow {
	JFrame mainFrame = new JFrame("密码管理");
	JMenu menu = new JMenu("更改");
	JMenuItem newMenuItem = new JMenuItem("新建");
	JMenuItem editMenuItem = new JMenuItem("修改");
	JMenuItem deleteMenuItem = new JMenuItem("删除");
	JMenuItem aboutMenuItem = new JMenuItem("关于");
	static JTable jTable;
//	JMenu editMenu = new JMenu("编辑");
//	JMenu deleteMenu = new JMenu("删除");
	//初始化界面
	public void init() {
		//界面初始化
		JMenuBar menuBar = new JMenuBar();
		List<Object[]> item = null;
		
		menu.add(newMenuItem);
		menu.add(editMenuItem);
		menu.add(deleteMenuItem);
		menu.add(aboutMenuItem);
		menuBar.add(menu);

		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "SELECT website,username,pwd,url FROM item";
		try {
			item = queryRunner.query(sql,new ArrayListHandler());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
//		JTable jTable;
		//读取数据库中的数据并显示在表格中
		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		for(Object[] objects:item) {
			Vector<Object> objects2 = new Vector<Object>();
			for(Object object:objects) {
				objects2.add(object);
			}
			tableData.add(objects2);
		}
		Vector<String> tableTitle = new Vector<String>();
		tableTitle.addElement("网站");tableTitle.addElement("用户名");
		tableTitle.addElement("密码");tableTitle.addElement("网址");
		DefaultTableModel tableModel = new DefaultTableModel(tableData, tableTitle) {
			/**
			 * 设置Jtable不能编辑只可以选择
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jTable = new JTable(tableModel);
//		tableModel.addRow(new Object[] {"网易","123","123","www"});
		//注册按钮事件
		newMenuItem.addActionListener(new newMenuListener());
		editMenuItem.addActionListener(new editMenuListener());
		deleteMenuItem.addActionListener(new deleteMenuListener());
		aboutMenuItem.addActionListener(new aboutMenuListener());
		
		mainFrame.setJMenuBar(menuBar);
		mainFrame.add(new JScrollPane(jTable));
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
//		jTable.setEnabled(false);
	}
	//关于按钮事件响应
	public class aboutMenuListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JDialog aboutDialog = new JDialog(mainFrame, "关于", true);
			JLabel label1 = new JLabel("作者：CHT");
			JLabel label2 = new JLabel("GitHub ID：ImCHT");
			JPanel jPanel = new JPanel(new GridLayout(2, 1));
			aboutDialog.setLayout(new BorderLayout(20,20));
//			aboutDialog.setBounds(100, 100, 100, 250);
			jPanel.add(label1);
			jPanel.add(label2);
			aboutDialog.add(jPanel);
			aboutDialog.add(new JPanel(),BorderLayout.EAST);
			aboutDialog.add(new JPanel(),BorderLayout.WEST);
			aboutDialog.add(new JPanel(),BorderLayout.NORTH);
			aboutDialog.add(new JPanel(),BorderLayout.SOUTH);
			aboutDialog.pack();
			aboutDialog.setLocationRelativeTo(null);
			aboutDialog.setVisible(true);
		}
	}
	//新建按钮事件响应
	public class newMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new NewItemDialog().init();
		}
	}	
	//编辑按钮事件响应
	public class editMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new EditItemDialog().init();
		}
	}	
	//删除按钮事件响应，不用界面，只写了内部类
	public class deleteMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int selectRow = jTable.getSelectedRow();
			if (selectRow==-1) {
				JOptionPane.showMessageDialog(mainFrame, "请选择要删除的行");
				return;
			}
			int option = JOptionPane.showConfirmDialog(mainFrame, "确定删除？");
			//确定删除执行代码
			if (option==JOptionPane.YES_OPTION) {
				DefaultTableModel model = (DefaultTableModel)jTable.getModel();
				//删除mysql数据库记录
				QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
				String sql = "DELETE FROM item WHERE website=? AND username=? AND pwd=? AND url=?";
				try {
					queryRunner.update(sql,model.getValueAt(selectRow, 0),model.getValueAt(selectRow, 1),
							model.getValueAt(selectRow, 2),model.getValueAt(selectRow, 3));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//删除jtable数据
				model.removeRow(selectRow);
			}
		}
	}	
}

//新建项目事件响应
class NewItemDialog{
	JDialog newItemDialog = new JDialog(new MainWindow().mainFrame,"新建",true);
	JTextField websiteText = new JTextField(20);
	JTextField userNameText = new JTextField(20);
	JTextField passwordText = new JTextField(20);
	JTextField urlText = new JTextField(20);
	JButton confiemButton = new JButton("确定");
	public void init() {
		JPanel newItemPanel = new JPanel();
		
		newItemDialog.setLayout(new BorderLayout(10,10));
		newItemPanel.setLayout(new GridLayout(9,1,10,10));
		newItemPanel.add(new JLabel("网站"));
		newItemPanel.add(websiteText);
		newItemPanel.add(new JLabel("用户名"));
		newItemPanel.add(userNameText);
		newItemPanel.add(new JLabel("密码"));
		newItemPanel.add(passwordText);
		newItemPanel.add(new JLabel("网址"));
		newItemPanel.add(urlText);
		newItemPanel.add(confiemButton);
		newItemDialog.add(newItemPanel);
		newItemDialog.add(new JPanel(),BorderLayout.EAST);
		newItemDialog.add(new JPanel(),BorderLayout.WEST);
		newItemDialog.add(new JPanel(),BorderLayout.NORTH);
		newItemDialog.add(new JPanel(),BorderLayout.SOUTH);
		
		confiemButton.addActionListener(new confirmListener());
//		newItemDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newItemDialog.setLocationRelativeTo(null);
		newItemDialog.pack();
		newItemDialog.setVisible(true);
	}
	//新建项目对话框确认按钮事件响应
	class confirmListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			System.out.println("click");
			QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
			String sql = null;
			//先判断用户有无输入内容
			if (websiteText.getText().equals("")) {
				JOptionPane.showMessageDialog(newItemDialog, "请输入网站名！");
				return;
			}
			//查询数据库中有无此记录，若有不让添加
			sql = "SELECT id FROM item WHERE website=? AND username=? "
					+ "AND pwd=? AND url=?;";
			try {
				Integer id = queryRunner.query(sql,new ScalarHandler<Integer>(),websiteText.getText(),userNameText.getText(),
						passwordText.getText(),urlText.getText());
				if (id!=null) {
					JOptionPane.showMessageDialog(newItemDialog, "已存在此记录，请重新输入！");
					return;	//直接返回
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//若没有此记录，添加进数据库
			sql = "INSERT INTO item (website,username,pwd,url) VALUES (?,?,?,?)";
			try {
				queryRunner.update(sql,websiteText.getText(),userNameText.getText(),
						passwordText.getText(),urlText.getText());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DefaultTableModel tableModel = (DefaultTableModel) MainWindow.jTable.getModel();
			tableModel.addRow(new Object[] {websiteText.getText(),userNameText.getText(),
					passwordText.getText(),urlText.getText()});
			newItemDialog.dispose();
		}
	}	
}
//修改记录对话框
class EditItemDialog{
	JDialog newItemDialog = new JDialog(new MainWindow().mainFrame,"修改",true);
	JTextField userNameText = new JTextField(20);
	JTextField passwordText = new JTextField(20);
	JTextField websiteText = new JTextField(20);
	JTextField urlText = new JTextField(20);
	JButton confiemButton = new JButton("确定");
	int databaseDataID = 0;
	public void init() {
		JPanel newItemPanel = new JPanel();
		
		newItemDialog.setLayout(new BorderLayout(10,10));
		newItemPanel.setLayout(new GridLayout(9,1,10,10));
		newItemPanel.add(new JLabel("网站"));
		newItemPanel.add(websiteText);
		newItemPanel.add(new JLabel("用户名"));
		newItemPanel.add(userNameText);
		newItemPanel.add(new JLabel("密码"));
		newItemPanel.add(passwordText);
		newItemPanel.add(new JLabel("网址"));
		newItemPanel.add(urlText);
		newItemPanel.add(confiemButton);
		newItemDialog.add(newItemPanel);
		newItemDialog.add(new JPanel(),BorderLayout.EAST);
		newItemDialog.add(new JPanel(),BorderLayout.WEST);
		newItemDialog.add(new JPanel(),BorderLayout.NORTH);
		newItemDialog.add(new JPanel(),BorderLayout.SOUTH);
		
		confiemButton.addActionListener(new confirmListener());
//		newItemDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		DefaultTableModel tableModel = (DefaultTableModel) MainWindow.jTable.getModel();
		//先判断有无选中行
		int editRowNum = MainWindow.jTable.getSelectedRow();
		if (editRowNum!=-1) {
			//显示选中行的信息
			String website = MainWindow.jTable.getValueAt(editRowNum, 0).toString();
			String userName = MainWindow.jTable.getValueAt(editRowNum, 1).toString();
			String password = MainWindow.jTable.getValueAt(editRowNum, 2).toString();
			String url = MainWindow.jTable.getValueAt(editRowNum, 3).toString();
//			Vector<Object> editRowData = (Vector<Object>) tableModel.getDataVector().get(editRowNum);
			websiteText.setText(website);
			userNameText.setText(userName);
			passwordText.setText(password);
			urlText.setText(url);
			
			QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
			String sql = "SELECT id FROM item WHERE website=? AND username=? "
					+ "AND pwd=? AND url=?;";
			try {
				databaseDataID = queryRunner.query(sql,new ScalarHandler<Integer>(),website,userName,password,url);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			newItemDialog.setLocationRelativeTo(null);
			newItemDialog.pack();
			newItemDialog.setVisible(true);
			
		}else {
			//若没有选中行，不显示界面
			JOptionPane.showMessageDialog(newItemDialog, "请选择要更改的行！");
			newItemDialog.dispose();
		}
		
	}
	//修改记录界面确认按钮事件
	class confirmListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//更新JTable
			DefaultTableModel tableModel = (DefaultTableModel) MainWindow.jTable.getModel();
			int editRowNum = MainWindow.jTable.getSelectedRow();
			tableModel.setValueAt(websiteText.getText(), editRowNum, 0);
			tableModel.setValueAt(userNameText.getText(), editRowNum, 1);
			tableModel.setValueAt(passwordText.getText(), editRowNum, 2);
			tableModel.setValueAt(urlText.getText(), editRowNum, 3);
			//更新数据库
			QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
			String sql = "UPDATE item SET website=?,username=?,pwd=?,url=? WHERE id=?";
			try {
				queryRunner.update(sql,websiteText.getText(),userNameText.getText(),
						passwordText.getText(),urlText.getText(),databaseDataID);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			newItemDialog.dispose();
		}
	}	
}





