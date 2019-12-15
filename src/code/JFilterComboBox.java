package code;
 
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
 
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
 
/**
 * �Զ�����������
 * @author Sun
 *
 */
@SuppressWarnings("serial")
public class JFilterComboBox extends JComboBox {
	
	/**
	 * ��ʾ��ģ��
	 */
	protected static DefaultComboBoxModel showModel = new DefaultComboBoxModel();
	/**
	 * ����ѡ��
	 */
	private boolean selectingItem;
	
	/**
	 * ����һ�� <code>JFilterComboBox</code>��
	 * ����ȡ�����е� <code>ComboBoxModel</code>��
	 * �����ṩ�� <code>ComboBoxModel</code>��
	 * ʹ�ô˹��췽����������Ͽ򲻴���Ĭ����Ͽ�ģ�ͣ�
	 * �����Ӱ����롢�Ƴ�����ӷ�������Ϊ��ʽ��
	 * @param aModel - �ṩ��ʾ�����б�� <code>ComboBoxModel</code>
	 */
	public JFilterComboBox(ComboBoxModel aModel) {
		super(aModel);
		initialize();
	}
 
	/**
	 * ��������ָ�������е�Ԫ�ص� <code>JFilterComboBox</code>��
	 * Ĭ������£�ѡ�������еĵ�һ����Ҳѡ���˸��������ģ�ͣ��� 
	 * @param items - Ҫ���뵽��Ͽ�Ķ�������
	 */
	public JFilterComboBox(final Object items[]) {
		super(items);
		initialize();
	}
	
	/**
	 * ��������ָ�� <code>Vector</code> �е�Ԫ�ص� <code>JFilterComboBox</code>��
	 * Ĭ������£�ѡ�������еĵ�һ����Ҳѡ���˸��������ģ�ͣ��� 
	 * @param items - Ҫ���뵽��Ͽ����������
	 */
	public JFilterComboBox(Vector<?> items) {
		super(items);
		initialize();
	}
	
	/**
	 * ��������Ĭ������ģ�͵� <code>JFilterComboBox</code>��
	 * Ĭ�ϵ�����ģ��Ϊ�ն����б�ʹ�� <code>addItem</code> ����
	 * Ĭ������£�ѡ������ģ���еĵ�һ�
	 */
	public JFilterComboBox() {
		super();
		initialize();
	}
	
	private void initialize() {
		showModel.addListDataListener(this);
	}
	
	@Override
	public void updateUI() {
		setUI(new MetalFilterComboBoxUI());
		ListCellRenderer renderer = getRenderer();
		if (renderer instanceof Component) {
			SwingUtilities.updateComponentTreeUI((Component) renderer);
		}
	}
	
	@Override
	public Object getSelectedItem() {
		return showModel.getSelectedItem();
	}
 
	@Override
	public void setSelectedItem(Object anObject) {
		Object oldSelection = selectedItemReminder;
		Object objectToSelect = anObject;
		if (oldSelection == null || !oldSelection.equals(anObject)) {
 
			if (anObject != null && !isEditable()) {
				boolean found = false;
				for (int i = 0; i < showModel.getSize(); i++) {
					Object element = showModel.getElementAt(i);
					if (anObject.equals(element)) {
						found = true;
						objectToSelect = element;
						break;
					}
				}
				if (!found) {
					return;
				}
			}
 
			selectingItem = true;
			showModel.setSelectedItem(objectToSelect);
			selectingItem = false;
 
			if (selectedItemReminder != showModel.getSelectedItem()) {
				selectedItemChanged();
			}
		}
		fireActionEvent();
	}
 
	@Override
	public void setSelectedIndex(int anIndex) {
		int size = showModel.getSize();
		if (anIndex == -1 || size == 0) {
			setSelectedItem(null);
		} else if (anIndex < -1) {
			throw new IllegalArgumentException("setSelectedIndex: " + anIndex
					+ " out of bounds");
		} else if (anIndex >= size) {
			setSelectedItem(showModel.getElementAt(size - 1));
		} else {
			setSelectedItem(showModel.getElementAt(anIndex));
		}
	}
 
	@Override
	public int getSelectedIndex() {
		Object sObject = showModel.getSelectedItem();
		int i, c;
		Object obj;
 
		for (i = 0, c = showModel.getSize(); i < c; i++) {
			obj = showModel.getElementAt(i);
			if (obj != null && obj.equals(sObject))
				return i;
		}
		return -1;
	}
 
	@Override
	public void contentsChanged(ListDataEvent e) {
		Object oldSelection = selectedItemReminder;
		Object newSelection = showModel.getSelectedItem();
		if (oldSelection == null || !oldSelection.equals(newSelection)) {
			selectedItemChanged();
			if (!selectingItem) {
				fireActionEvent();
			}
		}
	}
 
	@Override
	protected void selectedItemChanged() {
		if (selectedItemReminder != null) {
			fireItemStateChanged(new ItemEvent(this,
					ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,
					ItemEvent.DESELECTED));
		}
 
		selectedItemReminder = showModel.getSelectedItem();
 
		if (selectedItemReminder != null) {
			fireItemStateChanged(new ItemEvent(this,
					ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,
					ItemEvent.SELECTED));
		}
	}
 
	@Override
	public void intervalAdded(ListDataEvent e) {
		if (selectedItemReminder != showModel.getSelectedItem()) {
			selectedItemChanged();
		}
	}
 
	@Override
	public void setEditable(boolean aFlag) {
		super.setEditable(true);
	}
 
	/**
	 * ������ʾ��ģ��
	 * @return
	 */
	public DefaultComboBoxModel getShowModel() {
		return showModel;
	}
	
	/**
	 * Metal L&F ���� UI ��
	 * @author Sun
	 *
	 */
	class MetalFilterComboBoxUI extends MetalComboBoxUI {
		
		/**
		 * �༭���¼�������
		 */
		protected EditorListener editorListener;
		/**
		 * �� UI �ฺ����ƵĿؼ�
		 */
		protected JFilterComboBox filterComboBox;
 
		@Override
		public void installUI(JComponent c) {
			filterComboBox = (JFilterComboBox) c;
			filterComboBox.setEditable(true);
			super.installUI(c);
		}
 
		@Override
		public void configureEditor() {
			super.configureEditor();
			editor.addKeyListener(getEditorListener());
			editor.addMouseListener(getEditorListener());
			editor.addFocusListener(getEditorListener());
		}
		
		@Override
		public void unconfigureEditor() {
			super.unconfigureEditor();
			if (editorListener != null) {
				editor.removeKeyListener(editorListener);
				editor.removeMouseListener(editorListener);
				editor.removeFocusListener(editorListener);
				editorListener = null;
			}
		}
		
		@Override
		protected ComboPopup createPopup() {
			return new FilterComboPopup(filterComboBox);
		}
		
		/**
		 * ��ʼ�������ر༭���¼�������
		 * @return
		 */
		protected EditorListener getEditorListener() {
			if (editorListener == null) {
				editorListener = new EditorListener();
			}
			return editorListener;
		}
 
		/**
		 * ���ؼ��ֽ��в�ѯ���÷����У��������м�����ֲ�ѯ�㷨
		 */
		protected void findMatchs() {
			ComboBoxModel model = filterComboBox.getModel();
			DefaultComboBoxModel showModel = filterComboBox.getShowModel();
			showModel.removeAllElements();
			for (int i = 0; i < model.getSize(); i++) {
				String name = model.getElementAt(i).toString();
				if (name.indexOf(getEditorText()) >= 0) {
					showModel.addElement(model.getElementAt(i));
				}
			}
			((FilterComboPopup)popup ).repaint();
		}
		
		/**
		 * ���ر༭���ı�
		 * @return
		 */
		private String getEditorText() {
			return filterComboBox.getEditor().getItem().toString();
		}
		
		/**
		 * ���������
		 * @author Sun
		 *
		 */
		class FilterComboPopup extends BasicComboPopup {
 
			public FilterComboPopup(JComboBox combo) {
				super(combo);
			}
			
			@Override
			protected JList createList() {
				JList list = super.createList();
				list.setModel(filterComboBox.getShowModel());
				return list;
			}
			
			@Override
			public void setVisible(boolean b) {
				super.setVisible(b);
				if (!b) {
					comboBox.getEditor().setItem(comboBox.getSelectedItem());
				}
			}
			
			@Override
			public void show() {
				findMatchs();
				super.show();
			}
 
		}
		
		/**
		 * �༭���¼���������
		 * @author Sun
		 *
		 */
		class EditorListener implements KeyListener,MouseListener, FocusListener {
			
			/**
			 * ���ı������ڼ�������ʱ�ıȶ�
			 */
			private String oldText = "";
			
			@Override
			public void keyReleased(KeyEvent e) {
				String newText = getEditorText();
				if (!newText.equals(oldText)) {
					findMatchs();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				oldText = getEditorText();
				if (!isPopupVisible(filterComboBox)) {
					setPopupVisible(filterComboBox, true);
				}
			}
 
			@Override
			public void keyTyped(KeyEvent e) {
				findMatchs();
			}
 
			@Override
			public void mouseClicked(MouseEvent e) {}
 
			@Override
			public void mousePressed(MouseEvent e) {
				if (!isPopupVisible(filterComboBox)) {
					setPopupVisible(filterComboBox, true);
				}
			}
 
			@Override
			public void mouseReleased(MouseEvent e) {}
 
			@Override
			public void mouseEntered(MouseEvent e) {}
 
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (!isPopupVisible(filterComboBox)) {
					setPopupVisible(filterComboBox, true);
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {}
 
		}
 
	}
	
//	/**
//	 * ʹ��ʾ��
//	 * @param args
//	 */
//	public static void main(String... args) {
//		Vector<String> data = new Vector<String>(0);
//		data.add("ab b");
//		data.add("ac v");
//		data.add("bb");
//		data.add("cc");
//		JPanel panel = new JPanel();
//		panel.add(new JFilterComboBox(data));
//		JFrame frame = new JFrame();
//		frame.setSize(400, 300);
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setContentPane(panel);
//		frame.setVisible(true);
//	}
 
}