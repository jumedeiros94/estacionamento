package estacionamento.view;

import estacionamento.service.UsuarioService;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UsuarioView {

    private Scanner scanner;
    private UsuarioService usuarioService;

    public UsuarioView(){
        scanner = new Scanner(System.in);
        usuarioService = new UsuarioService();
    }

    public void incializacao(){
        int escolha;
        do {
            menu();
            escolha = selecionaEscolhaUsuario();

            switch (escolha){
                case 1:
                    usuarioService.visualizarCarros();
                    break;
                case 2:
                    System.out.println("Insira o nome do carro: ");
                    String nomeCarro = scanner.nextLine();
                    System.out.println("Insira a placa do carro: ");
                    String placa = scanner.nextLine();
                    System.out.println("Insira o nome do dono do carro: ");
                    String nomeDono = scanner.nextLine();
                    System.out.println("Insira o CPF do dono do carro: ");
                    String cpf = scanner.nextLine();
                    usuarioService.inserirCarro(nomeCarro, placa, nomeDono, cpf);
                    break;
                case 3:
                    System.out.println("Digite o ID do carro: ");
                    int idCarro = scanner.nextInt();
                    usuarioService.atualizarCarro(idCarro);
                    break;
                case 4:
                    System.out.println("Digite o id que deseja deletar: ");
                    int idParaDeletar = scanner.nextInt();
                    scanner.nextLine();
                    usuarioService.excluirCarro(idParaDeletar);
                    break;
                case 5:
                    System.out.println("Digite o ID do carro para consultar: ");
                    int idcarro = scanner.nextInt();
                    scanner.nextLine();
                    usuarioService.mostrarValorTempo(idcarro);
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Escolha inválida. Insira de 1 - 6");
                    break;
            }
        }while(escolha != 6);
    }

    public void menu(){
        System.out.println("BEM VINDO!");
        System.out.println("Digite uma das opções abaixo");
        System.out.println("1 - Consultar carros que estão no estacionamento");
        System.out.println("2 - Inserir a entrada de um novo carro");
        System.out.println("3 - Atualizar um carro já estacionado");
        System.out.println("4 - Deletar um carro");
        System.out.println("5 - Consultar tempo de permanência e valor para pagamento");
        System.out.println("6 - Sair do menu");
    }

    public int selecionaEscolhaUsuario(){
        try{
            int escolha = scanner.nextInt();
            scanner.nextLine();
            return escolha;
        }catch (InputMismatchException e){
            System.out.println(e.getMessage());
            scanner.nextLine();
        }
        return 1;
    }

}



