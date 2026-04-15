package dao;

import java.sql.Connection;
import java.sql.Statement;

public class CriarTabelas {

    public static void criarTabelas() {
        String sqlRecibos = """
                CREATE TABLE IF NOT EXISTS recibos (
                    id_recibo INTEGER PRIMARY KEY AUTOINCREMENT,
                    datahora TEXT NOT NULL,
                    total_geral REAL NOT NULL
                )
                """;

        String sqlItens = """
                CREATE TABLE IF NOT EXISTS itens_recibo (
                    id_item INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_recibo INTEGER NOT NULL,
                    nome_servico TEXT NOT NULL,
                    quantidade INTEGER NOT NULL,
                    preco REAL NOT NULL,
                    valor_total REAL NOT NULL,
                    FOREIGN KEY (id_recibo) REFERENCES recibos(id_recibo)
                )
                """;

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlRecibos);
            stmt.execute(sqlItens);

        } catch (Exception e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}