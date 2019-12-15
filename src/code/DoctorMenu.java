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
        jf = new JFrame("医生界面");
        personid=id;
        jf.setSize(800, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建选项卡面板
        tabbedPane = new JTabbedPane();


        // 创建第 1 个选项卡（选项卡只包含 标题）
        tabbedPane.addTab("挂号列表", createTextPanel1(jf));

        // 创建第 2 个选项卡（选项卡包含 标题 和 图标）
        tabbedPane.addTab("收入列表", createTextPanel2(jf));


        // 添加选项卡选中状态改变的监听器
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //System.out.println("当前选中的选项卡: " + tabbedPane.getSelectedIndex());
            }
        });

        // 设置默认选中的选项卡
        tabbedPane.setSelectedIndex(1);

        //jf.setLayout(new BorderLayout());
        jf.setContentPane(tabbedPane);
        jf.setVisible(true);
    }

    /**
     * 创建一个面板，面板中心显示一个标签，用于表示某个选项卡需要显示的内容
     */
    private JComponent createTextPanel1(JFrame jf) {
        // 创建面板, 使用一个 1 行 1 列的网格布局（为了让标签的宽高自动撑满面板）
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        refreshButton1 = new JButton("更新"); 
        exitButton1 = new JButton("退出");
//        // 创建标签
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
		
        JLabel label = new JLabel(name+",欢迎你");
        label.setFont(new Font(null, Font.BOLD, 15));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // 添加标签到面板
//        panel.add(label);
//
//        return panel;
        
     // 表头（列名）
        Vector columnNames1 = new Vector();

        // 表格所有行数据
        columnNames1.add("挂号编号");
        columnNames1.add("病人名称");
        columnNames1.add("挂号日期时间");
        columnNames1.add("号种类别");
          
        // 创建 表格模型，指定 所有行数据 和 表头
        TableModel tableModel = new DefaultTableModel(rowData1, columnNames1);

        // 使用 表格模型 创建 表格
        JTable table = new JTable(tableModel);
        
        // 使用 表格模型 创建 行排序器（TableRowSorter 实现了 RowSorter）
        RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tableModel);

        // 给 表格 设置 行排序器
        table.setRowSorter(rowSorter);
        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        // 把 表格内容 添加到容器中心
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
        // 创建面板, 使用一个 1 行 1 列的网格布局（为了让标签的宽高自动撑满面板）
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel();

//        // 创建标签
        JLabel label1 = new JLabel("开始时间");
        JLabel label2 = new JLabel("截止时间");
        
        refreshButton2 = new JButton("更新"); 
        exitButton2 = new JButton("退出");
//        label.setFont(new Font(null, Font.PLAIN, 50));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // 添加标签到面板
//        panel.add(label);
//
//        return panel;
        
     // 表头（列名）
        Vector columnNames2 = new Vector();
        columnNames2.add("科室名称");
        columnNames2.add("医生编号");
        columnNames2.add( "医生名称");
        columnNames2.add("号种类别");
        columnNames2.add("挂号人次");
        columnNames2.add("收入合计");

        // 创建 表格模型，指定 所有行数据 和 表头
        TableModel tableModel = new DefaultTableModel(rowData2, columnNames2);

        // 使用 表格模型 创建 表格
        JTable table = new JTable(tableModel);

        // 使用 表格模型 创建 行排序器（TableRowSorter 实现了 RowSorter）
        RowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tableModel);

        // 给 表格 设置 行排序器
        table.setRowSorter(rowSorter);

        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
        panel1.add(table.getTableHeader(), BorderLayout.NORTH);
        // 把 表格内容 添加到容器中心
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
							hang.add("专家号");
						}else {
							hang.add("普通号");
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
						hang.add("专家号");
					}else {
						hang.add("普通号");
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




