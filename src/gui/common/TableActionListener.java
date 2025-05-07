package gui.common;

public interface TableActionListener {
    
    void onEdit(int row);
    
    void onDelete(int row);
    
    default void onView(int row) {
    }
    
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
    
    @FunctionalInterface
    interface RowActionHandler {
        void handle(int row);
    }
}