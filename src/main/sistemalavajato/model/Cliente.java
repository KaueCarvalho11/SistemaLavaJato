public class Cliente {
    //atributos
    private String endereco;
    private int numeroTelefone;

    //construtor
    public Cliente(String endereco, int numeroTelefone) {

        this.endereco = endereco;
        this.numeroTelefone = numeroTelefone;
    }

    //metodos getters e setters
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(int numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    //metodos CRUD

    //Cadastrar um cliente
    public void cadastraCliente(){

    }

    //Remover um clinte
    public void removeCliente(){

    }

    //Atualizar um cliente
    public void atualizaCliente(){

    }

    //Listar todos os clientes
    public String listarCliente(){
        return "Cliente não encontrado";
    }

    //Exibe um cliente especifico 
    public String exibirCliente(){
        return "Cliente nao encontrado";
    }

    //métodos auxiliares
    public String toString(){
        return "Endereço: " + endereco + " Numero de telefone: " + numeroTelefone;
    }

}
