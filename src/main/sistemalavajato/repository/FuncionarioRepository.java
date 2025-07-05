package main.sistemalavajato.repository;

import java.util.ArrayList;
import java.util.List;

import main.sistemalavajato.model.Funcionario;

public class FuncionarioRepository {
    private static final List<Funcionario> funcionarios = new ArrayList<>();

    //Metodo para cadastrar funcionario
    public void cadastraFuncionario(Funcionario funcionario){
        funcionarios.add(funcionario);
    }

    //Metodo para remover funcionario
    public void removerFuncionario(String id){
        funcionarios.removeIf(funcionario -> funcionario.getId().equals(id));
    }

    //Metodo para atualizar funcionario
    public void atualizarFuncionario(Funcionario funcionarioAtualizado){
        for(int i = 0; i < funcionarios.size(); i++){
            if(funcionarios.get(i).getId().equals(funcionarioAtualizado.getId())){
                funcionarios.set(i, funcionarioAtualizado);
                return; //encerra o metodo aqui
            }
        }
    }

    //Metodo para listar funcionarios
    //FIXME: Metodo retorna void mas no diagrama retorna String
    public void listarFuncionario(){
        for(Funcionario f : funcionarios){
            System.out.println(f);
        }
    }

    // Verifica os serviços atuais do funcionário
    public void verServicosAtuais(){
         // TODO: Implementar a lógica para verificar os serviços que o funcionário está realizando
    }

    //Metodo para buscar funcionario
    public Funcionario buscarFuncionario(String id){
        for(Funcionario f : funcionarios){
            if(f.getId().equals(id)){
                return f;
            }
        }
        return null;
    }
}
