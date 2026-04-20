package view;

import controller.ReciboController;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private JTextField campoNomeEmpresa;
    private JTextField campoCnpj;
    private JTextField campoEndereco;

    private JTextField campoNomeServico;
    private JTextField campoQuantidade;
    private JTextField campoPreco;

    private final ReciboController controller;
    private TelaRecibo telaRecibo;
    private int xMouse, yMouse;

    public TelaPrincipal() {
        controller = new ReciboController();

        setTitle("Cadastro de Serviços");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        add(criarBarraTitulo());

        JLabel labelEmpresa = new JLabel("Nome da empresa:");
        labelEmpresa.setBounds(190, 65, 250, 25);
        labelEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelEmpresa);

        campoNomeEmpresa = new JTextField();
        campoNomeEmpresa.setBounds(150, 95, 300, 30);
        add(campoNomeEmpresa);

        JLabel labelCnpj = new JLabel("CNPJ:");
        labelCnpj.setBounds(255, 140, 120, 25);
        labelCnpj.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelCnpj);

        campoCnpj = new JTextField();
        campoCnpj.setBounds(150, 170, 300, 30);
        add(campoCnpj);

        JLabel labelEndereco = new JLabel("Endereço:");
        labelEndereco.setBounds(235, 215, 150, 25);
        labelEndereco.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelEndereco);

        campoEndereco = new JTextField();
        campoEndereco.setBounds(150, 245, 300, 30);
        add(campoEndereco);

        JLabel labelNome = new JLabel("Nome do serviço:");
        labelNome.setBounds(200, 295, 220, 25);
        labelNome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelNome);

        campoNomeServico = new JTextField();
        campoNomeServico.setBounds(150, 325, 300, 30);
        add(campoNomeServico);

        JLabel labelQuantidade = new JLabel("Quantidade:");
        labelQuantidade.setBounds(240, 370, 170, 25);
        labelQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelQuantidade);

        campoQuantidade = new JTextField();
        campoQuantidade.setBounds(235, 400, 130, 30);
        add(campoQuantidade);

        JLabel labelPreco = new JLabel("Preço:");
        labelPreco.setBounds(260, 445, 100, 25);
        labelPreco.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(labelPreco);

        campoPreco = new JTextField();
        campoPreco.setBounds(235, 475, 130, 30);
        add(campoPreco);

        JButton botaoAdicionar = new JButton("Adicionar");
        botaoAdicionar.setBounds(120, 570, 150, 42);
        botaoAdicionar.addActionListener(e -> adicionarServico());
        add(botaoAdicionar);

        JButton botaoVerRecibo = new JButton("Ver recibo");
        botaoVerRecibo.setBounds(320, 570, 150, 42);
        botaoVerRecibo.addActionListener(e -> abrirTelaRecibo());
        add(botaoVerRecibo);

        setVisible(true);
    }

    private void adicionarServico() {
        try {
            String nomeEmpresa = campoNomeEmpresa.getText().trim();
            String cnpj = campoCnpj.getText().trim();
            String endereco = campoEndereco.getText().trim();

            String nome = campoNomeServico.getText().trim();
            String quantidadeTexto = campoQuantidade.getText().trim();
            String precoTexto = campoPreco.getText().trim();

            if (nome.isEmpty() || quantidadeTexto.isEmpty() || precoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha os campos do serviço.");
                return;
            }

            int quantidade = Integer.parseInt(quantidadeTexto);
            double preco = Double.parseDouble(precoTexto.replace(",", "."));

            if (quantidade <= 0 || preco <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser maiores que zero.");
                return;
            }

            controller.definirDadosCliente(nomeEmpresa, cnpj, endereco);
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
        controller.definirDadosCliente(
                campoNomeEmpresa.getText().trim(),
                campoCnpj.getText().trim(),
                campoEndereco.getText().trim()
        );

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
        barra.setBackground(Color.WHITE);
        barra.setLayout(null);

        JLabel titulo = new JLabel("Chaveiro Palheta");
        titulo.setForeground(Color.BLACK);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(10, 10, 200, 20);
        barra.add(titulo);

        JButton botaoFechar = new JButton("X");
        botaoFechar.setBounds(560, 0, 40, 40);
        botaoFechar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoFechar.setMargin(new Insets(0, 0, 0, 0));
        botaoFechar.setFocusPainted(false);
        botaoFechar.setBorderPainted(false);
        botaoFechar.setBackground(Color.WHITE);
        botaoFechar.setForeground(Color.BLACK);
        botaoFechar.addActionListener(e -> System.exit(0));

        botaoFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botaoFechar.setBackground(Color.RED);
                botaoFechar.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botaoFechar.setBackground(Color.WHITE);
                botaoFechar.setForeground(Color.BLACK);
            }
        });

        barra.add(botaoFechar);

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