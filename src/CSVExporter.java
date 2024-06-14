import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;

public class CSVExporter {

    public static void exportTableToCSV(JTable table, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Get the number of rows and columns in the table
            int rowCount = table.getRowCount();
            int columnCount = table.getColumnCount();

            // Write the column names to the CSV file
            for (int i = 0; i < columnCount; i++) {
                writer.write(table.getColumnName(i) + ",");
            }
            writer.write("\n");

            // Write the table data to the CSV file
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    writer.write(table.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            // Flush the writer to ensure all data is written to the file
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Error exporting data to CSV file: " + e.getMessage());
        }
    }
}