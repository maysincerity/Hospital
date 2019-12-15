package code;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class PatientMenu extends JFrame implements ActionListener,ItemListener, DocumentListener{
	private static final long serialVersionUID = 1L;
	private JLabel mainlabel=new JLabel("门诊挂号");
	private JLabel[] label={new JLabel("科室名称"),new JLabel("医生名称"),new JLabel("号种类别"),new JLabel("号别名称"),
			new JLabel("交款金额"),new JLabel("应缴金额"),new JLabel("找零金额"),new JLabel("挂号名称")};
	String[] ct={"专家号","普通号"};
	
	private JFilterComboBox combo1;
	private JComboBox combo2=new JComboBox();
	private JComboBox combo3=new JComboBox();
	private JComboBox combo4=new JComboBox();
	
	private JTextField moneytotal=new JTextField(20);
	private JTextField moneypaid=new JTextField(20);
	private JTextField moneychange=new JTextField(20);
	private JTextField number=new JTextField(20);
	
	private JButton registerButton=new JButton("挂号");
	private JButton exitButton=new JButton("退出");
	
	Vector<String> items1 = new Vector<String>();
	Vector<String> items2 = new Vector<String>();
	Vector<String> items3 = new Vector<String>();
	Vector<String> items4 = new Vector<String>();
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String [] apartment;
	String[] category;
	String[] doctor;
	String sql;
	String moneyneed;
	String personid;
	
	public PatientMenu(String id){
		super("挂号系统");
		mainlabel.setFont(new Font("宋体",Font.BOLD,14));
		setLayout(null);
		mainlabel.setBounds(350,50,80,26);
		add(mainlabel);
		for(int i=0;i<8;i=i+2){
			label[i].setBounds(50,120+i*50,80,30);
			add(label[i]);
		}
		for(int i=1;i<8;i=i+2){
			label[i].setBounds(400,120+(i-1)*50,80,30);
			add(label[i]);
		}
		
		try{
			conn = DriverManager.getConnection(  
			          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&user=root&password=1234");   
			stmt=conn.createStatement();
			rs = stmt.executeQuery("select * from t_ksxx");
			while (rs.next()) {
				items1.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
			}

			//System.out.println(items1o.size());
			sql="SELECT ysbh,ysmc,pyzs FROM t_ksys WHERE ksbh='000001'and sfzj=1";
			rs=stmt.executeQuery(sql);
			while (rs.next()) {
				items2.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
			}
			
			sql="SELECT hzbh,hzmc,pyzs FROM t_hzxx WHERE ksbh='000001'and sfzj=1";
			rs=stmt.executeQuery(sql);
			while (rs.next()) {
				items4.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
			}
			items3.add(ct[0]);
			items3.add(ct[1]);
			
			sql="SELECT ghfy FROM t_hzxx WHERE ksbh='000001'and sfzj=1";
			rs=stmt.executeQuery(sql);
			while (rs.next()) {
				moneyneed=rs.getString(1);
			}
			moneypaid.setText(moneyneed);
		}catch(SQLException e){
			e.printStackTrace();
		}
		personid=id;
		//combo1.setModel(new DefaultComboBoxModel(items1));
		combo1=new JFilterComboBox(items1);
		combo1.setBounds(150,125,200,20);
		combo1.setEditable(true);
		combo1.addItemListener(this);
		combo2.setModel(new DefaultComboBoxModel(items2));
		combo2.setBounds(500,125,200,20);
		combo2.setEditable(true);
		combo2.addItemListener(this);
		combo3.setModel(new DefaultComboBoxModel(items3));
		combo3.setBounds(150,225,200,20);
		combo3.setEditable(true);
		combo3.addItemListener(this);
		combo4.setModel(new DefaultComboBoxModel(items4));
		combo4.setBounds(500,225,200,20);
		combo4.setEditable(true);
		combo4.addItemListener(this);
		
		moneytotal.setBounds(150, 325, 200, 20);
		moneytotal.addActionListener(this);
		moneytotal.getDocument().addDocumentListener(this);
		moneypaid.setBounds(500, 325, 200, 20);
		moneypaid.setEditable(false);
		moneypaid.addActionListener(this);
		moneychange.setBounds(150, 425, 200, 20);
		moneychange.setEditable(false);
		moneychange.addActionListener(this);
		number.setBounds(500, 425, 200, 20);
		number.setEditable(false);
		
		registerButton.setBounds(250, 550, 80, 20);
		exitButton.setBounds(450, 550, 80, 20);
		registerButton.addActionListener(this);
		exitButton.addActionListener(this);
		add(combo1);
		add(combo2);
		add(combo3);
		add(combo4);
		add(moneytotal);
		add(moneypaid);
		add(moneychange);
		add(number);
		add(exitButton);
		add(registerButton);
		pack();
		setSize(800,700);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
//	public static void main(String[] args){
//		PatientMenu pa=new PatientMenu("000002"); 
//	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getStateChange() == ItemEvent.SELECTED && (e.getSource()==combo1 || e.getSource()==combo3)){
			apartment = ((String) combo1.getSelectedItem()).split("\\s+");
			//System.out.println(apartment[0]);
			String expert=(String) combo3.getSelectedItem();
			//System.out.println(expert);
			int flag=1;
			if(expert.equals("专家号")){
				flag=1;
			}else{
				flag=0;
			}
			
			items2=new Vector<String>();
			items4=new Vector<String>();
			try {
				if(flag==1) {
					sql="SELECT ysbh,ysmc,pyzs FROM t_ksys WHERE ksbh='"+apartment[0]+"' and sfzj='"+flag+"'";
				}
				else {
					sql="SELECT ysbh,ysmc,pyzs FROM t_ksys WHERE ksbh='"+apartment[0]+"'";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					items2.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
				}
				combo2.setModel(new DefaultComboBoxModel(items2));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			doctor=((String) combo2.getSelectedItem()).split("\\s+");
			
			
			try {
				sql="SELECT hzbh,hzmc,pyzs FROM t_hzxx WHERE ksbh='"+apartment[0]+"'and sfzj='"+flag+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					items4.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
				}
				combo4.setModel(new DefaultComboBoxModel(items4));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			category=((String) combo4.getSelectedItem()).split("\\s+");
			try {
				sql="SELECT ghfy FROM t_hzxx WHERE ksbh='"+apartment[0]+"'and sfzj='"+flag+"' and hzbh='"+category[0]+"'";
				rs=stmt.executeQuery(sql);
				while (rs.next()) {
					moneyneed=rs.getString(1);
				}
				moneypaid.setText(moneyneed);
			}catch(SQLException e1) {
				
			}
		}else if(e.getSource()==combo2) {
			doctor=((String) combo2.getSelectedItem()).split("\\s+");
		}else if(e.getSource()==combo4) {
			category=((String) combo4.getSelectedItem()).split("\\s+");
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==exitButton){
			System.exit(0);
		}else if(e.getSource()==registerButton){
			if(moneytotal.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请填写交款金额"); 
			}
			else{
				String s =moneytotal.getText();
				float getmoney=Float.parseFloat(s);
				//System.out.println(getmoney);
				float needmoney=Float.parseFloat(moneypaid.getText());
				sql="SELECT ycje FROM t_brxx WHERE brbh='"+personid+"'";
				try {
					rs=stmt.executeQuery(sql);
					while (rs.next()) {
						s=rs.getString(1);
					}
				} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
				float change=getmoney-needmoney+Float.parseFloat(s);
				moneychange.setText(change+"");
				if(change<0) {
					JOptionPane.showMessageDialog(null, "交款金额太少！"); 
				}else {
					JOptionPane.showMessageDialog(null, "余额充足！"); 
					Date now= new Date();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateTimeString = df.format(now);
					//System.out.println(dateTimeString);
					int moneyback=0;
					String getGHBH =GetRegister(category[0],doctor[0],personid,moneyback,needmoney,dateTimeString);
					//System.out.println(doctor[0]);
					if(getGHBH==null) {
						
					}else {
						try {
							stmt.executeUpdate("UPDATE t_brxx set ycje='"+change+"'where brbh='"+personid+"'");
							//System.out.println(change);
						}catch(SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					
					number.setText(getGHBH);
				}	
			}
			
		}
	}

	
	private String GetRegister(String HZBH, String YSBH, String BRBH, int THBZ, float GHFY,String RQSJ) {
		// TODO Auto-generated method stub
		try {
			if(validateTableNameExist("t_ghxx")){
				if(!validateItemExist("t_ghxx",HZBH)) {
					String sql3="select count(*) from T_GHXX";
					String row=null;
					ResultSet rs3=stmt.executeQuery(sql3);
					while (rs3.next()) {
						row=rs3.getString(1);
					}
					int nownumber=Integer.parseInt(row)+1;
					String rownumber=String.valueOf(nownumber);
					String guahao="";
					for(int i=0;i<6-rownumber.length();i++) {
						guahao+="0";
					}
					guahao=guahao+rownumber;
					stmt.executeUpdate("insert into T_GHXX values('"+guahao+"','"+HZBH+"','"+YSBH+"','"+BRBH+"',1,'"+THBZ+"','"+GHFY+"','"+RQSJ+"',NULL)");
					JOptionPane.showMessageDialog(null, "挂号成功！"); 
					
					return guahao;
				}
				else{
					sql="select ghrc from T_GHXX where hzbh='"+HZBH+"'";
					String people1 = null,people2 = null;
					int registerpeople,peopletotal;
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						people1=rs.getString(1);
					}
					registerpeople=Integer.parseInt(people1);
					String sql2="select ghrs from t_hzxx where hzbh='"+HZBH+"'";
					ResultSet rs2=stmt.executeQuery(sql2);
					while (rs2.next()) {
						people2=rs2.getString(1);
					}
					peopletotal=Integer.parseInt(people2);
					if(registerpeople>=peopletotal) {
						JOptionPane.showMessageDialog(null, "挂号人满！您此次充值失败"); 
					}else {
						String sql3="select count(*) from T_GHXX";
						String row=null;
						ResultSet rs3=stmt.executeQuery(sql3);
						while (rs3.next()) {
							row=rs3.getString(1);
						}
						int nownumber=Integer.parseInt(row)+1;
						String rownumber=String.valueOf(nownumber);
						String guahao="";
						for(int i=0;i<6-rownumber.length();i++) {
							guahao+="0";
						}
						guahao=guahao+rownumber;
						int nowregister=registerpeople+1;
						//System.out.println(guahao);
						stmt.executeUpdate("insert into T_GHXX values('"+guahao+"','"+HZBH+"','"+YSBH+"','"+BRBH+"','"+nowregister+"','"+THBZ+"','"+GHFY+"','"+RQSJ+"',NULL)");
						//System.out.println("insert into T_GHXX values("+guahao+","+HZBH+","+YSBH+","+BRBH+","+nowregister+","+THBZ+","+GHFY+","+RQSJ+",NULL)");
//						sql3="update T_GHXX set GHRC='"+nowregister+"'where HZBH='"+HZBH+"'";
//						stmt.executeUpdate(sql3);
						JOptionPane.showMessageDialog(null, "挂号成功！"); 
						return guahao;
					//System.out.println(row.length());
					}
				//System.out.println(registerpeople+peopletotal);
				}
				
			}else {
				stmt.executeUpdate("CREATE TABLE T_GHXX("+ 
						"	GHBH CHAR(6) NOT NULL," + 
						"	HZBH CHAR(6) NOT NULL," + 
						"	YSBH CHAR(6) NOT NULL," + 
						"	BRBH CHAR(6) NOT NULL," + 
						"	GHRC INT NOT NULL," + 
						"	THBZ TINYINT NOT NULL," + 
						"	GHFY DECIMAL(8,2) NOT NULL," + 
						"	RQSJ DATETIME NOT NULL," + 
						"	KBSJ DATETIME," + 
						"	PRIMARY KEY(GHBH)" + 
						")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
				stmt.executeUpdate("insert into T_GHXX values('000001','"+HZBH+"','"+YSBH+"','"+BRBH+"',1,'"+THBZ+"','"+GHFY+"','"+RQSJ+"',NULL)");
				JOptionPane.showMessageDialog(null, "挂号成功！"); 
				return "000001";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "系统繁忙，请重新挂号！"); 
		}
		return null;
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
//		Document doc = e.getDocument();
//		String s = null;
//        try {
//			s = doc.getText(0, doc.getLength());
//		} catch (BadLocationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
//		float getmoney=Float.parseFloat(s);
//		//System.out.println(getmoney);
//		float needmoney=Float.parseFloat(moneypaid.getText());
//		sql="SELECT ycje FROM t_brxx WHERE brbh='"+personid+"'";
//		try {
//			rs=stmt.executeQuery(sql);
//			while (rs.next()) {
//				s=rs.getString(1);
//			}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		float change=getmoney-needmoney+Float.parseFloat(s);
//		moneychange.setText(change+"");
//		if(change<0) {
//			JOptionPane.showMessageDialog(null, "交款金额太少！"); 
//		}
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}
	public boolean validateTableNameExist(String tableName) throws SQLException {
        rs = conn.getMetaData().getTables(null, null, tableName, null);
        if (rs.next()) {
              return true;
        }else {
              return false;
        }
    }
	
	public boolean validateItemExist(String tableName,String HZBH) throws SQLException{
		String sqltest="select count(*) as ct from "+tableName+" where HZBH="+HZBH;
		ResultSet Judge = stmt.executeQuery(sqltest);
		Judge.next();
		int ct = Judge.getInt("ct");
		//System.out.println(ct);
		if(ct>0) {
			return true;
		}else {
			return false;
		}	
		
	}

}
