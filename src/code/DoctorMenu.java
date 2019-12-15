package code;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import com.github.lgooddatepicker.zinternaltools.TimeChangeEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Vector;

public class DoctorMenu implements ActionListener {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql;
	String personid;
	JButton refreshButton1;
	JButton exitButton1;
	JButton refreshButton2;
	JButton exitButton2;
	Vector rowData1 = new Vector();
	Vector rowData2 = new Vector();
	final JTabbedPane tabbedPane;
	JFrame jf;
	String startTime;
	String endTime;
	LocalDate oldDate = null,oldDate2 = null;
	LocalTime oldTime=null,oldTime2 = null;
    public DoctorMenu(String id){
        jf = new JFrame("ҽ������");
        personid=id;
        jf.setSize(800, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // ����ѡ����
        tabbedPane = new JTabbedPane();


        // ������ 1 ��ѡ���ѡ�ֻ���� ���⣩
        tabbedPane.addTab("�Һ��б�", createTextPanel1(jf));

        // ������ 2 ��ѡ���ѡ����� ���� �� ͼ�꣩
        tabbedPane.addTab("�����б�", createTextPanel2(jf));


        // ���ѡ�ѡ��״̬�ı�ļ�����
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //System.out.println("��ǰѡ�е�ѡ�: " + tabbedPane.getSelectedIndex());
            }
        });

        // ����Ĭ��ѡ�е�ѡ�
        tabbedPane.setSelectedIndex(1);

        //jf.setLayout(new BorderLayout());
        jf.setContentPane(tabbedPane);
        jf.setVisible(true);
    }

    /**
     * ����һ����壬���������ʾһ����ǩ�����ڱ�ʾĳ��ѡ���Ҫ��ʾ������
     */
    private JComponent createTextPanel1(JFrame jf) {
        // �������, ʹ��һ�� 1 �� 1 �е����񲼾֣�Ϊ���ñ�ǩ�Ŀ���Զ�������壩
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        refreshButton1 = new JButton("����"); 
        exitButton1 = new JButton("�˳�");
//        // ������ǩ
        String name = null;
        try {
			conn = DriverManager.getConnection(  
			          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&user=root&password=1234");
			stmt=conn.createStatement();
			sql="select ysmc from t_ksys where ysbh="+personid;
			rs=stmt.executeQuery(sql);
			while(rs.next()) {
				name=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        JLabel label = new JLabel(name+",��ӭ��");
        label.setFont(new Font(null, Font.BOLD, 15));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // ��ӱ�ǩ�����
//        panel.add(label);
//
//        return panel;
        
     // ��ͷ��������
        Vector columnNames1 = new Vector();

        // �������������
        columnNames1.add("�Һű��");
        columnNames1.add("��������");
        columnNames1.add("�Һ�����ʱ��");
        columnNames1.add("�������");
          
        // ���� ���ģ�ͣ�ָ�� ���������� �� ��ͷ
        TableModel tableModel = new DefaultTableModel(rowData1, columnNames1);

        // ʹ�� ���ģ�� ���� ���
        JTable table = new JTable(tableModel);
        
        // ʹ�� ���ģ�� ���� ����������TableRowSorter ʵ���� RowSorter��
        RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tableModel);

        // �� ��� ���� ��������
        table.setRowSorter(rowSorter);
        // �� ��ͷ ��ӵ�����������ʹ����ͨ���м�������ӱ��ʱ����ͷ �� ���� ��Ҫ�ֿ���ӣ�
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        // �� ������� ��ӵ���������
        panel.add(table, BorderLayout.CENTER);
        
        panel2.add(label);
        panel2.add(refreshButton1);
        panel2.add(exitButton1);
        panel.add(panel2, BorderLayout.SOUTH);  

        refreshButton1.addActionListener(this);
        exitButton1.addActionListener(this);
        jf.setContentPane(panel);
        //jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    
        return panel;
        
    }
    
    
    private JComponent createTextPanel2(JFrame jf) {
        // �������, ʹ��һ�� 1 �� 1 �е����񲼾֣�Ϊ���ñ�ǩ�Ŀ���Զ�������壩
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel();

//        // ������ǩ
        JLabel label1 = new JLabel("��ʼʱ��");
        JLabel label2 = new JLabel("��ֹʱ��");
        
        refreshButton2 = new JButton("����"); 
        exitButton2 = new JButton("�˳�");
//        label.setFont(new Font(null, Font.PLAIN, 50));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // ��ӱ�ǩ�����
//        panel.add(label);
//
//        return panel;
        
     // ��ͷ��������
        Vector columnNames2 = new Vector();
        columnNames2.add("��������");
        columnNames2.add("ҽ�����");
        columnNames2.add( "ҽ������");
        columnNames2.add("�������");
        columnNames2.add("�Һ��˴�");
        columnNames2.add("����ϼ�");

        // ���� ���ģ�ͣ�ָ�� ���������� �� ��ͷ
        TableModel tableModel = new DefaultTableModel(rowData2, columnNames2);

        // ʹ�� ���ģ�� ���� ���
        JTable table = new JTable(tableModel);

        // ʹ�� ���ģ�� ���� ����������TableRowSorter ʵ���� RowSorter��
        RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tableModel);

        // �� ��� ���� ��������
        table.setRowSorter(rowSorter);

        // �� ��ͷ ��ӵ�����������ʹ����ͨ���м�������ӱ��ʱ����ͷ �� ���� ��Ҫ�ֿ���ӣ�
        panel1.add(table.getTableHeader(), BorderLayout.NORTH);
        // �� ������� ��ӵ���������
        panel1.add(table, BorderLayout.CENTER);

        //panel2.add(label1);
        //panel2.add(label2);
        //jf.getContentPane("North", )
        
        
        DateTimePicker dateTimePicker1;
        dateTimePicker1 = new DateTimePicker();
        dateTimePicker1.addDateTimeChangeListener(new SimpleDateTimeChangeListener("dateTimePicker1"));
        DateTimePicker dateTimePicker2;
        dateTimePicker2 = new DateTimePicker();
        dateTimePicker2.addDateTimeChangeListener(new SimpleDateTimeChangeListener("dateTimePicker2"));
//        panel2.add(dateTimePicker1, getConstraints(1, (row * rowMultiplier), 1));
//        panel2.addLabel(panel.panel2, 1, (row++ * rowMultiplier),
//            "DateTimePicker 1, Default settings:");        
        

        
        //panel2.setLayout(new FlowLayout(1,5,5));
        panel2.add(label1);
        panel2.add(dateTimePicker1);
        panel2.add(label2);
        panel2.add(dateTimePicker2);
        panel2.add(refreshButton2);
        panel2.add(exitButton2);
        panel1.add(panel2, BorderLayout.SOUTH);  
        //jf.setLayout(new BorderLayout());
        //jf.add(panel1, BorderLayout.CENTER);
        //jf.add(panel2, BorderLayout.SOUTH);
        refreshButton2.addActionListener(this);
        exitButton2.addActionListener(this);
        
        //jf.setContentPane(panel1);
        //jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    
        return panel1;
        
    }
    
//    public static void main(String[] args) {
//    	DoctorMenu doc=new DoctorMenu("000001");
//    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(  
			          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&user=root&password=1234");
			stmt=conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if(e.getSource()==refreshButton1) {
			 try {
					rowData1.clear();
					sql="SELECT GHBH,BRMC,RQSJ,SFZJ FROM T_GHXX,T_BRXX,T_HZXX WHERE T_GHXX.BRBH=T_BRXX.BRBH and T_GHXX.HZBH=T_HZXX.HZBH and YSBH="+personid;
					rs = stmt.executeQuery(sql);
					while(rs.next()) {
						Vector hang=new Vector();
						hang.add(rs.getString(1));
						hang.add(rs.getString(2));
						hang.add(rs.getString(3));
						if(rs.getInt(4)==1) {
							hang.add("ר�Һ�");
						}else {
							hang.add("��ͨ��");
						}
						//System.out.println(hang);
						rowData1.add(hang);
					}
					 tabbedPane.updateUI();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   	
		}else if(e.getSource()==exitButton1||e.getSource()==exitButton2) {
			System.exit(0);
		}else if(e.getSource()==refreshButton2){
			try {
				Date now =new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String time= sdf.format(now);
				SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
				String time2= sdf2.format(now);
				if(startTime==null) {
					startTime=time+" 00:00:00";
				}
				if(endTime==null) {
					endTime=time+" "+time2;
				}
				//System.out.println(startTime+" "+endTime);
				rowData2.clear();
				sql="SELECT KSMC,T_KSYS.YSBH,YSMC,T_HZXX.SFZJ FROM T_KSXX,T_KSYS,T_HZXX,T_GHXX" + 
						" WHERE T_KSXX.KSBH=T_KSYS.KSBH and T_GHXX.YSBH=T_KSYS.YSBH and T_GHXX.HZBH=T_HZXX.HZBH and RQSJ>=\""+startTime+"\" and RQSJ<=\""+endTime+
						"\" GROUP BY YSBH,SFZJ";
				rs = stmt.executeQuery(sql);
				String sql2;
				Statement stmt2=conn.createStatement();
				ResultSet rs2;
				while(rs.next()) {
					Vector hang=new Vector();
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(3));
					if(rs.getInt(4)==1) {
						hang.add("ר�Һ�");
					}else {
						hang.add("��ͨ��");
					}
					sql2="SELECT count(*),sum(T_GHXX.GHFY) from T_GHXX,T_HZXX WHERE YSBH="+rs.getString(2)+" and SFZJ="+rs.getInt(4)+" and T_HZXX.HZBH=T_GHXX.HZBH"+
							" and RQSJ>=\""+startTime+"\" and RQSJ<=\""+endTime+"\"";
					rs2=stmt2.executeQuery(sql2);
					while(rs2.next()) {
						hang.add(rs2.getString(1));
						hang.add(rs2.getString(2));
					}
					//System.out.println(hang);
					rowData2.add(hang);
					
				}
				 tabbedPane.updateUI();
				 //System.out.print(startTime+endTime);
				 
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}  
		}
	}
	
	public class SimpleDateTimeChangeListener implements DateTimeChangeListener {

        /**
         * dateTimePickerName, This holds a chosen name for the component that we are listening to,
         * for generating time change messages in the demo.
         */
        public String dateTimePickerName;

        /**
         * Constructor.
         */
        private SimpleDateTimeChangeListener(String dateTimePickerName) {
            this.dateTimePickerName = dateTimePickerName;
        }

        /**
         * dateOrTimeChanged, This function will be called whenever the in date or time in the
         * applicable DateTimePicker has changed.
         */
        @Override
        public void dateOrTimeChanged(DateTimeChangeEvent event) {
            // Report on the overall DateTimeChangeEvent.
            String messageStart = "\n\nThe LocalDateTime in " + dateTimePickerName + " has changed from: (";
            String fullMessage = messageStart + event.getOldDateTimeStrict() + ") to (" + event.getNewDateTimeStrict() + ").";
//            if (!panel.messageTextArea.getText().startsWith(messageStart)) {
//                panel.messageTextArea.setText("");
//            }
//            panel.messageTextArea.append(fullMessage);
            // Report on any DateChangeEvent, if one exists.
            DateChangeEvent dateEvent = event.getDateChangeEvent();
            TimeChangeEvent timeEvent = event.getTimeChangeEvent();
            Date now =new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String time= sdf.format(now);
			SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm");
			String time2= sdf2.format(now);
			LocalDate newDate = null,newDate2 = null;
			LocalTime newTime = null,newTime2 = null;
			LocalTime aa = null;
			LocalDate bb=null;
			String front,back,front2,back2;
			
			if(this.dateTimePickerName.equals("dateTimePicker1")) {
				if(dateEvent!=null) {
					newDate=dateEvent.getNewDate();
					oldDate=newDate;
					//System.out.println(newDate);
					if(newDate==bb) {
						front=time;
					}else{
						front=newDate+"";
					}
					if(oldTime!=aa) {
						back=oldTime+"";
					}else {
						back="00:00";
					}
					startTime=front+" "+back+":00";
				}else if(timeEvent!=null){
					newTime=timeEvent.getNewTime();
					oldTime=newTime;
					if(newTime==aa) {
						back="00:00";
					}else {
						back=newTime+"";
					}
					if(oldDate!=bb){
						//System.out.println(oldDate);
						front=oldDate+"";
					}else {
						front=time;
					}
					startTime=front+" "+back+":00";
				}else {
					startTime = time+" "+"00:00:00";
				}
			}else {
				if(dateEvent!=null) {
					newDate2=dateEvent.getNewDate();
					oldDate2=newDate2;
					//System.out.println(newDate);
					if(newDate2==bb) {
						front2=time;
					}else{
						front2=newDate2+"";
					}
					if(oldTime2!=aa) {
						back2=oldTime2+"";
					}else {
						back2=time2;
					}
					endTime=front2+" "+back2+":00";
				}else if(timeEvent!=null){
					newTime2=timeEvent.getNewTime();
					oldTime2=newTime2;
					if(newTime2==aa) {
						back2=time2;
					}else {
						back2=newTime2+"";
					}
					if(oldDate2!=bb){
						//System.out.println(oldDate);
						front2=oldDate2+"";
					}else {
						front2=time;
					}
					endTime=front2+" "+back2+":00";
				}else {
					endTime = time+" "+time2+":00";
				}
			}
			//System.out.println(endTime);


        }
    }

}




