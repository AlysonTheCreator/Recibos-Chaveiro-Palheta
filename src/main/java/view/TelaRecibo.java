package view;

import controller.ReciboController;
import model.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaRecibo extends JFrame {
    private final ReciboController controller;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel labelTotal;

    private int xMouse;
    private int yMouse;

    public TelaRecibo(ReciboController controller) {
        this.controller = controller;

        setTitle("Recibo Atual");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setLayout(new BorderLayout());

        add(criarBarraTitulo(), BorderLayout.NORTH);
        add(criarConteudoCentral(), BorderLayout.CENTER);
        add(criarPainelInferior(), BorderLayout.SOUTH);

        atualizarTabela();
        setVisible(true);
    }

    private JPanel criarBarraTitulo() {
        JPanel barra = new JPanel(null);
        barra.setPreferredSize(new Dimension(700, 40));
        barra.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Recibo Atual");
        titulo.setBounds(12, 10, 200, 20);
        titulo.setForeground(Color.BLACK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        barra.add(titulo);

        JButton botaoFechar = new JButton("X");
        botaoFechar.setBounds(640, 0, 60, 40);
        botaoFechar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoFechar.setMargin(new Insets(0, 0, 0, 0));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setBorderPainted(false);
        botaoFechar.setContentAreaFilled(true);
        botaoFechar.setBackground(Color.WHITE);
        botaoFechar.setForeground(Color.BLACK);
        botaoFechar.setHorizontalAlignment(SwingConstants.CENTER);
        botaoFechar.addActionListener(e -> dispose());

        botaoFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botaoFechar.setBackground(Color.RED);
                botaoFechar.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botaoFechar.setBackground(Color.WHITE);
                botaoFechar.setForeground(Color.BLACK);
            }
        });

        barra.add(botaoFechar);

        barra.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });

        barra.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - xMouse, y - yMouse);
            }
        });

        return barra;
    }

    private JScrollPane criarConteudoCentral() {
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Serviço");
        modeloTabela.addColumn("Quantidade");
        modeloTabela.addColumn("Preço");
        modeloTabela.addColumn("Total");

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(28);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(tabela);
    }

    private JPanel criarPainelInferior() {
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelInferior.setBackground(Color.WHITE);

        JButton botaoRemover = new JButton("Remover item");
        botaoRemover.addActionListener(e -> removerItem());
        painelInferior.add(botaoRemover);

        JButton botaoGerar = new JButton("Gerar recibo");
        botaoGerar.addActionListener(e -> gerarRecibo());
        painelInferior.add(botaoGerar);

        labelTotal = new JLabel("Total: R$ 0,00");
        labelTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelInferior.add(labelTotal);

        return painelInferior;
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);

        for (Servico servico : controller.getReciboAtual().getItens()) {
            modeloTabela.addRow(new Object[]{
                    servico.getNome(),
                    servico.getQuantidade(),
                    String.format("R$ %.2f", servico.getPreco()),
                    String.format("R$ %.2f", servico.getValorTotal())
            });
        }

        labelTotal.setText("Total: R$ " + String.format("%.2f", controller.getReciboAtual().getTotalGeral()));
    }

    private void removerItem() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.");
            return;
        }

        controller.removerServico(linhaSelecionada);
        atualizarTabela();
    }

    private void gerarRecibo() {
        boolean sucesso = controller.finalizarReciboEGerarPDF();

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Recibo gerado e salvo com sucesso!");
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Não há itens no recibo.");
        }
    }
}