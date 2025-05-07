package gui.common;

/**
 * Interface para lidar com ações em componentes de tabela.
 * Implementada por classes que precisam responder a eventos de edição, exclusão
 * ou outras ações em linhas de tabela.
 */
public interface TableActionListener {
    
    /**
     * Chamado quando o usuário seleciona a ação de editar em uma linha da tabela
     * 
     * @param row O índice da linha na tabela onde a ação foi iniciada
     */
    void onEdit(int row);
    
    /**
     * Chamado quando o usuário seleciona a ação de excluir em uma linha da tabela
     * 
     * @param row O índice da linha na tabela onde a ação foi iniciada
     */
    void onDelete(int row);
    
    /**
     * Chamado quando o usuário seleciona a ação de visualizar em uma linha da tabela.
     * Esse método é opcional e pode ser implementado se necessário.
     * 
     * @param row O índice da linha na tabela onde a ação foi iniciada
     */
    default void onView(int row) {
        // Implementação padrão vazia
        // Classes que precisam desta funcionalidade podem sobrescrever
    }
    
    /**
     * Factory method para criar uma implementação simplificada quando apenas
     * as ações de edição e exclusão são necessárias.
     * 
     * @param editHandler Handler para ação de edição
     * @param deleteHandler Handler para ação de exclusão
     * @return Uma nova instância de TableActionListener
     */
    static TableActionListener create(RowActionHandler editHandler, RowActionHandler deleteHandler) {
        return new TableActionListener() {
            @Override
            public void onEdit(int row) {
                editHandler.handle(row);
            }
            
            @Override
            public void onDelete(int row) {
                deleteHandler.handle(row);
            }
        };
    }
    
    /**
     * Interface funcional para tratamento simplificado de ações em linhas.
     * Usada pelo método factory create().
     */
    @FunctionalInterface
    interface RowActionHandler {
        void handle(int row);
    }
}