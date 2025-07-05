package main.sistemalavajato.model;

public class Funcionario /*extends Usuario*/{
    //Placeholder: Os atributos como id, nome, email, etc., serão herdados da futura superclasse Usuario.
    private String id;
    private String nome;
    private String email;
    private String senha;
    
    //Construtor
    public Funcionario(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    //Metodos get
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    //Metodos set
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + id + 
        "\nNome: " + nome + 
        "\nEmail: " + email + 
        "\nSenha=" + senha;
    }
}
