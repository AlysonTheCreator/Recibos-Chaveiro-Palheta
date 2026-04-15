package dao;

import model.Recibo;
import model.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ReciboDAO {

    public int salvarRecibo(Recibo recibo) {
        String sql = "INSERT INTO recibos (datahora, total_geral) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recibo.getDataHora());
            stmt.setDouble(2, recibo.getTotalGeral());
            stmt.executeUpdate();

            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar recibo: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public void salvarItensRecibo(int idRecibo, Recibo recibo) {
        String sql = """
                INSERT INTO itens_recibo
                (id_recibo, nome_servico, quantidade, preco, valor_total)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Servico servico : recibo.getItens()) {
                stmt.setInt(1, idRecibo);
                stmt.setString(2, servico.getNome());
                stmt.setInt(3, servico.getQuantidade());
                stmt.setDouble(4, servico.getPreco());
                stmt.setDouble(5, servico.getValorTotal());
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar itens do recibo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}