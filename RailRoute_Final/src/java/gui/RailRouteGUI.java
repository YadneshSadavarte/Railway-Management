package gui;

import DS.core.COAProcessorEngine;
import DS.core.RouteManager;
import DS.data_structures.CancellationStack;
import DS.data_structures.TicketHashTable;
import DS.data_structures.TrainArray;
import DS.data_structures.TrainLinkedList;
import DS.data_structures.TrainRecord;
import DS.data_structures.WaitingListQueue;
import database.DatabaseManager;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import models.Bookable;
import models.Operator;
import models.Passenger;
import models.Person;
import models.Station;
import models.Ticket;
import models.Train;

public class RailRouteGUI {
    private static final String[] FALLBACK_FONT_NAMES = {"Segoe UI", "Segoe UI Symbol", "Segoe UI Emoji", "Arial Unicode MS", "SansSerif"};

    public static Font getFontForText(String text, int style, int size) {
        if (text == null || text.isEmpty()) {
            return new Font("Segoe UI", style, size);
        }
        String testStr = text.startsWith("<html>") ? text.replaceAll("<[^>]*>", "") : text;
        Font baseFont = new Font("Segoe UI", style, size);
        if (baseFont.canDisplayUpTo(testStr) == -1) {
            return baseFont;
        }
        for (String name : FALLBACK_FONT_NAMES) {
            Font candidate = new Font(name, style, size);
            if (candidate.canDisplayUpTo(testStr) == -1) {
                return candidate;
            }
        }
        return new Font("Segoe UI Symbol", style, size);
    }

    public static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel() {
            super();
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 48;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    public static class RoundedCardPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;
        private final Color accentColor;

        public RoundedCardPanel(int radius, Color bgColor, Color borderColor, Color accentColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            this.accentColor = accentColor;
            setOpaque(false);
        }

        public RoundedCardPanel(int radius, Color bgColor, Color borderColor) {
            this(radius, bgColor, borderColor, null);
        }

        public RoundedCardPanel(int radius, Color bgColor) {
            this(radius, bgColor, new Color(226, 232, 240), null);
        }

        public RoundedCardPanel() {
            this(16, Color.WHITE, new Color(226, 232, 240), null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            if (bgColor.equals(Color.WHITE)) {
                g2.setColor(new Color(15, 23, 42, 10));
                g2.fillRoundRect(1, 2, w - 2, h - 2, radius, radius);
            }

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);
            }

            if (accentColor != null) {
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, w - 1, 4, radius, radius);
                g2.fillRect(0, 2, w - 1, 2);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class ModernButton extends JButton {
        public enum Style {
            PRIMARY, SECONDARY, UTILITY, DANGER
        }

        private Color normalBg;
        private Color hoverBg;
        private Color pressedBg;
        private Color borderCol;
        private boolean isHovered = false;
        private boolean isPressed = false;
        private int cornerRadius = 12;

        public ModernButton(String text, Style style) {
            super(text);
            initStyle(style);
            setupDefaults();
            setFont(getFontForText(text, getFont().getStyle(), getFont().getSize()));
        }

        public ModernButton(String text, Style style, int height) {
            this(text, style);
            setPreferredSize(new Dimension(getPreferredSize().width, height));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        }

        public ModernButton(String text, Color bg, Color fg, Color hover, Color border, int radius) {
            super(text);
            this.normalBg = bg;
            this.hoverBg = hover;
            this.pressedBg = hover.darker();
            this.borderCol = border;
            this.cornerRadius = radius;
            setForeground(fg);
            setFont(getFontForText(text, Font.BOLD, 12));
            setupDefaults();
        }

        @Override
        public void setText(String text) {
            super.setText(text);
            if (getFont() != null) {
                setFont(getFontForText(text, getFont().getStyle(), getFont().getSize()));
            }
        }

        private void initStyle(Style style) {
            switch (style) {
                case PRIMARY:
                    normalBg = new Color(242, 143, 59);
                    hoverBg = new Color(224, 122, 38);
                    pressedBg = new Color(197, 104, 27);
                    borderCol = null;
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    break;
                case SECONDARY:
                    normalBg = new Color(18, 25, 115);
                    hoverBg = new Color(30, 42, 145);
                    pressedBg = new Color(12, 16, 101);
                    borderCol = null;
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    break;
                case UTILITY:
                    normalBg = new Color(238, 242, 255);
                    hoverBg = new Color(224, 231, 255);
                    pressedBg = new Color(199, 210, 254);
                    borderCol = new Color(199, 210, 254);
                    setForeground(new Color(12, 16, 101));
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    break;
                case DANGER:
                    normalBg = new Color(220, 38, 38);
                    hoverBg = new Color(185, 28, 28);
                    pressedBg = new Color(153, 27, 27);
                    borderCol = null;
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    break;
            }
        }

        private void setupDefaults() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        public void setCustomColors(Color normal, Color hover, Color fg) {
            this.normalBg = normal;
            this.hoverBg = hover;
            this.pressedBg = hover.darker();
            setForeground(fg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            Color bg = isPressed ? pressedBg : (isHovered ? hoverBg : normalBg);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

            if (borderCol != null) {
                g2.setColor(borderCol);
                g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class StyledTextField extends JTextField {
        private boolean focused = false;
        private final int radius = 10;

        public StyledTextField() {
            this("");
        }

        public StyledTextField(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setForeground(new Color(15, 23, 42));
            setBackground(Color.WHITE);
            setCaretColor(new Color(12, 16, 101));
            setMargin(new Insets(5, 10, 5, 10));
            setOpaque(false);
            setPreferredSize(new Dimension(getPreferredSize().width, 32));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    focused = true;
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(focused ? new Color(242, 143, 59) : new Color(203, 213, 225));
            g2.setStroke(new BasicStroke(focused ? 1.5f : 1.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    public static class DarkScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(51, 65, 85);
            this.trackColor = new Color(15, 23, 42);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(71, 85, 105));
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(new Color(15, 23, 42));
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    public static class TrainLogoIcon extends JComponent {
        public TrainLogoIcon() {
            setPreferredSize(new Dimension(46, 46));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(242, 143, 59));
            g2.fillRoundRect(0, 0, 44, 44, 14, 14);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(8, 7, 28, 24, 8, 8);
            g2.setColor(new Color(12, 16, 101));
            g2.fillRoundRect(12, 11, 20, 10, 4, 4);
            g2.setColor(new Color(242, 143, 59));
            g2.fillOval(12, 24, 5, 5);
            g2.fillOval(27, 24, 5, 5);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawLine(10, 36, 34, 36);
            g2.drawLine(14, 39, 30, 39);
            g2.dispose();
        }
    }

    private final JFrame frame;
    private final List<Train> trainList = new ArrayList<>();
    private final List<Person> personList = new ArrayList<>();
    private final Passenger[] passengerArray = new Passenger[100];
    private int passengerIndex = 0;
    private int confirmedTicketCount = 2;

    private final TrainArray trainArray = new TrainArray();
    private final TrainLinkedList trainLinkedList = new TrainLinkedList();
    private final WaitingListQueue waitingListQueue = new WaitingListQueue();
    private final CancellationStack cancellationStack = new CancellationStack();
    private final TicketHashTable ticketHashTable = new TicketHashTable();
    private final RouteManager routeManager = new RouteManager();

    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private ModernButton navBtn1;
    private ModernButton navBtn2;
    private ModernButton navBtn3;
    private ModernButton navBtn4;
    private ModernButton[] navBtns;

    private JPanel outputOverlayPanel;
    private RoundedCardPanel popupCard;
    private JTextArea popupOutputArea;
    private ModernButton btnOutputToggle;
    private boolean hasNewOutput = false;

    private JLabel lblActionStatus1;
    private JLabel lblActionStatus2;
    private JLabel lblActionStatus3;
    private JLabel lblActionStatus4;

    private JComboBox<String> cmbFromStation;
    private JComboBox<String> cmbToStation;
    private JComboBox<String> cmbTravelClass;
    private JComboBox<String> cmbTrains;
    private RouteManager.SegmentTrain[] currentSegmentTrains = new RouteManager.SegmentTrain[0];

    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtPhone;
    private JRadioButton rbMale;
    private JRadioButton rbFemale;
    private JTextField txtOpName;
    private JTextField txtOpPhone;
    private JTextField txtOpDept;
    private JTextField txtCancelTicketId;

    private JLabel lblKpiStations;
    private JLabel lblKpiTrains;
    private JLabel lblKpiTickets;
    private JLabel lblKpiWaitlist;

    private JTextField txtTId;
    private JTextField txtTName;
    private JTextField txtTSrc;
    private JTextField txtTDst;
    private JTextField txtTSeats;
    private JTextField txtTFare;
    private JComboBox<String> cmbFrom;
    private JComboBox<String> cmbTo;
    private JComboBox<String> cmbClass;
    private JLabel lblCompShortest;
    private JLabel lblCompLongest;
    private JLabel lblCompSavings;

    private JComboBox<String> cmbTab3From;
    private JComboBox<String> cmbTab3To;
    private JComboBox<String> cmbTab3Class;
    private JRadioButton rbTab3Shortest;
    private JRadioButton rbTab3Longest;
    private static Process activeVisualizerProcess = null;

    private JComboBox<String> cmbTab4From;
    private JComboBox<String> cmbTab4To;
    private JLabel lblRegRax;
    private JLabel lblRegRbx;
    private JLabel lblRegFlags;
    private JLabel lblRegR8;

    public RailRouteGUI() {
        frame = new JFrame("RailRoute Express - Maharashtra Railway Reservation, Route Analytics & Live Telemetry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setMinimumSize(new Dimension(1120, 720));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initHeader();
        initWorkspace();
        initOutputPopupOverlay();

        loadInitialData();

        frame.setVisible(true);
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(12, 16, 101), getWidth(), 0, new Color(22, 29, 122));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(242, 143, 59));
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(1000, 82));
        headerPanel.setLayout(new BorderLayout(10, 0));
        headerPanel.setBorder(new EmptyBorder(10, 16, 12, 16));

        JPanel brandPanel = new JPanel(new BorderLayout(10, 0));
        brandPanel.setOpaque(false);

        TrainLogoIcon logoIcon = new TrainLogoIcon();
        brandPanel.add(logoIcon, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("RAILROUTE EXPRESS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        textPanel.add(lblTitle);

        JLabel lblSub = new JLabel("<html><body style='margin:0;padding:0;'>MAHARASHTRA CORRIDOR &bull; RESERVATION,<br>ROUTE ANALYTICS &amp; LIVE TELEMETRY</body></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setForeground(new Color(199, 210, 254));
        textPanel.add(lblSub);

        brandPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(brandPanel, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new GridLayout(1, 4, 6, 0));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

        Color navBg = new Color(255, 255, 255, 25);
        Color navFg = new Color(226, 232, 240);
        Color navHov = new Color(255, 255, 255, 50);

        navBtn1 = new ModernButton("1. E-Ticket Reservation", ModernButton.Style.PRIMARY);
        navBtn2 = new ModernButton("2. Trains & Route Comparator", navBg, navFg, navHov, null, 12);
        navBtn3 = new ModernButton("3. 2D Live Map Visualizer", navBg, navFg, navHov, null, 12);
        navBtn4 = new ModernButton("4. COA Processor & Energy", navBg, navFg, navHov, null, 12);

        navBtns = new ModernButton[]{navBtn1, navBtn2, navBtn3, navBtn4};

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            navBtns[i].setFont(new Font("Segoe UI", Font.BOLD, 11));
            navBtns[i].addActionListener(e -> selectTab(idx));
            navPanel.add(navBtns[i]);
        }

        headerPanel.add(navPanel, BorderLayout.CENTER);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
        rightActions.setOpaque(false);

        btnOutputToggle = new ModernButton("\u25A3  Output Console: OFF", new Color(255, 255, 255, 25), new Color(226, 232, 240), new Color(255, 255, 255, 50), null, 14);
        btnOutputToggle.setPreferredSize(new Dimension(185, 36));
        btnOutputToggle.setFont(getFontForText(btnOutputToggle.getText(), Font.BOLD, 11));
        btnOutputToggle.addActionListener(e -> setOutputPopupVisible(!outputOverlayPanel.isVisible()));
        rightActions.add(btnOutputToggle);

        RoundedCardPanel statusPill = new RoundedCardPanel(16, new Color(255, 255, 255, 20), new Color(255, 255, 255, 40));
        statusPill.setLayout(new BorderLayout());
        statusPill.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel lblStatus = new JLabel("<html><center style='margin:0;padding:0;line-height:95%;'>\u25CF  SQLITE ONLINE<br>32 MH STATIONS<br>40 REAL TRAINS</center></html>");
        lblStatus.setFont(getFontForText(lblStatus.getText(), Font.BOLD, 9));
        lblStatus.setForeground(new Color(52, 211, 153));
        statusPill.add(lblStatus, BorderLayout.CENTER);
        rightActions.add(statusPill);

        headerPanel.add(rightActions, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private void selectTab(int index) {
        for (int i = 0; i < navBtns.length; i++) {
            if (i == index) {
                navBtns[i].setCustomColors(new Color(242, 143, 59), new Color(224, 122, 38), Color.WHITE);
            } else {
                navBtns[i].setCustomColors(new Color(255, 255, 255, 25), new Color(255, 255, 255, 50), new Color(226, 232, 240));
            }
        }
        cardLayout.show(cardsPanel, "PAGE_" + index);
    }

    private void initWorkspace() {
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(new Color(241, 245, 248));

        cardsPanel.add(createTab1(), "PAGE_0");
        cardsPanel.add(createTab2(), "PAGE_1");
        cardsPanel.add(createTab3(), "PAGE_2");
        cardsPanel.add(createTab4(), "PAGE_3");

        frame.add(cardsPanel, BorderLayout.CENTER);
    }

    private JScrollPane createColumnScrollPane(JPanel panel) {
        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private JPanel createActionStatusBar(JLabel lblStatus, int pageIndex) {
        RoundedCardPanel card = new RoundedCardPanel(14, Color.WHITE, new Color(226, 232, 240), new Color(18, 25, 115));
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(10, 16, 10, 14));

        JPanel textPanel = new JPanel(new BorderLayout(0, 2));
        textPanel.setOpaque(false);

        JLabel lblHeader = new JLabel("LAST ACTION STATUS / EXECUTION FEEDBACK");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblHeader.setForeground(new Color(100, 116, 139));
        textPanel.add(lblHeader, BorderLayout.NORTH);

        lblStatus.setFont(getFontForText(lblStatus.getText(), Font.BOLD, 12));
        lblStatus.setForeground(new Color(12, 16, 101));
        textPanel.add(lblStatus, BorderLayout.CENTER);

        card.add(textPanel, BorderLayout.CENTER);

        ModernButton btnOpen = new ModernButton("\u25A3 Open Output Popup", ModernButton.Style.SECONDARY);
        btnOpen.setPreferredSize(new Dimension(175, 34));
        btnOpen.setFont(getFontForText(btnOpen.getText(), Font.BOLD, 11));
        btnOpen.addActionListener(e -> setOutputPopupVisible(true));
        card.add(btnOpen, BorderLayout.EAST);

        return card;
    }

    private JPanel createTab1() {
        JPanel leftCol = createColumnPanel();

        RoundedCardPanel card1 = createSectionCard("Book New Railway E-Ticket (Station-First Booking)", "Select stations and class to filter scheduled operational train services", new Color(242, 143, 59));

        cmbFromStation = new JComboBox<>();
        cmbToStation = new JComboBox<>();
        styleComboBox(cmbFromStation);
        styleComboBox(cmbToStation);
        card1.add(createTwoColRow("Origin Station (From)", cmbFromStation, "Destination Station (To)", cmbToStation));
        card1.add(Box.createVerticalStrut(10));

        cmbTravelClass = new JComboBox<>(new String[]{"SL (Sleeper)", "CC (AC Chair Car)", "3A (AC 3-Tier)"});
        styleComboBox(cmbTravelClass);
        card1.add(createFormRow("Pricing & Accommodation Travel Class", cmbTravelClass));
        card1.add(Box.createVerticalStrut(10));

        cmbTrains = new JComboBox<>();
        styleComboBox(cmbTrains);
        card1.add(createFormRow("Available Scheduled Trains (Filtered for Corridor)", cmbTrains));
        card1.add(Box.createVerticalStrut(12));

        cmbFromStation.addActionListener(e -> updateAvailableTrainsForBooking());
        cmbToStation.addActionListener(e -> updateAvailableTrainsForBooking());
        cmbTravelClass.addActionListener(e -> updateAvailableTrainsForBooking());

        txtName = new StyledTextField();
        txtAge = new StyledTextField();
        card1.add(createTwoColRow("Passenger Full Name", txtName, "Age (1-120)", txtAge));
        card1.add(Box.createVerticalStrut(10));

        rbMale = new JRadioButton("Male", true);
        rbFemale = new JRadioButton("Female");
        styleRadioButton(rbMale);
        styleRadioButton(rbFemale);
        ButtonGroup bgGender = new ButtonGroup();
        bgGender.add(rbMale);
        bgGender.add(rbFemale);

        JPanel genderPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        genderPanel.setOpaque(false);
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);

        txtPhone = new StyledTextField();
        card1.add(createTwoColRow("Gender Selection", genderPanel, "10-Digit Mobile Number", txtPhone));
        card1.add(Box.createVerticalStrut(14));

        ModernButton btnBook = new ModernButton("\u2714  Confirm & Book E-Ticket", ModernButton.Style.PRIMARY, 38);
        btnBook.addActionListener(e -> bookTicket());
        card1.add(btnBook);

        addSeparator(card1);

        JLabel lblCancelHeader = new JLabel("Ticket Cancellation (CO4 LIFO Stack & Waitlist Promotion)");
        lblCancelHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCancelHeader.setForeground(new Color(12, 16, 101));
        card1.add(lblCancelHeader);
        card1.add(Box.createVerticalStrut(8));

        JPanel cancelRow = new JPanel(new BorderLayout(8, 0));
        cancelRow.setOpaque(false);
        txtCancelTicketId = new StyledTextField();
        txtCancelTicketId.setPreferredSize(new Dimension(160, 34));
        ModernButton btnCancel = new ModernButton("\u2716  Cancel Ticket by PNR/ID", ModernButton.Style.DANGER);
        btnCancel.setPreferredSize(new Dimension(220, 34));
        btnCancel.addActionListener(e -> cancelTicket());
        cancelRow.add(txtCancelTicketId, BorderLayout.CENTER);
        cancelRow.add(btnCancel, BorderLayout.EAST);
        card1.add(cancelRow);

        leftCol.add(card1);
        leftCol.add(Box.createVerticalStrut(16));

        lblActionStatus1 = new JLabel("System Ready \u2022 All 32 Stations & 40 Scheduled Trains Synchronized");
        leftCol.add(createActionStatusBar(lblActionStatus1, 0));

        JPanel rightCol = createColumnPanel();

        JPanel kpiRibbon = new JPanel(new GridLayout(1, 4, 10, 0));
        kpiRibbon.setOpaque(false);
        kpiRibbon.setPreferredSize(new Dimension(kpiRibbon.getPreferredSize().width, 74));
        kpiRibbon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

        lblKpiStations = new JLabel("32 Nodes");
        lblKpiTrains = new JLabel("2 Operational");
        lblKpiTickets = new JLabel("2 Confirmed");
        lblKpiWaitlist = new JLabel("0 Waiting");

        kpiRibbon.add(createKpiCard("32 MH Stations", lblKpiStations, "SQLITE", new Color(18, 25, 115)));
        kpiRibbon.add(createKpiCard("Matching Trains", lblKpiTrains, "CORRIDOR", new Color(12, 16, 101)));
        kpiRibbon.add(createKpiCard("Confirmed Tickets", lblKpiTickets, "RESERVED", new Color(242, 143, 59)));
        kpiRibbon.add(createKpiCard("Waitlist Queue", lblKpiWaitlist, "CIRCULAR", new Color(220, 38, 38)));

        rightCol.add(kpiRibbon);
        rightCol.add(Box.createVerticalStrut(16));

        RoundedCardPanel card2 = createSectionCard("Railway Staff & Operator Registration", "Personnel hierarchy, role management & polymorphic entity modeling", new Color(18, 25, 115));

        txtOpName = new StyledTextField();
        txtOpPhone = new StyledTextField();
        txtOpDept = new StyledTextField();

        JPanel opRow = new JPanel(new GridLayout(1, 3, 8, 0));
        opRow.setOpaque(false);
        opRow.add(createFormRow("Operator Name", txtOpName));
        opRow.add(createFormRow("Contact Phone", txtOpPhone));
        opRow.add(createFormRow("Department", txtOpDept));
        card2.add(opRow);
        card2.add(Box.createVerticalStrut(12));

        ModernButton btnAddOp = new ModernButton("+ Register Railway Operator", ModernButton.Style.SECONDARY, 34);
        btnAddOp.addActionListener(e -> addOperator());
        card2.add(btnAddOp);

        rightCol.add(card2);
        rightCol.add(Box.createVerticalStrut(16));

        RoundedCardPanel card3 = createSectionCard("Object-Oriented Architecture & Lambda Filter Studio", "Runtime polymorphism, interface abstraction & Predicate<T> filtering", null);

        JPanel oopGrid = new JPanel(new GridLayout(3, 2, 10, 10));
        oopGrid.setOpaque(false);

        ModernButton btnDemo = new ModernButton("Run Polymorphism & Entity Demo", ModernButton.Style.UTILITY);
        btnDemo.addActionListener(e -> runPolymorphismDemo());
        oopGrid.add(btnDemo);

        ModernButton btnFilterPassengers = new ModernButton("Filter: Only Passengers (Lambda)", ModernButton.Style.UTILITY);
        btnFilterPassengers.addActionListener(e -> filterByPassengers());
        oopGrid.add(btnFilterPassengers);

        ModernButton btnFilterOperators = new ModernButton("Filter: Only Operators (Lambda)", ModernButton.Style.UTILITY);
        btnFilterOperators.addActionListener(e -> filterByOperators());
        oopGrid.add(btnFilterOperators);

        ModernButton btnFilterPhone = new ModernButton("Filter: Phone Starts With '98'", ModernButton.Style.UTILITY);
        btnFilterPhone.addActionListener(e -> filterByPhonePrefix());
        oopGrid.add(btnFilterPhone);

        ModernButton btnDbStatus = new ModernButton("Inspect SQLite Database Status", ModernButton.Style.SECONDARY);
        btnDbStatus.addActionListener(e -> showDatabaseStatus());
        oopGrid.add(btnDbStatus);

        ModernButton btnClearTab1 = new ModernButton("Clear Input Forms", ModernButton.Style.UTILITY);
        btnClearTab1.addActionListener(e -> {
            txtName.setText("");
            txtAge.setText("");
            txtPhone.setText("");
            txtOpName.setText("");
            txtOpPhone.setText("");
            txtOpDept.setText("");
            txtCancelTicketId.setText("");
            updateActionStatus("Input fields cleared");
        });
        oopGrid.add(btnClearTab1);

        card3.add(oopGrid);
        rightCol.add(card3);

        return createTabPage(leftCol, rightCol);
    }

    private void updateAvailableTrainsForBooking() {
        if (cmbFromStation == null || cmbToStation == null || cmbTravelClass == null || cmbTrains == null) {
            return;
        }
        String from = (String) cmbFromStation.getSelectedItem();
        String to = (String) cmbToStation.getSelectedItem();
        String travelClass = (String) cmbTravelClass.getSelectedItem();

        RouteManager.SegmentTrain[] segTrains = routeManager.findTrainsForSegment(from, to, travelClass);
        currentSegmentTrains = segTrains;

        cmbTrains.removeAllItems();
        if (segTrains != null && segTrains.length > 0) {
            for (RouteManager.SegmentTrain st : segTrains) {
                cmbTrains.addItem(st.displayLabel);
            }
            if (lblKpiTrains != null) {
                lblKpiTrains.setText(segTrains.length + " Operational");
            }
        } else {
            cmbTrains.addItem("No direct scheduled train on this corridor (Check Tab 2 for connecting paths)");
            if (lblKpiTrains != null) {
                lblKpiTrains.setText("0 Direct Trains");
            }
        }
    }

    private JPanel createTab2() {
        JPanel leftCol = createColumnPanel();

        RoundedCardPanel card1 = createSectionCard("Shortest vs. Longest Route & Fare Comparator", "Evaluates real multi-corridor passenger train timetables across 32 Maharashtra junctions", new Color(242, 143, 59));

        cmbFrom = new JComboBox<>();
        cmbTo = new JComboBox<>();
        styleComboBox(cmbFrom);
        styleComboBox(cmbTo);
        card1.add(createTwoColRow("Origin Station (From)", cmbFrom, "Destination Station (To)", cmbTo));
        card1.add(Box.createVerticalStrut(10));

        cmbClass = new JComboBox<>(new String[]{"SL (Sleeper)", "CC (AC Chair Car)", "3A (AC 3-Tier)"});
        styleComboBox(cmbClass);
        card1.add(createFormRow("Pricing & Accommodation Travel Class", cmbClass));
        card1.add(Box.createVerticalStrut(12));

        JPanel presetRow = new JPanel(new GridLayout(1, 3, 8, 0));
        presetRow.setOpaque(false);

        ModernButton btnPreset1 = new ModernButton("PUNE -> AMI (2 Routes)", ModernButton.Style.UTILITY);
        btnPreset1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnPreset1.addActionListener(e -> {
            cmbFrom.setSelectedIndex(13);
            cmbTo.setSelectedIndex(4);
            compareRoutesDS();
        });
        presetRow.add(btnPreset1);

        ModernButton btnPreset2 = new ModernButton("CSMT -> NGP (3 Routes)", ModernButton.Style.UTILITY);
        btnPreset2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnPreset2.addActionListener(e -> {
            cmbFrom.setSelectedIndex(12);
            cmbTo.setSelectedIndex(5);
            compareRoutesDS();
        });
        presetRow.add(btnPreset2);

        ModernButton btnPreset3 = new ModernButton("CSMT -> KUDL (1 Route)", ModernButton.Style.UTILITY);
        btnPreset3.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnPreset3.addActionListener(e -> {
            cmbFrom.setSelectedIndex(12);
            cmbTo.setSelectedIndex(31);
            compareRoutesDS();
        });
        presetRow.add(btnPreset3);

        card1.add(presetRow);
        card1.add(Box.createVerticalStrut(14));

        ModernButton btnCompareRoutes = new ModernButton("\u21C4  Compare Operational Routes & Fares", ModernButton.Style.PRIMARY, 38);
        btnCompareRoutes.addActionListener(e -> compareRoutesDS());
        card1.add(btnCompareRoutes);

        leftCol.add(card1);
        leftCol.add(Box.createVerticalStrut(16));

        JPanel summaryBanner = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryBanner.setOpaque(false);
        summaryBanner.setPreferredSize(new Dimension(summaryBanner.getPreferredSize().width, 74));
        summaryBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

        lblCompShortest = new JLabel("719 km  |  Rs. 1334.20");
        lblCompLongest = new JLabel("832 km  |  Rs. 1537.60");
        lblCompSavings = new JLabel("-113 km  |  Save Rs. 203.40");

        summaryBanner.add(createKpiCard("Shortest Route", lblCompShortest, "OPTIMAL", new Color(18, 25, 115)));
        summaryBanner.add(createKpiCard("Longest Route", lblCompLongest, "SCENIC", new Color(100, 116, 139)));
        summaryBanner.add(createKpiCard("Net Savings", lblCompSavings, "SAVED", new Color(242, 143, 59)));

        leftCol.add(summaryBanner);
        leftCol.add(Box.createVerticalStrut(16));

        lblActionStatus2 = new JLabel("Route Comparator Ready \u2022 Select Origin & Destination to Compare");
        leftCol.add(createActionStatusBar(lblActionStatus2, 1));

        JPanel rightCol = createColumnPanel();

        RoundedCardPanel card2 = createSectionCard("Dynamic Train Inventory Management (CO2 Array & CO3 Linked List)", "Real-time inventory mutation with dual-layer array and linked list syncing", new Color(18, 25, 115));

        txtTId = new StyledTextField("TRN141");
        txtTName = new StyledTextField("Pune Solapur Superfast");
        card2.add(createTwoColRow("Train ID", txtTId, "Train Service Name", txtTName));
        card2.add(Box.createVerticalStrut(10));

        txtTSrc = new StyledTextField("MH-014");
        txtTDst = new StyledTextField("MH-027");
        card2.add(createTwoColRow("Source Station ID", txtTSrc, "Destination Station ID", txtTDst));
        card2.add(Box.createVerticalStrut(10));

        txtTSeats = new StyledTextField("5");
        txtTFare = new StyledTextField("1.80");
        card2.add(createTwoColRow("Total Passenger Seats", txtTSeats, "Base Rate (Rs./km)", txtTFare));
        card2.add(Box.createVerticalStrut(14));

        JPanel crudRow = new JPanel(new GridLayout(1, 4, 8, 0));
        crudRow.setOpaque(false);
        crudRow.setPreferredSize(new Dimension(crudRow.getPreferredSize().width, 36));
        crudRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        ModernButton btnAddTrain = new ModernButton("Add Train", ModernButton.Style.SECONDARY);
        btnAddTrain.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAddTrain.addActionListener(e -> addTrainDS());
        crudRow.add(btnAddTrain);

        ModernButton btnUpdateTrain = new ModernButton("Update Train", ModernButton.Style.SECONDARY);
        btnUpdateTrain.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUpdateTrain.addActionListener(e -> updateTrainDS());
        crudRow.add(btnUpdateTrain);

        ModernButton btnDeleteTrain = new ModernButton("Delete Train", ModernButton.Style.DANGER);
        btnDeleteTrain.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDeleteTrain.addActionListener(e -> deleteTrainDS());
        crudRow.add(btnDeleteTrain);

        ModernButton btnBinarySearch = new ModernButton("Binary Search", ModernButton.Style.UTILITY);
        btnBinarySearch.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBinarySearch.addActionListener(e -> binarySearchDS());
        crudRow.add(btnBinarySearch);

        card2.add(crudRow);
        rightCol.add(card2);
        rightCol.add(Box.createVerticalStrut(16));

        RoundedCardPanel card3 = createSectionCard("Data Structures & Algorithm Verification (CO2 - CO6)", "Interactive execution of custom syllabus data structures", null);

        JPanel dsGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        dsGrid.setOpaque(false);

        ModernButton btnDisplayLL = new ModernButton("Linked List (CO3)", ModernButton.Style.UTILITY);
        btnDisplayLL.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDisplayLL.addActionListener(e -> displayLinkedListDS());
        dsGrid.add(btnDisplayLL);

        ModernButton btnShowArray = new ModernButton("1D Array (CO2)", ModernButton.Style.UTILITY);
        btnShowArray.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnShowArray.addActionListener(e -> show1DArrayDS());
        dsGrid.add(btnShowArray);

        ModernButton btnQuickSort = new ModernButton("QuickSort Fares (CO5)", ModernButton.Style.UTILITY);
        btnQuickSort.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnQuickSort.addActionListener(e -> quickSortDS());
        dsGrid.add(btnQuickSort);

        ModernButton btnAdjMatrix = new ModernButton("Adjacency Matrix (CO2)", ModernButton.Style.UTILITY);
        btnAdjMatrix.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdjMatrix.addActionListener(e -> showAdjacencyMatrix());
        dsGrid.add(btnAdjMatrix);

        ModernButton btnQueueStack = new ModernButton("Queue & Stack (CO4)", ModernButton.Style.UTILITY);
        btnQueueStack.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnQueueStack.addActionListener(e -> showQueueAndStack());
        dsGrid.add(btnQueueStack);

        ModernButton btnBoundaryTest = new ModernButton("<html><center style='margin:0;padding:0;'>Test CO3 Stack/Queue<br>Boundary Cases</center></html>", ModernButton.Style.UTILITY);
        btnBoundaryTest.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBoundaryTest.addActionListener(e -> testStackQueueBoundaryCases());
        dsGrid.add(btnBoundaryTest);

        ModernButton btnHashLookup = new ModernButton("Hash Table (CO6)", ModernButton.Style.UTILITY);
        btnHashLookup.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnHashLookup.addActionListener(e -> showHashTableLookup());
        dsGrid.add(btnHashLookup);

        card3.add(dsGrid);
        rightCol.add(card3);

        return createTabPage(leftCol, rightCol);
    }

    private JPanel createTab3() {
        JPanel leftCol = createColumnPanel();

        RoundedCardPanel card1 = createSectionCard("Maharashtra 32-Station 2D OpenGL Visualizer", "Hardware-accelerated FreeGLUT engine with Cohen-Sutherland line clipping", new Color(242, 143, 59));

        cmbTab3From = new JComboBox<>();
        cmbTab3To = new JComboBox<>();
        styleComboBox(cmbTab3From);
        styleComboBox(cmbTab3To);
        card1.add(createTwoColRow("Origin Station", cmbTab3From, "Destination Station", cmbTab3To));
        card1.add(Box.createVerticalStrut(10));

        cmbTab3Class = new JComboBox<>(new String[]{"SL (Sleeper)", "CC (AC Chair Car)", "3A (AC 3-Tier)"});
        styleComboBox(cmbTab3Class);
        card1.add(createFormRow("Travel Class Multiplier", cmbTab3Class));
        card1.add(Box.createVerticalStrut(12));

        rbTab3Shortest = new JRadioButton("Animate Shortest Operational Route", true);
        rbTab3Longest = new JRadioButton("Animate Longest Operational Route");
        styleRadioButton(rbTab3Shortest);
        styleRadioButton(rbTab3Longest);
        ButtonGroup bgTab3 = new ButtonGroup();
        bgTab3.add(rbTab3Shortest);
        bgTab3.add(rbTab3Longest);

        JPanel togglePanel = new JPanel(new GridLayout(1, 2, 8, 0));
        togglePanel.setOpaque(false);
        togglePanel.add(rbTab3Shortest);
        togglePanel.add(rbTab3Longest);
        card1.add(togglePanel);
        card1.add(Box.createVerticalStrut(16));

        ModernButton btnSyncRoute = new ModernButton("\u21BB  1. Sync Route Coordinates to OpenGL (active_route.txt)", ModernButton.Style.SECONDARY, 36);
        btnSyncRoute.addActionListener(e -> syncRouteToOpenGL());
        card1.add(btnSyncRoute);
        card1.add(Box.createVerticalStrut(10));

        ModernButton btnLaunchOpenGL = new ModernButton("\u25B6  2. Launch Full 2D OpenGL Maharashtra Railway Visualizer", ModernButton.Style.PRIMARY, 38);
        btnLaunchOpenGL.addActionListener(e -> launchOpenGLVisualizer());
        card1.add(btnLaunchOpenGL);

        leftCol.add(card1);
        leftCol.add(Box.createVerticalStrut(16));

        lblActionStatus3 = new JLabel("OpenGL Engine Ready \u2022 Select Route & Launch FreeGLUT Window");
        leftCol.add(createActionStatusBar(lblActionStatus3, 2));

        JPanel rightCol = createColumnPanel();

        JPanel statusBanner = new JPanel(new GridLayout(1, 3, 10, 0));
        statusBanner.setOpaque(false);
        statusBanner.setPreferredSize(new Dimension(statusBanner.getPreferredSize().width, 74));
        statusBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

        JLabel lblEng = new JLabel("FreeGLUT 60 FPS Engine");
        JLabel lblClip = new JLabel("Active Line Clipping");
        JLabel lblShad = new JLabel("RGB <-> HSV Pulsing");

        statusBanner.add(createKpiCard("Rendering Engine", lblEng, "ACTIVE", new Color(18, 25, 115)));
        statusBanner.add(createKpiCard("Cohen-Sutherland", lblClip, "CO5", new Color(242, 143, 59)));
        statusBanner.add(createKpiCard("Dynamic Shading", lblShad, "CO6", new Color(16, 185, 129)));

        rightCol.add(statusBanner);
        rightCol.add(Box.createVerticalStrut(16));

        RoundedCardPanel card2 = createSectionCard("OpenGL Interactive Viewport & Keyboard Controls", "Real-time keyboard event hooks handled by glutKeyboardFunc & glutSpecialFunc", null);

        JPanel keyTable = new JPanel();
        keyTable.setLayout(new BoxLayout(keyTable, BoxLayout.Y_AXIS));
        keyTable.setOpaque(false);

        keyTable.add(createKeycapRow("W / A / S / D  |  Arrows", "Pan 2D Viewport Orthographic Window (glTranslatef)"));
        keyTableStrut(keyTable);
        keyTable.add(createKeycapRow("+  /  -", "Zoom In / Zoom Out Continuous Scale (0.5x to 4.5x)"));
        keyTableStrut(keyTable);
        keyTable.add(createKeycapRow("SPACEBAR", "Pause / Resume Live 2D Vande Bharat Train Animation"));
        keyTableStrut(keyTable);
        keyTable.add(createKeycapRow("1  /  2", "Instant Toggle Between Shortest & Longest Scenic Route"));
        keyTableStrut(keyTable);
        keyTable.add(createKeycapRow("R", "Reset Viewport Transformation Matrix & Restart Train at Origin"));
        keyTableStrut(keyTable);
        keyTable.add(createKeycapRow("ESC", "Exit OpenGL Visualizer Window & Return to Main Console"));

        card2.add(keyTable);
        card2.add(Box.createVerticalStrut(14));

        card2.add(createCourseOutcomesBox("COMPUTER GRAPHICS SYLLABUS MAPPING (CO1 - CO6):", new String[]{
            "CO1 & CO3: Double-Rail Sleeper Tracks & Geometric Primitives",
            "CO2 & CO4: 2D Matrix Transforms & Window-to-Viewport Math",
            "CO5: Cohen-Sutherland Bitwise Line Clipping (Outcodes)",
            "CO6: RGB <-> HSV Pulsating Shading & Radial Station Glow"
        }));

        rightCol.add(card2);

        return createTabPage(leftCol, rightCol);
    }

    private JPanel createTab4() {
        JPanel leftCol = createColumnPanel();

        RoundedCardPanel card1 = createSectionCard("Live 64-Bit ALU Route & Operational Energy Analyzer", "Cycle-accurate register modeling and native Win64 quadword execution", new Color(242, 143, 59));

        cmbTab4From = new JComboBox<>();
        cmbTab4To = new JComboBox<>();
        styleComboBox(cmbTab4From);
        styleComboBox(cmbTab4To);
        card1.add(createTwoColRow("Origin Station", cmbTab4From, "Destination Station", cmbTab4To));
        card1.add(Box.createVerticalStrut(16));

        ModernButton btnRun64Bit = new ModernButton("\u25B6  1. Run 64-Bit Live Route & IMUL Energy Trace", ModernButton.Style.PRIMARY, 38);
        btnRun64Bit.addActionListener(e -> run64BitLiveTrace());
        card1.add(btnRun64Bit);
        card1.add(Box.createVerticalStrut(10));

        ModernButton btnRunCo3Trace = new ModernButton("\u25B6  Run CO3: Add-and-Shift + 64-Bit String Trace", ModernButton.Style.SECONDARY, 36);
        btnRunCo3Trace.addActionListener(e -> runCo3AddShiftStringTrace());
        card1.add(btnRunCo3Trace);
        card1.add(Box.createVerticalStrut(10));

        ModernButton btnRun16Bit = new ModernButton("\u2699  2. Run 16-Bit 8086 Processor Fetch-Decode-Execute Trace", ModernButton.Style.SECONDARY, 36);
        btnRun16Bit.addActionListener(e -> run16BitTask1Trace());
        card1.add(btnRun16Bit);
        card1.add(Box.createVerticalStrut(10));

        ModernButton btnRunAsmExe = new ModernButton("\u25B6  3. Execute Native 64-Bit Assembly Binary (distance_calc.exe)", ModernButton.Style.SECONDARY, 36);
        btnRunAsmExe.addActionListener(e -> executeNativeAssemblyExe());
        card1.add(btnRunAsmExe);
        card1.add(Box.createVerticalStrut(10));

        ModernButton btnViewAsm = new ModernButton("\uD83D\uDCC4  4. Inspect Raw Assembly Source Files (.asm)", ModernButton.Style.UTILITY, 36);
        btnViewAsm.addActionListener(e -> viewAssemblySourceFiles());
        card1.add(btnViewAsm);

        leftCol.add(card1);
        leftCol.add(Box.createVerticalStrut(16));

        lblActionStatus4 = new JLabel("COA Processor Ready \u2022 Select Operation to Execute Micro-Trace");
        leftCol.add(createActionStatusBar(lblActionStatus4, 3));

        JPanel rightCol = createColumnPanel();

        JPanel regBanner = new JPanel(new GridLayout(1, 4, 10, 0));
        regBanner.setOpaque(false);
        regBanner.setPreferredSize(new Dimension(regBanner.getPreferredSize().width, 74));
        regBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));

        lblRegRax = new JLabel("0x000AF8B0 (719,000 m)");
        lblRegRbx = new JLabel("0x000CB200 (832,000 m)");
        lblRegFlags = new JLabel("CF=1 ZF=0 SF=1 (JL)");
        lblRegR8 = new JLabel("0x00A49250");

        regBanner.add(createKpiCard("RAX (Shortest m)", lblRegRax, "QUAD", new Color(18, 25, 115)));
        regBanner.add(createKpiCard("RBX (Longest m)", lblRegRbx, "QUAD", new Color(100, 116, 139)));
        regBanner.add(createKpiCard("RFLAGS (Branch)", lblRegFlags, "ALU", new Color(242, 143, 59)));
        regBanner.add(createKpiCard("R8 (IMUL Energy)", lblRegR8, "SCORE", new Color(16, 185, 129)));

        rightCol.add(regBanner);
        rightCol.add(Box.createVerticalStrut(16));

        RoundedCardPanel card2 = createSectionCard("x86-64 Architecture Register & Instruction Specifications", "Hardware register mappings and quadword arithmetic rules", null);

        JPanel regTable = new JPanel();
        regTable.setLayout(new BoxLayout(regTable, BoxLayout.Y_AXIS));
        regTable.setOpaque(false);

        regTable.add(createRegisterRow("RAX", "Shortest Route Distance in Meters (Quadword Integer)"));
        keyTableStrut(regTable);
        regTable.add(createRegisterRow("RBX", "Longest Route Distance in Meters (Quadword Integer)"));
        keyTableStrut(regTable);
        regTable.add(createRegisterRow("RCX", "Distance Delta |RBX - RAX| (SUB Instruction Result)"));
        keyTableStrut(regTable);
        regTable.add(createRegisterRow("R8 / R9", "Hardware Energy Score via IMUL (Meters \u00D7 Elevation Factor)"));
        keyTableStrut(regTable);
        regTable.add(createRegisterRow("RFLAGS", "ZF, SF, CF, OF Evaluation for Condition Branching (JL / JG / JE)"));

        card2.add(regTable);
        card2.add(Box.createVerticalStrut(14));

        card2.add(createCourseOutcomesBox("COMPUTER ARCHITECTURE COURSE OUTCOMES (CO1 - CO4):", new String[]{
            "CO1: 16-Bit 8086 Bus Transfers & Pipeline Cycle Simulation",
            "CO2: Instruction Sets & 64-Bit Arithmetic / Branching",
            "CO3: 64-Bit Hardware Multiplier (IMUL) & Operational Energy",
            "CO4: Polyglot Java-to-Assembly Native Architecture Integration"
        }));

        rightCol.add(card2);

        return createTabPage(leftCol, rightCol);
    }

    private void keyTableStrut(JPanel panel) {
        panel.add(Box.createVerticalStrut(6));
    }

    private void initOutputPopupOverlay() {
        outputOverlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(15, 23, 42, 165));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }

            @Override
            public void doLayout() {
                if (popupCard != null) {
                    popupCard.setBounds(calculatePopupBounds(getWidth(), getHeight()));
                }
                super.doLayout();
            }
        };
        outputOverlayPanel.setOpaque(false);
        outputOverlayPanel.setLayout(null);
        outputOverlayPanel.setVisible(false);

        popupCard = new RoundedCardPanel(18, new Color(15, 23, 42), new Color(51, 65, 85));
        popupCard.setLayout(new BorderLayout());

        JPanel popupHeader = new JPanel(new BorderLayout(10, 0));
        popupHeader.setBackground(new Color(30, 41, 59));
        popupHeader.setPreferredSize(new Dimension(100, 52));
        popupHeader.setBorder(new EmptyBorder(8, 18, 8, 14));

        JLabel lblPopupTitle = new JLabel("\u25A3  LIVE SYSTEM OUTPUT & TELEMETRY CONSOLE");
        lblPopupTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPopupTitle.setForeground(new Color(248, 250, 252));
        popupHeader.add(lblPopupTitle, BorderLayout.WEST);

        JPanel popupActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        popupActions.setOpaque(false);

        ModernButton btnPopupClear = new ModernButton("Clear Output", new Color(51, 65, 85), new Color(226, 232, 240), new Color(71, 85, 105), null, 8);
        btnPopupClear.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnPopupClear.setPreferredSize(new Dimension(105, 34));
        btnPopupClear.addActionListener(e -> {
            popupOutputArea.setText("");
            updateActionStatus("Output console cleared");
        });
        popupActions.add(btnPopupClear);

        ModernButton btnClosePopup = new ModernButton("\u2715", new Color(71, 85, 105), Color.WHITE, new Color(220, 38, 38), null, 8);
        btnClosePopup.setPreferredSize(new Dimension(40, 34));
        btnClosePopup.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnClosePopup.addActionListener(e -> setOutputPopupVisible(false));
        popupActions.add(btnClosePopup);

        popupHeader.add(popupActions, BorderLayout.EAST);
        popupCard.add(popupHeader, BorderLayout.NORTH);

        popupOutputArea = new JTextArea();
        popupOutputArea.setEditable(false);
        popupOutputArea.setLineWrap(false);
        popupOutputArea.setBackground(new Color(11, 15, 25));
        popupOutputArea.setForeground(new Color(226, 232, 240));
        popupOutputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        popupOutputArea.setCaretColor(new Color(242, 143, 59));
        popupOutputArea.setSelectionColor(new Color(30, 58, 138));
        popupOutputArea.setMargin(new Insets(16, 20, 16, 20));

        JScrollPane popupScroll = new JScrollPane(popupOutputArea);
        popupScroll.setBorder(BorderFactory.createEmptyBorder());
        popupScroll.setOpaque(false);
        popupScroll.getViewport().setOpaque(false);
        popupScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        popupScroll.getVerticalScrollBar().setUnitIncrement(16);
        popupScroll.getHorizontalScrollBar().setUI(new DarkScrollBarUI());
        popupScroll.getHorizontalScrollBar().setUnitIncrement(16);

        popupCard.add(popupScroll, BorderLayout.CENTER);
        outputOverlayPanel.add(popupCard);

        outputOverlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!popupCard.getBounds().contains(e.getPoint())) {
                    setOutputPopupVisible(false);
                }
            }
        });

        popupCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }
        });

        KeyStroke escKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        outputOverlayPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKey, "closePopup");
        outputOverlayPanel.getActionMap().put("closePopup", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setOutputPopupVisible(false);
            }
        });

        frame.setGlassPane(outputOverlayPanel);
    }

    private void setOutputPopupVisible(boolean visible) {
        if (outputOverlayPanel != null) {
            outputOverlayPanel.setVisible(visible);
            if (visible) {
                hasNewOutput = false;
                outputOverlayPanel.requestFocusInWindow();
                if (popupCard != null) {
                    popupCard.setBounds(calculatePopupBounds(outputOverlayPanel.getWidth(), outputOverlayPanel.getHeight()));
                    outputOverlayPanel.revalidate();
                    outputOverlayPanel.repaint();
                }
            }
        }
        updateToggleBtnAppearance();
    }

    private Rectangle calculatePopupBounds(int w, int h) {
        int cardW = Math.min(1100, Math.max(600, w - 160));
        int cardH = Math.min(680, Math.max(400, h - 140));
        return new Rectangle(Math.max(20, (w - cardW) / 2), Math.max(20, (h - cardH) / 2), cardW, cardH);
    }

    private void updateToggleBtnAppearance() {
        if (btnOutputToggle == null) {
            return;
        }
        boolean isVis = outputOverlayPanel != null && outputOverlayPanel.isVisible();
        if (isVis) {
            btnOutputToggle.setText("\u25A3  Output Console: ON");
            btnOutputToggle.setCustomColors(new Color(242, 143, 59), new Color(224, 122, 38), Color.WHITE);
        } else if (hasNewOutput) {
            btnOutputToggle.setText("\u25A3  Output Console [New \u25CF]");
            btnOutputToggle.setCustomColors(new Color(245, 158, 11), new Color(217, 119, 6), Color.WHITE);
        } else {
            btnOutputToggle.setText("\u25A3  Output Console: OFF");
            btnOutputToggle.setCustomColors(new Color(255, 255, 255, 25), new Color(255, 255, 255, 50), new Color(226, 232, 240));
        }
    }

    private void updateActionStatus(String msg) {
        Font f = getFontForText(msg, Font.BOLD, 12);
        if (lblActionStatus1 != null) { lblActionStatus1.setFont(f); lblActionStatus1.setText(msg); }
        if (lblActionStatus2 != null) { lblActionStatus2.setFont(f); lblActionStatus2.setText(msg); }
        if (lblActionStatus3 != null) { lblActionStatus3.setFont(f); lblActionStatus3.setText(msg); }
        if (lblActionStatus4 != null) { lblActionStatus4.setFont(f); lblActionStatus4.setText(msg); }
        hasNewOutput = true;
        updateToggleBtnAppearance();
    }

    private void appendOutputWithStatus(String text, String statusMsg) {
        if (popupOutputArea != null) {
            popupOutputArea.append(text);
            if (!text.endsWith("\n")) {
                popupOutputArea.append("\n");
            }
            popupOutputArea.setCaretPosition(popupOutputArea.getDocument().getLength());
        }
        if (statusMsg != null && !statusMsg.isEmpty()) {
            updateActionStatus(statusMsg);
        }
    }

    private JPanel createKpiCard(String labelText, JLabel valueLabel, String badgeText, Color accentColor) {
        RoundedCardPanel card = new RoundedCardPanel(14, Color.WHITE, new Color(226, 232, 240), accentColor);
        card.setLayout(new BorderLayout(0, 4));
        card.setBorder(new EmptyBorder(8, 10, 8, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(labelText.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(new Color(100, 116, 139));
        topPanel.add(lblTitle, BorderLayout.WEST);

        if (badgeText != null && !badgeText.isEmpty()) {
            JLabel lblBadge = new JLabel(badgeText);
            lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            lblBadge.setForeground(accentColor);
            topPanel.add(lblBadge, BorderLayout.EAST);
        }
        card.add(topPanel, BorderLayout.NORTH);

        int fontSize = (valueLabel.getText() != null && valueLabel.getText().length() > 16) ? 12 : 13;
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        valueLabel.setForeground(new Color(12, 16, 101));
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBadgeDescRow(String badgeText, String descText, Font badgeFont, Color badgeFg, Color badgeBg, Color badgeBorder, int badgeW) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel lblBadge = new JLabel(badgeText, JLabel.CENTER);
        lblBadge.setFont(badgeFont);
        lblBadge.setForeground(badgeFg);
        lblBadge.setBackground(badgeBg);
        lblBadge.setOpaque(true);
        lblBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(badgeBorder, 1),
            new EmptyBorder(2, 6, 2, 6)
        ));
        lblBadge.setPreferredSize(new Dimension(badgeW, 24));

        JLabel lblDesc = new JLabel(descText);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(51, 65, 85));

        row.add(lblBadge, BorderLayout.WEST);
        row.add(lblDesc, BorderLayout.CENTER);
        return row;
    }

    private JPanel createKeycapRow(String keyText, String descText) {
        return createBadgeDescRow(keyText, descText, new Font("Consolas", Font.BOLD, 11),
                new Color(12, 16, 101), new Color(241, 245, 248), new Color(203, 213, 225), 170);
    }

    private JPanel createRegisterRow(String regName, String regDesc) {
        return createBadgeDescRow(regName, regDesc, new Font("Consolas", Font.BOLD, 12),
                new Color(242, 143, 59), new Color(15, 23, 42), new Color(51, 65, 85), 100);
    }

    private JPanel createCourseOutcomesBox(String title, String[] outcomes) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblHeader = new JLabel(title);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblHeader.setForeground(new Color(12, 16, 101));
        box.add(lblHeader);
        box.add(Box.createVerticalStrut(6));

        for (String co : outcomes) {
            JLabel lblCo = new JLabel(" \u2022  " + co);
            lblCo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblCo.setForeground(new Color(71, 85, 105));
            box.add(lblCo);
        }
        return box;
    }

    private RoundedCardPanel createSectionCard(String title, String subtitle, Color accentColor) {
        RoundedCardPanel card = new RoundedCardPanel(16, Color.WHITE, new Color(226, 232, 240), accentColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.add(createCardHeader(title, subtitle));
        return card;
    }

    private JPanel createColumnPanel() {
        ScrollablePanel col = new ScrollablePanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        return col;
    }

    private JPanel createTabPage(JPanel leftCol, JPanel rightCol) {
        JPanel page = new JPanel(new GridLayout(1, 2, 24, 0));
        page.setBackground(new Color(241, 245, 248));
        page.setBorder(new EmptyBorder(24, 28, 24, 28));
        page.add(createColumnScrollPane(leftCol));
        page.add(createColumnScrollPane(rightCol));
        return page;
    }

    private void addSeparator(JPanel panel) {
        panel.add(Box.createVerticalStrut(14));
        JPanel sep = new JPanel();
        sep.setBackground(new Color(226, 232, 240));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(12));
    }

    private JPanel createCardHeader(String titleText, String subtitleText) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("<html><body style='margin:0;padding:0;'>" + titleText + "</body></html>");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(12, 16, 101));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(lblTitle);

        if (subtitleText != null && !subtitleText.isEmpty()) {
            header.add(Box.createVerticalStrut(2));
            JLabel lblSub = new JLabel("<html><body style='margin:0;padding:0;'>" + subtitleText + "</body></html>");
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSub.setForeground(new Color(100, 116, 139));
            lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
            header.add(lblSub);
        }

        return header;
    }

    private JPanel createFormRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        row.add(lbl, BorderLayout.NORTH);
        row.add(comp, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        return row;
    }

    private JPanel createTwoColRow(String label1, JComponent comp1, String label2, JComponent comp2) {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.add(createFormRow(label1, comp1));
        row.add(createFormRow(label2, comp2));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        return row;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(15, 23, 42));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 32));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(5, 8, 5, 8));
                if (isSelected) {
                    label.setBackground(new Color(238, 242, 255));
                    label.setForeground(new Color(12, 16, 101));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(15, 23, 42));
                }
                return label;
            }
        });
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rb.setForeground(new Color(15, 23, 42));
        rb.setBackground(Color.WHITE);
        rb.setFocusPainted(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateKPIs() {
        if (lblKpiStations != null) {
            lblKpiStations.setText("32 Nodes");
        }
        if (lblKpiTickets != null) {
            lblKpiTickets.setText(confirmedTicketCount + " Confirmed");
        }
        if (lblKpiWaitlist != null) {
            lblKpiWaitlist.setText(waitingListQueue.getSize() + " Waiting");
        }
    }

    private void loadInitialData() {
        trainList.clear();
        trainList.addAll(DatabaseManager.loadTrainsFromSQL());
        refreshStationCombos();

        DatabaseManager.syncTrainsToDS(trainArray, trainLinkedList);
        routeManager.loadDefaultNetwork();

        Passenger sample1 = new Passenger("Aarav Sharma", 28);
        sample1.setGender("Male");
        sample1.setContactNumber("9823012345");
        passengerArray[passengerIndex++] = sample1;
        personList.add(sample1);

        Passenger sample2 = new Passenger("Priya Patil", 24);
        sample2.setGender("Female");
        sample2.setContactNumber("9890123456");
        passengerArray[passengerIndex++] = sample2;
        personList.add(sample2);

        Operator op1 = new Operator("Ramesh Kulkarni", "9819234567", "Signal & Telecom");
        personList.add(op1);

        Operator op2 = new Operator("Sunita Deshmukh", "9422345678", "Traffic Control");
        personList.add(op2);

        Ticket sampleTicket1 = new Ticket(sample1.getPassengerId(), "TRN101", 150.0);
        sampleTicket1.setFromStation("CSMT");
        sampleTicket1.setToStation("SUR");
        DatabaseManager.saveTicket(sampleTicket1);
        ticketHashTable.put(sampleTicket1.getTicketId(), "Passenger: Aarav Sharma | Train: TRN101 | From: CSMT -> To: SUR | Status: CONFIRMED | Fare: Rs.150.00");

        Ticket sampleTicket2 = new Ticket(sample2.getPassengerId(), "TRN109", 320.0);
        sampleTicket2.setFromStation("PUNE");
        sampleTicket2.setToStation("AMI");
        DatabaseManager.saveTicket(sampleTicket2);
        ticketHashTable.put(sampleTicket2.getTicketId(), "Passenger: Priya Patil | Train: TRN109 | From: PUNE -> To: AMI | Status: CONFIRMED | Fare: Rs.320.00");

        appendOutputWithStatus("========================================================================================\n"
                + " RAILROUTE EXPRESS: COMMERCIAL RESERVATION & PASSENGER PORTAL                           \n"
                + "========================================================================================\n"
                + "Loaded " + trainList.size() + " operational Indian Railways services from SQLite.\n"
                + "Synchronized SQLite Trains to 1D TrainArray (Cap 60) & Singly LinkedList.\n"
                + "Active Person Registry: " + Person.getPersonCount() + " entities.\n\n"
                + DatabaseManager.getDatabaseSummary() + "\n\n"
                + "========================================================================================\n"
                + "            STAGE 2: DATA STRUCTURES & ROUTE COMPARATOR INITIALIZED                    \n"
                + "========================================================================================\n"
                + " - CO1/CO2: 1D Fixed TrainArray populated with 40 operational train records.\n"
                + " - CO3:     Train Singly Linked List initialized for dynamic CRUD operations.\n"
                + " - CO4:     Circular Queue (Waiting List) & LIFO Stack (Cancellation) ready.\n"
                + " - CO5:     QuickSort by Fare & Binary Search by ID initialized.\n"
                + " - CO6:     Hash Table with Separate Chaining (Size 13) active for O(1) Ticket Lookup.\n"
                + " - Graph:   32-Station Maharashtra Network with 100% connected operational tracks.\n"
                + "========================================================================================\n\n",
                "System Initialized \u2022 All 32 Stations & 40 Scheduled Trains Synchronized");

        hasNewOutput = false;
        updateToggleBtnAppearance();
        updateAvailableTrainsForBooking();
        updateKPIs();
    }

    private void refreshStationCombos() {
        List<Station> stations = DatabaseManager.loadStationsFromSQL();
        if (stations == null || stations.isEmpty()) {
            return;
        }
        String[] displayNames = new String[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            Station s = stations.get(i);
            displayNames[i] = s.getStationId() + " - " + s.getIrctcCode() + " (" + s.getStationName() + ")";
        }

        populateCombo(cmbFromStation, displayNames, 13);
        populateCombo(cmbToStation, displayNames, 4);

        populateCombo(cmbFrom, displayNames, 13);
        populateCombo(cmbTo, displayNames, 4);

        populateCombo(cmbTab3From, displayNames, 13);
        populateCombo(cmbTab3To, displayNames, 4);

        populateCombo(cmbTab4From, displayNames, 13);
        populateCombo(cmbTab4To, displayNames, 4);
    }

    private void populateCombo(JComboBox<String> combo, String[] items, int defaultIndex) {
        if (combo == null) {
            return;
        }
        combo.removeAllItems();
        for (String item : items) {
            combo.addItem(item);
        }
        if (defaultIndex >= 0 && defaultIndex < items.length) {
            combo.setSelectedIndex(defaultIndex);
        }
    }

    private void bookTicket() {
        String name = txtName.getText().trim();
        String ageText = txtAge.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter passenger name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age = 0;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            age = 0;
        }

        if (currentSegmentTrains == null || currentSegmentTrains.length == 0) {
            JOptionPane.showMessageDialog(frame, "No scheduled train service available for this station pair.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedIndex = cmbTrains.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= currentSegmentTrains.length) {
            JOptionPane.showMessageDialog(frame, "Please select an available train service.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RouteManager.SegmentTrain segTrain = currentSegmentTrains[selectedIndex];

        Train targetTrain = null;
        for (Train t : trainList) {
            if (t.getTrainId().equalsIgnoreCase(segTrain.trainId)) {
                targetTrain = t;
                break;
            }
        }
        if (targetTrain == null && !trainList.isEmpty()) {
            targetTrain = trainList.get(0);
        }

        Passenger passenger = new Passenger(name, age);
        passenger.setGender(rbMale.isSelected() ? "Male" : "Female");
        passenger.setContactNumber(phone);

        String fromStn = (String) cmbFromStation.getSelectedItem();
        String toStn = (String) cmbToStation.getSelectedItem();

        if (targetTrain != null && !targetTrain.isAvailable()) {
            String qMsg = waitingListQueue.enqueue(passenger.getPassengerId(), passenger.getName(), targetTrain.getTrainId(), segTrain.fare);

            Ticket wlTicket = new Ticket(passenger.getPassengerId(), targetTrain.getTrainId(), segTrain.fare);
            wlTicket.setStatus("WAITLIST");
            wlTicket.setSeatNumber("WL-" + waitingListQueue.getSize());
            wlTicket.setFromStation(fromStn);
            wlTicket.setToStation(toStn);

            DatabaseManager.savePassenger(passenger);
            DatabaseManager.saveTicket(wlTicket);

            ticketHashTable.put(wlTicket.getTicketId(),
                    "Passenger: " + passenger.getName() + " | Train: " + targetTrain.getTrainId() + " (" + segTrain.trainNumber + ") | From: " + fromStn + " -> " + toStn + " | Status: WAITLIST (WL-" + waitingListQueue.getSize() + ") | Fare: Rs." + segTrain.fare);

            if (passengerIndex < passengerArray.length) {
                passengerArray[passengerIndex++] = passenger;
            }
            personList.add(passenger);

            StringBuilder sb = new StringBuilder();
            sb.append("========================================================================================\n");
            sb.append("                         WAITING LIST PLACEMENT (CO4)                                   \n");
            sb.append("========================================================================================\n");
            sb.append(qMsg).append("\n");
            sb.append("Ticket ID:       ").append(wlTicket.getTicketId()).append("\n");
            sb.append("Allocated Seat:  ").append(wlTicket.getSeatNumber()).append("\n");
            sb.append("Train:           ").append(targetTrain.getTrainId()).append(" - ").append(segTrain.trainNumber).append(" ").append(targetTrain.getTrainName()).append(" (COACH FULL)\n");
            sb.append("Origin -> Dest:  ").append(fromStn).append(" -> ").append(toStn).append("\n");
            sb.append("Segment Dist:    ").append(segTrain.segmentDistanceKm).append(" km\n");
            sb.append("Saved to SQLite: passengers & tickets (status=WAITLIST)\n");
            sb.append("Stored in CO6:   Hash Table bucket updated\n");
            sb.append("========================================================================================\n\n");

            appendOutputWithStatus(sb.toString(), "\u26A0 Waitlist allocated: " + wlTicket.getTicketId() + " (WL-" + waitingListQueue.getSize() + ") \u2014 Click 'Output Console' to view");

            txtName.setText("");
            txtAge.setText("");
            txtPhone.setText("");
            updateKPIs();
            return;
        }

        if (targetTrain != null) {
            targetTrain.reserve();
            DatabaseManager.updateTrainSeats(targetTrain.getTrainId(), targetTrain.getAvailableSeats());
        }

        Ticket ticket = new Ticket(passenger.getPassengerId(), segTrain.trainId, segTrain.fare);
        ticket.setFromStation(fromStn);
        ticket.setToStation(toStn);

        DatabaseManager.savePassenger(passenger);
        DatabaseManager.saveTicket(ticket);

        ticketHashTable.put(ticket.getTicketId(),
                "Passenger: " + passenger.getName() + " | Train: " + segTrain.trainId + " (" + segTrain.trainNumber + ") | From: " + fromStn + " -> " + toStn + " | Status: CONFIRMED (" + ticket.getSeatNumber() + ") | Fare: Rs." + segTrain.fare);

        if (passengerIndex < passengerArray.length) {
            passengerArray[passengerIndex++] = passenger;
        }
        personList.add(passenger);

        confirmedTicketCount++;
        updateAvailableTrainsForBooking();
        updateKPIs();

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                            E-TICKET BOOKING CONFIRMED                                  \n");
        sb.append("========================================================================================\n");
        sb.append("Ticket ID:       ").append(ticket.getTicketId()).append("\n");
        sb.append("Status:          ").append(ticket.getStatus()).append("\n");
        sb.append("Seat Allocated:  ").append(ticket.getSeatNumber()).append(" (Confirmed Reservation)\n");
        sb.append("Passenger ID:    ").append(passenger.getPassengerId()).append("\n");
        sb.append("Passenger Name:  ").append(passenger.getName()).append(" (").append(passenger.getGender()).append(", Age: ").append(passenger.getAge()).append(")\n");
        sb.append("Contact Number:  ").append(passenger.getContactNumber()).append("\n");
        sb.append("Train Service:   ").append(segTrain.trainId).append(" - ").append(segTrain.trainNumber).append(" ").append(segTrain.trainName).append("\n");
        sb.append("Origin -> Dest:  ").append(fromStn).append(" -> ").append(toStn).append("\n");
        sb.append("Stops Visited:   ").append(segTrain.stopsSummary).append("\n");
        sb.append("Segment Dist:    ").append(segTrain.segmentDistanceKm).append(" km (").append(String.format("%.1f", segTrain.segmentTravelTimeHr)).append(" hrs)\n");
        sb.append("Segment Fare:    Rs. ").append(String.format("%.2f", ticket.getFare())).append("\n");
        if (targetTrain != null) {
            sb.append("Remaining Seats: ").append(targetTrain.getAvailableSeats()).append("/").append(targetTrain.getTotalSeats()).append("\n");
        }
        sb.append("Persistence:     Saved into SQLite 'passengers' and 'tickets'\n");
        sb.append("Data Structures: Stored in TicketHashTable (O(1) lookup)\n");
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Confirmed booking: " + ticket.getTicketId() + " (Seat " + ticket.getSeatNumber() + ") \u2014 Click 'Output Console' to view");

        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
    }

    private void cancelTicket() {
        String ticketId = txtCancelTicketId.getText().trim();
        if (ticketId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a Ticket ID to cancel.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DatabaseManager.updateTicketStatusInSQL(ticketId, "CANCELLED");

        String trainId = "TRN101";
        double fare = 150.0;
        String passId = "PSG001";

        cancellationStack.push(ticketId, passId, trainId, fare);
        ticketHashTable.remove(ticketId);

        String[] waitlisted = waitingListQueue.dequeueForTrain(trainId);

        if (confirmedTicketCount > 0) {
            confirmedTicketCount--;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   TICKET CANCELLATION EXECUTED (CO4 STACK)                             \n");
        sb.append("========================================================================================\n");
        sb.append("Ticket ID:           ").append(ticketId).append(" marked as CANCELLED\n");
        sb.append("Pushed to Stack:     CancellationStack (LIFO refund buffer)\n");
        sb.append("Removed from CO6:    TicketHashTable index deleted\n");

        if (waitlisted != null) {
            String wlPassId = waitlisted[0];
            String wlPassName = waitlisted[1];
            String wlTrainId = waitlisted[2];
            double wlFare = Double.parseDouble(waitlisted[3]);

            Ticket promoTicket = new Ticket(wlPassId, wlTrainId, wlFare);
            promoTicket.setStatus("CONFIRMED");
            promoTicket.setSeatNumber("S1-PROMO");
            DatabaseManager.saveTicket(promoTicket);

            confirmedTicketCount++;

            ticketHashTable.put(promoTicket.getTicketId(),
                    "Passenger: " + wlPassName + " | Train: " + wlTrainId + " | Status: CONFIRMED (PROMOTED FROM WL) | Fare: Rs." + wlFare);

            sb.append("WAITLIST PROMOTION:  Passenger ").append(wlPassName).append(" (").append(wlPassId).append(") promoted from Waiting List!\n");
            sb.append("New Confirmed Tkt:   ").append(promoTicket.getTicketId()).append(" (Seat ").append(promoTicket.getSeatNumber()).append(")\n");
        } else {
            for (Train t : trainList) {
                if (t.getTrainId().equalsIgnoreCase(trainId)) {
                    t.releaseSeat();
                    DatabaseManager.updateTrainSeats(t.getTrainId(), t.getAvailableSeats());
                    break;
                }
            }
            updateAvailableTrainsForBooking();
            sb.append("Seat Released:       1 seat restored to inventory on ").append(trainId).append("\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2716 Ticket cancelled: " + ticketId + " \u2014 Click 'Output Console' to view");

        updateKPIs();
        txtCancelTicketId.setText("");
    }

    private void addOperator() {
        String name = txtOpName.getText().trim();
        String phone = txtOpPhone.getText().trim();
        String dept = txtOpDept.getText().trim();

        if (name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter operator name and department.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Operator op = new Operator(name, phone, dept);
        personList.add(op);

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                            OPERATOR REGISTERED                                         \n");
        sb.append("========================================================================================\n");
        sb.append("Operator ID:     ").append(op.getOperatorId()).append("\n");
        sb.append("Name:            ").append(op.getName()).append("\n");
        sb.append("Department:      ").append(op.getDepartment()).append("\n");
        sb.append("Contact:         ").append(op.getContactNumber()).append("\n");
        sb.append("Role:            ").append(op.getRole()).append("\n");
        sb.append("Total Persons:   ").append(Person.getPersonCount()).append("\n");
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Registered operator: " + op.getName() + " \u2014 Click 'Output Console' to view");

        txtOpName.setText("");
        txtOpPhone.setText("");
        txtOpDept.setText("");
    }

    private void runPolymorphismDemo() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                    POLYMORPHISM & ENTITY DEMONSTRATION                                 \n");
        sb.append("========================================================================================\n");
        sb.append("Total Person Objects Instantiated: ").append(Person.getPersonCount()).append("\n");
        sb.append("Polymorphic invocation of p.getRole() and p.toString():\n");
        for (Person p : personList) {
            sb.append(" -> [").append(p.getRole()).append("] ").append(p.toString()).append("\n");
        }
        sb.append("\nDemonstrating Bookable Interface on Trains:\n");
        for (Train t : trainList) {
            Bookable b = t;
            sb.append(" -> Bookable ID: ").append(b.getBookableId()).append(" | Available: ").append(b.isAvailable()).append(" | Train: ").append(t.getTrainName()).append("\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Polymorphism demo executed \u2014 Click 'Output Console' to view report");
    }

    private void filterByPassengers() {
        List<Person> filtered = Person.filterPeople(personList, p -> p instanceof Passenger);
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("       FILTER RESULTS: PASSENGERS (").append(filtered.size()).append(" records) \n");
        sb.append("========================================================================================\n");
        for (Person p : filtered) {
            sb.append(" -> ").append(p.toString()).append("\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Filtered: " + filtered.size() + " passengers \u2014 Click 'Output Console' to view");
    }

    private void filterByOperators() {
        List<Person> filtered = Person.filterPeople(personList, p -> p instanceof Operator);
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("        FILTER RESULTS: OPERATORS (").append(filtered.size()).append(" records)  \n");
        sb.append("========================================================================================\n");
        for (Person p : filtered) {
            sb.append(" -> ").append(p.toString()).append("\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Filtered: " + filtered.size() + " operators \u2014 Click 'Output Console' to view");
    }

    private void filterByPhonePrefix() {
        List<Person> filtered = Person.filterPeople(personList, p -> p.getContactNumber() != null && p.getContactNumber().startsWith("98"));
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("  FILTER RESULTS: PHONE STARTS WITH 98 (").append(filtered.size()).append(" records)\n");
        sb.append("========================================================================================\n");
        for (Person p : filtered) {
            sb.append(" -> ").append(p.toString()).append("\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Filtered: " + filtered.size() + " records \u2014 Click 'Output Console' to view");
    }

    private void showDatabaseStatus() {
        appendOutputWithStatus(DatabaseManager.getDatabaseSummary() + "\n\n", "\u2714 SQLite database summary queried \u2014 Click 'Output Console' to view");
    }

    private void addTrainDS() {
        String id = txtTId.getText().trim();
        String name = txtTName.getText().trim();
        String src = txtTSrc.getText().trim();
        String dst = txtTDst.getText().trim();
        String seatsStr = txtTSeats.getText().trim();
        String fareStr = txtTFare.getText().trim();

        if (id.isEmpty() || name.isEmpty() || src.isEmpty() || dst.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter all train fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int seats = 5;
        double fare = 1.80;
        try {
            seats = Integer.parseInt(seatsStr);
            fare = Double.parseDouble(fareStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid numeric input for seats or fare.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String llMsg = trainLinkedList.insertTrainFromGUI(id, name, src, dst, seats, 8.0, fare);
        if (llMsg.startsWith("Error")) {
            JOptionPane.showMessageDialog(frame, llMsg, "Insertion Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TrainRecord rec = new TrainRecord(id, name, src, dst, seats, 8.0, fare);
        trainArray.addRecord(rec);

        DatabaseManager.saveOrUpdateTrainInSQL(id, name, src, dst, seats, seats, 8.0, fare);

        trainList.clear();
        trainList.addAll(DatabaseManager.loadTrainsFromSQL());
        updateAvailableTrainsForBooking();
        updateKPIs();

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   TRAIN INSERTION: LINKED LIST & SQL PERSISTENCE                       \n");
        sb.append("========================================================================================\n");
        sb.append(llMsg).append("\n");
        sb.append("1D TrainArray: Appended TrainRecord [").append(id).append("]\n");
        sb.append("SQLite DB:     Saved into table 'trains'\n");
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Added train: " + id + " \u2014 Click 'Output Console' to view");
    }

    private void updateTrainDS() {
        String id = txtTId.getText().trim();
        String name = txtTName.getText().trim();
        String src = txtTSrc.getText().trim();
        String dst = txtTDst.getText().trim();
        String seatsStr = txtTSeats.getText().trim();
        String fareStr = txtTFare.getText().trim();

        int seats = 5;
        double fare = 1.80;
        try {
            seats = Integer.parseInt(seatsStr);
            fare = Double.parseDouble(fareStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid numbers for seats or fare.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msg = trainLinkedList.updateTrainFromGUI(id, name, src, dst, seats, fare);
        DatabaseManager.saveOrUpdateTrainInSQL(id, name, src, dst, seats, seats, 8.0, fare);

        trainList.clear();
        trainList.addAll(DatabaseManager.loadTrainsFromSQL());
        updateAvailableTrainsForBooking();
        updateKPIs();

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   TRAIN UPDATE: SINGLY LINKED LIST (CO3)                               \n");
        sb.append("========================================================================================\n");
        sb.append(msg).append("\n");
        sb.append("SQLite Database synchronized for train ID: ").append(id).append("\n");
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Updated train: " + id + " \u2014 Click 'Output Console' to view");
    }

    private void deleteTrainDS() {
        String id = txtTId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a Train ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msg = trainLinkedList.deleteTrainById(id);
        DatabaseManager.deleteTrainFromSQL(id);

        trainList.clear();
        trainList.addAll(DatabaseManager.loadTrainsFromSQL());
        updateAvailableTrainsForBooking();
        updateKPIs();

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   TRAIN DELETION: SINGLY LINKED LIST (CO3)                             \n");
        sb.append("========================================================================================\n");
        sb.append(msg).append("\n");
        sb.append("SQLite record deleted for train ID: ").append(id).append("\n");
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2716 Deleted train: " + id + " \u2014 Click 'Output Console' to view");
    }

    private void displayLinkedListDS() {
        appendOutputWithStatus(trainLinkedList.displayAllTrainsAsString() + "\n", "\u2714 Singly Linked List displayed \u2014 Click 'Output Console' to view");
    }

    private void show1DArrayDS() {
        appendOutputWithStatus(trainArray.getAllArrayTrainsAsString() + "\n", "\u2714 1D TrainArray displayed \u2014 Click 'Output Console' to view");
    }

    private void quickSortDS() {
        appendOutputWithStatus(trainArray.getQuickSortedByFareAsString() + "\n", "\u2714 QuickSort by fare executed \u2014 Click 'Output Console' to view");
    }

    private void binarySearchDS() {
        String id = txtTId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter Train ID in ID field to binary search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TrainRecord found = trainArray.binarySearchById(id);
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("           CO5: BINARY SEARCH ON SORTED COPY OF 1D ARRAY                                \n");
        sb.append("========================================================================================\n");
        sb.append("Target Train ID: ").append(id).append("\n");
        if (found != null) {
            sb.append("MATCH LOCATED IN O(log N) STEPS:\n");
            sb.append(" - ID:              ").append(found.trainId).append("\n");
            sb.append(" - Number:          ").append(found.trainNumber).append("\n");
            sb.append(" - Name:            ").append(found.trainName).append("\n");
            sb.append(" - Route:           ").append(found.sourceStationId).append(" -> ").append(found.destinationStationId).append("\n");
            sb.append(" - Available Seats: ").append(found.availableSeats).append(" / ").append(found.totalSeats).append("\n");
            sb.append(" - Base Rate:       Rs. ").append(found.baseFare).append(" / km\n");
        } else {
            sb.append("Result: Target Train ID '").append(id).append("' was not found in 1D TrainArray.\n");
        }
        sb.append("========================================================================================\n\n");

        appendOutputWithStatus(sb.toString(), "\u2714 Binary search executed for " + id + " \u2014 Click 'Output Console' to view");
    }

    private void compareRoutesDS() {
        String fromItem = (String) cmbFrom.getSelectedItem();
        String toItem = (String) cmbTo.getSelectedItem();
        String travelClass = (String) cmbClass.getSelectedItem();

        String report = routeManager.compareRoutesAndFares(fromItem, toItem, travelClass);
        appendOutputWithStatus(report + "\n", "\u2714 Route comparison generated \u2014 Click 'Output Console' to view");

        RouteManager.OperationalCorridor[] corridors = routeManager.findOperationalRoutesBetween(fromItem, toItem, travelClass);
        if (corridors != null && corridors.length >= 2) {
            RouteManager.OperationalCorridor shortest = corridors[0];
            RouteManager.OperationalCorridor longest = corridors[corridors.length - 1];
            int distDiff = longest.distanceKm - shortest.distanceKm;
            double fareDiff = longest.fare - shortest.fare;

            lblCompShortest.setText(shortest.distanceKm + " km  |  Rs. " + String.format("%.2f", shortest.fare));
            lblCompLongest.setText(longest.distanceKm + " km  |  Rs. " + String.format("%.2f", longest.fare));
            lblCompSavings.setText("-" + distDiff + " km  |  Save Rs. " + String.format("%.2f", fareDiff));
        } else if (corridors != null && corridors.length == 1) {
            RouteManager.OperationalCorridor single = corridors[0];
            lblCompShortest.setText(single.distanceKm + " km  |  Rs. " + String.format("%.2f", single.fare));
            lblCompLongest.setText("Single Dedicated Route");
            lblCompSavings.setText("No Alternative Detour");
        } else {
            RouteManager.RouteDetail rd = routeManager.getRouteDetail(fromItem, toItem, true);
            if (rd != null) {
                lblCompShortest.setText(rd.totalDistanceKm + " km (Interchange)");
                lblCompLongest.setText("No Direct Scheduled Train");
                lblCompSavings.setText("Transfer Required");
            } else {
                lblCompShortest.setText("No Route");
                lblCompLongest.setText("No Route");
                lblCompSavings.setText("N/A");
            }
        }
        lblCompShortest.setFont(new Font("Segoe UI", Font.BOLD, lblCompShortest.getText().length() > 16 ? 12 : 13));
        lblCompLongest.setFont(new Font("Segoe UI", Font.BOLD, lblCompLongest.getText().length() > 16 ? 12 : 13));
        lblCompSavings.setFont(new Font("Segoe UI", Font.BOLD, lblCompSavings.getText().length() > 16 ? 12 : 13));
    }

    private void showAdjacencyMatrix() {
        appendOutputWithStatus(routeManager.getDistanceMatrixAsString() + "\n", "\u2714 32x32 track adjacency matrix queried \u2014 Click 'Output Console' to view");
    }

    private void showQueueAndStack() {
        appendOutputWithStatus(waitingListQueue.displayQueueAsString() + "\n" + cancellationStack.displayStackAsString() + "\n", "\u2714 Circular queue & LIFO stack queried \u2014 Click 'Output Console' to view");
    }

    private void testStackQueueBoundaryCases() {
        String report = WaitingListQueue.runBoundaryCaseTests();
        appendOutputWithStatus(report + "\n", "\u2714 Stack & Queue boundary tests completed \u2014 Click 'Output Console' to view");
    }

    private void showHashTableLookup() {
        String tktId = txtTId.getText().trim();
        String dump = ticketHashTable.displayHashTableAsString() + "\n";
        if (tktId.startsWith("TKT") || tktId.length() >= 5) {
            dump += ticketHashTable.get(tktId) + "\n\n";
        }
        appendOutputWithStatus(dump, "\u2714 TicketHashTable bucket lookup executed \u2014 Click 'Output Console' to view");
    }

    private boolean syncRouteToOpenGL() {
        String from = (String) cmbTab3From.getSelectedItem();
        String to = (String) cmbTab3To.getSelectedItem();
        String travelClass = (String) cmbTab3Class.getSelectedItem();
        boolean useLongest = rbTab3Longest.isSelected();

        String result = routeManager.exportSelectedRouteForOpenGL(from, to, travelClass, useLongest);
        if (result != null && result.startsWith("Error:")) {
            appendOutputWithStatus(result + "\n", "\u26A0 " + result.trim());
            return false;
        }
        appendOutputWithStatus(result + "\n", "\u2714 Active route coordinates synced to active_route.txt \u2014 Click 'Output Console' to view");
        return true;
    }

    private static void setupProcessEnvironment(ProcessBuilder pb, File exeDir) {
        if (exeDir != null && exeDir.isDirectory()) {
            pb.directory(exeDir);
        }
        pb.redirectErrorStream(true);
        Map<String, String> env = pb.environment();
        String existingPath = env.get("PATH");
        if (existingPath == null) {
            existingPath = "";
        }
        StringBuilder sb = new StringBuilder();
        if (exeDir != null && exeDir.isDirectory()) {
            sb.append(exeDir.getAbsolutePath()).append(File.pathSeparator);
        }
        String[] candidates = new String[]{
            "C:\\msys64\\ucrt64\\bin",
            "C:\\msys64\\mingw64\\bin",
            "C:\\msys64\\clang64\\bin",
            "E:\\Msys\\mingw64\\bin"
        };
        for (String c : candidates) {
            File dir = new File(c);
            if (dir.isDirectory()) {
                sb.append(c).append(File.pathSeparator);
            }
        }
        sb.append(existingPath);
        env.put("PATH", sb.toString());
    }

    private static File findExecutable(String exeName) {
        File[] candidates = new File[]{
            new File("build/output", exeName),
            new File(exeName),
            new File(System.getProperty("user.dir"), "build/output/" + exeName),
            new File(System.getProperty("user.dir"), exeName)
        };
        for (File f : candidates) {
            if (f.exists()) {
                return f.getAbsoluteFile();
            }
        }
        return new File("build/output", exeName).getAbsoluteFile();
    }

    private void launchOpenGLVisualizer() {
        if (!syncRouteToOpenGL()) {
            return;
        }

        Thread runner = new Thread(() -> {
            try {
                File exe = findExecutable("railroute_visualizer.exe");
                if (!exe.exists()) {
                    SwingUtilities.invokeLater(() -> {
                        appendOutputWithStatus("ERROR: 'railroute_visualizer.exe' not found in build/output/.\n"
                                + "Please execute .\\build.ps1 first to compile C++ OpenGL visualizer.\n\n",
                                "ERROR: railroute_visualizer.exe not found");
                    });
                    return;
                }

                if (activeVisualizerProcess != null && activeVisualizerProcess.isAlive()) {
                    try {
                        activeVisualizerProcess.destroyForcibly();
                    } catch (Exception ignored) {
                    }
                }

                File activeRouteFile = new File("data/active_route.txt").getAbsoluteFile();
                ProcessBuilder pb = new ProcessBuilder(exe.getAbsolutePath(), activeRouteFile.getAbsolutePath());
                setupProcessEnvironment(pb, exe.getParentFile());

                SwingUtilities.invokeLater(() -> {
                    appendOutputWithStatus("========================================================================================\n"
                            + "LAUNCHING C++ OPENGL 2D ROUTE & VANDE BHARAT VISUALIZER...\n"
                            + "========================================================================================\n",
                            "\u25B6 OpenGL 2D Visualizer process spawned \u2014 Click 'Output Console' to view stream");
                });

                Process process = pb.start();
                activeVisualizerProcess = process;
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    final String outLine = line;
                    SwingUtilities.invokeLater(() -> {
                        if (popupOutputArea != null) {
                            popupOutputArea.append(outLine + "\n");
                            popupOutputArea.setCaretPosition(popupOutputArea.getDocument().getLength());
                        }
                    });
                }
                int exitCode = process.waitFor();
                SwingUtilities.invokeLater(() -> {
                    String termMsg = "OpenGL Visualizer terminated with exit code " + exitCode + ".\n";
                    if (exitCode == -1073741515) {
                        termMsg += "A required DLL was not found. Run .\\build.ps1 again so the runtime DLLs are copied next to the exe.\n";
                    }
                    appendOutputWithStatus(termMsg + "\n",
                            "OpenGL Visualizer closed (Exit Code " + exitCode + ")");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    appendOutputWithStatus("Process Execution Error: " + ex.getMessage() + "\n\n",
                            "OpenGL Launch Error: " + ex.getMessage());
                });
            }
        });
        runner.setDaemon(true);
        runner.start();
    }

    private void run64BitLiveTrace() {
        String from = (String) cmbTab4From.getSelectedItem();
        String to = (String) cmbTab4To.getSelectedItem();
        String report = COAProcessorEngine.run64BitLiveRouteTrace(routeManager, from, to);
        appendOutputWithStatus(report + "\n", "\u2714 64-Bit live route ALU trace completed \u2014 Click 'Output Console' to view");

        RouteManager.RouteDetail shortest = routeManager.getRouteDetail(from, to, true);
        RouteManager.RouteDetail longest = routeManager.getRouteDetail(from, to, false);
        if (shortest != null && longest != null) {
            long rax = shortest.totalDistanceKm * 1000L;
            long rbx = longest.totalDistanceKm * 1000L;
            long elevationFactorA = 10L + (long)(shortest.maxElevationMeters / 100.0);
            long r8 = rax * elevationFactorA;
            lblRegRax.setText(String.format("0x%08X (%d m)", rax, rax));
            lblRegRbx.setText(String.format("0x%08X (%d m)", rbx, rbx));
            lblRegFlags.setText(rax < rbx ? "CF=1 ZF=0 SF=1 (JL)" : (rax == rbx ? "CF=0 ZF=1 SF=0 (JE)" : "CF=0 ZF=0 SF=0 (JG)"));
            lblRegR8.setText(String.format("0x%08X (%d)", r8, r8));
        }
    }

    private void runCo3AddShiftStringTrace() {
        String from = (String) cmbTab4From.getSelectedItem();
        String to = (String) cmbTab4To.getSelectedItem();
        String report = COAProcessorEngine.runCo3AddShiftStringTrace(routeManager, from, to);
        appendOutputWithStatus(report + "\n", "\u2714 CO3 add-and-shift & 64-bit string trace completed \u2014 Click 'Output Console' to view");
    }

    private void run16BitTask1Trace() {
        String report = COAProcessorEngine.run16BitTask1Trace();
        appendOutputWithStatus(report + "\n", "\u2714 16-Bit 8086 processor model trace completed \u2014 Click 'Output Console' to view");
    }

    private void executeNativeAssemblyExe() {
        File exe = findExecutable("distance_calc.exe");

        if (exe.exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append("========================================================================================\n");
            sb.append("EXECUTING NATIVE 64-BIT ASSEMBLY BINARY: ").append(exe.getName()).append("\n");
            sb.append("========================================================================================\n");
            try {
                ProcessBuilder pb = new ProcessBuilder(exe.getAbsolutePath());
                setupProcessEnvironment(pb, exe.getParentFile());
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                int exitCode = process.waitFor();
                sb.append("\nNative assembly process terminated with exit code ").append(exitCode).append(".\n");
                if (exitCode == -1073741515) {
                    sb.append("A required DLL was not found. Run .\\build.ps1 again so the runtime DLLs are copied next to the exe.\n");
                }
            } catch (Exception ex) {
                sb.append("Execution Error: ").append(ex.getMessage()).append("\n");
            }
            appendOutputWithStatus(sb.toString(), "\u2714 Native distance_calc.exe executed \u2014 Click 'Output Console' to view");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("========================================================================================\n");
            sb.append("      NATIVE 64-BIT ASSEMBLY BINARY EXECUTION (DEMONSTRATION RUN)                       \n");
            sb.append("========================================================================================\n");
            sb.append("Note: Native 'distance_calc.exe' was not built because NASM assembler is not installed.\n");
            sb.append("Executing pre-compiled 64-bit assembly emulation pipeline for 'distance_calc.asm'...\n\n");
            sb.append(COAProcessorEngine.run64BitLiveRouteTrace(routeManager, "PUNE", "CSMT")).append("\n");
            appendOutputWithStatus(sb.toString(), "\u2714 64-Bit assembly emulation executed \u2014 Click 'Output Console' to view");
        }
    }

    private void viewAssemblySourceFiles() {
        String code = COAProcessorEngine.readAssemblySourceFiles();
        appendOutputWithStatus(code + "\n", "\u2714 Assembly source files loaded \u2014 Click 'Output Console' to view");
    }
}
