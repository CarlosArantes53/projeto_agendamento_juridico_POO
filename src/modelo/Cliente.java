package modelo;

public class Cliente {
    private int id;
    private String nome;
    private String documento;
    private String telefone;
    private String email;
    private String endereco;
    
    public Cliente() {
    }
    
    public Cliente(String nome, String documento, String telefone, String email, String endereco) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }
    
    public Cliente(int id, String nome, String documento, String telefone, String email, String endereco) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nome=" + nome + ", documento=" + documento + 
               ", telefone=" + telefone + ", email=" + email + ", endereco=" + endereco + "]";
    }
}