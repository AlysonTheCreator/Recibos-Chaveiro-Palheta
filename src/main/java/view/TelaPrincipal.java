package view;

import controller.ReciboController;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private JTextField campoNomeServico;
    private JTextField campoQuantidade;
    private JTextField campoPreco;

    private final ReciboController controller;
    private TelaRecibo telaRecibo;
    private int xMouse, yMouse;

    public TelaPrincipal() {
        controller = new ReciboController();

        setTitle("Cadastro de Serviços");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);
        add(criarBarraTitulo());

        JLabel labelNome = new JLabel("Nome do serviço:");
        labelNome.setBounds(180, 100, 250, 30);
        labelNome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(labelNome);

        campoNomeServico = new JTextField();
        campoNomeServico.setBounds(150, 135, 280, 30);
        add(campoNomeServico);

        JLabel labelQuantidade = new JLabel("Quantidade:");
        labelQuantidade.setBounds(210, 185, 200, 30);
        labelQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(labelQuantidade);

        campoQuantidade = new JTextField();
        campoQuantidade.setBounds(225, 220, 120, 30);
        add(campoQuantidade);

        JLabel labelPreco = new JLabel("Preço:");
        labelPreco.setBounds(245, 270, 200, 30);
        labelPreco.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(labelPreco);

        campoPreco = new JTextField();
        campoPreco.setBounds(225, 305, 120, 30);
        add(campoPreco);

        JButton botaoAdicionar = new JButton("Adicionar");
        botaoAdicionar.setBounds(130, 380, 140, 40);
        botaoAdicionar.addActionListener(e -> adicionarServico());
        add(botaoAdicionar);

        JButton botaoVerRecibo = new JButton("Ver recibo");
        botaoVerRecibo.setBounds(300, 380, 140, 40);
        botaoVerRecibo.addActionListener(e -> abrirTelaRecibo());
        add(botaoVerRecibo);

        setVisible(true);
    }


    private void adicionarServico() {
        try {
            String nome = campoNomeServico.getText().trim();
            String quantidadeTexto = campoQuantidade.getText().trim();
            String precoTexto = campoPreco.getText().trim();

            if (nome.isEmpty() || quantidadeTexto.isEmpty() || precoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            int quantidade = Integer.parseInt(quantidadeTexto);
            double preco = Double.parseDouble(precoTexto.replace(",", "."));

            if (quantidade <= 0 || preco <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser maiores que zero.");
                return;
            }

            controller.adicionarServico(nome, quantidade, preco);

            JOptionPane.showMessageDialog(this, "Item adicionado ao recibo!");

            campoNomeServico.setText("");
            campoQuantidade.setText("");
            campoPreco.setText("");

            if (telaRecibo != null) {
                telaRecibo.atualizarTabela();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números válidos.");
        }
    }


    private void abrirTelaRecibo() {
        if (telaRecibo == null) {
            telaRecibo = new TelaRecibo(controller);
        } else {
            telaRecibo.setVisible(true);
            telaRecibo.atualizarTabela();
        }
    }
    private JPanel criarBarraTitulo() {
        JPanel barra = new JPanel();
        barra.setBounds(0, 0, 600, 40);
        barra.setBackground(new Color(255, 255, 255));
        barra.setLayout(null);

        JLabel titulo = new JLabel("Chaveiro Palheta");
        titulo.setForeground(Color.BLACK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(10, 10, 200, 20);
        barra.add(titulo);

        JButton botaoFechar = new JButton("X");
        botaoFechar.setBounds(560, 0, 40, 50);
        botaoFechar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoFechar.setMargin(new Insets(0, 0, 0, 0));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setBorderPainted(false);
        botaoFechar.setBackground(new Color(255, 255, 255));
        botaoFechar.setForeground(Color.BLACK);

        botaoFechar.addActionListener(e -> System.exit(0));

        botaoFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoFechar.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoFechar.setBackground(new Color(255, 255, 255));
            }
        });

        barra.add(botaoFechar);

        // Lógica de arrastar a janela
        barra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                xMouse = evt.getX();
                yMouse = evt.getY();
            }
        });

        barra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                int x = evt.getXOnScreen();
                int y = evt.getYOnScreen();

                setLocation(x - xMouse, y - yMouse);
            }
        });

        return barra;
    }
}