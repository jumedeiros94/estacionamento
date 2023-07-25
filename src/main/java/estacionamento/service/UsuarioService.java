package estacionamento.service;

import estacionamento.connection.Conexao;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class UsuarioService {


    public void inserirCarro(String nomeCarro, String placa, String nomeDono, String cpf) {
        Connection conexao = Conexao.getConnection();
        if (cpf.length() != 14){
            System.out.println("Erro: o CPF deve ter traços e ponto");
        }
        String sql = "INSERT INTO clientes(nome, cpf) VALUES(?, ?) RETURNING id";
        if (conexao != null) {
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, nomeDono);
                pstmt.setString(2, cpf);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int idCliente = rs.getInt("id");
                    sql = "INSERT INTO carros(nome_carro, id_cliente, placa, data_entrada, hora_entrada) VALUES(?, ?, ?, CURRENT_DATE, CURRENT_TIME)";
                    try (PreparedStatement pstmt2 = conexao.prepareStatement(sql)) {
                        pstmt2.setString(1, nomeCarro);
                        pstmt2.setInt(2, idCliente);
                        pstmt2.setString(3, placa);
                        pstmt2.executeUpdate();
                        System.out.println("Dado inserido com sucesso!");
                        System.out.println(" ");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void atualizarCarro(int idCarro) {
        Connection conexao = Conexao.getConnection();
        String sql = "SELECT data_entrada, hora_entrada FROM carros WHERE id = ?";
        if (conexao != null) {
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setInt(1, idCarro);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Date dataEntrada = rs.getDate("data_entrada");
                    Time horaEntrada = rs.getTime("hora_entrada");
                    LocalDateTime entrada = LocalDateTime.of(dataEntrada.toLocalDate(), horaEntrada.toLocalTime());
                    long minutos = entrada.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                    BigDecimal valor;
                    if (minutos <= 60) {
                        valor = new BigDecimal("10.00");
                    } else if (minutos <= 720) {
                        valor = new BigDecimal("10.00").add(new BigDecimal((minutos - 60) / 30 * 2));
                    } else {
                        valor = new BigDecimal("90.00");
                    }
                    sql = "INSERT INTO permanencias(idcarro, data_saida, hora_saida, valor) VALUES(?, CURRENT_DATE, CURRENT_TIME, ?)";
                    try (PreparedStatement pstmt2 = conexao.prepareStatement(sql)) {
                        pstmt2.setInt(1, idCarro);
                        pstmt2.setBigDecimal(2, valor);
                        pstmt2.executeUpdate();
                        System.out.println("Dado atualizado com sucesso!");
                        System.out.println(" ");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void excluirCarro(int idCarro) {
        Connection conexao = Conexao.getConnection();
        String sql = "DELETE FROM carros WHERE id = ?";
        if (conexao != null) {
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setInt(1, idCarro);
                pstmt.executeUpdate();
                System.out.println("Dado excluido com sucesso!");
                System.out.println(" ");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void visualizarCarros() {
        Connection conexao = Conexao.getConnection();
        String sql = "SELECT c.id, c.nome_carro, c.placa, cl.nome as dono_carro FROM carros c JOIN clientes cl ON c.id_cliente = cl.id";
        if (conexao != null) {
            try (Statement stmt = conexao.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int idCarro = rs.getInt("id");
                    String nomeCarro = rs.getString("nome_carro");
                    String placa = rs.getString("placa");
                    String donoCarro = rs.getString("dono_carro");
                    System.out.println("ID carro: " + idCarro +  ", Nome carro: "+ nomeCarro + ", Placa: " + placa + ", Nome do cliente: " + donoCarro);
                    System.out.println(" ");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void mostrarValorTempo(int idCarro) {
        Connection conexao = Conexao.getConnection();
        String sql = "SELECT data_entrada, hora_entrada FROM carros WHERE id = ?";
        if (conexao != null) {
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setInt(1, idCarro);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Date dataEntrada = rs.getDate("data_entrada");
                    Time horaEntrada = rs.getTime("hora_entrada");
                    LocalDateTime entrada = LocalDateTime.of(dataEntrada.toLocalDate(), horaEntrada.toLocalTime());
                    long minutos = entrada.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                    BigDecimal valor;
                    if (minutos <= 60) {
                        valor = new BigDecimal("10.00");
                    } else if (minutos <= 720) {
                        valor = new BigDecimal("10.00").add(new BigDecimal((minutos - 60) / 30 * 2));
                    } else {
                        valor = new BigDecimal("90.00");
                    }
                    System.out.println("Tempo de permanência: " + minutos + " minutos");
                    System.out.println("Valor a ser pago: R$" + valor);
                    System.out.println(" ");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}




