import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class LotsOfPagesPanel extends JPanel {
    private CardLayout cardlayout = new CardLayout();
    private JPanel pageHolder = new JPanel(cardlayout);
    private DefaultComboBoxModel<String> nameComboModel = new DefaultComboBoxModel<String>();
    private JComboBox<String> nameCombo = new JComboBox<String>(nameComboModel);

    public LotsOfPagesPanel() {
        JPanel btnPanel = new JPanel(new GridLayout(1, 0, 5, 0));
        btnPanel.add(new JButton(new PrevAction(this, "Previous", KeyEvent.VK_P)));
        btnPanel.add(new JButton(new NextAction(this, "Next", KeyEvent.VK_N)));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnPanel);
        bottomPanel.add(nameCombo);

        nameCombo.addActionListener(new NameComboListener());
        pageHolder.setBorder(BorderFactory.createEtchedBorder());

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(5, 5));
        add(pageHolder, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public void previousPage() {
        cardlayout.previous(pageHolder);
    }

    public void nextPage() {
        cardlayout.next(pageHolder);
    }

    public void show(String name) {
        cardlayout.show(pageHolder, name);
    }

    public void registerPage(JComponent page, String name) {
        pageHolder.add(page, name);
        nameComboModel.addElement(name);
    }

    private class NameComboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selection = nameCombo.getSelectedItem().toString();
            show(selection);
        }
    }

    private class PrevAction extends AbstractAction {
        private LotsOfPagesPanel lotsOfPages;

        public PrevAction(LotsOfPagesPanel lotsOfPages, String name, Integer keyCode) {
            super(name);
            this.lotsOfPages = lotsOfPages;
            putValue(MNEMONIC_KEY, keyCode);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            lotsOfPages.previousPage();
        }
    }

    private class NextAction
            extends AbstractAction {
        private LotsOfPagesPanel lotsOfPages;

        public NextAction(LotsOfPagesPanel lotsOfPages, String name, Integer keyCode) {
            super(name);
            this.lotsOfPages = lotsOfPages;
            putValue(MNEMONIC_KEY, keyCode);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            lotsOfPages.nextPage();
        }
    }
}