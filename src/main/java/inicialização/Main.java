package inicialização;

import dao.CriarTabelas;
import view.TelaPrincipal;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CriarTabelas.criarTabelas();
        new TelaPrincipal();

    }
}