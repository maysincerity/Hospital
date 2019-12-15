package code;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class Login extends JFrame implements ActionListener, ItemListener{
	private static final long serialVersionUID = 1L;
	private JLabel mainlabel=new JLabel("用户登录");
	private JLabel[] label={new JLabel("用户编号"),new JLabel("密码")};
	private JTextField idT=new JTextField(20);
	private JPasswordField pwT=new JPasswordField(20);
	private JButton loginButton=new JButton("登录");
	private JButton exitButton=new JButton("退出");
	String []ct= {"病人","医生"};
	private JComboBox choice=new JComboBox(ct);	
	//private Login_db log;
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt=null;
	private boolean flag=true;
	private boolean com;
	String id=null;
	//private static boolean flag;
	
	public Login(){
		super("用户登录");
		//log=new Login_db();
		mainlabel.setFont(new Font("宋体",Font.BOLD,14));
		loginButton.addActionListener(this);
		exitButton.addActionListener(this);
		choice.addItemListener(this);
		setLayout(null);
		mainlabel.setBounds(180,50,80,26);
		add(mainlabel);
		for(int i=0;i<2;i++){
			label[i].setBounds(50,120+i*50,80,30);
			add(label[i]);
		}
		idT.setBounds(150, 120, 150, 30);
		pwT.setBounds(150, 170, 150, 30);
		add(idT);
		add(pwT);
		loginButton.setBounds(150, 300, 80, 20);
		exitButton.setBounds(250, 300, 80, 20);
		choice.setBounds(50, 300, 80, 20);
		add(loginButton);
		add(exitButton);
		add(choice);
//		log.setButtons(loginButton, exitButton);
//		log.setname(idT);
//		log.setPassword(pwT);
//		log.setComboBox(choice);
		pack();
		setSize(400,450);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		//flag=log.closeLogin();
		//System.out.println("1"+log.closeLogin());
		  
		
		
	}
	
//	public Login_db getLogin_db(){
//		return log;
//	}
//	
//	public void setCom(Login_db log){
//		this.log=log;
//	}
//	
	public static void main(String[] args){
		Login lo=new Login();
//		flag=lo.getLogin_db().getCom();
//		System.out.println("1"+flag);
//		if(com){
//			lo.dispose();
//		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getStateChange() == ItemEvent.SELECTED){
			String ec=(String)choice.getSelectedItem();
			//System.out.println(ec);
			if(ec.equals("病人")){
				flag=true;
			}else{
				flag=false;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==loginButton){
			if(idT.getText()=="")
				JOptionPane.showMessageDialog(null, "请填写账号！"); 
			else {
				String pa=String.valueOf(pwT.getPassword());
				if(pa.length()==0)
					JOptionPane.showMessageDialog(null, "请填写密码！"); 
				else{
					String idString=idT.getText();
					try{
						com=CompareWithSql(idString,pa,flag);
						//System.out.println(com);
						//System.out.print(flag);
						if(com){
							JOptionPane.showMessageDialog(null, "登录成功");  
							Date now= new Date();
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dateTimeString = df.format(now);
							conn = DriverManager.getConnection(  
							          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&user=root&password=1234");
							stmt=conn.createStatement();
							if(flag){
								try {
									stmt.executeUpdate("UPDATE t_brxx set dlrq='"+dateTimeString+"'where brbh='"+id+"'");	
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 
								new PatientMenu(id);
							}else{
								try {
									System.out.println(flag+id);
									stmt.executeUpdate("UPDATE t_ksys set dlrq='"+dateTimeString+"'where ysbh='"+id+"'");	
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 
								new DoctorMenu(id);	
							}
						}else{
							JOptionPane.showMessageDialog(null, "编号或密码为空，请重新输入");  
							idT.setText("");
							pwT.setText("");
						}
					}catch(Exception ee){
						ee.printStackTrace();
					}
				}
			}
		}else if(e.getSource()==exitButton){
			System.exit(0);
		}
			
	}
	
	private boolean CompareWithSql(String idString, String pwString,boolean flag) throws Exception{
		// TODO Auto-generated method stub
		String sql;
		conn = DriverManager.getConnection(  
		          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&user=root&password=1234");
		Statement stmt=conn.createStatement();
		if(flag){
			sql="SELECT * FROM t_brxx";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				id=rs.getString(1);
				String ps=rs.getString(3);
				if(id .equals(idString)&&ps.equals(pwString)){
					return true;
				}
			}
			
		}else{
			sql="SELECT * FROM t_ksys";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				id=rs.getString(1);
				String ps=rs.getString(5);
				if(id .equals(idString)&&ps.equals(pwString)){
					return true;
				}
			}
		}
		
		return false;
	}
}
